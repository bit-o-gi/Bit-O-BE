package bit.app.day.service;

import bit.app.couple.domain.Couple;
import bit.app.day.domain.Day;
import bit.app.day.dto.DayRegisterCommand;
import bit.app.day.dto.DayUpdateCommand;
import bit.app.day.exception.DayException;
import bit.app.day.repository.DayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayService {
    private final DayRepository dayRepository;

    public Day getDayById(Long id) {
        return dayRepository.findById(id)
                .orElseThrow(() -> new DayException("시작일을 찾을 수 없습니다."));
    }

    public Day getDayByCouple(Couple couple) {
        return dayRepository.findByCouple(couple)
                .orElseThrow(() -> new DayException("커플로 시작일을 찾을 수 없습니다."));
    }

    public Long createDay(Couple couple, DayRegisterCommand command) {
        Day day = Day.create(command, couple);

        return dayRepository.save(day).getId();
    }

    public Long updateDay(Day day, DayUpdateCommand command) {
        day.update(command);
        return day.getId();
    }

    public void deleteDay(Long id, Couple couple) {
        Day deleteDay = getDayById(id);
        deleteDay.checkCouple(couple);

        dayRepository.delete(deleteDay);
    }
}
