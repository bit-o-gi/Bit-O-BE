package bit.user.repository;

import bit.user.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserId(Long userId);

    RefreshToken findByRefreshToken(String refreshToken);

}
