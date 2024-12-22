package bit.user.entity;

import static bit.user.oauth.enums.OauthPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

import bit.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserEntityTest {

    @DisplayName("User 객체를 UserJpaEntity로 변환할 수 있다.")
    @Test
    void fromUserToUserJpaEntity() {
        // given
        User user = User.builder()
                .id(1L)
                .email("pjh@abcd.com")
                .nickName("MR_JO")
                .platform(KAKAO)
                .build();

        // when
        UserEntity userEntity = UserEntity.from(user);

        // then
        assertThat(user).extracting(
                "id",
                "email",
                "nickName",
                "platform"
        ).containsExactly(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getNickName(),
                userEntity.getPlatform()
        );
    }

}