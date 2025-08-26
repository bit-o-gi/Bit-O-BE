package bit.app.day.dto;

import bit.app.base.BaseCommand;
import java.time.LocalDate;

public record DayRegisterCommand(Long coupleId, String title, LocalDate startDate) implements
        BaseCommand {
}
