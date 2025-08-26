package bit.schedule.repository;


import static bit.schedule.util.ScheduleFixture.getNewSchedule;
import static bit.schedule.util.UserEntityFixture.getNewUserEntity;
import static org.assertj.core.api.Assertions.assertThat;

import bit.app.couple.domain.Couple;
import bit.app.couple.enums.CoupleStatus;
import bit.app.schedule.domain.Schedule;
import bit.app.schedule.repository.ScheduleRepository;
import bit.app.user.entity.UserEntity;
import bit.schedule.config.TestConfig;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class ScheduleRepositoryTest {


    @Autowired
    private ScheduleRepository scheduleRepository;


    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("커플의 일정을 조회한다")
    void findAllCoupleSchedule() {
        // given
        UserEntity user1 = getNewUserEntity("test@test.com");
        UserEntity user2 = getNewUserEntity("test2@test.com");
        Schedule schedule1 = getNewSchedule(user1);
        Schedule schedule2 = getNewSchedule(user2);

        LocalDate now = LocalDate.now();

        Couple couple = Couple.of(user1, user2, CoupleStatus.APPROVED);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);
        entityManager.persist(couple);

        // when
        List<Schedule> result = scheduleRepository.findAllCoupleScheduleByYearAndMonth(user1.getId(), now.getYear(),
                now.getMonthValue());

        // then
        assertThat(result).hasSize(2).containsOnly(schedule1, schedule2);

        // user2로도 동일한 결과를 가져오는지 확인
        List<Schedule> result2 = scheduleRepository.findAllCoupleScheduleByYearAndMonth(user2.getId(), now.getYear(),
                now.getMonthValue());
        assertThat(result2).hasSize(2).containsOnly(schedule1, schedule2);
    }

    @Test
    @DisplayName("소속된 커플이 없으면, 자신의 일정을 조회한다")
    void findAllCoupleScheduleWhenUserIsNotCouple() {
        // given
        UserEntity user1 = getNewUserEntity();
        Schedule schedule1 = getNewSchedule(user1);
        Schedule schedule2 = getNewSchedule(user1);

        entityManager.persist(user1);
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);

        LocalDate now = LocalDate.now();

        // when
        List<Schedule> result3 = scheduleRepository.findAllCoupleScheduleByYearAndMonth(user1.getId(), now.getYear(),
                now.getMonthValue());

        // then
        assertThat(result3).hasSize(2).containsOnly(schedule1, schedule2);
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
        Optional<Schedule> schedule = scheduleRepository.findSchedule(user1.getId(),
                schedule1.getId());

        assertThat(schedule).isPresent();
        assertThat(schedule.get()).isEqualTo(schedule1);
    }

    @Test
    @DisplayName("사용자의 일정을 조회한다")
    void findAllUserScheduleByYearAndMonth() {
        // given
        UserEntity user1 = getNewUserEntity();
        Schedule schedule1 = getNewSchedule(user1);
        Schedule schedule2 = getNewSchedule(user1);
        Schedule schedule3 = getNewSchedule(user1);
        entityManager.persist(user1);
        entityManager.persist(schedule1);
        entityManager.persist(schedule2);
        entityManager.persist(schedule3);
        LocalDate now = LocalDate.now();

        // when
        List<Schedule> result = scheduleRepository.findAllUserScheduleByYearAndMonth(user1.getId(), now.getYear(),
                now.getMonthValue());

        // then
        assertThat(result).hasSize(3).containsOnly(schedule1, schedule2, schedule3);
    }

}
