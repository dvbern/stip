package ch.dvbern.stip.api.partner.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_PARTNER_KOSTEN_FIELDS_REQUIRED_MESSAGE;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator.class)
@Documented
public @interface AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraint {

    String message() default VALIDATION_PARTNER_KOSTEN_FIELDS_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
