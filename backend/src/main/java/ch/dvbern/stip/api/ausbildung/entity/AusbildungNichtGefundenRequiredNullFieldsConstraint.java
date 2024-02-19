package ch.dvbern.stip.api.ausbildung.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

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
