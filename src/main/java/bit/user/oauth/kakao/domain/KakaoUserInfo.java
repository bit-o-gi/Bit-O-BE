package bit.user.oauth.kakao.domain;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoUserInfo {
    private final LocalDateTime connectedAt;
    private final Long id;
    private final String email;
    private final String nickname;

    public static KakaoUserInfo of(JsonNode kakaoInfoBody) {
        JsonNode id = kakaoInfoBody.get("id");
        JsonNode properties = kakaoInfoBody.get("properties");
        JsonNode kakaoAccount = kakaoInfoBody.get("kakao_account");

        return KakaoUserInfo.builder()
                .connectedAt(OffsetDateTime.parse(kakaoInfoBody.get("connected_at").asText()).toLocalDateTime())
                .id(id.asLong())
                .email(kakaoAccount.get("email").asText())
                .nickname(properties.get("nickname").asText())
                .build();
    }


}
