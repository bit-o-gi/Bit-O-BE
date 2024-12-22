package bit.anniversary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bit.anniversary.dto.AnDto;
import bit.anniversary.dto.AnReqDto;
import bit.anniversary.dto.AnResDto;
import bit.anniversary.entity.Anniversary;
import bit.anniversary.repository.AnRepository;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.oauth.enums.OauthPlatformType;
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

    private UserEntity writerEntity;
    private UserEntity withPeopleEntity;
    private AnReqDto anReqDto;
    private AnDto anDto;
    private Anniversary anniversary;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // User 객체 생성 후 UserEntity로 변환
        User writer = User.builder()
                .id(1L)
                .email("writer@example.com")
                .nickName("Writer")
                .platform(OauthPlatformType.KAKAO)
                .build();
        User withPeople = User.builder()
                .id(2L)
                .email("withpeople@example.com")
                .nickName("WithPeople")
                .platform(OauthPlatformType.KAKAO)
                .build();

        // UserEntity로 변환
        writerEntity = UserEntity.from(writer);
        withPeopleEntity = UserEntity.from(withPeople);

        anReqDto = AnReqDto.builder()
                .writerEmail("writer@example.com")
                .withPeopleEmail("withpeople@example.com")
                .title("Test Anniversary")
                .anniversaryDate("2023-12-25T00:00")
                .build();

        anDto = AnDto.builder()
                .id(1L)
                .title("Test Anniversary")
                .writerEmail("writer@example.com")
                .withPeopleEmail("withpeople@example.com")
                .anniversaryDate("2023-12-25T00:00")
                .build();

        anniversary = Anniversary.builder()
                .id(1L)
                .title("Test Anniversary")
                .anniversaryDate(LocalDateTime.parse("2023-12-25T00:00"))
                .writer(writerEntity)
                .withPeople(withPeopleEntity)
                .build();
    }

    @DisplayName("기념일 생성 테스트")
    @Test
    void createAnniversaryTest() {
        // Arrange
        when(userRepository.findByEmail(anReqDto.getWriterEmail())).thenReturn(Optional.of(writerEntity));
        when(userRepository.findByEmail(anReqDto.getWithPeopleEmail())).thenReturn(Optional.of(withPeopleEntity));

        // anReqDto -> anDto 변환을 위한 ModelMapper 설정
        when(modelMapper.map(anReqDto, AnDto.class)).thenReturn(anDto);

        // anDto -> Anniversary 변환을 위한 ModelMapper 설정
        Anniversary anniversary = Anniversary.builder()
                .title(anDto.getTitle())
                .anniversaryDate(LocalDateTime.parse(anDto.getAnniversaryDate()))
                .writer(writerEntity)
                .withPeople(withPeopleEntity)
                .build();
        when(modelMapper.map(anDto, Anniversary.class)).thenReturn(anniversary);

        // Mocking: `anRepository.save`가 `anniversary`를 반환하도록 설정
        when(anRepository.save(any(Anniversary.class))).thenReturn(anniversary);

        // Act
        AnResDto result = anService.createAnniversary(anReqDto);

        // Assert
        assertEquals(anDto.getTitle(), result.getTitle());
        assertEquals(anDto.getWriterEmail(), result.getWriter().getEmail());
        assertEquals(anDto.getWithPeopleEmail(), result.getWithPeople().getEmail());
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

        // Mock 기존 Anniversary
        Anniversary existingAnniversary = Anniversary.builder()
                .id(anniversaryId)
                .title("Old Title")
                .anniversaryDate(LocalDateTime.parse("2023-12-24T00:00"))
                .writer(writerEntity)
                .withPeople(withPeopleEntity)
                .build();
        when(anRepository.findById(anniversaryId)).thenReturn(Optional.of(existingAnniversary));

        // Mock UserRepository로 writer와 withPeople 찾기
        when(userRepository.findByEmail("writer@example.com")).thenReturn(Optional.of(writerEntity));
        when(userRepository.findByEmail("withpeople@example.com")).thenReturn(Optional.of(withPeopleEntity));

        // Mock anReqDto -> anDto 변환
        AnDto updatedAnDto = anReqDto.toAnDto();
        when(modelMapper.map(anReqDto, AnDto.class)).thenReturn(updatedAnDto);

        // anDto -> Anniversary로 변환 후 업데이트 및 저장 설정
        when(anRepository.save(existingAnniversary)).thenReturn(existingAnniversary);

        // Act
        AnResDto result = anService.updateAnniversary(anniversaryId, anReqDto);

        // Assert
        verify(anRepository).save(existingAnniversary); // save() 호출 검증
        assertEquals(updatedAnDto.getTitle(), result.getTitle());
        assertEquals(updatedAnDto.getWriterEmail(), result.getWriter().getEmail());
        assertEquals(updatedAnDto.getWithPeopleEmail(), result.getWithPeople().getEmail());
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

        when(anRepository.findAllByAnniversaryDateBetween(startDate, endDate)).thenReturn(List.of(anniversary));

        List<AnResDto> results = anService.findAnniversariesInRange(startDate, endDate);

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
