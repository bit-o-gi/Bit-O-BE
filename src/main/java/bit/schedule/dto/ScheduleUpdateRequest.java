package bit.schedule.dto;


import bit.schedule.validate.annotation.ScheduleUpdateValidate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@ScheduleUpdateValidate
public class ScheduleUpdateRequest {

    private final String title;

    private final String content;

    private final String location;

    private final LocalDateTime startDateTime;

    private final LocalDateTime endDateTime;

    @Builder
    public ScheduleUpdateRequest(String title, String content, String location, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}