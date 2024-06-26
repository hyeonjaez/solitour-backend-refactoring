package solitour_backend.solitour.auth.service;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.auth.service.dto.response.LoginResponse;
import solitour_backend.solitour.auth.service.dto.response.OauthLinkResponse;
import solitour_backend.solitour.auth.support.JwtTokenProvider;
import solitour_backend.solitour.auth.support.kakao.KakaoConnector;
import solitour_backend.solitour.auth.support.kakao.KakaoProvider;
import solitour_backend.solitour.auth.support.kakao.dto.KakaoUserResponse;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.entity.UserRepository;

@RequiredArgsConstructor
@Service
public class OauthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoConnector oauthConnector;
    private final KakaoProvider oauthProvider;

    public OauthLinkResponse generateAuthUrl(String redirectUrl) {
        String oauthLink = oauthProvider.generateAuthUrl(redirectUrl);
        return new OauthLinkResponse(oauthLink);
    }

    @Transactional
    public LoginResponse requestAccessToken(String code, String redirectUrl) {
        KakaoUserResponse response = requestUserInfo(code, redirectUrl);

        User user = saveUser(response);
        String token = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        return new LoginResponse(token, refreshToken);
    }

    private KakaoUserResponse requestUserInfo(String code, String redirectUrl) {
        ResponseEntity<KakaoUserResponse> responseEntity = oauthConnector.requestUserInfo(code, redirectUrl);

        return Optional.ofNullable(responseEntity.getBody())
            .orElseThrow(() -> new RuntimeException("카카오 사용자 정보를 가져오는데 실패했습니다."));
    }

    private User saveUser(KakaoUserResponse response) {
        User user = User.builder()
            .name(response.getName())
            .build();
        return userRepository.save(user);
    }
}
