package bit.schedule.dto;

import bit.schedule.domain.Schedule;
import bit.schedule.enums.ScheduleColor;
import bit.user.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "색상 코드가 필요합니다.")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$", message = "색상 코드는 #RRGGBB 형식이어야 합니다")
    private String colorCode;

    @Builder
    public ScheduleCreateRequest(String title, String content, String location,
        LocalDateTime startDateTime, LocalDateTime endDateTime, String color) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.colorCode = color;
    }

    public Schedule toEntity(UserEntity user) {
        return Schedule.builder()
            .title(title)
            .user(user)
            .content(content)
            .location(location)
            .startDateTime(startDateTime)
            .endDateTime(endDateTime)
            .color(ScheduleColor.fromHexCode(colorCode))
            .build();
    }
}
