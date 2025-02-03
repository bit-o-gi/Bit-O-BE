package bit.couple.service;

import bit.couple.domain.Couple;
import bit.couple.dto.*;
import bit.couple.enums.CoupleStatus;
import bit.couple.exception.CoupleException;
import bit.couple.fixture.CoupleFixtures;
import bit.couple.repository.CoupleRepository;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CoupleServiceTest {

    @InjectMocks
    private CoupleService coupleService;

    @Mock
    private ConcurrentHashMap<String, CodeEntry> codeStore;  // ✅ Mock 객체로 선언

    @Mock
    private ConcurrentHashMap<CodeEntry, String> reverseCodeStore;

    @Mock
    private CoupleRepository coupleRepository;

    private List<User> users;
    private Couple testCouple;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        users = CoupleFixtures.initialUsers();
        testCouple = CoupleFixtures.initialCouple();

        // Reflection을 사용하여 CoupleService의 codeStore 필드를 Mock으로 변경
        Field codeStoreField = CoupleService.class.getDeclaredField("codeStore");
        codeStoreField.setAccessible(true);
        codeStoreField.set(coupleService, codeStore);  // 실제 `codeStore`를 Mock 객체로 교체

        // 현재 시간 기준으로 CodeEntry 생성
        User userA = users.get(0);
        CodeEntry codeEntry = new CodeEntry(userA, System.currentTimeMillis());

        // Mock 설정 추가
        when(codeStore.get("some-code")).thenReturn(codeEntry); // 유효한 코드 반환
        when(codeStore.get("invalid-code")).thenReturn(null);   // 존재하지 않는 코드
    }




    @Test
    @DisplayName(" 커플 코드 생성 테스트")
    void testCreateCode() {
        // given
        User user = users.get(0);
        when(coupleRepository.findById(anyLong())).thenReturn(Optional.of(testCouple));

        // when
        CoupleRcodeResponseDto response = coupleService.createCode(user);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isNotBlank();
    }

    @Test
    @DisplayName("이미 존재하는 코드 예외 발생")
    void testCreateCode_AlreadyExists() {
        // given
        User user = users.get(0);
        coupleService.createCode(user); // 첫 번째 코드 생성

        // when & then
        assertThatThrownBy(() -> coupleService.createCode(user))
                .isInstanceOf(CoupleException.CoupleAlreadyExistsException.class);
    }

    @Test
    @DisplayName("커플 코드 조회 테스트")
    void testGetCodeByUser() {
        // given
        User user = users.get(0);
        CoupleRcodeResponseDto createdCode = coupleService.createCode(user);

        // when
        CoupleRcodeResponseDto foundCode = coupleService.getCodeByUser(user);

        // then
        assertThat(foundCode).isNotNull();
        assertThat(foundCode.getCode()).isEqualTo(createdCode.getCode());
    }

    @Test
    @DisplayName("존재하지 않는 코드 조회 시 예외 발생")
    void testGetCodeByUser_NotFound() {
        // given
        User user = users.get(1);

        // when & then
        assertThatThrownBy(() -> coupleService.getCodeByUser(user))
                .isInstanceOf(CoupleException.CodeNotFoundException.class);
    }

    @Test
    @DisplayName("커플 정보 조회 테스트")
    void testGetCouple() {
        // given
        Long coupleId = 1L;
        when(coupleRepository.findById(coupleId)).thenReturn(Optional.of(testCouple));

        // when
        CoupleResponDto response = coupleService.getCouple(coupleId);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 커플 조회 시 예외 발생")
    void testGetCouple_NotFound() {
        // given
        Long coupleId = 999L;
        when(coupleRepository.findById(coupleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> coupleService.getCouple(coupleId))
                .isInstanceOf(CoupleException.CoupleNotFoundException.class);
    }

    @Test
    @DisplayName("커플 승인 테스트")
    void testConfirmCouple() {
        // given
        CoupleCreateRequest request = new CoupleCreateRequest("some-code");
        User userA = users.get(0);
        User userB = users.get(1);
        Couple couple = Couple.of(UserEntity.from(userA), UserEntity.from(userB), CoupleStatus.APPROVED);
        when(coupleRepository.save(any())).thenReturn(couple);

        // when
        coupleService.confirmCouple(userB, request);

        // then
        verify(coupleRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 코드 승인 시 예외 발생")
    void testConfirmCouple_CodeNotFound() {
        // given
        CoupleCreateRequest request = new CoupleCreateRequest("invalid-code");
        User userB = users.get(1);

        // when & then
        assertThatThrownBy(() -> coupleService.confirmCouple(userB, request))
                .isInstanceOf(CoupleException.CodeNotFoundException.class);
    }

    @Test
    @DisplayName("커플 삭제 테스트")
    void testDeleteCouple() {
        // given
        Long coupleId = 1L;
        when(coupleRepository.existsById(coupleId)).thenReturn(true);

        // when
        coupleService.deleteCouple(coupleId);

        // then
        verify(coupleRepository, times(1)).deleteById(coupleId);
    }

    @Test
    @DisplayName("존재하지 않는 커플 삭제 시 예외 발생")
    void testDeleteCouple_NotFound() {
        // given
        Long coupleId = 999L;
        when(coupleRepository.existsById(coupleId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> coupleService.deleteCouple(coupleId))
                .isInstanceOf(CoupleException.CoupleNotFoundException.class);
    }
}
