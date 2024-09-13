package solitour_backend.solitour.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class HmacUtils {
    private HmacUtils() {
    }

    @Value("${view.count.cookie.key}")
    private String viewCountKeyTemp;

    private static String viewCountKey;

    public static final String HMAC_SHA256 = "HmacSHA256";

    @PostConstruct
    public void init() {
        viewCountKey = this.viewCountKeyTemp;
    }

    public static String generateHmac(String data) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(viewCountKey.getBytes(), HMAC_SHA256);
        mac.init(secretKeySpec);

        byte[] hmacBytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes); // Base64로 인코딩된 HMAC 값 반환
    }

    public static boolean verifyHmac(String data, String providedHmac) throws Exception {
        String generatedHmac = generateHmac(data);
        return providedHmac.equals(generatedHmac); // 제공된 HMAC과 생성된 HMAC 비교
    }
}
