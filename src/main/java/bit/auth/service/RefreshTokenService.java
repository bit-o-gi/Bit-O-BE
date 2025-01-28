package bit.auth.service;

import bit.auth.domain.RefreshToken;

public interface RefreshTokenService {
    RefreshToken getByRefreshToken(String refreshToken);
}
