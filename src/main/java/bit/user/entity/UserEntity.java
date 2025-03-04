package bit.user.entity;

import bit.base.BaseEntity;
import bit.couple.domain.Couple;
import bit.user.domain.User;
import bit.user.enums.OauthPlatformType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bit_o_user")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private OauthPlatformType platform;

    private Long providerId;

    private LocalDateTime connectedDt;

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.email = user.getEmail();
        userEntity.nickName = user.getNickName();
        userEntity.platform = user.getPlatform();
        userEntity.providerId = user.getProviderId();
        userEntity.connectedDt = user.getConnectedDt();
        return userEntity;
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .email(email)
                .nickName(nickName)
                .platform(platform)
                .providerId(providerId)
                .connectedDt(connectedDt)
                .build();
    }
}
