package bit.integration.jwt;

import bit.config.jwt.JwtFactory;
import bit.config.jwt.JwtProperties;
import bit.config.jwt.TokenProvider;
import bit.user.domain.User;
import bit.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class TokenProviderIntegrationTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("유저 정보와 만료 기간을 전달해 jwt 토큰을 만들 수 있다.")
    @Test
    void generateTokenIsSuccessful() {
        // given
        User testUser = userRepository.save(User.builder().email("test@email.com").build());

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then
        Long userId = Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody().get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    @DisplayName("유효성 검사 시 만료된 토큰이면 검증에 실패한다.")
    @Test
    void throwExceptionInvalidToken() {
        // given
        String token = JwtFactory.builder().expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis())).build().createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("유효성 검사 시 유효한 토큰이면 검증에 성공한다.")
    @Test
    void validTokenIsSuccessful() {
        // given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthenticationIsSuccessful() {
        // given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);
        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserIdIsSuccessful() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder().claims(Map.of("id", userId)).build().createToken(jwtProperties);

        // when
        Long userIdByToken = tokenProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }

}

