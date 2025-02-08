package bit.couple.service;

import bit.couple.domain.Couple;
import bit.couple.dto.*;
import bit.couple.enums.CoupleStatus;
import bit.couple.exception.CoupleException;
import bit.couple.fixture.CoupleFixtures;
import bit.couple.repository.CoupleRepository;
import bit.couple.vo.CodeEntryVo;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.mock.FakeUserRepository;
import bit.user.repository.UserJpaRepository;
import bit.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static bit.user.enums.OauthPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CoupleServiceTest {

    @InjectMocks
    private CoupleService coupleService;

    @Mock
    private ConcurrentHashMap<String, CodeEntryVo> codeStore;

    @Mock
    private ConcurrentHashMap<CodeEntryVo, String> reverseCodeStore;

    @Mock
    private CoupleRepository coupleRepository;

    @Mock
    private UserJpaRepository userJpaRepository;  


    private UserServiceImpl userService;


    private List<UserEntity> users;
    private Couple testCouple;

    @BeforeEach
    void setUp() throws Exception {
        // 가짜 저장소(Fake Repository) 생성
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        userService = new UserServiceImpl(fakeUserRepository);

        // 테스트용 사용자 데이터 저장
        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("pjhwork97@gmail.com")
                .nickName("MR_JO")
                .platform(KAKAO)
                .build()
        );

        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        // 초기 사용자 및 커플 데이터 설정
        users = CoupleFixtures.initialUsers()
                .stream()
                .map(UserEntity::from)
                .toList();

        testCouple = CoupleFixtures.initialCouple();

        // `codeStore` 및 `reverseCodeStore`를 Mock 객체로 주입
        injectMockField(coupleService, "codeStore", codeStore);
        injectMockField(coupleService, "reverseCodeStore", reverseCodeStore);

        // 현재 시간 기준으로 CodeEntry 생성
        UserEntity userA = users.get(0);
        CodeEntryVo codeEntry = new CodeEntryVo(userA.getId(), System.currentTimeMillis());

        // Mock 설정 추가
        when(codeStore.get("some-code")).thenReturn(codeEntry);
        when(codeStore.get("invalid-code")).thenReturn(null);

        when(reverseCodeStore.get(codeEntry)).thenReturn("some-code");
        when(reverseCodeStore.get(new CodeEntryVo(999L, System.currentTimeMillis()))).thenReturn(null);
    }

    private void injectMockField(Object target, String fieldName, Object mockInstance) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mockInstance);
    }

    @Test
    @DisplayName("커플 코드 생성 테스트")
    void testCreateCode() {
        // given
        UserEntity user = users.get(0);
        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(coupleRepository.findById(anyLong())).thenReturn(Optional.of(testCouple));

        // when
        CoupleRcodeResponseDto response = coupleService.createCode(user.getId());

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isNotBlank();
    }

    @Test
    @DisplayName("이미 존재하는 코드 예외 발생")
    void testCreateCode_AlreadyExists() {
        // given
        long userId = 1L;

        // userService.getById()를 사용하여 유저 가져오기
        User user = userService.getById(userId);

        // 기존 코드가 존재하는 것처럼 `reverseCodeStore`에 직접 값 추가
        String existingCode = "some-code";
        CodeEntryVo existingEntry = new CodeEntryVo(userId, System.currentTimeMillis());

        // 정확한 키를 사용해서 값 추가
        when(reverseCodeStore.get(any(CodeEntryVo.class))).thenReturn(existingCode);
        when(codeStore.get(existingCode)).thenReturn(existingEntry);

        // reverseCodeStore에서 키가 올바르게 저장되었는지 확인
        System.out.println("reverseCodeStore contains key? " + reverseCodeStore.containsKey(existingEntry));
        System.out.println("reverseCodeStore.get(existingEntry): " + reverseCodeStore.get(existingEntry));

        // when & then (이미 존재하는 경우 예외 발생 검증)
        assertThatThrownBy(() -> coupleService.createCode(user.getId()))
                .isInstanceOf(CoupleException.CoupleAlreadyExistsException.class);
    }



    @Test
    @DisplayName("커플 코드 조회 테스트")
    void testGetCodeByUser() {
        // given
        UserEntity user = users.get(0);
        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));
        CoupleRcodeResponseDto createdCode = coupleService.createCode(user.getId());

        // when
        CoupleRcodeResponseDto foundCode = coupleService.getCodeByUser(user.getId());

        // then
        assertThat(foundCode).isNotNull();
        assertThat(foundCode.getCode()).isEqualTo(createdCode.getCode());
    }

    @Test
    @DisplayName("존재하지 않는 코드 조회 시 예외 발생")
    void testGetCodeByUser_NotFound() {
        // given
        UserEntity user = users.get(0);
        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> coupleService.getCodeByUser(user.getId()))
                .isInstanceOf(CoupleException.CodeNotFoundException.class);
    }

    @Test
    @DisplayName("커플 승인 테스트")
    void testConfirmCouple() {
        // given
        CoupleRcodeReqestDto request = new CoupleRcodeReqestDto("some-code");
        UserEntity userA = users.get(0);
        UserEntity userB = users.get(1);
        Couple couple = Couple.of(userA, userB, CoupleStatus.APPROVED);
        when(userJpaRepository.findById(userB.getId())).thenReturn(Optional.of(userB));
        when(coupleRepository.save(any())).thenReturn(couple);

        // when
        coupleService.confirmCouple(userB.getId(), request);

        // then
        verify(coupleRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("존재하지 않는 코드 승인 시 예외 발생")
    void testConfirmCouple_CodeNotFound() {
        // given
        CoupleRcodeReqestDto request = new CoupleRcodeReqestDto("invalid-code");
        UserEntity userB = users.get(1);
        when(userJpaRepository.findById(userB.getId())).thenReturn(Optional.of(userB));

        // when & then
        assertThatThrownBy(() -> coupleService.confirmCouple(userB.getId(), request))
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
