package solitour_backend.solitour.book_mark_information.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import solitour_backend.solitour.book_mark_information.entity.BookMarkInformation;
import solitour_backend.solitour.book_mark_information.service.BookMarkInformationService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark/information")
public class BookMarkInformationController {

    private final BookMarkInformationService service;

    @Transactional
    @PostMapping("/users/{userId}")
    public ResponseEntity<Long> createUserBookmark(@PathVariable Long userId, @RequestParam Long infoId) {
        BookMarkInformation bookMarkInformation = service.createUserBookmark(userId, infoId);

        return ResponseEntity.ok(bookMarkInformation.getId());
    }

    @Transactional
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserBookmark(@PathVariable Long userId,
                                                   @RequestParam Long infoId) {
        service.deleteUserBookmark(userId, infoId);

        return ResponseEntity.noContent().build();
    }
}
