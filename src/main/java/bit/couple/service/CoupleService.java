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

    @Transactional
    public CoupleRcodeResponseDto createCode(User user) {
        String existingCode = reverseCodeStore.get(new CodeEntryVo(user, 0));

        if (existingCode != null) {
            CodeEntryVo existingEntry = codeStore.get(existingCode);

            // NOTE: 기존 코드가 5분 이내라면 예외 발생
            if (existingEntry != null && !existingEntry.isExpired()) {
                throw new CoupleException.CoupleAlreadyExistsException();
            }

            // NOTE: 5분이 지났다면 기존 코드 삭제
            removeCode(existingEntry);
        }

        String randomCode;

        do {
            randomCode = RandomStringUtils.randomAlphanumeric(12);
//            NOTE: 없으면 유일한 코드 만듬.
        } while (codeStore.putIfAbsent(randomCode, new CodeEntryVo(user, System.currentTimeMillis())) != null);

        CodeEntryVo CodeEntryVo = new CodeEntryVo(user, System.currentTimeMillis());

        codeStore.put(randomCode, CodeEntryVo);
        reverseCodeStore.put(CodeEntryVo, randomCode);

        return CoupleRcodeResponseDto.of(randomCode);
    }

    public CoupleRcodeResponseDto getCodeByUser(User user) {
        String code = reverseCodeStore.get(new CodeEntryVo(user, 0));
        if (code == null) {
            throw new CoupleException.CodeNotFoundException();
        }

        return CoupleRcodeResponseDto.of(code);
    }

    private void removeCode(CodeEntryVo CodeEntryVo) {
        String code = reverseCodeStore.remove(CodeEntryVo);
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
                //NOTE codeStore에서 삭제
                iterator.remove();
                //NOTE reverseCodeStore에서도 삭제
                reverseCodeStore.remove(entry.getValue());
            }
        }
    }


    @Transactional
    public void updateCouple(User user, CoupleRequestDto coupleRequestDto) {
        Couple couple = coupleRepository.findById(coupleRequestDto.getId())
                .orElseThrow(CoupleException.CoupleNotFoundException::new);

        couple.validateUserIsInCouple(UserEntity.from(user));

        couple.fromReq(coupleRequestDto);
    }

    @Transactional
    public void coupleApprove(User user, Long coupleId) {
        Couple couple = coupleRepository.findById(coupleId)
                .orElseThrow(CoupleException.CoupleNotFoundException::new);

        couple.validateUserIsInCouple(UserEntity.from(user));
        couple.approve();
    }

    @Transactional
    public void confirmCouple(User user, CoupleCreateRequest coupleCreateRequest) {
        //NOTE: 1. codeStore에서 코드로 CodeEntryVo 조회
        String code = coupleCreateRequest.getCode();
        CodeEntryVo CodeEntryVo = codeStore.get(code);

        //NOTE: 2. 코드가 존재하지 않으면 예외 처리
        if (CodeEntryVo == null) {
            throw new CoupleException.CodeNotFoundException();
        }

        //NOTE: 3. 사용자 A와 사용자 B 가져오기
        //NOTE: 코드 발급한 사용자
        User initiatorUser = CodeEntryVo.getUser();
        //NOTE: 요청한 사용자
        User partnerUser = user;


        //NOTE: 4. 커플 생성 로직
        Couple couple = Couple.of(UserEntity.from(initiatorUser), UserEntity.from(partnerUser), CoupleStatus.APPROVED);
        coupleRepository.save(couple); // 커플 정보를 저장

        //NOTE: 5. codeStore에서 코드 삭제
        codeStore.remove(code);
    }


    @Transactional
    public void deleteCouple(Long coupleId) {
        if (!coupleRepository.existsById(coupleId)) {
            throw new CoupleNotFoundException();
        }
        coupleRepository.deleteById(coupleId);
    }
}