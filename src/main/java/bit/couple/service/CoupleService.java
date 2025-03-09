package bit.couple.service;

import bit.couple.domain.Couple;
import bit.couple.dto.*;
import bit.couple.enums.CoupleStatus;
import bit.couple.exception.CoupleException;
import bit.couple.exception.CoupleException.CoupleNotFoundException;
import bit.couple.repository.CoupleRepository;
import bit.couple.vo.CodeEntryVo;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.repository.UserJpaRepository;
import bit.user.service.UserService;
import bit.user.service.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CoupleService {
    private final ConcurrentHashMap<String, CodeEntryVo> codeStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<CodeEntryVo, String> reverseCodeStore = new ConcurrentHashMap<>();

    private final CoupleRepository coupleRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public CoupleResponseDto getCoupleByUserId(Long userId) {
        Couple couple = coupleRepository.findByUserId(userId)
                .orElseThrow(() -> new CoupleException.CoupleNotFoundException());
        return CoupleResponseDto.of(couple);
    }


    @Transactional
    public CoupleRcodeResponseDto createCode(Long userId) {

        String existingCode = reverseCodeStore.get(new CodeEntryVo(userId, 0));

        if (existingCode != null) {
            CodeEntryVo existingEntry = codeStore.get(existingCode);

            if (existingEntry != null && !existingEntry.isExpired()) {
                throw new CoupleException.CoupleAlreadyExistsException();
            }

            removeCode(existingEntry);
        }

        String randomCode;

        do {
            randomCode = RandomStringUtils.randomAlphanumeric(12);
        } while (codeStore.putIfAbsent(randomCode, new CodeEntryVo(userId, System.currentTimeMillis())) != null);

        CodeEntryVo CodeEntryVo = new CodeEntryVo(userId, System.currentTimeMillis());

        codeStore.put(randomCode, CodeEntryVo);
        reverseCodeStore.put(CodeEntryVo, randomCode);

        return CoupleRcodeResponseDto.of(randomCode);
    }

    public CoupleRcodeResponseDto getCodeByUser(Long userId) {

        String code = reverseCodeStore.get(new CodeEntryVo(userId, 0));
        if (code == null) {
            throw new CoupleException.CodeNotFoundException();
        }

        return CoupleRcodeResponseDto.of(code);
    }

    private void removeCode(CodeEntryVo codeEntryVo) {
        if (codeEntryVo == null) {
            return;
        }

        String code = reverseCodeStore.remove(codeEntryVo);
        if (code != null) {
            codeStore.remove(code);
        }
    }

    @Transactional(readOnly = true)
    public CoupleResponseDto getCouple(Long id) {
        return CoupleResponseDto.of(coupleRepository.findById(id)
                .orElseThrow(() -> new CoupleException.CoupleNotFoundException()));
    }

    @Transactional
    public void removeExpiredCodes() {
        Iterator<Map.Entry<String, CodeEntryVo>> iterator = codeStore.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, CodeEntryVo> entry = iterator.next();

            if (entry.getValue().isExpired()) {
                iterator.remove();
                reverseCodeStore.remove(entry.getValue());
            }
        }
    }


    @Transactional
    public void updateCouple(Long userId, CoupleRequestDto coupleRequestDto) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException());

        Couple couple = coupleRepository.findById(coupleRequestDto.getId())
                .orElseThrow(CoupleException.CoupleNotFoundException::new);

        couple.validateUserIsInCouple(UserEntity.from(user));

        couple.fromReq(coupleRequestDto);
    }

    @Transactional
    public void coupleApprove(Long userId, Long coupleId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException());

        Couple couple = coupleRepository.findById(coupleId)
                .orElseThrow(CoupleException.CoupleNotFoundException::new);

        couple.validateUserIsInCouple(UserEntity.from(user));

        couple.approve();
    }

    public void confirmCouple(Long userId, CoupleRcodeReqestDto coupleCreateRequest) {
        User partnerUser = userService.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException());

        String code = coupleCreateRequest.getCode();
        CodeEntryVo CodeEntryVo = codeStore.get(code);

        if (CodeEntryVo == null) {
            throw new CoupleException.CodeNotFoundException();
        }

        long initiatorUserId = CodeEntryVo.getUserId();
        User initiatorUser = userService.findById(initiatorUserId)
                .orElseThrow(() -> new EntityNotFoundException());

        Couple couple = Couple.of(UserEntity.from(initiatorUser), UserEntity.from(partnerUser), CoupleStatus.APPROVED);
        coupleRepository.save(couple); // 커플 정보를 저장

        codeStore.remove(code);
        reverseCodeStore.remove(CodeEntryVo);
    }


    @Transactional
    public void deleteCouple(Long coupleId) {
        if (!coupleRepository.existsById(coupleId)) {
            throw new CoupleNotFoundException();
        }
        coupleRepository.deleteById(coupleId);
    }
}