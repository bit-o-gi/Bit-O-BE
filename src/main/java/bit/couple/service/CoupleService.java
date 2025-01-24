package bit.couple.service;

import bit.couple.domain.Couple;
import bit.couple.dto.CoupleCreateCommand;
import bit.couple.enums.CoupleStatus;
import bit.couple.exception.CoupleException.CoupleNotFoundException;
import bit.couple.repository.CoupleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoupleService {

    private final CoupleRepository coupleRepository;

    @Transactional
    public void createCouple(CoupleCreateCommand command) {
        Couple savedCouple = coupleRepository.save(
                Couple.of(command.getCode(), null, CoupleStatus.CREATING)
        );
    }

    @Transactional
    public void approveCouple(String code1, String code2) {
        Couple couple = coupleRepository.findByCode1AndCode2(code1, code2)
                .orElseThrow(CoupleNotFoundException::new);
        couple.approve();
        coupleRepository.save(couple);
    }

    @Transactional
    public void deleteCouple(Long coupleId) {
        if (!coupleRepository.existsById(coupleId)) {
            throw new CoupleNotFoundException();
        }
        coupleRepository.deleteById(coupleId);
    }
}