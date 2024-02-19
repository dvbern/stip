package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALIMENTE_REQUIRED_WHEN_ALIMENTEREGELUNG;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlimenteRequiredWhenAlimenteRegelungConstraintValidator.class)
@Documented
public @interface AlimenteRequiredWhenAlimenteregelungConstraint {
    String message() default VALIDATION_ALIMENTE_REQUIRED_WHEN_ALIMENTEREGELUNG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
