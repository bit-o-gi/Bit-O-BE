package bit.user.service;

import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.oauth.enums.OauthPlatformType;
import bit.user.repository.UserJpaRepository;
import bit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    private void saveOrUpdate(OAuth2User user) {
        Map<String, Object> attribute = user.getAttributes();
        String email = (String) attribute.get("email");
        String nickname = (String) attribute.get("nickname");
        Long id = (Long) attribute.get("id");
        LocalDateTime connected_at = (LocalDateTime) attribute.get("connected_at");

        Optional<User> byEmail = userRepository.findByEmail(email);

        if (byEmail.isEmpty()) {
            User userDomain = User.builder()
                    .nickName(nickname)
                    .providerId(id)
                    .email(email)
                    .platform(OauthPlatformType.KAKAO)
                    .connectedDt(connected_at)
                    .build();

            userRepository.save(userDomain);
        }

    }
}
