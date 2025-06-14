package bit.couple.fixture;

import bit.couple.domain.Couple;
import bit.couple.enums.CoupleStatus;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.enums.OauthPlatformType;
import java.lang.reflect.Field;
import java.util.List;

public class CoupleTestFixture {

    public static Couple initialCouple() throws Exception {
        List<User> users = initialUsers();
        UserEntity initiatorUser = UserEntity.from(users.get(0));
        UserEntity partnerUser = UserEntity.from(users.get(1));

        Couple couple = Couple.of(initiatorUser, partnerUser, CoupleStatus.CREATING);
        Class<?> clazz = Couple.class;
        Field idField = clazz.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(couple, 0L);

        return couple;
    }

    public static List<User> initialUsers() {
        User user1 = User.builder()
                .id(1L)
                .email("email1@naver.com")
                .nickName("nickname1")
                .platform(OauthPlatformType.KAKAO)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("email2@naver.com")
                .nickName("nickname2")
                .platform(OauthPlatformType.KAKAO)
                .build();

        return List.of(user1, user2);
    }
}
