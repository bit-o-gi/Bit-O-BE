package bit.day.dto;

import bit.base.BaseCommand;
import bit.day.domain.Day;
import java.time.LocalDate;

public record DayCommand(Long coupleId, String title, LocalDate startDate) implements BaseCommand<Day> {
}
