package solitour_backend.solitour.book_mark_gathering.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import solitour_backend.solitour.book_mark_gathering.entity.BookMarkGathering;
import solitour_backend.solitour.book_mark_gathering.service.BookMarkGatheringService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark/gathering")
public class BookMarkGatheringController {

    private final BookMarkGatheringService service;

    @Transactional
    @PostMapping("/users/{userId}")
    public ResponseEntity<Long> createUserBookmark(@PathVariable Long userId, @RequestParam Long gatheringId) {
        BookMarkGathering bookMarkGathering = service.createUserBookmark(userId, gatheringId);

        return ResponseEntity.ok(bookMarkGathering.getId());
    }

    @Transactional
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserBookmark(@PathVariable Long userId, @RequestParam Long gatheringId) {
        service.deleteUserBookmark(userId, gatheringId);

        return ResponseEntity.noContent().build();
    }
}
