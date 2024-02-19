package ch.dvbern.stip.api.common.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WohnsitzAnteilBerechnungConstraintValidator.class)
@Documented
public @interface WohnsitzAnteilBerechnungConstraint {

    String message() default VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
