package ch.dvbern.stip.api.partner.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_PARTNER_KOSTEN_FIELDS_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator.class)
@Documented
public @interface AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraint {

    String message() default VALIDATION_PARTNER_KOSTEN_FIELDS_REQUIRED_MESSAGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
