package solitour_backend.solitour.auth.support;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") final String secretKey,
                            @Value("${security.jwt.token.access-token-expire-length}") final long accessTokenValidityInMilliseconds,
                            @Value("${security.jwt.token.refresh-token-expire-length}") final long refreshTokenValidityInMilliseconds) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    public String createAccessToken(Long payload) {
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken(Long payload) {
        return createToken(payload, refreshTokenValidityInMilliseconds);
    }

    private String createToken(Long payload, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .subject(Long.toString(payload))
            .issuedAt(new Date())
            .expiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
