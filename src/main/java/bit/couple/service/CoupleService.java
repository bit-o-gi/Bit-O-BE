package bit.couple.service;

import bit.couple.domain.Couple;
import bit.couple.dto.CoupleCreateCommand;
import bit.couple.enums.CoupleStatus;
import bit.couple.exception.CoupleException.CoupleNotFoundException;
import bit.couple.repository.CoupleRepository;
import bit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoupleService {

    private final CoupleRepository coupleRepository;
    private final UserService userService;

    @Transactional
    public void createCouple(CoupleCreateCommand command) {
        String sender = command.getSenderEmail();
        String receiver = command.getReceiverEmail();

        Couple savedCouple = coupleRepository.save(
                Couple.of(sender, receiver, CoupleStatus.CREATING)
        );

        userService.updateCouple(sender, receiver, savedCouple);
    }

    public void approveCouple(Long coupleId) {
        Couple couple = coupleRepository.findById(coupleId).orElseThrow(CoupleNotFoundException::new);
        couple.approve();
        coupleRepository.save(couple);
    }

    public void deleteCouple(Long coupleId) {
        if (!coupleRepository.existsById(coupleId)) {
            throw new CoupleNotFoundException();
        }
        // TODO: 바로 지우는 대신 스케줄링 등록
        coupleRepository.deleteById(coupleId);
    }
}
