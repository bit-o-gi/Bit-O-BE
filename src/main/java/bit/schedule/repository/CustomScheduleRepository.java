package bit.schedule.repository;

import bit.schedule.domain.Schedule;

import java.util.List;
import java.util.Optional;

public interface CustomScheduleRepository {
    List<Schedule> findCoupleSchedule(Long userId);

    Optional<Schedule> findSchedule(Long userId, Long scheduleId);

    List<Schedule> findUserSchedule(Long userId);
}
