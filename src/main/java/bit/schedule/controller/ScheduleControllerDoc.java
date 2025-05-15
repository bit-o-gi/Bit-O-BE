package bit.schedule.controller;

import bit.auth.domain.UserPrincipal;
import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleResponse;
import bit.schedule.dto.ScheduleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ScheduleControllerDoc {

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "스케줄 조회 API", description = "스케줄을 조회합니다",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
        }
    )
    ScheduleResponse getSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long scheduleId);

    @Operation(summary = "사용자별 스케줄 조회 API", description = "사용자별 스케줄을 조회합니다",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
        }
    )
    List<ScheduleResponse> getScheduleByUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam @Min(1000) @Max(9999) Integer year, @RequestParam @Min(1) @Max(12) Integer month);

    @Operation(summary = "커플별 스케줄 조회 API", description = "커플별 스케줄을 조회합니다",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
        }
    )
    List<ScheduleResponse> getScheduleByCouple(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam @Min(1000) @Max(9999) Integer year, @RequestParam @Min(1) @Max(12) Integer month);

    @Operation(summary = "스케줄 생성 API", description = "스케줄을 생성합니다",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "잘못된 요청"),
        }
    )
    ScheduleResponse createSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @Valid @RequestBody ScheduleCreateRequest scheduleCreateRequest);

    @Operation(summary = "스케줄 수정 API", description = "스케줄을 수정합니다",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 스케줄이 존재하지 않습니다."),
        }
    )
    ScheduleResponse updateSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long scheduleId,
        @Valid @RequestBody ScheduleUpdateRequest scheduleUpdateRequest);

    @Operation(summary = "스케줄 삭제 API", description = "스케줄을 삭제합니다",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "해당 스케줄이 존재하지 않습니다."),
        }
    )
    ScheduleResponse deleteSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Long scheduleId);
}
