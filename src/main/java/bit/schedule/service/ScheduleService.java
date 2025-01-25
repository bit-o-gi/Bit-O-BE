package bit.schedule.service;

import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;

import java.util.List;

public interface ScheduleService {
    ScheduleResponse getSchedule(Long userId, Long scheduleId);

    List<ScheduleResponse> getSchedulesByUserId(Long userId);

    List<ScheduleResponse> getCoupleSchedulesByUserId(Long userId);

    ScheduleResponse saveSchedule(Long userId, ScheduleCreateRequest scheduleCreateRequest);

    ScheduleResponse updateSchedule(Long userId, Long scheduleId, ScheduleUpdateRequest scheduleUpdateRequest);

    ScheduleResponse deleteSchedule(Long userId, Long scheduleId);
}
