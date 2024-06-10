package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_WOHNKOSTEN_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = EinnahmenKostenWohnkostenRequiredConstraintValidator.class)
@Constraint(validatedBy = EinnahmenKostenVermoegenRequiredConstraintValidator.class)
@Documented
public @interface EinnahmenKostenVermoegenRequiredConstraint {
    String message() default VALIDATION_EINNAHMEN_KOSTEN_WOHNKOSTEN_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property() default "";
}
