package bit.couple.dto;

import bit.couple.domain.Couple;
import bit.couple.enums.CoupleStatus;
import bit.user.domain.User;
import bit.user.dto.UserResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CoupleResponDto {

    private Long id;

    private User initiatorUser;

    private User partnerUser;

    private CoupleStatus status;

    public static CoupleResponDto of(Couple couple) {
        return CoupleResponDto.builder()
                .initiatorUser(couple.getInitiatorUser().toDomain())
                .partnerUser(couple.getPartnerUser().toDomain())
                .status(couple.getStatus())
                .build();
    }
}
