package solitour_backend.solitour.great_information.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import solitour_backend.solitour.great_information.entity.GreatInformation;
import solitour_backend.solitour.great_information.service.GreatInformationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/information/great")
public class GreatInformationController {

    private final GreatInformationService service;

    @PostMapping("/users/{userId}")
    public ResponseEntity<Long> createInformationGreat(@PathVariable Long userId,
                                                       @RequestParam Long infoId) {
        GreatInformation greatInformation = service.createInformationGreat(userId, infoId);

        return ResponseEntity.ok(greatInformation.getId());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteInformationGreat(@PathVariable Long userId,
                                                       @RequestParam Long infoId) {
        service.deleteInformationGreat(userId, infoId);

        return ResponseEntity.noContent().build();
    }
}
