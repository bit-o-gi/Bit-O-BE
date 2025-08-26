package bit.app.day.dto;

import bit.app.base.BaseCommand;

public record DayDeleteCommand(Long userId, Long dayId) implements BaseCommand {
}
