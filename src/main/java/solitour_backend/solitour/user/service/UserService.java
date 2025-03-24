package solitour_backend.solitour.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import solitour_backend.solitour.user.dto.UserResponse;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.repository.UserRepository;

/**
 * packageName    : solitour_backend.solitour.user.service<br>
 * fileName       : UserService.java<br>
 * author         : Fiat_lux<br>
 * date           : 2025-03-24<br>
 * description    :  User 비즈니스 로직 클래스입니다.<br>
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return new UserResponse(user.getId());
    }
}
