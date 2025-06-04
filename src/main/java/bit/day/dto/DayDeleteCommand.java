package bit.day.dto;

import bit.base.BaseCommand;

public record DayDeleteCommand(Long userId, Long dayId) implements BaseCommand {
}
