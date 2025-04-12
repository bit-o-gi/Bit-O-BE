package bit.day.service;

import bit.couple.domain.Couple;
import bit.couple.exception.CoupleException.CoupleNotFoundException;
import bit.couple.repository.CoupleRepository;
import bit.day.domain.Day;
import bit.day.dto.DayCommand;
import bit.day.exception.DayException.DayNotFoundException;
import bit.day.repository.DayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DayService {
    private final DayRepository dayRepository;
    private final CoupleRepository coupleRepository;

    public Day getDay(Long id) {
        return dayRepository.findById(id).orElseThrow(DayNotFoundException::new);
    }

    public Day createDay(DayCommand command) {
        Couple couple = coupleRepository.findById(command.coupleId).orElseThrow(CoupleNotFoundException::new);
        Day dday = Day.builder()
                .couple(couple)
                .title(command.title)
                .startDate(command.startDate)
                .build();
        return dayRepository.save(dday);
    }

    public Day updateDay(Long id, DayCommand dayCommand) {
        Day day = dayRepository.findById(id).orElseThrow(DayNotFoundException::new);
        day.update(dayCommand);
        return dayRepository.save(day);
    }

    public void deleteDay(Long id) {
        if (!dayRepository.existsById(id)) {
            throw new DayNotFoundException();
        }
        dayRepository.deleteById(id);
    }
}
