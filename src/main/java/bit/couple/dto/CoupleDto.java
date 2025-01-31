package bit.couple.dto;

import bit.couple.enums.CoupleStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CoupleDto {

    private Long id;

    private Long initiatorUserId;

    private Long partnerUserId;

    private CoupleStatus status;
}
