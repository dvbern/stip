package ch.dvbern.stip.api.personinausbildung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class IdentischerZivilrechtlicherWohnsitzRequiredConstraintValidator implements ConstraintValidator<IdentischerZivilrechtlicherWohnsitzRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (!personInAusbildung.isIdentischerZivilrechtlicherWohnsitz()) {
			return StringUtils.isNotEmpty(personInAusbildung.getIdentischerZivilrechtlicherWohnsitzPLZ()) && StringUtils.isNotEmpty(personInAusbildung.getIdentischerZivilrechtlicherWohnsitzOrt());
		}
		return true;
	}
}
