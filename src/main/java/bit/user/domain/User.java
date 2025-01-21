package bit.user.domain;

import bit.couple.domain.Couple;
import bit.user.dto.UserCreateRequest;
import bit.user.enums.OauthPlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private final Long id;
    private final String email;
    private final String nickName;
    private final OauthPlatformType platform;
    private final Couple couple;
    private final Long providerId;
    private final LocalDateTime connectedDt;

    public static User from(UserCreateRequest userCreateRequest) {
        return User.builder()
                .connectedDt(userCreateRequest.getConnectedDt())
                .providerId(userCreateRequest.getProviderId())
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
