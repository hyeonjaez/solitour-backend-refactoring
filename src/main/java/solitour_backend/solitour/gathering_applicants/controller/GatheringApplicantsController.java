package solitour_backend.solitour.gathering_applicants.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solitour_backend.solitour.auth.config.AuthenticationPrincipal;
import solitour_backend.solitour.gathering_applicants.dto.request.GatheringApplicantsModifyRequest;
import solitour_backend.solitour.gathering_applicants.service.GatheringApplicantsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatherings/applicants")
public class GatheringApplicantsController {
    private final GatheringApplicantsService gatheringApplicantsService;

    @PostMapping("/{gatheringId}")
    public ResponseEntity<Void> participateGathering(@AuthenticationPrincipal Long userId, @PathVariable Long gatheringId) {
        gatheringApplicantsService.participateGatheringFromAnotherUser(userId, gatheringId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{gatheringId}")
    public ResponseEntity<Void> deleteParticipateGathering(@AuthenticationPrincipal Long userId, @PathVariable Long gatheringId) {
        gatheringApplicantsService.deleteGatheringApplicantsFromAnotherUser(userId, gatheringId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{gatheringId}")
    public ResponseEntity<Void> updateParticipateGatheringStatus(@AuthenticationPrincipal Long userId,
                                                                 @PathVariable Long gatheringId,
                                                                 @Valid @RequestBody GatheringApplicantsModifyRequest gatheringApplicantsModifyRequest) {

        boolean result = gatheringApplicantsService.updateGatheringApplicantsManagement(userId, gatheringId, gatheringApplicantsModifyRequest);

        if (result) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
