package bit.anniversary.repository;

import static org.assertj.core.api.Assertions.assertThat;

import bit.anniversary.dto.AnDto;
import bit.anniversary.dto.AnReqDto;
import bit.anniversary.entity.Anniversary;
import bit.user.domain.User;
import bit.user.entity.UserEntity;
import bit.user.enums.OauthPlatformType;
import bit.user.repository.UserJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnRepositoryTest {

    @Autowired
    private AnRepository anRepository;

    @Autowired
    private UserJpaRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();
    private UserEntity writerEntity;
    private UserEntity withPeopleEntity;

    @BeforeEach
    void setUp() {
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

        // UserEntity로 변환하여 저장
        writerEntity = UserEntity.from(writer);
        withPeopleEntity = UserEntity.from(withPeople);
        userRepository.save(writerEntity);
        userRepository.save(withPeopleEntity);
    }

    @DisplayName("기념일 저장 및 조회 테스트")
    @Test
    void saveAndFindAnniversaryTest() {
        // 기념일 저장
        Anniversary savedAnniversary = saveTestAnniversary("2023-12-25T00:00");

        // 기념일 조회
        Anniversary foundAnniversary = anRepository.findById(savedAnniversary.getId()).orElse(null);

        // 검증
        assertThat(foundAnniversary).isNotNull();
        assertThat(foundAnniversary.getTitle()).isEqualTo("Test Anniversary");
        assertThat(foundAnniversary.getWriter().getEmail()).isEqualTo(writerEntity.getEmail());
        assertThat(foundAnniversary.getWithPeople().getEmail()).isEqualTo(withPeopleEntity.getEmail());
    }

    @DisplayName("특정 날짜 범위의 기념일 조회 테스트")
    @Test
    void findAllByAnniversaryDateBetweenTest() {
        // 테스트용 기념일 저장
        Anniversary anniversary1 = saveTestAnniversary("2023-12-25T00:00");
        saveTestAnniversary("2024-01-10T00:00");

        LocalDateTime startDate = LocalDateTime.of(2023, 12, 24, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 5, 23, 59);

        // 특정 날짜 범위 내의 기념일 검색
        List<Anniversary> anniversaries = anRepository.findAllByAnniversaryDateBetween(startDate, endDate);

        // 검증
        assertThat(anniversaries).hasSize(1);
        assertThat(anniversaries.get(0)).isEqualTo(anniversary1);
    }

    @DisplayName("특정 날짜 이후의 기념일 조회 테스트")
    @Test
    void findAllByAnniversaryDateAfterTest() {
        // 테스트용 기념일 저장
        saveTestAnniversary("2023-12-25T00:00");
        Anniversary anniversary2 = saveTestAnniversary("2024-01-10T00:00");

        LocalDateTime date = LocalDateTime.of(2023, 12, 26, 0, 0);

        // 특정 날짜 이후의 기념일 검색
        List<Anniversary> anniversaries = anRepository.findAllByAnniversaryDateAfter(date);

        // 검증
        assertThat(anniversaries).hasSize(1);
        assertThat(anniversaries.get(0)).isEqualTo(anniversary2);
    }

    @DisplayName("특정 날짜 이전의 기념일 조회 테스트")
    @Test
    void findAllByAnniversaryDateBeforeTest() {
        // 테스트용 기념일 저장
        Anniversary anniversary1 = saveTestAnniversary("2023-12-25T00:00");
        saveTestAnniversary("2024-01-10T00:00");

        LocalDateTime date = LocalDateTime.of(2024, 1, 1, 0, 0);

        // 특정 날짜 이전의 기념일 검색
        List<Anniversary> anniversaries = anRepository.findAllByAnniversaryDateBefore(date);

        // 검증
        assertThat(anniversaries).hasSize(1);
        assertThat(anniversaries.get(0)).isEqualTo(anniversary1);
    }

    // 테스트 기념일 저장 헬퍼 메서드
    private Anniversary saveTestAnniversary(String anniversaryDate) {
        AnReqDto anReqDto = AnReqDto.builder()
                .writerEmail(writerEntity.getEmail())
                .withPeopleEmail(withPeopleEntity.getEmail())
                .title("Test Anniversary")
                .anniversaryDate(anniversaryDate)
                .build();

        AnDto anDto = anReqDto.toAnDto();
        Anniversary anniversary = anDto.toEntity(modelMapper);
        anniversary.updateAnniversary(anDto, writerEntity, withPeopleEntity);

        return anRepository.save(anniversary); // 반환값을 명확히 함
    }
}
