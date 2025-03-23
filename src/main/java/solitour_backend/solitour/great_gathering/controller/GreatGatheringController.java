package solitour_backend.solitour.great_gathering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solitour_backend.solitour.great_gathering.entity.GreatGathering;
import solitour_backend.solitour.great_gathering.service.GreatGatheringService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gathering/great")
public class GreatGatheringController {

    private final GreatGatheringService service;

    @PostMapping("/users/{userId}")
    public ResponseEntity<Long> createGatheringGreat(@PathVariable Long userId,
                                                     @RequestParam Long gatheringId) {
        GreatGathering greatGathering = service.createGatheringGreat(userId, gatheringId);

        return ResponseEntity.ok(greatGathering.getId());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteGatheringGreat(@PathVariable Long userId,
                                                     @RequestParam Long gatheringId) {
        service.deleteGatheringGreat(userId, gatheringId);

        return ResponseEntity.noContent().build();
    }
}
