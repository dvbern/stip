package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE2_REQUIRED_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AusbildungskostenStufeRequiredConstraintValidator.class)
@Documented
public @interface AusbildungskostenStufeRequiredConstraint {
    String message() default VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE2_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
