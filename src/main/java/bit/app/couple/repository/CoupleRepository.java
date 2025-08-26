package bit.app.couple.repository;

import bit.app.couple.domain.Couple;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {
    @Query("SELECT c FROM Couple c LEFT JOIN FETCH c.initiatorUser i LEFT JOIN FETCH c.partnerUser p WHERE i.id = :userId OR p.id = :userId")
    Optional<Couple> findByUserId(@Param("userId") Long userId);
}
