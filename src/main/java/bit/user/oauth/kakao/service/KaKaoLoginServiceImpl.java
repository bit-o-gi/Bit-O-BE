package bit.user.oauth.kakao.service;

import bit.user.dto.UserCreateRequest;
import bit.user.exception.KaKaoRestTemplateProcessingException;
import bit.user.oauth.kakao.domain.KakaoUserInfo;
import bit.user.oauth.port.OAuthService;
import bit.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KaKaoLoginServiceImpl implements OAuthService {
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public String getToken(String code, String clientId, String redirectUri, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        httpBody.add("grant_type", "authorization_code");
        httpBody.add("client_id", clientId);
        httpBody.add("redirect_uri", redirectUri);
        httpBody.add("code", code);
        httpBody.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", httpEntity,
                String.class);

        return response.getBody();
    }

    @Override
    public String getUserInfo(String accessToken) throws KaKaoRestTemplateProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://kapi.kakao.com/v2/user/me", httpEntity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new KaKaoRestTemplateProcessingException("카카오 로그인 중 응답이 없습니다."));
            createUserDomain(parseJson(responseBody));
        }
        return response.getBody();
    }

    private void createUserDomain(JsonNode jsonNode) {
        if (!userService.isRegisteredEmail(KakaoUserInfo.of(jsonNode).getEmail())) {
            userService.createUser(UserCreateRequest.fromKakaoUser(KakaoUserInfo.of(jsonNode)));
        }
    }

    private JsonNode parseJson(String responseBody) throws KaKaoRestTemplateProcessingException {
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new KaKaoRestTemplateProcessingException("카카오 로그인 중 에러가 발생하였습니다.");
        }
    }
}
