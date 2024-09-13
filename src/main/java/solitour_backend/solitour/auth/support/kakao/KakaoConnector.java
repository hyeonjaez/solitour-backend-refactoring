package solitour_backend.solitour.auth.support.kakao;


import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import solitour_backend.solitour.auth.support.kakao.dto.KakaoTokenAndUserResponse;
import solitour_backend.solitour.auth.support.kakao.dto.KakaoTokenResponse;
import solitour_backend.solitour.auth.support.kakao.dto.KakaoUserResponse;

@Getter
@RequiredArgsConstructor
@Component
public class KakaoConnector {

    private static final String BEARER_TYPE = "Bearer";
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private final KakaoProvider provider;

    public KakaoTokenAndUserResponse requestKakaoUserInfo(String code, String redirectUrl) {
        KakaoTokenResponse kakaoToken = requestAccessToken(code, redirectUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", String.join(" ", BEARER_TYPE, kakaoToken.getAccessToken()));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> responseEntity = REST_TEMPLATE.exchange(provider.getUserInfoUrl(), HttpMethod.GET, entity,
                KakaoUserResponse.class);

        return new KakaoTokenAndUserResponse(kakaoToken,responseEntity.getBody());

    }

    public KakaoTokenResponse requestAccessToken(String code, String redirectUrl) {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
                createBody(code, redirectUrl), createHeaders());

        return REST_TEMPLATE.postForEntity(
                provider.getAccessTokenUrl(),
                entity, KakaoTokenResponse.class).getBody();
    }

    public String refreshToken(String refreshToken) {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
                createRefreshBody(refreshToken), createHeaders());

        return REST_TEMPLATE.postForEntity(
                provider.getAccessTokenUrl(),
                entity, KakaoTokenResponse.class).getBody().getAccessToken();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private MultiValueMap<String, String> createBody(String code, String redirectUrl) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", provider.getClientId());
        body.add("client_secret", provider.getClientSecret());
        body.add("redirect_uri", redirectUrl);
        body.add("grant_type", provider.getGrantType());
        return body;
    }

    private MultiValueMap<String, String> createRefreshBody(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", provider.getClientId());
        body.add("client_secret", provider.getClientSecret());
        body.add("grant_type", provider.getRefreshGrantType());
        body.add("refresh_token", refreshToken);
        return body;
    }

    public HttpStatusCode requestRevoke(String token) throws IOException {
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(createRevokeHeaders(token));

        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(provider.getRevokeUrl(), entity, String.class);

        return response.getStatusCode();
    }

    private HttpHeaders createRevokeHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", String.join(" ", BEARER_TYPE, token));
        return headers;
    }
}
