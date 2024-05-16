package ch.dvbern.stip.api.ausbildung.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNGSORT_IF_SWISS_MESSAGE;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AusbildungsortRequiredIfSwissConstraintValidator.class)
@Documented
public @interface AusbildungsortRequiredIfSwissConstraint {
    String message() default VALIDATION_AUSBILDUNGSORT_IF_SWISS_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
