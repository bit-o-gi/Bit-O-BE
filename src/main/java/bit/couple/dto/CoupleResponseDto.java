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

    private Long id;

    private User initiatorUser;

    private User partnerUser;

    private CoupleStatus status;

    public static CoupleResponseDto of(Couple couple) {
        return CoupleResponseDto.builder()
                .initiatorUser(couple.getInitiatorUser().toDomain())
                .partnerUser(couple.getPartnerUser().toDomain())
                .status(couple.getStatus())
                .build();
    }
}
