package bit.schedule.util;

import static bit.schedule.util.UserEntityFixture.getNewUserEntity;

import bit.app.schedule.domain.Schedule;
import bit.app.schedule.enums.ScheduleColor;
import bit.app.user.entity.UserEntity;
import java.time.LocalDateTime;

public class ScheduleFixture {

    public static Schedule getNewSchedule(LocalDateTime start, LocalDateTime end) {
        return Schedule.builder()
                .user(getNewUserEntity(1L))
                .title("title")
                .content("content")
                .location("location")
                .startDateTime(start)
                .endDateTime(end)
                .color(ScheduleColor.RED)
                .build();
    }

    public static Schedule getNewSchedule(UserEntity user) {
        return Schedule.builder()
                .user(user)
                .title("title")
                .content("content")
                .location("location")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .color(ScheduleColor.RED)
                .build();
    }
}
