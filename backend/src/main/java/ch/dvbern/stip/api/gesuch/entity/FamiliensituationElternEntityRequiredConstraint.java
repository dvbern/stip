package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FamiliensituationElternEntityRequiredConstraintValidator.class)
@Documented
public @interface FamiliensituationElternEntityRequiredConstraint {

    String message() default VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
