package bit.app.schedule.validate.annotation;

import bit.app.schedule.validate.ScheduleUpdateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ScheduleUpdateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduleUpdateValidate {
    String message() default "제목, 내용, 위치, 시작 일시, 종료 일시 모두 null 이면 안됩니다.";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}
