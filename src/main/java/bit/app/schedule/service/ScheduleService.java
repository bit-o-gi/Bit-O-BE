package bit.app.schedule.service;

import bit.app.schedule.domain.Schedule;
import bit.app.schedule.dto.ScheduleCreateRequest;
import bit.app.schedule.dto.ScheduleResponse;
import bit.app.schedule.dto.ScheduleUpdateRequest;
import bit.app.schedule.exception.ScheduleNotFoundException;
import bit.app.schedule.repository.ScheduleRepository;
import bit.app.user.domain.User;
import bit.app.user.entity.UserEntity;
import bit.app.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    public ScheduleResponse getSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findSchedule(userId, scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);
        return new ScheduleResponse(schedule);
    }

    public List<ScheduleResponse> getSchedulesByUserId(Long userId, Integer year, Integer month) {
        List<Schedule> schedules = scheduleRepository.findAllUserScheduleByYearAndMonth(userId, year, month);
        return schedules.stream()
                .map(ScheduleResponse::new)
                .toList();
    }

    public List<ScheduleResponse> getCoupleSchedulesByUserId(Long userId, Integer year, Integer month) {
        List<Schedule> schedules = scheduleRepository.findAllCoupleScheduleByYearAndMonth(userId, year, month);
        return schedules.stream()
                .map(ScheduleResponse::new)
                .toList();
    }

    @Transactional
    public ScheduleResponse saveSchedule(Long userId, ScheduleCreateRequest scheduleCreateRequest) {
        User user = userService.getById(userId);
        UserEntity userEntity = UserEntity.from(user);
        Schedule schedule = scheduleCreateRequest.toEntity(userEntity);
        scheduleRepository.save(schedule);
        return new ScheduleResponse(schedule);
    }

    @Transactional
    public ScheduleResponse updateSchedule(Long userId, Long scheduleId, ScheduleUpdateRequest scheduleUpdateRequest) {
        Schedule schedule = scheduleRepository.findSchedule(userId, scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);
        schedule.update(scheduleUpdateRequest);
        return new ScheduleResponse(schedule);
    }

    @Transactional
    public ScheduleResponse deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findSchedule(userId, scheduleId)
                .orElseThrow(ScheduleNotFoundException::new);
        scheduleRepository.deleteById(scheduleId);
        return new ScheduleResponse(schedule);
    }
}
