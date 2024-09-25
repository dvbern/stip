package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_FAMILIENSITUATION_ELTERN_ABWESENHEITSGRUND_WEDERNOCH_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AbstractFamilienEntityWohnsitzConstraintValidator.class)
@Documented
public @interface AbstractFamilieEntityWohnsitzConstraint {
    String message() default VALIDATION_FAMILIENSITUATION_ELTERN_ABWESENHEITSGRUND_WEDERNOCH_MESSAGE ;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
