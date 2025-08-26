package bit.app.day.repository;

import bit.app.couple.domain.Couple;
import bit.app.day.domain.Day;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, Long> {
    Optional<Day> findByCouple(Couple couple);
}
