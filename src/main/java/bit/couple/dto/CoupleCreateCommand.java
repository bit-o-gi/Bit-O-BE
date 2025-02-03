package bit.couple.dto;

import bit.base.BaseCommand;
import bit.couple.domain.Couple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class CoupleCreateCommand implements BaseCommand<Couple> {
    private final String code;
}
