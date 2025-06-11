package bit.couple.fixture;

import bit.couple.domain.Couple;
import bit.couple.enums.CoupleStatus;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.enums.OauthPlatformType;
import java.util.List;

public class CoupleTestFixture {

    public static Couple initialCouple() {
        List<User> users = initialUsers();
        UserEntity initiatorUser = UserEntity.from(users.get(0));
        UserEntity partnerUser = UserEntity.from(users.get(1));

        return Couple.builder()
                .id(1L)
                .initiatorUser(initiatorUser)
                .partnerUser(partnerUser)
                .status(CoupleStatus.APPROVED)
                .build();
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
