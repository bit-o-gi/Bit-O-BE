package bit.schedule.repository;

import bit.couple.domain.QCouple;
import bit.schedule.domain.QSchedule;
import bit.schedule.domain.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomScheduleRepositoryImpl implements CustomScheduleRepository {

    private final JPAQueryFactory queryFactory;

    public List<Schedule> findAllCoupleScheduleByYearAndMonth(Long userId, Integer year, Integer month) {
        QSchedule schedule = QSchedule.schedule;
        QCouple couple = QCouple.couple;
        LocalDate startDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth())
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

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
                    .and((schedule.startDateTime.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
                        .or(schedule.endDateTime.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX))))
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

    public List<Schedule> findAllUserScheduleByYearAndMonth(Long userId, Integer year, Integer month) {
        QSchedule schedule = QSchedule.schedule;
        LocalDate startDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth())
            .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        return queryFactory
            .select(schedule)
            .from(schedule)
            .where(schedule.user.id.eq(userId)
                .and((schedule.startDateTime.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)))
                    .or(schedule.endDateTime.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX))))
            )
            .fetch();
    }
}
