package bit.app.schedule.dto;


import bit.app.schedule.enums.ScheduleColor;
import bit.app.schedule.validate.annotation.ScheduleUpdateValidate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@ScheduleUpdateValidate
public class ScheduleUpdateRequest {

    private final String title;

    private final String content;

    private final String location;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    private final ScheduleColor color;

    @Builder
    public ScheduleUpdateRequest(String title, String content, String location,
                                 LocalDateTime startDateTime, LocalDateTime endDateTime, ScheduleColor color) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.color = color;
    }
}
