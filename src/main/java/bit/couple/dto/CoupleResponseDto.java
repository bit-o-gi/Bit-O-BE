package bit.couple.dto;

import bit.couple.domain.Couple;
import bit.couple.enums.CoupleStatus;
import bit.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CoupleResponseDto {

    private final Long id;

    private final User initiatorUser;

    private final User partnerUser;

    private final CoupleStatus status;

    public static CoupleResponseDto of(Couple couple) {
        return CoupleResponseDto.builder()
                .initiatorUser(couple.getInitiatorUser().toDomain())
                .partnerUser(couple.getPartnerUser().toDomain())
                .status(couple.getStatus())
                .build();
    }
}
