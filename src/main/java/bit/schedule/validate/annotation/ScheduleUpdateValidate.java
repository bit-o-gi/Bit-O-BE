package bit.schedule.validate.annotation;

import bit.schedule.validate.ScheduleUpdateValidator;
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
    String message() default "제목, 내용, 위치, 시작 일시, 종료 일시 중 하나는 null 이 아니어야 합니다.";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};
}
