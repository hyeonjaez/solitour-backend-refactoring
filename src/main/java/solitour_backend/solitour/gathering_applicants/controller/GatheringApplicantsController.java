package solitour_backend.solitour.gathering_applicants.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solitour_backend.solitour.error.Utils;
import solitour_backend.solitour.gathering_applicants.dto.request.GatheringApplicantsModifyRequest;
import solitour_backend.solitour.gathering_applicants.service.GatheringApplicantsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gatherings/applicants")
public class GatheringApplicantsController {
    private final GatheringApplicantsService gatheringApplicantsService;

    @PostMapping("/{gatheringId}/users/{userId}")
    public ResponseEntity<Void> participateGathering(@PathVariable Long userId,
                                                     @PathVariable Long gatheringId) {
        gatheringApplicantsService.participateGatheringFromAnotherUser(userId, gatheringId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{gatheringId}/users/{userId}")
    public ResponseEntity<Void> deleteParticipateGathering(@PathVariable Long userId,
                                                           @PathVariable Long gatheringId) {
        gatheringApplicantsService.deleteGatheringApplicantsFromAnotherUser(userId, gatheringId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{gatheringId}/users/{userId}")
    public ResponseEntity<Void> updateParticipateGatheringStatus(@PathVariable Long userId,
                                                                 @PathVariable Long gatheringId,
                                                                 @Valid @RequestBody GatheringApplicantsModifyRequest gatheringApplicantsModifyRequest,
                                                                 BindingResult bindingResult) {
        Utils.validationRequest(bindingResult);

        boolean result = gatheringApplicantsService.updateGatheringApplicantsManagement(userId, gatheringId,
                gatheringApplicantsModifyRequest);

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
