package bit.app.day.dto;

import bit.app.base.BaseCommand;
import java.time.LocalDate;

public record DayUpdateCommand(Long dayId, Long userId, String title, LocalDate startDate) implements
        BaseCommand {
}
