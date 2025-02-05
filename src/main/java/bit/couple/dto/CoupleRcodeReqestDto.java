package bit.couple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CoupleRcodeReqestDto {
    private final String code;

    public static CoupleRcodeReqestDto of(String code) {
        return CoupleRcodeReqestDto.builder()
                .code(code)
                .build();
    }
}
