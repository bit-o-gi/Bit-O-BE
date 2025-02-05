package bit.couple.dto;

import bit.couple.domain.Couple;
import bit.couple.enums.CoupleStatus;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class CoupleRequestDto {
    private final Long id;

    private final User initiatorUser;

    private final User partnerUser;

    private final CoupleStatus status;

    public static CoupleRequestDto from(Couple couple) {
        return CoupleRequestDto.builder()
                .id(couple.getId())
                .initiatorUser(couple.getInitiatorUser().toDomain())
                .partnerUser(couple.getPartnerUser().toDomain())
                .status(couple.getStatus())
                .build();
    }
}


