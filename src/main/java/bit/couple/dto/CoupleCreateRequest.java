package bit.couple.dto;

import bit.base.BaseRequest;
import bit.couple.domain.Couple;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CoupleCreateRequest implements BaseRequest<Couple> {
    private final String code;

    @Override
    public CoupleCreateCommand toCommand() {
        return new CoupleCreateCommand(code);
    }
}
