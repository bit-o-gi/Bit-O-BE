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

    private final Long id;

    private final Long initiatorUserId;

    private final Long partnerUserId;

    private final CoupleStatus status;
}
