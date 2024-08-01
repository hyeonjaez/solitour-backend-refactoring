package solitour_backend.solitour.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import solitour_backend.solitour.auth.config.AuthenticationPrincipal;
import solitour_backend.solitour.user.dto.UpdateNicknameRequest;
import solitour_backend.solitour.user.exception.NicknameAlreadyExistsException;
import solitour_backend.solitour.user.exception.UserNotExistsException;
import solitour_backend.solitour.user.service.UserService;
import solitour_backend.solitour.user.service.dto.response.UserInfoResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;


    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> retrieveUserInfo(@AuthenticationPrincipal Long userId) {
        UserInfoResponse response = service.retrieveUserInfo(userId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal Long userId, @RequestBody UpdateNicknameRequest request) {
        try {
            service.updateNickname(userId, request.nickname());
            return ResponseEntity.ok("Nickname updated successfully");
        } catch (UserNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (NicknameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred");
        }
    }

}
