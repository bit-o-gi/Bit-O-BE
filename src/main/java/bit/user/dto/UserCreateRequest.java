package bit.user.dto;

import bit.user.domain.User;
import bit.user.oauth.enums.OauthPlatformType;
import bit.user.oauth.kakao.domain.KakaoUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "유저 등록 DTO")
public class UserCreateRequest {
    @Schema(description = "아이디")
    private final Long id;

    @Schema(description = "이메일 주소", example = "<EMAIL>", required = true)
    private final String email;

    @Schema(description = "닉네임")
    private final String nickName;

    @Schema(description = "성별", example = "MALE")
    private final String gender;

    @Schema(description = "가입 플랫폼", example = "KAKAO", required = true)
    private final OauthPlatformType platform;

    @Schema(description = "가입 일시")
    private final LocalDateTime registerDate;

    @Builder
    public UserCreateRequest(Long id, String email, String nickName, String gender, OauthPlatformType platform,
                             LocalDateTime registerDate) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.gender = gender;
        this.platform = platform;
        this.registerDate = registerDate;
    }

    public static UserCreateRequest fromKakaoUser(KakaoUserInfo info) {
        return UserCreateRequest.builder()
                .email(info.getEmail())
                .nickName(info.getNickname())
                .platform(OauthPlatformType.KAKAO)
                .registerDate(info.getConnectedAt())
                .build();
    }

    public static UserCreateRequest fromUser(User userInfo) {
        return UserCreateRequest.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickName())
                .gender(userInfo.getGender())
                .platform(userInfo.getPlatform())
                .registerDate(userInfo.getRegisterDate())
                .build();
    }
}
