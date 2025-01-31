package bit.couple.dto;

import bit.couple.enums.CoupleStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CoupleDto {

    private Long id;

    // 커플을 발급한 사용자 ID (User A)
    private Long initiatorUserId;

    // 커플 요청을 승인한 사용자 ID (User B)
    private Long partnerUserId;

    // Couple 상태
    private CoupleStatus status;
}
