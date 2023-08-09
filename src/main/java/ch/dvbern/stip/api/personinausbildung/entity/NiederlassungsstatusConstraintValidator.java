package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NiederlassungsstatusConstraintValidator implements ConstraintValidator<NiederlassungsstatusRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (personInAusbildung.getNationalitaet() != Land.CH) {
			return personInAusbildung.getNiederlassungsstatus() != null;
		}
		return true;
	}
}
