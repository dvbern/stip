package ch.dvbern.stip.api.personinausbildung.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_IZW_FIELD_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentischerZivilrechtlicherWohnsitzRequiredConstraintValidator.class)
@Documented
public @interface IdentischerZivilrechtlicherWohnsitzRequiredConstraint {

	String message() default VALIDATION_IZW_FIELD_REQUIRED_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
