package solitour_backend.solitour.gathering_applicants.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.gathering.entity.Gathering;
import solitour_backend.solitour.gathering.exception.GatheringNotExistsException;
import solitour_backend.solitour.gathering.repository.GatheringRepository;
import solitour_backend.solitour.gathering_applicants.entity.GatheringApplicants;
import solitour_backend.solitour.gathering_applicants.entity.GatheringStatus;
import solitour_backend.solitour.gathering_applicants.exception.GatheringApplicantsAlreadyExistsException;
import solitour_backend.solitour.gathering_applicants.exception.GatheringApplicantsAlreadyFullPeopleException;
import solitour_backend.solitour.gathering_applicants.exception.GatheringApplicantsNotExistsException;
import solitour_backend.solitour.gathering_applicants.repository.GatheringApplicantsRepository;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.entity.UserRepository;
import solitour_backend.solitour.user.exception.UserNotExistsException;

@Service
@Transactional
@RequiredArgsConstructor
public class GatheringApplicantsService {
    private final GatheringRepository gatheringRepository;
    private final GatheringApplicantsRepository gatheringApplicantsRepository;
    private final UserRepository userRepository;

    public void participateGatheringFromAnotherUser(Long userId, Long gatheringId) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () ->
                                new GatheringNotExistsException("해당하는 id의 gathering 이 존재 하지 않습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () ->
                                new UserNotExistsException("해당하는 id의 user가 없습니다"));

        if (gatheringApplicantsRepository.existsByGatheringIdAndUserId(gathering.getId(), user.getId())) {
            throw new GatheringApplicantsAlreadyExistsException("해당 유저는 이미 참여 해 있습니다.");
        }

        Integer personCount = gathering.getPersonCount();
        int nowPersonCount = gatheringApplicantsRepository.countAllByGathering_IdAndGatheringStatus(gatheringId, GatheringStatus.CONSENT);

        if (personCount <= nowPersonCount) {
            throw new GatheringApplicantsAlreadyFullPeopleException("이미 인원이 가득 찼습니다.");
        }

        GatheringApplicants gatheringApplicants = new GatheringApplicants(gathering, user, GatheringStatus.WAIT);

        gatheringApplicantsRepository.save(gatheringApplicants);
    }

    public void deleteGatheringApplicantsFromAnotherUser(Long userId, Long gatheringId) {
        Gathering gathering = gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () ->
                                new GatheringNotExistsException("해당하는 id의 gathering 이 존재 하지 않습니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () ->
                                new UserNotExistsException("해당하는 id의 user가 없습니다"));

        GatheringApplicants gatheringApplicants = gatheringApplicantsRepository.findByGatheringIdAndUserId(gathering.getId(), user.getId())
                .orElseThrow(
                        () ->
                                new GatheringApplicantsNotExistsException("해당하는 모임과 user의 gathering applicants가 없습니다."));

        gatheringApplicantsRepository.delete(gatheringApplicants);
    }
}
