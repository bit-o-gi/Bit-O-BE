package bit.schedule.domain;

import static bit.schedule.util.ScheduleFixture.getNewSchedule;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bit.schedule.dto.ScheduleUpdateRequest;
import bit.schedule.enums.ScheduleColor;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @DisplayName("스케줄 객체를 수정 한다.")
    @Test
    void updateTest() {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleUpdateRequest scheduleUpdateRequest = ScheduleUpdateRequest.builder()
            .title("title update test")
            .content("content update test")
            .location("location update test")
            .startDateTime(LocalDateTime.of(2024, 9, 1, 1, 1))
            .endDateTime(LocalDateTime.of(2024, 9, 1, 1, 1))
            .build();
        //When
        schedule.update(scheduleUpdateRequest);
        //Then
        assertThat(schedule).extracting(
            "title",
            "content",
            "location",
            "startDateTime",
            "endDateTime"
        ).containsExactly(
            "title update test",
            "content update test",
            "location update test",
            LocalDateTime.of(2024, 9, 1, 1, 1),
            LocalDateTime.of(2024, 9, 1, 1, 1)
        );
    }

    @DisplayName("스케줄 객체를 수정 시 시작 시간이 종료 시간보다 늦으면 에러를 발생시킨다.")
    @Test
    void updateValidStartEndTimeTest() {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        ScheduleUpdateRequest scheduleUpdateRequest = ScheduleUpdateRequest.builder()
            .title("title update test")
            .content("content update test")
            .startDateTime(LocalDateTime.of(2024, 9, 1, 1, 1))
            .endDateTime(LocalDateTime.of(2024, 8, 1, 1, 1))
            .color(ScheduleColor.RED)
            .build();
        //When
        //Then
        assertThatThrownBy(() -> schedule.update(scheduleUpdateRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("시작 시간은 종료 시간보다 늦을 수 없습니다.");
    }

    @DisplayName("스케줄 객체를 수정 시 null 객체를 넣으면 에러를 발생시킨다.")
    @Test
    void updateValidScheduleRequest() {
        //Given
        Schedule schedule = getNewSchedule(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        //When
        //Then
        assertThatThrownBy(() -> schedule.update(null))
            .isInstanceOf(NullPointerException.class);
    }
}