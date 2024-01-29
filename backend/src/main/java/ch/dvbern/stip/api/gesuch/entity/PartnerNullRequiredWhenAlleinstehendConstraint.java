package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_PARTNER_NULL_REQUIRED_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PartnerNullRequiredWhenAlleinstehendConstraintValidator.class)
@Documented
public @interface PartnerNullRequiredWhenAlleinstehendConstraint {
	String message() default VALIDATION_PARTNER_NULL_REQUIRED_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
