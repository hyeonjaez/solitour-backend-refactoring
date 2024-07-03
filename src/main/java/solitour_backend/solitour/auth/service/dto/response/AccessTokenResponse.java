package solitour_backend.solitour.auth.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessTokenResponse {

  private final String accessToken;
}
