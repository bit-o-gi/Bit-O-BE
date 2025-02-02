package bit.schedule.dto;

import bit.schedule.domain.Schedule;
import bit.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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

    @Builder
    public ScheduleCreateRequest(String title, String content, String location, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Schedule toEntity(UserEntity user) {
        return Schedule.builder()
                .title(title)
                .user(user)
                .content(content)
                .location(location)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }
}
