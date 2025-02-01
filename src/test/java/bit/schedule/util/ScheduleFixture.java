package bit.schedule.util;

import bit.schedule.domain.Schedule;

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
}
