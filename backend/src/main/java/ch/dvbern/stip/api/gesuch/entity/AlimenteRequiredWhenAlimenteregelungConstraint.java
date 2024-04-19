package ch.dvbern.stip.api.gesuch.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALIMENTE_REQUIRED_WHEN_ALIMENTEREGELUNG;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlimenteRequiredWhenAlimenteRegelungConstraintValidator.class)
@Documented
public @interface AlimenteRequiredWhenAlimenteregelungConstraint {
    String message() default VALIDATION_ALIMENTE_REQUIRED_WHEN_ALIMENTEREGELUNG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
