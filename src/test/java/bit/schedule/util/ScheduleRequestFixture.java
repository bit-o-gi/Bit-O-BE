package bit.schedule.util;

import bit.schedule.dto.ScheduleCreateRequest;
import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.enums.ScheduleColor;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduleRequestFixture {

    public static ScheduleCreateRequest getNewScheduleCreateRequest(LocalDateTime start,
        LocalDateTime end) {
        return ScheduleCreateRequest.builder()
            .title("title")
            .content("content")
            .location("location")
            .startDateTime(start)
            .endDateTime(end)
            .color(ScheduleColor.RED)
            .build();
    }

    public static ScheduleUpdateRequest getNewScheduleUpdateRequest(LocalDateTime start,
        LocalDateTime end) {
        return ScheduleUpdateRequest.builder()
            .title("title")
            .content("content")
            .location("location")
            .startDateTime(start)
            .endDateTime(end)
            .color(ScheduleColor.RED)
            .build();
    }

    public static List<ScheduleCreateRequest> getNewScheduleRequests() {
        return List.of(
            ScheduleCreateRequest.builder()
                .content("content")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .location("location")
                .color(ScheduleColor.RED)
                .build(),
            ScheduleCreateRequest.builder()
                .title("title")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .location("location")
                .color(ScheduleColor.RED)
                .build(),
            ScheduleCreateRequest.builder()
                .title("title")
                .content("content")
                .endDateTime(LocalDateTime.now().plusHours(1))
                .location("location")
                .color(ScheduleColor.RED)
                .build(),
            ScheduleCreateRequest.builder()
                .title("title")
                .content("content")
                .startDateTime(LocalDateTime.now())
                .location("location")
                .color(ScheduleColor.RED)
                .build(),
            ScheduleCreateRequest.builder()
                .title("title")
                .content("content")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .location("location")
                .build(),
            ScheduleCreateRequest.builder()
                .title("title")
                .content("content")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .color(ScheduleColor.RED)
                .build()
        );
    }
}
