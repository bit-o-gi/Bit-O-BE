package bit.schedule.service;

import bit.schedule.domain.Schedule;
import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.exception.ScheduleNotFoundException;
import bit.schedule.repository.ScheduleRepository;
import bit.user.entity.UserEntity;
import bit.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public ScheduleResponse getSchedule(Long userId, Long scheduleId) {
        Schedule schedule = findByUserIdAndId(userId, scheduleId);
        return new ScheduleResponse(schedule);
    }

    @Override
    public List<ScheduleResponse> getSchedulesByUserId(Long userId) {
        return scheduleRepository.findByUserId(userId).stream()
                .map(ScheduleResponse::new)
                .toList();
    }

    @Override
    public List<ScheduleResponse> getCoupleSchedulesByUserId(Long userId) {
        return scheduleRepository.findByCoupleScheduleByUserId(userId);
    }

    @Transactional
    @Override
    public ScheduleResponse saveSchedule(Long userId, ScheduleCreateRequest scheduleCreateRequest) {
        UserEntity user = userJpaRepository.getReferenceById(userId);
        Schedule schedule = scheduleCreateRequest.toEntity(user);
        scheduleRepository.save(schedule);
        return new ScheduleResponse(schedule);
    }

    // TODO 수정 권한을 커플에게 부여할 것인지?
    @Transactional
    @Override
    public ScheduleResponse updateSchedule(Long userId, Long scheduleId, ScheduleUpdateRequest scheduleUpdateRequest) {
        Schedule schedule = findByUserIdAndId(userId, scheduleId);
        schedule.update(scheduleUpdateRequest);
        return new ScheduleResponse(schedule);
    }

    @Transactional
    @Override
    public ScheduleResponse deleteSchedule(Long userId, Long scheduleId) {
        Schedule schedule = findByUserIdAndId(userId, scheduleId);
        scheduleRepository.deleteById(scheduleId);
        return new ScheduleResponse(schedule);
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    protected Schedule findByUserIdAndId(Long userId, Long scheduleId) {
        return scheduleRepository.findByUserIdAndId(userId, scheduleId).orElseThrow(ScheduleNotFoundException::new);
    }
}
