package bit.couple.repository;

import bit.couple.domain.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {
    @Query("SELECT c FROM Couple c LEFT JOIN FETCH c.initiatorUser i LEFT JOIN FETCH c.partnerUser p WHERE i.id = :userId OR p.id = :userId")
    Optional<Couple> findByUserId(@Param("userId") Long userId);
}