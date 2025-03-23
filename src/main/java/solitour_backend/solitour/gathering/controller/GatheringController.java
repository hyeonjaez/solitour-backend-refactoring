package solitour_backend.solitour.gathering.controller;

import static solitour_backend.solitour.information.controller.InformationController.PAGE_SIZE;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import solitour_backend.solitour.error.Utils;
import solitour_backend.solitour.error.exception.RequestValidationFailedException;
import solitour_backend.solitour.gathering.dto.request.GatheringModifyRequest;
import solitour_backend.solitour.gathering.dto.request.GatheringPageRequest;
import solitour_backend.solitour.gathering.dto.request.GatheringRegisterRequest;
import solitour_backend.solitour.gathering.dto.response.GatheringBriefResponse;
import solitour_backend.solitour.gathering.dto.response.GatheringDetailResponse;
import solitour_backend.solitour.gathering.dto.response.GatheringRankResponse;
import solitour_backend.solitour.gathering.dto.response.GatheringResponse;
import solitour_backend.solitour.gathering.service.GatheringService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatherings")
public class GatheringController {
    private final GatheringService gatheringService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<GatheringResponse> createGathering(@PathVariable Long userId,
                                                             @Valid @RequestBody GatheringRegisterRequest gatheringRegisterRequest,
                                                             BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);

        if (gatheringRegisterRequest.getEndAge() > gatheringRegisterRequest.getStartAge()) {
            throw new RequestValidationFailedException("시작 나이 연도가 끝 나이 연도 보다 앞에 있네요");
        }
        if (gatheringRegisterRequest.getScheduleStartDate().isAfter(gatheringRegisterRequest.getScheduleEndDate())) {
            throw new RequestValidationFailedException("시작 날짜는 종료 날짜보다 앞에 있어야 합니다.");
        }

        if (gatheringRegisterRequest.getDeadline().isBefore(LocalDateTime.now())) {
            throw new RequestValidationFailedException("마감일은 현재 시간보다 이후여야 합니다.");
        }

        GatheringResponse gatheringResponse = gatheringService.registerGathering(userId, gatheringRegisterRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gatheringResponse);
    }

    @GetMapping("/{id}/users/{userId}")
    public ResponseEntity<GatheringDetailResponse> getGatheringDetail(@PathVariable Long userId,
                                                                      @PathVariable Long id) {
        GatheringDetailResponse gatheringDetail = gatheringService.getGatheringDetail(userId, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gatheringDetail);
    }

    @PutMapping("/{gatheringId}/users/{userId}")
    public ResponseEntity<GatheringResponse> updateGathering(@PathVariable Long userId,
                                                             @PathVariable Long gatheringId,
                                                             @Valid @RequestBody GatheringModifyRequest gatheringModifyRequest) {

        if (gatheringModifyRequest.getEndAge() > gatheringModifyRequest.getStartAge()) {
            throw new RequestValidationFailedException("시작 나이 연도가 끝 나이 연도 보다 앞에 있네요");
        }

        if (gatheringModifyRequest.getScheduleStartDate().isAfter(gatheringModifyRequest.getScheduleEndDate())) {
            throw new RequestValidationFailedException("시작 날짜는 종료 날짜보다 앞에 있어야 합니다.");
        }

        if (gatheringModifyRequest.getDeadline().isBefore(LocalDateTime.now())) {
            throw new RequestValidationFailedException("마감일은 현재 시간보다 이후여야 합니다.");
        }

        GatheringResponse gatheringResponse = gatheringService.modifyGathering(userId, gatheringId,
                gatheringModifyRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gatheringResponse);
    }

    @DeleteMapping("/{gatheringId}/users/{userId}")
    public ResponseEntity<Void> deleteGathering(@PathVariable Long userId,
                                                @PathVariable Long gatheringId) {

        gatheringService.deleteGathering(gatheringId, userId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<GatheringBriefResponse>> pageGatheringSortAndFilter(@PathVariable Long userId,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @Valid @ModelAttribute GatheringPageRequest gatheringPageRequest,
                                                                                   BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<GatheringBriefResponse> pageGathering = gatheringService.getPageGathering(pageable, gatheringPageRequest,
                userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pageGathering);
    }

    @GetMapping("/tag/search/users/{userId}")
    public ResponseEntity<Page<GatheringBriefResponse>> getPageGatheringByTag(@PathVariable Long userId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @Valid @ModelAttribute GatheringPageRequest gatheringPageRequest,
                                                                              @RequestParam(required = false, name = "tagName") String tag,
                                                                              BindingResult bindingResult)
            throws UnsupportedEncodingException {
        String decodedValue = java.net.URLDecoder.decode(tag, "UTF-8");
        String filteredTag = decodedValue.replaceAll("[^a-zA-Z0-9가-힣]", "");

        Utils.validationRequest(bindingResult);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<GatheringBriefResponse> briefGatheringPage = gatheringService.getPageGatheringByTag(pageable, userId, gatheringPageRequest, filteredTag);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(briefGatheringPage);
    }

    @GetMapping("/ranks")
    public ResponseEntity<List<GatheringRankResponse>> getGatheringRankOrderByLikes() {
        List<GatheringRankResponse> gatheringRankOrderByLikes = gatheringService.getGatheringRankOrderByLikes();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gatheringRankOrderByLikes);
    }

    @PutMapping("/finish/{gatheringId}/users/{userId}")
    public ResponseEntity<Void> gatheringFinish(@PathVariable Long userId,
                                                @PathVariable Long gatheringId) {
        gatheringService.setGatheringFinish(userId, gatheringId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/not-finish/{gatheringId}/users/{userId}")
    public ResponseEntity<Void> gatheringNotFinish(@PathVariable Long userId,
                                                   @PathVariable Long gatheringId) {

        gatheringService.setGatheringNotFinish(userId, gatheringId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


    @GetMapping("/home/users/{userId}")
    public ResponseEntity<List<GatheringBriefResponse>> getHomeGathering(@PathVariable Long userId,
                                                                         HttpServletRequest request) {

        List<GatheringBriefResponse> gatheringOrderByLikesFilterByCreate3After = gatheringService.getGatheringOrderByLikesFilterByCreate3After(
                userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gatheringOrderByLikesFilterByCreate3After);
    }


}
