package bit.couple.service;

import static bit.user.enums.OauthPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bit.couple.domain.Couple;
import bit.couple.dto.CoupleRcodeReqestDto;
import bit.couple.dto.CoupleRcodeResponseDto;
import bit.couple.dto.CoupleResponseDto;
import bit.couple.dto.CoupleStartDayRequest;
import bit.couple.enums.CoupleStatus;
import bit.couple.exception.CoupleException;
import bit.couple.fixture.CoupleTestFixture;
import bit.couple.repository.CoupleRepository;
import bit.couple.vo.CodeEntryVo;
import bit.day.service.DayService;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.repository.UserJpaRepository;
import bit.user.service.UserServiceImpl;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CoupleServiceTest {

    CoupleStartDayRequest dayRequest = new CoupleStartDayRequest(LocalDate.now(), "커플 애칭");
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

    @Mock
    private UserServiceImpl userService;

    @Mock
    private DayService dayService;

    private List<UserEntity> users;
    private UserEntity userA;
    private UserEntity userB;
    private Couple testCouple;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userService = mock(UserServiceImpl.class);

        User mrJo = User.builder()
                .id(1L)
                .email("pjhwork97@gmail.com")
                .nickName("MR_JO")
                .platform(KAKAO)
                .build();

        when(userService.getById(1L)).thenReturn(mrJo);

        users = CoupleTestFixture.initialUsers()
                .stream()
                .map(UserEntity::from)
                .toList();

        testCouple = CoupleTestFixture.initialCouple();

        injectMockField(coupleService, "userService", userService);
        injectMockField(coupleService, "codeStore", codeStore);
        injectMockField(coupleService, "reverseCodeStore", reverseCodeStore);

        userA = users.get(0);
        userB = users.get(1);

        CodeEntryVo codeEntry = new CodeEntryVo(userA.getId(), dayRequest, System.currentTimeMillis());

        when(codeStore.get("some-code")).thenReturn(codeEntry);
        when(reverseCodeStore.get(codeEntry)).thenReturn("some-code");
        testCouple = Couple.of(testCouple.getInitiatorUser(), testCouple.getPartnerUser(), CoupleStatus.APPROVED);

    }

    private void injectMockField(Object target, String fieldName, Object mockInstance) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mockInstance);
    }

    @Test
    @DisplayName("사용자의 커플 정보 조회 테스트")
    void testGetCoupleByIdByUserId() {
        testCouple = Couple.of(testCouple.getInitiatorUser(), testCouple.getPartnerUser(), CoupleStatus.APPROVED);

        when(coupleRepository.findByUserId(userA.getId())).thenReturn(Optional.of(testCouple));

        CoupleResponseDto response = coupleService.getCoupleByUserId(userA.getId());

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(CoupleStatus.APPROVED);
    }


    @Test
    @DisplayName("커플 코드 생성 테스트")
    void testCreateCode() {
        UserEntity user = users.get(0);

        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(reverseCodeStore.get(any(CodeEntryVo.class))).thenReturn(null);
        when(codeStore.putIfAbsent(anyString(), any())).thenReturn(null);

        CoupleRcodeResponseDto response = coupleService.createCode(user.getId(), dayRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isNotBlank();

        verify(codeStore, times(1)).putIfAbsent(anyString(), any(CodeEntryVo.class));
        verify(reverseCodeStore, times(1)).put(any(CodeEntryVo.class), anyString());
    }

    @Test
    @DisplayName("하루 이상 지난 커플 코드 만료 테스트")
    void testCreateCode_Expired() {
        UserEntity user = users.get(0);
        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));

        long expiredTime = System.currentTimeMillis() - 24 * 60 * 60 * 1050;
        CodeEntryVo expiredCodeEntry = new CodeEntryVo(user.getId(), dayRequest, expiredTime);

        when(codeStore.get(anyString())).thenReturn(expiredCodeEntry);

        CoupleRcodeResponseDto response = coupleService.createCode(user.getId(), dayRequest);
        String generatedCode = response.getCode();

        assertThat(generatedCode).isNotBlank();
        assertThat(generatedCode).isNotEqualTo(expiredCodeEntry);

        reverseCodeStore.put(expiredCodeEntry, generatedCode);

        CoupleRcodeResponseDto newResponse = coupleService.createCode(user.getId(), dayRequest);
        String newGeneratedCode = newResponse.getCode();

        assertThat(newGeneratedCode).isNotBlank();
        assertThat(newGeneratedCode).isNotEqualTo(generatedCode);
    }


    @Test
    @DisplayName("이미 존재하는 코드 예외 발생")
    void testCreateCode_AlreadyExists() {
        long userId = 1L;
        User user = userService.getById(userId);
        assertThat(user).isNotNull();

        String existingCode = "some-code";
        CodeEntryVo existingEntry = new CodeEntryVo(userId, dayRequest, System.currentTimeMillis());

        when(reverseCodeStore.get(any(CodeEntryVo.class))).thenReturn(existingCode);
        when(codeStore.get(existingCode)).thenReturn(existingEntry);

        assertThatThrownBy(() -> coupleService.createCode(user.getId(), dayRequest))
                .isInstanceOf(CoupleException.CoupleAlreadyExistsException.class);
    }


    @Test
    @DisplayName("커플 코드 조회 테스트")
    void testGetCodeByUser() {
        UserEntity user = users.get(0);
        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));

        String expectedCode = "generated-code";
        CodeEntryVo codeEntryVo = new CodeEntryVo(user.getId(), dayRequest, System.currentTimeMillis());

        when(reverseCodeStore.get(new CodeEntryVo(user.getId(), dayRequest, 0))).thenReturn(expectedCode);
        when(codeStore.get(expectedCode)).thenReturn(codeEntryVo);

        CoupleRcodeResponseDto foundCode = coupleService.getCodeByUser(user.getId());

        assertThat(foundCode).isNotNull();
        assertThat(foundCode.getCode()).isEqualTo(expectedCode);
    }


    @Test
    @DisplayName("존재하지 않는 코드 조회 시 예외 발생")
    void testGetCodeByUser_NotFound() {
        UserEntity user = users.get(0);
        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));

        when(reverseCodeStore.get(any(CodeEntryVo.class))).thenReturn(null);

        assertThatThrownBy(() -> coupleService.getCodeByUser(user.getId()))
                .isInstanceOf(CoupleException.CodeNotFoundException.class);
    }


    @Test
    @DisplayName("커플 승인 테스트")
    void testConfirmCouple() {
        CoupleRcodeReqestDto request = new CoupleRcodeReqestDto("some-code");
//        UserEntity userB = users.get(1);
        Couple couple = Couple.of(userA, userB, CoupleStatus.APPROVED);

        when(userService.findById(userB.getId())).thenReturn(Optional.of(userB.toDomain()));
        when(userService.findById(userA.getId())).thenReturn(Optional.of(userA.toDomain()));

        CodeEntryVo codeEntryVo = new CodeEntryVo(userA.getId(), dayRequest, System.currentTimeMillis());
        when(codeStore.get(request.getCode())).thenReturn(codeEntryVo);

        when(coupleRepository.save(any())).thenReturn(couple);

        coupleService.confirmCouple(userB.getId(), request);

        verify(coupleRepository, times(1)).save(any());
        verify(codeStore, times(1)).remove(request.getCode());
        verify(reverseCodeStore, times(1)).remove(codeEntryVo);
    }


    @Test
    @DisplayName("존재하지 않는 코드 승인 시 예외 발생")
    void testConfirmCouple_CodeNotFound() {
        CoupleRcodeReqestDto request = new CoupleRcodeReqestDto("invalid-code");
        UserEntity userB = users.get(1);

        when(userService.findById(userB.getId())).thenReturn(Optional.of(userB.toDomain()));

        when(codeStore.get(request.getCode())).thenReturn(null);

        assertThatThrownBy(() -> coupleService.confirmCouple(userB.getId(), request))
                .isInstanceOf(CoupleException.CodeNotFoundException.class);
    }


    @Test
    @DisplayName("커플 삭제 테스트")
    void testDeleteCouple() {
        Long coupleId = 1L;
        when(coupleRepository.existsById(coupleId)).thenReturn(true);

        coupleService.deleteCouple(coupleId);

        verify(coupleRepository, times(1)).deleteById(coupleId);
    }

    @Test
    @DisplayName("존재하지 않는 커플 삭제 시 예외 발생")
    void testDeleteCouple_NotFound() {
        Long coupleId = 999L;
        when(coupleRepository.existsById(coupleId)).thenReturn(false);

        assertThatThrownBy(() -> coupleService.deleteCouple(coupleId))
                .isInstanceOf(CoupleException.CoupleNotFoundException.class);
    }

    @Test
    @DisplayName("자기 자신과 커플을 만들 수 없는 경우 예외 발생")
    void testConfirmCouple_SelfPairingNotAllowed() {
        CoupleRcodeReqestDto request = new CoupleRcodeReqestDto("some-code");

        when(userService.findById(userA.getId())).thenReturn(Optional.of(userA.toDomain()));

        CodeEntryVo codeEntryVo = new CodeEntryVo(userA.getId(), dayRequest, System.currentTimeMillis());
        when(codeStore.get(request.getCode())).thenReturn(codeEntryVo);

        assertThatThrownBy(() -> coupleService.confirmCouple(userA.getId(), request))
                .isInstanceOf(CoupleException.CannotPairWithYourselfException.class)
                .hasMessage("나자신을 커플로 만들수 없습니다.");
    }
}
