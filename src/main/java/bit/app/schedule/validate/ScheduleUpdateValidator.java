package bit.app.schedule.validate;

import bit.app.schedule.dto.ScheduleUpdateRequest;
import bit.app.schedule.validate.annotation.ScheduleUpdateValidate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ScheduleUpdateValidator implements
        ConstraintValidator<ScheduleUpdateValidate, ScheduleUpdateRequest> {

    @Override
    public boolean isValid(ScheduleUpdateRequest scheduleUpdateRequest,
                           ConstraintValidatorContext context) {
        return scheduleUpdateRequest.getTitle() != null ||
                scheduleUpdateRequest.getContent() != null ||
                scheduleUpdateRequest.getLocation() != null ||
                scheduleUpdateRequest.getStartDateTime() != null ||
                scheduleUpdateRequest.getEndDateTime() != null ||
                scheduleUpdateRequest.getColor() != null;
    }
}
