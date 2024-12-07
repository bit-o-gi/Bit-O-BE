package bit.couple.dto;

import bit.base.BaseCommand;
import bit.couple.domain.Couple;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CoupleCommand implements BaseCommand<Couple> {

    private final String sender;
    private final String receiver;

}
