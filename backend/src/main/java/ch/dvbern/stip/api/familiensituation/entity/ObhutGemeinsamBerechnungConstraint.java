package ch.dvbern.stip.api.familiensituation.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_OBHUT_GEMEINSAM_BERECHNUNG_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ObhutGemeinsamBerechnungConstraintValidator.class)
@Documented
public @interface ObhutGemeinsamBerechnungConstraint {

    String message() default VALIDATION_OBHUT_GEMEINSAM_BERECHNUNG_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
