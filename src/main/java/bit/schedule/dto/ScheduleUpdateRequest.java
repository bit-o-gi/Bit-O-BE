package bit.schedule.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleUpdateRequest {

    @NotNull(message = "제목이 필요합니다.")
    private String title;

    @NotNull(message = "내용이 필요합니다.")
    private String content;

    @NotNull(message = "위치가 필요합니다.")
    private String location;

    @NotNull(message = "시작 일시가 필요합니다.")
    private LocalDateTime startDateTime;

    @NotNull(message = "종료 일시가 필요합니다.")
    private LocalDateTime endDateTime;

    @Builder
    public ScheduleUpdateRequest(String title, String content, String location, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}