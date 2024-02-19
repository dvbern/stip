package ch.dvbern.stip.api.familiensituation.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WerZahltAlimenteRequiredFieldConstraintValidator.class)
@Documented
public @interface WerZahltAlimenteRequiredFieldConstraint {

    String message() default VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
