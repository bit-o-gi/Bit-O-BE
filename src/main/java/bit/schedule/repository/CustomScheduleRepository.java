package bit.schedule.repository;

import bit.schedule.domain.Schedule;

import java.util.List;
import java.util.Optional;

public interface CustomScheduleRepository {
    List<Schedule> findAllCoupleSchedule(Long userId);

    Optional<Schedule> findSchedule(Long userId, Long scheduleId);

    List<Schedule> findAllUserSchedule(Long userId);
}
