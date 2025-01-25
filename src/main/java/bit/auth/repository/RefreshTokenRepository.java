package bit.auth.repository;

import bit.auth.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    RefreshToken save(RefreshToken refreshToken);
}
