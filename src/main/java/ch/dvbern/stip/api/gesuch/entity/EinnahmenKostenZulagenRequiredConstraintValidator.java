package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenZulagenRequiredConstraintValidator implements ConstraintValidator<EinnahmenKostenZulagenRequiredConstraint, GesuchFormular> {
	@Override
	public boolean isValid(
			GesuchFormular gesuchFormular,
			ConstraintValidatorContext constraintValidatorContext) {
		if (gesuchFormular.getPersonInAusbildung() == null || gesuchFormular.getEinnahmenKosten() == null) {
			return true;
		} else if (gesuchFormular.getPersonInAusbildung().isKinder()) {
			return gesuchFormular.getEinnahmenKosten().getZulagen() != null;
		}
		return true;
	}
}
