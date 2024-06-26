package ch.dvbern.stip.api.gesuch.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_FAMILIENSITUATION_STEUERDATEN_ENTITY_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SteuerdatenTabsRequiredConstraintValidator.class)
@Documented
public @interface SteuerdatenTabsRequiredConstraint {
    String message() default VALIDATION_FAMILIENSITUATION_STEUERDATEN_ENTITY_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
