package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WohnsitzAnteilRequiredConstraintValidator implements ConstraintValidator<WohnsitzAnteilRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (personInAusbildung.getWohnsitz() == Wohnsitz.MUTTER_VATER) {
			return personInAusbildung.getWohnsitzAnteilVater() != null && personInAusbildung.getWohnsitzAnteilMutter() != null;
		}
		return true;
	}
}
