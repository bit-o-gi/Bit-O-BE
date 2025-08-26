package bit.app.couple.dto;

import bit.app.couple.enums.CoupleStatus;
import bit.app.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoupleRequestDto {
    private final Long id;

    private final User initiatorUser;

    private final User partnerUser;

    private final CoupleStatus status;
}
