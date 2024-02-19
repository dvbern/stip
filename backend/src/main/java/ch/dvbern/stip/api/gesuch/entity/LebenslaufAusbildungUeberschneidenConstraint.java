package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUF_AUSBILDUNG_UEBERSCHNEIDEN_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LebenslaufAusbildungUeberschneidenConstraintValidator.class)
@Documented
public @interface LebenslaufAusbildungUeberschneidenConstraint {
    String message() default VALIDATION_LEBENSLAUF_AUSBILDUNG_UEBERSCHNEIDEN_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
