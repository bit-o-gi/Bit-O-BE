package bit.day.dto;

import bit.base.BaseCommand;
import java.time.LocalDate;

public record DayRegisterCommand(Long coupleId, String title, LocalDate startDate) implements
        BaseCommand {
}
