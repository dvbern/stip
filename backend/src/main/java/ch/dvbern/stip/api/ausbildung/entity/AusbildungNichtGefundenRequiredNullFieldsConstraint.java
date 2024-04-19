package ch.dvbern.stip.api.ausbildung.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AusbildungNichtGefundenRequiredNullFieldsConstraintValidator.class)
@Documented
public @interface AusbildungNichtGefundenRequiredNullFieldsConstraint {
    String message() default VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
