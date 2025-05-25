package bit.day.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bit.couple.domain.Couple;
import bit.couple.enums.CoupleStatus;
import bit.couple.fixture.CoupleTestFixture;
import bit.day.dto.DayRegisterCommand;
import bit.day.dto.DayUpdateCommand;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DayTest {
    @Test
    void createTest() throws Exception {
        DayRegisterCommand registerCommand = new DayRegisterCommand(null, "타이틀", LocalDate.now());
        Couple couple = CoupleTestFixture.initialCouple();

        Day day = Day.create(registerCommand, couple);

        assertThat(day).extracting("title", "startDate", "couple")
                .containsExactly("타이틀", registerCommand.startDate(), couple);
    }

    @Test
    void updateTest() throws Exception {
        Couple couple = CoupleTestFixture.initialCouple();
        Day day = new Day(1L, couple, "day title", LocalDate.now());
        DayUpdateCommand updateCommand = new DayUpdateCommand(1L, couple.getInitiatorUser().getId(),
                "update title", LocalDate.of(2024, 12, 12));

        day.update(updateCommand);

        assertThat(day).extracting("title", "startDate", "couple")
                .containsExactly("update title", LocalDate.of(2024, 12, 12), couple);
    }

    @DisplayName("내 커플의 디데이가 아니면 예외 발생")
    @Test
    void checkCoupleTest() throws Exception {
        Couple couple = CoupleTestFixture.initialCouple();
        Day day = new Day(1L, couple, "day title", LocalDate.now());

        Couple anotherCouple = Couple.of(couple.getPartnerUser(), couple.getInitiatorUser(), CoupleStatus.APPROVED);

        assertThatThrownBy(() -> day.checkCouple(anotherCouple))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not match couple in day");
    }
}
