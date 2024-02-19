package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_PERSONS_AHV_NUMBER_UNIQUENESS_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueSvNumberConstraintValidator.class)
@Documented
public @interface UniqueSvNumberConstraint {
    String message() default VALIDATION_PERSONS_AHV_NUMBER_UNIQUENESS_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
