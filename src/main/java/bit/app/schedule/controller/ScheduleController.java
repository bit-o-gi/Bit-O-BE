package bit.app.schedule.controller;

import bit.app.auth.domain.UserPrincipal;
import bit.app.schedule.dto.ScheduleCreateRequest;
import bit.app.schedule.dto.ScheduleResponse;
import bit.app.schedule.dto.ScheduleUpdateRequest;
import bit.app.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
public class ScheduleController implements ScheduleControllerDoc {

    private final ScheduleService scheduleService;

    @GetMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse getSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @PathVariable Long scheduleId) {
        return scheduleService.getSchedule(userPrincipal.getId(), scheduleId);
    }

    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getScheduleByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @RequestParam @Min(1000) @Max(9999) Integer year,
                                                    @RequestParam @Min(1) @Max(12) Integer month) {
        return scheduleService.getSchedulesByUserId(userPrincipal.getId(), year, month);
    }

    @GetMapping("/couple")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleResponse> getScheduleByCouple(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @RequestParam @Min(1000) @Max(9999) Integer year,
                                                      @RequestParam @Min(1) @Max(12) Integer month) {
        return scheduleService.getCoupleSchedulesByUserId(userPrincipal.getId(), year, month);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse createSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest) {
        return scheduleService.saveSchedule(userPrincipal.getId(), scheduleCreateRequest);
    }

    @PutMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse updateSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable Long scheduleId,
                                           @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest) {
        return scheduleService.updateSchedule(userPrincipal.getId(), scheduleId, scheduleUpdateRequest);
    }

    @DeleteMapping("/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleResponse deleteSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                           @PathVariable Long scheduleId) {
        return scheduleService.deleteSchedule(userPrincipal.getId(), scheduleId);
    }
}
