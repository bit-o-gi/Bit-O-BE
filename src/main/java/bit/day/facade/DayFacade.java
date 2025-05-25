package bit.day.facade;

import bit.couple.domain.Couple;
import bit.couple.service.CoupleService;
import bit.day.domain.Day;
import bit.day.dto.DayDeleteCommand;
import bit.day.dto.DayRegisterCommand;
import bit.day.dto.DayResponse;
import bit.day.dto.DayUpdateCommand;
import bit.day.service.DayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DayFacade {
    private final DayService dayService;
    private final CoupleService coupleService;

    public DayResponse getDay(Long id) {
        Day day = dayService.getDayById(id);

        return DayResponse.from(day);
    }

    @Transactional(readOnly = true)
    public DayResponse getDayByCouple(Long userId) {
        Couple couple = coupleService.getCoupleEntityByUserId(userId);
        Day day = dayService.getDayByCouple(couple);

        return DayResponse.from(day);
    }

    @Transactional
    public Long createDay(DayRegisterCommand command) {
        Couple couple = coupleService.getCoupleEntityById(command.coupleId());

        return dayService.createDay(couple, command);
    }

    @Transactional
    public Long updateDay(DayUpdateCommand command) {
        Couple couple = coupleService.getCoupleEntityByUserId(command.userId());
        Day day = dayService.getDayByCouple(couple);

        return dayService.updateDay(day, command);
    }

    @Transactional
    public void deleteDay(DayDeleteCommand command) {
        Couple couple = coupleService.getCoupleEntityByUserId(command.userId());

        dayService.deleteDay(command.dayId(), couple);
    }
}
