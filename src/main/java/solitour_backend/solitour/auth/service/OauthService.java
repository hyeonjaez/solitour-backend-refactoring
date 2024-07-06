package solitour_backend.solitour.auth.service;


import jakarta.security.auth.message.AuthException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solitour_backend.solitour.auth.entity.Token;
import solitour_backend.solitour.auth.service.dto.response.AccessTokenResponse;
import solitour_backend.solitour.auth.service.dto.response.LoginResponse;
import solitour_backend.solitour.auth.service.dto.response.OauthLinkResponse;
import solitour_backend.solitour.auth.support.JwtTokenProvider;
import solitour_backend.solitour.auth.support.google.GoogleProvider;
import solitour_backend.solitour.auth.support.google.dto.GoogleUserResponse;
import solitour_backend.solitour.auth.support.kakao.KakaoConnector;
import solitour_backend.solitour.auth.support.kakao.KakaoProvider;
import solitour_backend.solitour.auth.support.kakao.dto.KakaoUserResponse;
import solitour_backend.solitour.user.entity.User;
import solitour_backend.solitour.user.entity.UserRepository;
import solitour_backend.solitour.user.user_status.UserStatus;

@RequiredArgsConstructor
@Service
public class OauthService {

  private final TokenService tokenService;
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final KakaoConnector kakaoConnector;
  private final KakaoProvider kakaoProvider;
  private final GoogleProvider googleProvider;

  public OauthLinkResponse generateAuthUrl(String type, String redirectUrl) {
    String oauthLink = getAuthLink(type, redirectUrl);
    return new OauthLinkResponse(oauthLink);
  }

  @Transactional
  public LoginResponse requestAccessToken(String type, String code, String redirectUrl) {
    KakaoUserResponse response = requestUserInfo(type, code, redirectUrl);

    User user = findOrSaveUser(response);
    String token = jwtTokenProvider.createAccessToken(user.getId());
    String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

    tokenService.synchronizeRefreshToken(user, refreshToken);

    return new LoginResponse(token, refreshToken);
  }

  private KakaoUserResponse requestUserInfo(String type, String code, String redirectUrl) {

    ResponseEntity<KakaoUserResponse> responseEntity = kakaoConnector.requestUserInfo(code,
        redirectUrl);

    return Optional.ofNullable(responseEntity.getBody())
        .orElseThrow(() -> new RuntimeException("카카오 사용자 정보를 가져오는데 실패했습니다."));
  }

  private User findOrSaveUser(KakaoUserResponse response) {
    String nickname = response.getKakaoAccount().getProfile().getNickName();
    return userRepository.findByNickname(nickname)
        .orElseGet(() -> saveUser(response));
  }

  private User saveUser(KakaoUserResponse response) {
    User user = User.builder()
        .userStatus(UserStatus.ACTIVATE)
        .oauthId(String.valueOf(response.getId()))
        .provider("kakao")
        .isAdmin(false)
        .name(response.getKakaoAccount().getName())
        .nickname(response.getKakaoAccount().getProfile().getNickName())
        .age(Integer.valueOf(response.getKakaoAccount().getBirthYear()))
        .sex(response.getKakaoAccount().getGender())
        .email(response.getKakaoAccount().getEmail())
        .createdAt(LocalDateTime.now())
        .build();
    return userRepository.save(user);
  }

  private String getAuthLink(String type, String redirectUrl) {
    return switch (type) {
      case "kakao" -> kakaoProvider.generateAuthUrl(redirectUrl);
      case "google" -> googleProvider.generateAuthUrl(redirectUrl);
      default -> throw new RuntimeException("지원하지 않는 oauth 타입입니다.");
    };
  }

  public AccessTokenResponse reissueAccessToken(Long userId) {
    boolean isExistMember = userRepository.existsById(userId);
    if (!isExistMember) {
      throw new RuntimeException("유효하지 않은 토큰입니다.");
    }
    String accessToken = jwtTokenProvider.createAccessToken(userId);
    return new AccessTokenResponse(accessToken);
  }

  @Transactional
  public void logout(Long memberId) {
    tokenService.deleteByMemberId(memberId);
  }
}
