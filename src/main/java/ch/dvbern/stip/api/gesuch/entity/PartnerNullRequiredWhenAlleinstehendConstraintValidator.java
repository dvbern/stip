package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PartnerNullRequiredWhenAlleinstehendConstraintValidator
		implements ConstraintValidator<PartnerNullRequiredWhenAlleinstehendConstraint, GesuchFormular> {
	@Override
	public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
		if (gesuchFormular.getPersonInAusbildung() == null) {
			return true;
		}
		return !gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft() && gesuchFormular.getPartner() == null
				|| gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft() && gesuchFormular.getPartner() != null;
	}
}