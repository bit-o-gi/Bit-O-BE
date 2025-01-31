package bit.couple.dto;

import bit.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CodeEntry {
    private final User user;
    private final long createdAt;
}
