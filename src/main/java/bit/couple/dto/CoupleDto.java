package bit.couple.dto;

import bit.couple.enums.CoupleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class CoupleDto {

    private Long id;

    private Long initiatorUserId;

    private Long partnerUserId;

    private CoupleStatus status;
}
