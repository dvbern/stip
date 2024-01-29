package ch.dvbern.stip.api.personinausbildung.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NiederlassungsstatusConstraintValidator.class)
@Documented
public @interface NiederlassungsstatusRequiredConstraint {

	String message() default VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
