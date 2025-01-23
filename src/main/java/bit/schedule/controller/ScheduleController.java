package bit.schedule.controller;

import bit.auth.domain.UserPrincipal;
import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
public class ScheduleController implements ScheduleControllerDoc {
    private final ScheduleService scheduleService;

    @GetMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse getSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long scheduleId) {
        return scheduleService.getSchedule(userPrincipal.getId(), scheduleId);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getScheduleByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return scheduleService.getSchedulesByUserId(userPrincipal.getId());
    }

    @GetMapping("/couple")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getScheduleByCouple(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return scheduleService.getCoupleSchedulesByUserId(userPrincipal.getId());
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse createSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest) {
        return scheduleService.saveSchedule(userPrincipal.getId(), scheduleCreateRequest);
    }

    @PutMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse updateSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long scheduleId, @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest) {
        return scheduleService.updateSchedule(userPrincipal.getId(), scheduleId, scheduleUpdateRequest);
    }

    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse deleteSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long scheduleId) {
        return scheduleService.deleteSchedule(userPrincipal.getId(), scheduleId);
    }
}
