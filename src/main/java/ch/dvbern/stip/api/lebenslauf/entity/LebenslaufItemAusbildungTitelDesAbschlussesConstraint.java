package ch.dvbern.stip.api.lebenslauf.entity;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NOTNULL_MESSAGE;

@Target({ ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator.class)
@Documented
public @interface LebenslaufItemAusbildungTitelDesAbschlussesConstraint {
	String message() default VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NOTNULL_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
