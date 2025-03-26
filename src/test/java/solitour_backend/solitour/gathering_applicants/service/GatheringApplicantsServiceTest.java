package solitour_backend.solitour.gathering_applicants.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import solitour_backend.solitour.gathering.entity.AllowedSex;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.gathering.repository.GatheringRepository;
import solitour_backend.solitour.gathering_applicants.dto.request.GatheringApplicantsModifyRequest;
import solitour_backend.solitour.gathering_applicants.entity.GatheringStatus;
import solitour_backend.solitour.gathering_applicants.exception.GatheringApplicantsAlreadyFullPeopleException;
import solitour_backend.solitour.gathering_applicants.exception.GatheringNotManagerException;
import solitour_backend.solitour.gathering_applicants.repository.GatheringApplicantsRepository;
import solitour_backend.solitour.user.entity.Sex;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GatheringApplicantsServiceTest {

    @Autowired
    private GatheringApplicantsService gatheringApplicantsService;

    @Autowired
    private GatheringRepository gatheringRepository;

    @Autowired
    private GatheringApplicantsRepository gatheringApplicantsRepository;

    @Autowired
    private UserRepository userRepository;

    private User manager;
    private Gathering gathering;
    private List<User> applicants;

    private final int PERSON_LIMIT = 3;
    private final int TOTAL_USERS = 10;

    @BeforeEach
    void setUp() {
        manager = userRepository.save(User.builder()
                .name("manager")
                .age(30)
                .email("manager@example.com")
                .phoneNumber("010-1234-5678")
                .sex(Sex.MALE)
                .build());

        gathering = gatheringRepository.save(
                new Gathering(
                        manager, null, null, null,
                        "테스트 모임", "내용",
                        PERSON_LIMIT, 0,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now().plusDays(2),
                        false,
                        LocalDateTime.now().plusHours(5),
                        AllowedSex.ALL,
                        20, 40,
                        "https://open.kakao.com/test"
                ));

        applicants = new ArrayList<>();
        for (int i = 0; i < TOTAL_USERS; i++) {
            User user = userRepository.save(User.builder()
                    .name("user" + i)
                    .age(25)
                    .email("user" + i + "@example.com")
                    .phoneNumber("010-1234-000" + i)
                    .sex(Sex.FEMALE)
                    .build());

            applicants.add(user);

            gatheringApplicantsService.participateGatheringFromAnotherUser(user.getId(), gathering.getId());
        }
    }

    @Test
    @DisplayName("모임 참여 수락 시 인원 제한 초과 방지 테스트")
    void acceptParticipantsShouldNotExceedPersonLimit() {
        int success = 0;
        int failCount = 0;

        for (User user : applicants) {
            GatheringApplicantsModifyRequest request = new GatheringApplicantsModifyRequest();
            ReflectionTestUtils.setField(request, "userId", user.getId());
            ReflectionTestUtils.setField(request, "gatheringStatus", GatheringStatus.CONSENT);

            try {
                boolean result = gatheringApplicantsService.updateGatheringApplicantsManagement(
                        manager.getId(), gathering.getId(), request);
                if (result) success++;
            } catch (GatheringApplicantsAlreadyFullPeopleException e) {
                failCount++;
            }
        }

        long consentCount = gatheringApplicantsRepository.countAllByGathering_IdAndGatheringStatus(
                gathering.getId(), GatheringStatus.CONSENT);

        assertEquals(PERSON_LIMIT, consentCount);
        assertEquals(PERSON_LIMIT, success);
        assertEquals(TOTAL_USERS - PERSON_LIMIT, failCount);
    }

    @Test
    @DisplayName("작성자가 아닌 다른 사용자가 모임 신청 상태 변환시 예외 발생 테스트")
    void nonManagerTryingToAcceptParticipantShouldThrowException() {
        User nonManager = applicants.get(0);

        GatheringApplicantsModifyRequest request = new GatheringApplicantsModifyRequest();
        ReflectionTestUtils.setField(request, "userId", applicants.get(1).getId());
        ReflectionTestUtils.setField(request, "gatheringStatus", GatheringStatus.CONSENT);

        assertThrows(GatheringNotManagerException.class, () -> {
                    gatheringApplicantsService.updateGatheringApplicantsManagement(nonManager.getId(), gathering.getId(), request);
                }
        );
    }

    @Test
    @DisplayName("제한 인원 초과 시 여러 일반 사용자가 동시에 수락 시도하면 전부 예외 테스트")
    void concurrentAcceptanceByNonManagersShouldAllThrowException() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        List<String> logs = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < 5; i++) {
            int idx = i;
            executor.execute(() -> {
                try {
                    GatheringApplicantsModifyRequest request = new GatheringApplicantsModifyRequest();
                    ReflectionTestUtils.setField(request, "userId", applicants.get((idx + 1) % TOTAL_USERS).getId());
                    ReflectionTestUtils.setField(request, "gatheringStatus", GatheringStatus.CONSENT);

                    gatheringApplicantsService.updateGatheringApplicantsManagement(
                            applicants.get(idx).getId(), gathering.getId(), request);

                    logs.add("예외 없이 수락됨 (문제 있음)");

                } catch (GatheringNotManagerException e) {
                    logs.add("예외 정상 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        logs.forEach(System.out::println);
        assertEquals(5, logs.size());
        assertTrue(logs.stream().allMatch(log -> log.contains("예외 정상 발생")));
    }

    @Test
    @DisplayName("작성자가 서로 다른 유저를 동시에 수락하면 personCount 초과가 발생하는 동시성 문제 테스트")
    void updateGatheringApplicantsManagementConcurrencyTest() throws InterruptedException {
        gathering.setPersonCount(3);
        gatheringRepository.save(gathering);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<String> logs = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            User applicantUser = applicants.get(i);
            GatheringApplicantsModifyRequest request = new GatheringApplicantsModifyRequest();
            ReflectionTestUtils.setField(request, "userId", applicantUser.getId());
            ReflectionTestUtils.setField(request, "gatheringStatus", GatheringStatus.CONSENT);

            executor.execute(() -> {
                try {
                    boolean result = gatheringApplicantsService.updateGatheringApplicantsManagement(
                            manager.getId(), gathering.getId(), request);
                    if (result) {
                        logs.add(count.incrementAndGet() + "번째 수락 성공: " + applicantUser.getId());
                    } else {
                        logs.add("변경 없음");
                    }
                } catch (Exception e) {
                    logs.add("예외: " + e.getClass().getSimpleName());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        logs.forEach(System.out::println);

        long consentCount = gatheringApplicantsRepository.countAllByGathering_IdAndGatheringStatus(
                gathering.getId(), GatheringStatus.CONSENT);


        assertTrue(3 >= consentCount);
    }

    @Test
    @DisplayName("낙관적인 락을 사용했을때는 여러 동시 요청이 들어와도 가장 처음 요청 만 수락하고 전부 예외처리 한다.")
    void optimisticLockShouldAllowOnlyFirstRequestAndRejectOthersWithException() throws InterruptedException {
        gathering.setPersonCount(1);
        gatheringRepository.save(gathering);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<String> logs = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger count = new AtomicInteger();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            User applicantUser = applicants.get(i);
            GatheringApplicantsModifyRequest request = new GatheringApplicantsModifyRequest();
            ReflectionTestUtils.setField(request, "userId", applicantUser.getId());
            ReflectionTestUtils.setField(request, "gatheringStatus", GatheringStatus.CONSENT);

            executor.execute(() -> {
                try {
                    boolean result = gatheringApplicantsService.updateGatheringApplicantsManagement(
                            manager.getId(), gathering.getId(), request);
                    if (result) {
                        logs.add(count.incrementAndGet() + "번째 수락 성공: " + applicantUser.getId());
                        successCount.getAndIncrement();
                    } else {
                        logs.add("변경 없음");
                    }
                } catch (Exception e) {
                    failCount.getAndIncrement();
                    logs.add("예외: " + e.getClass().getSimpleName());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        logs.forEach(System.out::println);

        long consentCount = gatheringApplicantsRepository.countAllByGathering_IdAndGatheringStatus(
                gathering.getId(), GatheringStatus.CONSENT);

        assertEquals(1, successCount.intValue());
        assertEquals(9, failCount.intValue());
        assertEquals(1, consentCount);
    }

    @Test
    @DisplayName("작성자가 인원 제한 내에서 정상적으로 수락 요청을 처리하면 모두 성공하는 테스트")
    void acceptParticipantsWithinLimitShouldSucceedForAll() {
        int personLimit = 3;
        gathering.setPersonCount(personLimit);
        gatheringRepository.save(gathering);

        List<User> consentTargets = applicants.subList(0, personLimit);
        for (User applicant : consentTargets) {
            GatheringApplicantsModifyRequest request = new GatheringApplicantsModifyRequest();
            ReflectionTestUtils.setField(request, "userId", applicant.getId());
            ReflectionTestUtils.setField(request, "gatheringStatus", GatheringStatus.CONSENT);

            boolean result = gatheringApplicantsService.updateGatheringApplicantsManagement(
                    manager.getId(), gathering.getId(), request
            );

            assertTrue(result, "작성자가 정상적으로 수락 요청을 처리해야 합니다.");
        }

        long consentCount = gatheringApplicantsRepository.countAllByGathering_IdAndGatheringStatus(
                gathering.getId(), GatheringStatus.CONSENT);

        assertEquals(personLimit, consentCount);
    }


}
