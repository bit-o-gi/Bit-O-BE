package bit.user.oauth.kakao.controller;

import bit.config.oauth.KakaoOAuth2Properties;
import bit.user.oauth.port.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class KaKaoLoginController implements KaKaoLoginControllerDoc {
    private final OAuthService oAuthService;
    private final KakaoOAuth2Properties properties;

    @GetMapping("/kakao")
    public void getAuthKakao(HttpServletResponse response) throws IOException {
        String authKakaoUrl = properties.getAuthorizationUri()
                + "?client_id=" + properties.getClientId()
                + "&redirect_uri=" + properties.getRedirectUri()
                + "&response_type=" + properties.getResponseType()
                + "&scope=" + properties.getScope();
        response.sendRedirect(authKakaoUrl);
    }

    @PostMapping("/kakao/token")
    public ResponseEntity<String> postAuthTokenToKakao(@RequestBody String code) throws JsonProcessingException {
        return ResponseEntity.ok(oAuthService.getToken(
                code,
                properties.getClientId(),
                properties.getRedirectUri(),
                properties.getClientSecret()
        ));
    }

    @PostMapping("/kakao/access")
    public ResponseEntity<String> postGetKaKaoUserInfo(@RequestBody String accessToken) throws JsonProcessingException {
        return ResponseEntity.ok(oAuthService.getUserInfo(accessToken));
    }
}
