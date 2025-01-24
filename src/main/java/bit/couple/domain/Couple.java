package bit.couple.domain;

import bit.base.BaseEntity;
import bit.couple.enums.CoupleStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Couple extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code1;

    private String code2;

    @Enumerated(EnumType.STRING)
    private CoupleStatus status;

    public static Couple of(String code1, String code2, CoupleStatus status) {
        return Couple.builder()
                .code1(code1)
                .code2(code2)
                .status(status)
                .build();
    }

    public void approve() {
        this.status = CoupleStatus.APPROVED;
    }
}