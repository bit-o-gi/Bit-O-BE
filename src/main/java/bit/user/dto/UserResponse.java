package bit.user.dto;

import bit.user.domain.User;
import bit.user.enums.OauthPlatformType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private final Long id;
    private final String email;
    private final String nickName;
    private final OauthPlatformType oauthPlatformType;
    private final Long oauthProviderId;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .oauthPlatformType(user.getPlatform())
                .oauthProviderId(user.getProviderId())
                .build();
    }
}
