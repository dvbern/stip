package ch.dvbern.stip.api.gesuch.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Validator;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_ZULAGEN_REQUIRED_MESSAGE;

public class PartnerNullRequiredWhenAlleinstehendConstraintValidator
		implements ConstraintValidator<PartnerNullRequiredWhenAlleinstehendConstraint, GesuchFormular> {
	@Override
	public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
		if (gesuchFormular.getPersonInAusbildung() == null) {
			return true;
		}
		return gesuchFormular.getPersonInAusbildung().getZivilstand().hasOnePerson() && gesuchFormular.getPartner() == null
				|| !gesuchFormular.getPersonInAusbildung().getZivilstand().hasOnePerson() && gesuchFormular.getPartner() != null;
	}
}