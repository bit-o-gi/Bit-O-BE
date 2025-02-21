package bit.schedule.dto;

import bit.schedule.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
public class ScheduleResponse {
    @Schema(description = "스케줄 ID", example = "1")
    private final Long id;

    @Schema(description = "닉네임", example = "user1")
    private final String nickName;

    @Schema(description = "제목", example = "제목")
    private final String title;

    @Schema(description = "내용", example = "내용")
    private final String content;

    @Schema(description = "위치", example = "위치")
    private final String location;

    @Schema(description = "시작 일시", example = "2024-12-01T00:00:00")
    private final LocalDateTime startDateTime;

    @Schema(description = "종료 일시", example = "2024-12-02T00:00:00")
    private final LocalDateTime endDateTime;

    public ScheduleResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.nickName = schedule.getUser().getNickName();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.location = schedule.getLocation();
        this.startDateTime = schedule.getStartDateTime();
        this.endDateTime = schedule.getEndDateTime();
    }
}
