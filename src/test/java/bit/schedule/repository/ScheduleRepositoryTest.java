package bit.schedule.repository;


import bit.couple.domain.Couple;
import bit.couple.enums.CoupleStatus;
import bit.schedule.config.TestConfig;
import bit.schedule.domain.Schedule;
import bit.user.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static bit.schedule.util.ScheduleFixture.getNewSchedule;
import static bit.schedule.util.UserEntityFixture.getNewUserEntity;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class ScheduleRepositoryTest {


    @Autowired
    private ScheduleRepository scheduleRepository;


    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("커플의 모든 일정을 조회한다")
    void findAllCoupleSchedule() {
        // given
        UserEntity user1 = getNewUserEntity();
        UserEntity user2 = getNewUserEntity();
        Schedule schedule1 = getNewSchedule(user1);
        Schedule schedule2 = getNewSchedule(user2);

        Couple couple = Couple.of(user1, user2, CoupleStatus.APPROVED);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);
        entityManager.persist(couple);

        // when
        List<Schedule> result = scheduleRepository.findAllCoupleSchedule(user1.getId());

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsOnly(schedule1, schedule2);

        // user2로도 동일한 결과를 가져오는지 확인
        List<Schedule> result2 = scheduleRepository.findAllCoupleSchedule(user2.getId());
        assertThat(result2).hasSize(2);
        assertThat(result2).containsOnly(schedule1, schedule2);
    }

    @Test
    @DisplayName("소속된 커플이 없으면, 자신의 모든 일정을 조회한다")
    void findAllCoupleScheduleWhenUserIsNotCouple() {
        // given
        UserEntity user1 = getNewUserEntity();
        Schedule schedule1 = getNewSchedule(user1);
        Schedule schedule2 = getNewSchedule(user1);

        entityManager.persist(user1);
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);

        // when
        List<Schedule> result3 = scheduleRepository.findAllCoupleSchedule(user1.getId());

        // then
        assertThat(result3).hasSize(2);
        assertThat(result3).containsOnly(schedule1, schedule2);
    }


    @Test
    @DisplayName("특정 일정을 ID로 조회한다")
    void findScheduleById() {
        // given
        UserEntity user1 = getNewUserEntity();
        Schedule schedule1 = getNewSchedule(user1);
        entityManager.persist(user1);
        entityManager.persist(schedule1);

        // when
        Optional<Schedule> schedule = scheduleRepository.findSchedule(user1.getId(), schedule1.getId());

        assertThat(schedule).isPresent();
        assertThat(schedule.get()).isEqualTo(schedule1);
    }

    @Test
    @DisplayName("사용자의 모든 일정을 조회한다")
    void findAllUserSchedule() {
        // given
        UserEntity user1 = getNewUserEntity();
        Schedule schedule1 = getNewSchedule(user1);
        Schedule schedule2 = getNewSchedule(user1);
        Schedule schedule3 = getNewSchedule(user1);
        entityManager.persist(user1);
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);
        entityManager.persist(schedule3);
        // when
        List<Schedule> result = scheduleRepository.findAllUserSchedule(user1.getId());

        // then
        assertThat(result).hasSize(3);
        assertThat(result).containsOnly(schedule1, schedule2, schedule3);
    }

}
