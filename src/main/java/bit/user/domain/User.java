package bit.user.domain;

import bit.couple.domain.Couple;
import bit.user.dto.UserCreateRequest;
import bit.user.oauth.enums.OauthPlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private final Long id;
    private final String email;
    private final String nickName;
    private final String gender;
    private final OauthPlatformType platform;
    private final LocalDateTime registerDate;
    private final Couple couple;

    public static User from(UserCreateRequest userCreateRequest) {
        return User.builder()
                .email(userCreateRequest.getEmail())
                .nickName(userCreateRequest.getNickName())
                .gender(userCreateRequest.getGender())
                .platform(userCreateRequest.getPlatform())
                .registerDate(userCreateRequest.getRegisterDate())
                .build();
    }

    public User updateCouple(Couple couple) {
        return User.builder()
                .id(this.id)
                .email(this.getEmail())
                .nickName(this.getNickName())
                .gender(this.getGender())
                .platform(this.getPlatform())
                .registerDate(this.getRegisterDate())
                .couple(couple)
                .build();
    }

}
