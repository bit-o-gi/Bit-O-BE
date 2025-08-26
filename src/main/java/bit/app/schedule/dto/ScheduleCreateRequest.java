package bit.app.schedule.dto;

import bit.app.schedule.domain.Schedule;
import bit.app.schedule.enums.ScheduleColor;
import bit.app.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleCreateRequest {

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

    @NotNull(message = "색상 코드가 필요합니다.")
    private ScheduleColor color;

    @Builder
    public ScheduleCreateRequest(String title, String content, String location,
                                 LocalDateTime startDateTime, LocalDateTime endDateTime, ScheduleColor color) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.color = color;
    }

    public Schedule toEntity(UserEntity user) {
        return Schedule.builder()
                .title(title)
                .user(user)
                .content(content)
                .location(location)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .color(color)
                .build();
    }
}
