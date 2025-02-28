package bit.schedule.util;

import bit.user.domain.User;
import bit.user.entity.UserEntity;

import static bit.user.enums.OauthPlatformType.KAKAO;

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

    public static UserEntity getNewUserEntity() {
        User user = User.builder()
                .email("test@test.com")
                .nickName("test")
                .platform(KAKAO)
                .build();
        return UserEntity.from(user);
    }
}
