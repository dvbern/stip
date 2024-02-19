package ch.dvbern.stip.api.common.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WohnsitzAnteilRequiredConstraintValidator.class)
@Documented
public @interface WohnsitzAnteilRequiredConstraint {

    String message() default VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
