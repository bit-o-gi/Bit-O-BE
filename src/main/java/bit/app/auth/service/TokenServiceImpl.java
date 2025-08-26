package bit.app.auth.service;

import static bit.config.oauth.OAuth2SuccessHandler.ACCESS_TOKEN_DURATION;

import bit.app.user.domain.User;
import bit.app.user.service.UserService;
import bit.config.jwt.TokenProvider;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Override
    public String createNewAccessToken(String refreshToken) {
        // 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid RefreshToken");
        }

        Long userId = refreshTokenService.getByRefreshToken(refreshToken).getUserId();
        User user = userService.getById(userId);
        return tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
    }

    @Override
    public String createNewFakeToken() {
        User user = User.builder()
                .id(0L)
                .email("fakeEmail@fakeDomain.com")
                .build();
        return tokenProvider.generateToken(user, Duration.ofHours(3));
    }
}
