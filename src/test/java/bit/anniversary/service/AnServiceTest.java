package bit.anniversary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bit.anniversary.dto.AnDto;
import bit.anniversary.dto.AnReqDto;
import bit.anniversary.dto.AnResDto;
import bit.anniversary.entity.Anniversary;
import bit.anniversary.repository.AnRepository;
import bit.auth.domain.UserPrincipal;
import bit.couple.domain.Couple;
import bit.couple.dto.CoupleResponseDto;
import bit.couple.enums.CoupleStatus;
import bit.couple.fixture.CoupleTestFixture;
import bit.couple.service.CoupleService;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.repository.UserJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

class AnServiceTest {

    @InjectMocks
    private AnService anService;

    @Mock
    private AnRepository anRepository;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    private CoupleService coupleService;

    private UserEntity writerEntity;
    private UserEntity withPeopleEntity;
    private AnReqDto anReqDto;
    private AnDto anDto;
    private Anniversary anniversary;

    private User writer;
    private UserPrincipal mockUserPrincipal;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Fixture에서 Couple 생성
        Couple couple = CoupleTestFixture.initialCouple();
        writerEntity = couple.getInitiatorUser();
        withPeopleEntity = couple.getPartnerUser();

        // Entity → Domain 변환
        writer = writerEntity.toDomain();
        User withPeople = withPeopleEntity.toDomain();

        // UserPrincipal mock 설정
        mockUserPrincipal = mock(UserPrincipal.class);
        when(mockUserPrincipal.getId()).thenReturn(writer.getId());

        // 요청 DTO 설정 (하드코딩 대신 실제 객체에서 값 가져오기)
        anReqDto = AnReqDto.builder()
                .writerEmail(writer.getEmail())
                .withPeopleEmail(withPeople.getEmail())
                .title("Test Anniversary")
                .anniversaryDate("2023-12-25T00:00:00")
                .build();

        // 중간 DTO
        anDto = AnDto.builder()
                .id(1L)
                .title(anReqDto.getTitle())
                .writerEmail(anReqDto.getWriterEmail())
                .withPeopleEmail(anReqDto.getWithPeopleEmail())
                .anniversaryDate(anReqDto.getAnniversaryDate())
                .build();

        // 최종 저장될 엔티티
        anniversary = Anniversary.builder()
                .id(1L)
                .title(anDto.getTitle())
                .anniversaryDate(LocalDateTime.parse(anDto.getAnniversaryDate()))
                .writer(writerEntity)
                .withPeople(withPeopleEntity)
                .build();
    }


    @DisplayName("기념일 생성 테스트")
    @Test
    void createAnniversaryTest() {
        when(coupleService.getCoupleByUserId(writer.getId()))
                .thenReturn(CoupleResponseDto.of(Couple.of(writerEntity, withPeopleEntity, CoupleStatus.APPROVED)));
        when(mockUserPrincipal.getId()).thenReturn(writer.getId());

        when(modelMapper.map(anReqDto, AnDto.class)).thenReturn(anDto);
        when(modelMapper.map(anDto, Anniversary.class)).thenReturn(anniversary);
        when(anRepository.save(any(Anniversary.class))).thenReturn(anniversary);

        AnResDto result = anService.createAnniversary(mockUserPrincipal, anReqDto);

        assertEquals(anReqDto.getTitle(), result.getTitle());
        assertEquals(writer.getEmail(), result.getWriter().getEmail());
        assertEquals(withPeopleEntity.getEmail(), result.getWithPeople().getEmail());
    }


    @DisplayName("기념일 ID로 조회 테스트")
    @Test
    void getAnniversaryTest() {
        when(anRepository.findById(1L)).thenReturn(Optional.of(anniversary));
        when(modelMapper.map(anniversary, AnDto.class)).thenReturn(anDto);

        AnResDto result = anService.getAnniversary(1L);

        assertEquals(anDto.getTitle(), result.getTitle());
        assertEquals(anDto.getWriterEmail(), result.getWriter().getEmail());
    }

    @DisplayName("기념일 업데이트 테스트")
    @Test
    void updateAnniversaryTest() {
        // Arrange
        Long anniversaryId = 1L;

        when(coupleService.getCoupleByUserId(writer.getId()))
                .thenReturn(CoupleResponseDto.of(Couple.of(writerEntity, withPeopleEntity, CoupleStatus.APPROVED)));
        when(mockUserPrincipal.getId()).thenReturn(writer.getId());

        when(anRepository.findById(anniversaryId)).thenReturn(Optional.of(anniversary));

        AnDto updatedAnDto = anReqDto.toAnDto();
        when(modelMapper.map(anReqDto, AnDto.class)).thenReturn(updatedAnDto);
        when(anRepository.save(anniversary)).thenReturn(anniversary);

        AnResDto result = anService.updateAnniversary(mockUserPrincipal, anniversaryId, anReqDto);
        verify(anRepository).save(anniversary);
        assertEquals(updatedAnDto.getTitle(), result.getTitle());

    }


    @DisplayName("기념일 삭제 테스트")
    @Test
    void deleteAnniversaryTest() {
        when(anRepository.findById(1L)).thenReturn(Optional.of(anniversary));

        anService.deleteAnniversary(1L);

        verify(anRepository).deleteById(1L);
    }

    @DisplayName("특정 날짜 범위 내 기념일 조회 테스트")
    @Test
    void findAnniversariesInRangeTest() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);

        when(anRepository.findByDateRangeAndUserInvolvedById(startDate, endDate, mockUserPrincipal.getId()))
                .thenReturn(List.of(anniversary));

        List<AnResDto> results = anService.findAnniversariesInRange(mockUserPrincipal, startDate, endDate);

        assertEquals(1, results.size());
        assertEquals("Test Anniversary", results.get(0).getTitle());
    }

    @DisplayName("다음 반복 기념일 계산 테스트")
    @Test
    void calculateNextAnniversaryTest() {
        when(anRepository.findById(1L)).thenReturn(Optional.of(anniversary));

        LocalDateTime nextAnniversary = anService.calculateNextAnniversary(1L);

        assertEquals(anniversary.getAnniversaryDate().plusYears(1), nextAnniversary);
    }
}
