package ch.dvbern.stip.api.personinausbildung.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_HEIMATORT_FIELD_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LandCHRequiredConstraintValidator.class)
@Documented
public @interface LandCHRequiredConstraint {

    String message() default VALIDATION_HEIMATORT_FIELD_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
