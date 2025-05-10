package bit.schedule.repository;

import bit.schedule.domain.Schedule;
import java.util.List;
import java.util.Optional;

public interface CustomScheduleRepository {

    List<Schedule> findAllCoupleScheduleByYearAndMonth(Long userId, Integer year, Integer month);

    Optional<Schedule> findSchedule(Long userId, Long scheduleId);

    List<Schedule> findAllUserScheduleByYearAndMonth(Long userId, Integer year, Integer month);
}
