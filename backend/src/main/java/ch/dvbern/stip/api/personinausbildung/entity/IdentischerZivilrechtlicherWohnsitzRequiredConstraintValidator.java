package ch.dvbern.stip.api.personinausbildung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE;

public class IdentischerZivilrechtlicherWohnsitzRequiredConstraintValidator implements ConstraintValidator<IdentischerZivilrechtlicherWohnsitzRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (!personInAusbildung.isIdentischerZivilrechtlicherWohnsitz()) {
			return StringUtils.isNotEmpty(personInAusbildung.getIdentischerZivilrechtlicherWohnsitzPLZ()) && StringUtils.isNotEmpty(personInAusbildung.getIdentischerZivilrechtlicherWohnsitzOrt());
		}
		else {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE)
					.addConstraintViolation();
			return personInAusbildung.getIdentischerZivilrechtlicherWohnsitzPLZ() == null && personInAusbildung.getIdentischerZivilrechtlicherWohnsitzOrt() == null;
		}
	}
}
