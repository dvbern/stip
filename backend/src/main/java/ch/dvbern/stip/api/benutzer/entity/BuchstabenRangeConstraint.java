package ch.dvbern.stip.api.benutzer.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_BUCHSTABEN_RANGE_MESSAGE;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BuchstabenRangeConstraintValidator.class)
@Documented
public @interface BuchstabenRangeConstraint {
    String message() default VALIDATION_BUCHSTABEN_RANGE_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean optional() default false;
}
