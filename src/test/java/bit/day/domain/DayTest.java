package bit.day.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import bit.app.couple.domain.Couple;
import bit.app.couple.enums.CoupleStatus;
import bit.app.day.domain.Day;
import bit.app.day.domain.DayFile;
import bit.app.day.dto.DayRegisterCommand;
import bit.app.day.dto.DayUpdateCommand;
import bit.couple.fixture.CoupleTestFixture;
import bit.day.fixture.DayTestFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DayTest {
    @Test
    void createTest() {
        DayRegisterCommand registerCommand = new DayRegisterCommand(null, "타이틀", LocalDate.now());
        Couple couple = CoupleTestFixture.initialCouple();

        Day day = Day.create(registerCommand, couple);

        assertThat(day).extracting("title", "startDate", "couple")
                .containsExactly("타이틀", registerCommand.startDate(), couple);
    }

    @Test
    void updateTest() {
        Couple couple = CoupleTestFixture.initialCouple();
        Day day = DayTestFixture.createDay(1L, couple, "day title", LocalDate.now(), null);

        DayUpdateCommand updateCommand = new DayUpdateCommand(1L, couple.getInitiatorUser().getId(),
                "update title", LocalDate.of(2024, 12, 12));

        day.update(updateCommand);

        assertThat(day).extracting("title", "startDate", "couple")
                .containsExactly("update title", LocalDate.of(2024, 12, 12), couple);
    }

    @DisplayName("내 커플의 디데이가 아니면 예외 발생")
    @Test
    void checkCoupleTest() {
        Couple couple = CoupleTestFixture.initialCouple();
        Day day = DayTestFixture.createDay(1L, couple, "day title", LocalDate.now(), null);

        Couple anotherCouple = Couple.of(couple.getPartnerUser(), couple.getInitiatorUser(), CoupleStatus.APPROVED);

        assertThatThrownBy(() -> day.checkCouple(anotherCouple))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not match couple in day");
    }

    @Test
    void registerDayFileTest() {
        Couple couple = CoupleTestFixture.initialCouple();
        Day day = DayTestFixture.createDay(1L, couple, "day title", LocalDate.now(), null);
        DayFile dayFile = DayTestFixture.createDayFile("test.jpg", "uuid12345.jpg", "image/jpeg", 100L);

        day.registerDayFile(dayFile);

        assertThat(day.getDayFile()).isEqualTo(dayFile);
        assertThat(day.getDayFile().getUuidFileName()).isEqualTo("uuid12345.jpg");
    }

    @Test
    void changeThumbnailUrlTest() {
        Couple couple = CoupleTestFixture.initialCouple();
        Day day = DayTestFixture.createDay(1L, couple, "day title", LocalDate.now(), null);

        day.changeThumbnailUrl("thumbnailUrl");

        assertThat(day.getThumbnailUrl()).isEqualTo("thumbnailUrl");
    }
}
