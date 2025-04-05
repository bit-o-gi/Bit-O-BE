package bit.couple.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CodeEntryVo {
    @EqualsAndHashCode.Include
    private final long userId;
    private final long createdAt;

    public boolean isExpired() {
        return System.currentTimeMillis() - createdAt > 24 * 60 * 60 * 1000; // 24시간 초과 여부 확인
    }
}
