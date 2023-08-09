package ch.dvbern.stip.api.personinausbildung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdentischerZivilrechtlicherWohnsitzRequiredConstraintValidator implements ConstraintValidator<IdentischerZivilrechtlicherWohnsitzRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (!personInAusbildung.isIdentischerZivilrechtlicherWohnsitz()) {
			return personInAusbildung.getIdentischerZivilrechtlicherWohnsitzPLZ() != null && personInAusbildung.getIdentischerZivilrechtlicherWohnsitzOrt() != null;
		}
		return true;
	}
}
