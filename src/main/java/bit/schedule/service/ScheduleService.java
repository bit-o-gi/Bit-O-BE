package bit.schedule.service;

import bit.schedule.domain.Schedule;
import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.exception.ScheduleNotFoundException;
import bit.schedule.repository.ScheduleRepository;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    public ScheduleResponse getSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findSchedule(userId, scheduleId).orElseThrow(ScheduleNotFoundException::new);
        return new ScheduleResponse(schedule);
    }

    public List<ScheduleResponse> getSchedulesByUserId(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllUserSchedule(userId);
        return schedules.stream()
                .map(ScheduleResponse::new)
                .toList();
    }

    public List<ScheduleResponse> getCoupleSchedulesByUserId(Long userId) {
        List<Schedule> schedules = scheduleRepository.findAllCoupleSchedule(userId);
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
        Schedule schedule = scheduleRepository.findSchedule(userId, scheduleId).orElseThrow(ScheduleNotFoundException::new);
        schedule.update(scheduleUpdateRequest);
        return new ScheduleResponse(schedule);
    }

    @Transactional
    public ScheduleResponse deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findSchedule(userId, scheduleId).orElseThrow(ScheduleNotFoundException::new);
        scheduleRepository.deleteById(scheduleId);
        return new ScheduleResponse(schedule);
    }
}
