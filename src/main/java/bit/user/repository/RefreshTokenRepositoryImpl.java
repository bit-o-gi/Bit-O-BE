package bit.user.repository;

import bit.user.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return Optional.empty();
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return Optional.empty();
    }
}
