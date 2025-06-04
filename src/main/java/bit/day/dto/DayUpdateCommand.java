package bit.day.dto;

import bit.base.BaseCommand;
import java.time.LocalDate;

public record DayUpdateCommand(Long dayId, Long userId, String title, LocalDate startDate) implements
        BaseCommand {
}
