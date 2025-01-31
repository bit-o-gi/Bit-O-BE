package bit.couple.service;

import bit.auth.domain.UserPrincipal;
import bit.couple.domain.Couple;
import bit.couple.dto.*;
import bit.couple.enums.CoupleStatus;
import bit.couple.exception.CoupleException;
import bit.couple.exception.CoupleException.CoupleNotFoundException;
import bit.couple.repository.CoupleRepository;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoupleService {
    private final HashMap<String, CodeEntry> codeStore = new HashMap<>();

    private final CoupleRepository coupleRepository;

    @Transactional
    public CoupleRcodeResponseDto createCode(UserPrincipal userPrincipal) {
        String randomCode;
        do {
            randomCode = RandomStringUtils.randomAlphanumeric(12);
        } while (codeStore.containsKey(randomCode)); // 중복 검사
        codeStore.put(randomCode, new CodeEntry(userPrincipal.getUser(), System.currentTimeMillis()));
        return CoupleRcodeResponseDto.of(randomCode);
    }

    @Transactional(readOnly = true)
    public CoupleResponDto getCouple(Long id) {
        return CoupleResponDto.of(coupleRepository.findById(id)
                .orElseThrow(() -> new CoupleException.CoupleNotFoundException()));
    }

    @Transactional
    public void removeExpiredCodes() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<String, CodeEntry>> iterator = codeStore.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, CodeEntry> entry = iterator.next();
            long createdAt = entry.getValue().getCreatedAt();

            if (now - createdAt > 5 * 60 * 1000) { // 5분 지난 경우
                iterator.remove();
            }
        }
    }


    @Transactional
    public void refreshApprove(CoupleRequestDto coupleRequestDto) {
        coupleRepository.findById(coupleRequestDto.getId()).orElseThrow(() -> new CoupleException.CoupleNotFoundException()).fromReq(coupleRequestDto);
    }

    @Transactional
    public void approveCouple(UserPrincipal userPrincipal, CoupleCreateRequest coupleCreateRequest) {
        //NOTE: 1. codeStore에서 코드로 CodeEntry 조회
        String code = coupleCreateRequest.getCode();
        CodeEntry codeEntry = codeStore.get(code);

        //NOTE: 2. 코드가 존재하지 않으면 예외 처리
        if (codeEntry == null) {
            throw new CoupleException.CodeNotFoundException();
        }

        //NOTE: 3. 사용자 A와 사용자 B 가져오기
        User userA = codeEntry.getUser(); // 코드 발급한 사용자 A
        User userB = userPrincipal.getUser(); // 요청한 사용자 B

        //NOTE: 4. 커플 생성 로직
        Couple couple = Couple.of(UserEntity.from(userA), UserEntity.from(userB), CoupleStatus.APPROVED);
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