package solitour_backend.solitour.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solitour_backend.solitour.user.dto.UserResponse;
import solitour_backend.solitour.user.service.UserService;

/**
 * packageName    : solitour_backend.solitour.user.controller<br>
 * fileName       : UserController.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-03-24<br>
 * description    : user 관련 api controller 클래스입니다. <br>
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findUserId(@PathVariable String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserIdByEmail(email));
    }
}
