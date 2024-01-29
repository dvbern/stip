package ch.dvbern.stip.api.eltern.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AHV_IF_SWISS_MESSAGE;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AhvIfSwissConstraintValidator.class)
@Documented
public @interface AhvIfSwissConstraint {
    String message() default VALIDATION_AHV_IF_SWISS_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
