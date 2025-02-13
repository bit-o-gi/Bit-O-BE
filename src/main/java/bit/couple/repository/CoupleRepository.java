package bit.couple.repository;

import bit.couple.domain.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {

//    Optional<Couple> findById(String coupleId);
}