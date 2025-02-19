package bit.couple.repository;

import bit.couple.domain.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {
    @Query("SELECT c FROM Couple c WHERE c.initiatorUser.id = :userId OR c.partnerUser.id = :userId")
    Optional<Couple> findByUserId(@Param("userId") Long userId);
//    Optional<Couple> findById(String coupleId);
}