package bit.user.oauth.kakao.controller;

import bit.user.oauth.port.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

    @Value("${kakao.client.id}")
    String clientId;
    @Value("${kakao.redirect.uri}")
    String redirectUri;
    @Value("${kakao.client.secret}")
    String clientSecret;

    @GetMapping("/kakao")
    public void getAuthKakao(HttpServletResponse response) throws IOException {
        String authKakaoUrl = "https://kauth.kakao.com/oauth/authorize?"
                + "client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&scope=account_email,profile_nickname";
        response.sendRedirect(authKakaoUrl);
    }

    @PostMapping("/kakao/token")
    public ResponseEntity<String> postAuthTokenToKakao(@RequestBody String code) throws JsonProcessingException {
        return ResponseEntity.ok(oAuthService.getToken(code, clientId, redirectUri, clientSecret));
    }

    @PostMapping("/kakao/access")
    public ResponseEntity<String> postGetKaKaoUserInfo(@RequestBody String accessToken) throws JsonProcessingException {
        return ResponseEntity.ok(oAuthService.getUserInfo(accessToken));
    }
}
