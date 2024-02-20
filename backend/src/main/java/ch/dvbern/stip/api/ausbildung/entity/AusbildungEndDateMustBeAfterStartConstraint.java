package ch.dvbern.stip.api.ausbildung.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_ENDDATE_AFTER_STARTDATE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AusbildungEndDateMustBeAfterStartConstraintValidator.class)
@Documented
public @interface AusbildungEndDateMustBeAfterStartConstraint {
    String message() default VALIDATION_AUSBILDUNG_ENDDATE_AFTER_STARTDATE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
