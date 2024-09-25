package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AbstractFamilienEntityWohnsitzConstraintValidator.class)
@Documented
public @interface AbstractFamilieEntityWohnsitzConstraint {
    String message() default "tbd";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
