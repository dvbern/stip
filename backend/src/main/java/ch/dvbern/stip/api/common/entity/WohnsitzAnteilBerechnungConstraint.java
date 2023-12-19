package ch.dvbern.stip.api.common.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WohnsitzAnteilBerechnungConstraintValidator.class)
@Documented
public @interface WohnsitzAnteilBerechnungConstraint {

	String message() default VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
