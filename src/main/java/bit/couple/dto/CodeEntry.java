package bit.couple.dto;

import bit.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class CodeEntry {
    private final User user;
    private final long createdAt;

    public boolean isExpired() {
        return System.currentTimeMillis() - createdAt > 5 * 60 * 1000; // 5분 초과 여부 확인
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CodeEntry that = (CodeEntry) obj;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
