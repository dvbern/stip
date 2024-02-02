package ch.dvbern.stip.api.personinausbildung.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

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
