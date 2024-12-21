package bit.user.entity;

import bit.base.BaseEntity;
import bit.couple.domain.Couple;
import bit.user.domain.User;
import bit.user.oauth.enums.OauthPlatformType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "bit_o_user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickName;

    private String gender;

    @Enumerated(EnumType.STRING)
    private OauthPlatformType platform;

    @ManyToOne
    @JoinColumn(name = "couple_id")
    private Couple couple;

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.email = user.getEmail();
        userEntity.nickName = user.getNickName();
        userEntity.platform = user.getPlatform();
        userEntity.couple = user.getCouple();
        return userEntity;
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .email(email)
                .nickName(nickName)
                .gender(gender)
                .platform(platform)
                .couple(couple)
                .build();
    }
}
