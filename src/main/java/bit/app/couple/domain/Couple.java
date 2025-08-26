package bit.app.couple.domain;

import bit.app.base.BaseEntity;
import bit.app.couple.dto.CoupleRequestDto;
import bit.app.couple.enums.CoupleStatus;
import bit.app.couple.exception.CoupleException;
import bit.app.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.Objects;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_user_id", nullable = false)
    private UserEntity initiatorUser;

    //NOTE: 커플 요청을 승인한 사용자 (User B)
    @OneToOne(fetch = FetchType.LAZY)
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

    public void validateUserIsInCouple(UserEntity user) {
        if (!this.initiatorUser.equals(user) && !this.partnerUser.equals(user)) {
            throw new CoupleException.CouplePermissionException(); // 유효성 검사 실패 시 예외 발생
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Couple couple = (Couple) o;
        return Objects.equals(id, couple.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
