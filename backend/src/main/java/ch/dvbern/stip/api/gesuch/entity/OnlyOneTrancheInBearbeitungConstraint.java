package ch.dvbern.stip.api.gesuch.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_TRANCHEN_INVALID_STATUS;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyOneTrancheInBearbeitungConstraintValidator.class)
@Documented
public @interface OnlyOneTrancheInBearbeitungConstraint {
    String message() default VALIDATION_TRANCHEN_INVALID_STATUS;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
