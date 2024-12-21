package bit.user.domain;

import static bit.user.oauth.enums.OauthPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

import bit.user.dto.UserCreateRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @DisplayName("User 객체를 userDto 객체로 생성할 수 있다.")
    @Test
    void createUserByDto() {
        // given
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .email("pjhwork97@gmail.com")
                .nickName("AIJoBumSuk")
                .gender("Male")
                .platform(KAKAO)
                .registerDate(LocalDateTime.of(2024, 8, 27, 13, 5, 12))
                .build();

        // when
        User user = User.from(userCreateRequest);

        // then
        assertThat(user).extracting(
                "email",
                "nickName",
                "gender",
                "platform",
                "registerDate"
        ).containsExactly(
                "pjhwork97@gmail.com",
                "AIJoBumSuk",
                "Male",
                KAKAO,
                LocalDateTime.of(2024, 8, 27, 13, 5, 12)
        );
    }

}