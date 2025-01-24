package bit.couple.dto;

import bit.base.BaseCommand;
import bit.couple.domain.Couple;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CoupleCreateCommand implements BaseCommand<Couple> {
    //    TODO: 여기서 이메일 -> 말고 매번 바뀌는 커플 코드 생성
    private final String code;
//    private final String senderEmail;
//    private final String receiverEmail;
}
