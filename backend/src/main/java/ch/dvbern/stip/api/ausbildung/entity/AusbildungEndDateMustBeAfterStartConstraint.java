package ch.dvbern.stip.api.ausbildung.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

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
