package ch.dvbern.stip.api.personinausbildung.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_VERMOEGEN_VORJAHR_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VermoegenVorjahrRequiredConstraintValidator.class)
@Documented
public @interface VermoegenVorjahrRequiredConstraint {
    String message() default VALIDATION_VERMOEGEN_VORJAHR_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
