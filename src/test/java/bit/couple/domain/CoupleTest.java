package bit.couple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import bit.couple.enums.CoupleStatus;
import bit.couple.fixture.CoupleFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoupleTest {

    @DisplayName("커플 객체 변환 테스트")
    @Test
    void createCoupleOf() throws Exception {
        // given // when
        Couple couple = CoupleFixtures.initialCouple();

        // then
        assertThat(couple).extracting(
                "id",
                "sender",
                "receiver",
                "status"
        ).containsExactly(
                0L,
                "test1@test.com",
                "test2@test.com",
                CoupleStatus.CREATING
        );
    }

}