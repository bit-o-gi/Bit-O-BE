package bit.app.schedule.exception;

import jakarta.persistence.EntityNotFoundException;

public class ScheduleNotFoundException extends EntityNotFoundException {
    public ScheduleNotFoundException() {
        super("해당 스케줄이 존재하지 않습니다.");
    }
}
