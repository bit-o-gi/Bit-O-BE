package bit.schedule.controller;

import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
public class ScheduleController implements ScheduleControllerDoc {
    private final ScheduleService scheduleService;

    @GetMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse getSchedule(@PathVariable Long scheduleId) {
        return scheduleService.getSchedule(scheduleId);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getScheduleByUser(@PathVariable Long userId) {
        return scheduleService.getSchedulesByUserId(userId);
    }

    @GetMapping("/couple/{coupleId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getScheduleByCouple(@PathVariable Long coupleId) {
        return scheduleService.getSchedulesByCoupleId(coupleId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse createSchedule(@Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest) {
        return scheduleService.saveSchedule(scheduleCreateRequest);
    }

    @PutMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse updateSchedule(@PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest) {
        return scheduleService.updateSchedule(scheduleId, scheduleUpdateRequest);
    }

    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse deleteSchedule(@PathVariable Long scheduleId) {
        return scheduleService.deleteSchedule(scheduleId);
    }
}
