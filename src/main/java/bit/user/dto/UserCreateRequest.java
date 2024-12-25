package bit.user.dto;

import bit.user.domain.User;
import bit.user.oauth.enums.OauthPlatformType;
import bit.user.oauth.kakao.domain.KakaoUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "유저 등록 DTO")
public class UserCreateRequest {
    @Schema(description = "이메일 주소", example = "abc@email.com", required = true)
    private final String email;

    @Schema(description = "닉네임")
    private final String nickName;

    @Schema(description = "가입 플랫폼", example = "KAKAO", required = true)
    private final OauthPlatformType platform;

    @Schema(description = "Oauth 프로바이더 제공 id", example = "366332253")
    private Long providerId;

    @Schema(description = "Oauth 연동 시간")
    private LocalDateTime connectedDt;

    public static UserCreateRequest fromKakaoUser(KakaoUserInfo info) {
        return UserCreateRequest.builder()
                .connectedDt(info.getConnectedAt())
                .providerId(info.getId())
                .email(info.getEmail())
                .nickName(info.getNickname())
                .platform(OauthPlatformType.KAKAO)
                .build();
    }

    public static UserCreateRequest fromUser(User userInfo) {
        return UserCreateRequest.builder()
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickName())
                .platform(userInfo.getPlatform())
                .build();
    }
}
