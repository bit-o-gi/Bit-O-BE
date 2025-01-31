package bit.couple.domain;

import bit.base.BaseEntity;
import bit.couple.dto.CoupleRequestDto;
import bit.couple.dto.CoupleResponDto;
import bit.couple.enums.CoupleStatus;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Couple extends BaseEntity { // 클래스 이름을 CoupleConnection으로 변경

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //NOTE: 커플을 발급한 사용자 (User A)
    @ManyToOne(fetch = FetchType.LAZY)     @JoinColumn(name = "initiator_user_id", nullable = false)
    private UserEntity initiatorUser;

    //NOTE: 커플 요청을 승인한 사용자 (User B)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_user_id", nullable = false)
    private UserEntity partnerUser;

    @Enumerated(EnumType.STRING)
    private CoupleStatus status;

    //NOTE: 정적 팩토리 메서드로 CoupleConnection 생성
    public static Couple of(UserEntity initiatorUser, UserEntity partnerUser, CoupleStatus status) {
        return Couple.builder()
                .initiatorUser(initiatorUser)
                .partnerUser(partnerUser)
                .status(status)
                .build();
    }
    public void fromReq(CoupleRequestDto coupleRequestDto) {
        this.status = coupleRequestDto.getStatus();
        this.initiatorUser = UserEntity.from(coupleRequestDto.getInitiatorUser());
        this.partnerUser = UserEntity.from(coupleRequestDto.getPartnerUser());
    }


    //NOTE: 상태를 APPROVED로 변경
    public void approve() {
        this.status = CoupleStatus.APPROVED;
    }

}