package ch.dvbern.stip.api.steuerdaten.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_STEUERDATEN_STEUERJAHR_INVALID_MESSAGE;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SteuerdatenSteuerjahrInPastOrCurrentConstraintValidator.class)
@Documented
public @interface SteuerdatenSteuerjahrInPastOrCurrentConstraint {
    String message() default VALIDATION_STEUERDATEN_STEUERJAHR_INVALID_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
