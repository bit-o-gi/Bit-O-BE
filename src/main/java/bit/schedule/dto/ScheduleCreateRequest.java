package bit.schedule.dto;

import bit.schedule.domain.Schedule;
import bit.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleCreateRequest {

    @NotNull(message = "유저 아이디가 필요합니다.")
    private Long userId;

    @NotNull(message = "제목이 필요합니다.")
    private String title;

    @NotNull(message = "내용이 필요합니다.")
    private String content;

    @NotNull(message = "시작 일시가 필요합니다.")
    private LocalDateTime startDateTime;

    @NotNull(message = "종료 일시가 필요합니다.")
    private LocalDateTime endDateTime;

    @Builder
    public ScheduleCreateRequest(Long userId, String title, String content, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Schedule toEntity(UserEntity user) {
        return Schedule.builder()
                .title(title)
                .user(user)
                .content(content)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }
}
