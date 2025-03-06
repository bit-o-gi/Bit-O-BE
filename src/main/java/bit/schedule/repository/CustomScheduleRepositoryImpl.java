package bit.schedule.repository;

import bit.couple.domain.QCouple;
import bit.schedule.domain.QSchedule;
import bit.schedule.domain.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomScheduleRepositoryImpl implements CustomScheduleRepository {
    private final JPAQueryFactory queryFactory;

    public List<Schedule> findAllCoupleSchedule(Long userId) {
        QSchedule schedule = QSchedule.schedule;
        QCouple couple = QCouple.couple;

        return queryFactory
                .selectDistinct(schedule)
                .from(schedule)
                .leftJoin(couple)
                .on(couple.initiatorUser.id.eq(schedule.user.id)
                        .or(couple.partnerUser.id.eq(schedule.user.id)))
                .where(
                        couple.initiatorUser.id.eq(userId)
                                .or(couple.partnerUser.id.eq(userId))
                                .or(schedule.user.id.eq(userId))
                )
                .fetch();
    }

    public Optional<Schedule> findSchedule(Long userId, Long scheduleId) {
        QSchedule schedule = QSchedule.schedule;
        QCouple couple = QCouple.couple;

        return Optional.ofNullable(
                queryFactory
                        .select(schedule)
                        .from(schedule)
                        .leftJoin(couple)
                        .on(couple.initiatorUser.id.eq(schedule.user.id)
                                .or(couple.partnerUser.id.eq(schedule.user.id)))
                        .where((couple.initiatorUser.id.eq(userId)
                                .or(couple.partnerUser.id.eq(userId))
                                .or(schedule.user.id.eq(userId)))
                                .and(schedule.id.eq(scheduleId)))
                        .fetchOne());
    }

    public List<Schedule> findAllUserSchedule(Long userId) {
        QSchedule schedule = QSchedule.schedule;

        return queryFactory
                .select(schedule)
                .from(schedule)
                .where(schedule.user.id.eq(userId))
                .fetch();
    }
}
