package bit.app.couple.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoupleRcodeResponseDto {
    private final String code;

    public static CoupleRcodeResponseDto of(String code) {
        return CoupleRcodeResponseDto.builder()
                .code(code)
                .build();
    }
}
