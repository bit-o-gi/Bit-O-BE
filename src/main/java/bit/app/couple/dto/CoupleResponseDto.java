package bit.app.couple.dto;

import bit.app.couple.domain.Couple;
import bit.app.couple.enums.CoupleStatus;
import bit.app.user.domain.User;
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
                .id(couple.getId())
                .initiatorUser(couple.getInitiatorUser().toDomain())
                .partnerUser(couple.getPartnerUser().toDomain())
                .status(couple.getStatus())
                .build();
    }
}
