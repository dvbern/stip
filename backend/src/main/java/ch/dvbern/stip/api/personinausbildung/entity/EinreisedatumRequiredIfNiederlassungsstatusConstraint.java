package ch.dvbern.stip.api.personinausbildung.entity;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINREISEDATUM_FIELD_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator.class)
@Documented
public @interface EinreisedatumRequiredIfNiederlassungsstatusConstraint {
    String message() default VALIDATION_EINREISEDATUM_FIELD_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
