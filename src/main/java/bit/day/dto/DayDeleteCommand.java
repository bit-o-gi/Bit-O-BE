package bit.day.dto;

import bit.base.BaseCommand;
import bit.day.domain.Day;

public record DayDeleteCommand(Long userId, Long dayId) implements BaseCommand<Day> {
}
