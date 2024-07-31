package bit.schedule.service;

import bit.schedule.dto.ScheduleRequest;
import bit.schedule.repository.ScheduleRepository;
import bit.schedule.domain.Schedule;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public Schedule save(ScheduleRequest scheduleRequest) {
        checkScheduleStartToEndValid(scheduleRequest.getStartDateTime(), scheduleRequest.getEndDateTime());

        return scheduleRepository.save(scheduleRequest.toEntity());
    }

    public Schedule patch(Long id, ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));
        checkScheduleStartToEndValid(scheduleRequest.getStartDateTime(), schedule.getEndDateTime());

        schedule.update(scheduleRequest);
        return scheduleRepository.save(schedule);
    }

    public Schedule delete(Long id) {
        Schedule deleteSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));
        scheduleRepository.deleteById(id);
        return deleteSchedule;
    }

    private void checkScheduleStartToEndValid(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }
    }
}