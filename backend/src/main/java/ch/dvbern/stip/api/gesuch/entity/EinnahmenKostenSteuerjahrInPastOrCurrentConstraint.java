package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_STEUERJAHR_INVALID_MESSAGE;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator.class)
@Documented
public @interface EinnahmenKostenSteuerjahrInPastOrCurrentConstraint {
    String message() default VALIDATION_EINNAHMEN_KOSTEN_STEUERJAHR_INVALID_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
