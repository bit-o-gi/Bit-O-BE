package bit.app.couple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoupleRcodeReqestDto {
    private String code;

    public static CoupleRcodeReqestDto of(String code) {
        return CoupleRcodeReqestDto.builder()
                .code(code)
                .build();
    }
}
