package bit.schedule.repository;

import bit.schedule.domain.Schedule;
import bit.schedule.dto.ScheduleResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
//    List<Schedule> findByUserId(Long userId);

    //    @Query("""
//            SELECT new bit.schedule.dto.ScheduleResponse(s, s.user)
//            FROM Schedule s
//            WHERE s.user.id = :userId
//            """)
    @EntityGraph(attributePaths = {"user"})
    List<Schedule> findByUserId(Long userId);


    @Query("""
            SELECT new bit.schedule.dto.ScheduleResponse(s, s.user)
            FROM Schedule s
            JOIN UserEntity u2 ON s.user.couple = u2.couple
            WHERE u2.id = :userId
            """)
    List<ScheduleResponse> findByCoupleScheduleByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<Schedule> findByUserIdAndId(Long userId, Long id);
}
