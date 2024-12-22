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
    private final OauthPlatformType platform;
    private final Couple couple;
    private final Long providerUserId;
    private final LocalDateTime connectedDt;

    public static User from(UserCreateRequest userCreateRequest) {
        return User.builder()
                .connectedDt(userCreateRequest.getConnectedDt())
                .providerUserId(userCreateRequest.getProviderUserId())
                .email(userCreateRequest.getEmail())
                .nickName(userCreateRequest.getNickName())
                .platform(userCreateRequest.getPlatform())
                .build();
    }

    public User updateCouple(Couple couple) {
        return User.builder()
                .id(this.id)
                .email(this.getEmail())
                .nickName(this.getNickName())
                .platform(this.getPlatform())
                .couple(couple)
                .build();
    }

}
