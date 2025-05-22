package bit.day.service;

import bit.day.domain.Day;
import bit.day.dto.DayCommand;
import bit.day.dto.DayResponse;
import bit.day.exception.DayException;
import bit.day.repository.DayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DayService {
    private final DayRepository dayRepository;
//    private final CoupleService coupleService;

    private Day getDayById(Long id) {
        return dayRepository.findById(id)
                .orElseThrow(() -> new DayException("시작일을 찾을 수 없습니다."));
    }

    public DayResponse getDay(Long id) {
        Day day = getDayById(id);

        return DayResponse.from(day);
    }

    @Transactional(readOnly = true)
    public DayResponse getDayByCouple(Long coupleId) {
//        Couple couple = coupleService.getCoupleById(coupleId);
//        Day day = dayRepository.findByCouple(couple)
//                .orElseThrow(() -> new DayException("커플로 시작일을 찾을 수 없습니다."));
        Day day = null;

        return DayResponse.from(null);
    }

    @Transactional
    public Long createDay(DayCommand command) {
//        Couple couple = coupleService.getCoupleById(command.coupleId());
        Day day = Day.builder()
                .couple(null)
                .title(command.title())
                .startDate(command.startDate())
                .build();

        return dayRepository.save(day).getId();
    }

    @Transactional
    public DayResponse updateDay(Long id, DayCommand dayCommand) {
        Day day = getDayById(id);
        day.update(dayCommand);

        return DayResponse.from(day);
    }

    public void deleteDay(Long id) {
        Day day = getDayById(id);

        dayRepository.delete(day);
    }
}
