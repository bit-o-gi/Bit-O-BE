package bit.schedule.util;

import bit.schedule.domain.Schedule;
import bit.user.entity.UserEntity;

import java.time.LocalDateTime;

import static bit.schedule.util.UserEntityFixture.getNewUserEntity;

public class ScheduleFixture {

    public static Schedule getNewSchedule(LocalDateTime start, LocalDateTime end) {
        return Schedule.builder()
                .user(getNewUserEntity(1L))
                .title("title")
                .content("content")
                .location("location")
                .startDateTime(start)
                .endDateTime(end)
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
                .build();
    }
}
