package ch.dvbern.stip.api.benutzer.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_BUCHSTABEN_RANGE_MESSAGE;

@Target({ ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BuchstabenRangeConstraintValidator.class)
@Documented
public @interface BuchstabenRangeConstraint {
	String message() default VALIDATION_BUCHSTABEN_RANGE_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	boolean optional() default false;
}
