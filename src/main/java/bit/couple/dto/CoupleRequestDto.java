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

    // CoupleRequestDto -> Couple 변환
    public Couple toCouple() {
        return Couple.builder()
                .initiatorUser(UserEntity.from(this.initiatorUser))
                .partnerUser(UserEntity.from(this.partnerUser))
                .status(this.status)
                .build();
    }
}


