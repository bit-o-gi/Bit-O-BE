package bit.schedule.util;

import static bit.user.enums.OauthPlatformType.KAKAO;

import bit.user.domain.User;
import bit.user.entity.UserEntity;

public class UserEntityFixture {
    public static UserEntity getNewUserEntity(Long id) {
        User user = User.builder()
                .id(id)
                .email("test@test.com")
                .nickName("test")
                .platform(KAKAO)
                .build();
        return UserEntity.from(user);
    }
}
