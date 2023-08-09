package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LandCHRequiredConstraintValidator implements ConstraintValidator<LandCHRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (personInAusbildung.getAdresse().getLand() == Land.CH) {
			return personInAusbildung.getHeimatort() != null;
		}
		return true;
	}
}
