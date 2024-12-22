package bit.user.dto;

import lombok.Getter;

@Getter
public class AccessTokenCreateRequest {
    private String refreshToken;
}
