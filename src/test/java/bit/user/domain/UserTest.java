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
                .platform(KAKAO)
                .providerUserId(12345131L)
                .build();

        // when
        User user = User.from(userCreateRequest);

        // then
        assertThat(user).extracting(
                "email",
                "nickName",
                "providerUserId",
                "platform"
        ).containsExactly(
                "pjhwork97@gmail.com",
                "AIJoBumSuk",
                12345131L,
                KAKAO
        );
    }

}