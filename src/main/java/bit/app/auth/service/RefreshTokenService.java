package bit.app.auth.service;

import bit.app.auth.domain.RefreshToken;

public interface RefreshTokenService {
    RefreshToken getByRefreshToken(String refreshToken);
}
