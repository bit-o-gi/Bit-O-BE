package bit.user.service;

import bit.user.domain.RefreshToken;

public interface RefreshTokenService {
    RefreshToken getByRefreshToken(String refreshToken);
}
