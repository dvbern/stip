package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE;

public class IdentischerZivilrechtlicherWohnsitzRequiredConstraintValidator implements ConstraintValidator<IdentischerZivilrechtlicherWohnsitzRequiredConstraint, Eltern> {

	@Override
	public boolean isValid(
			Eltern eltern,
			ConstraintValidatorContext constraintValidatorContext) {
		if (!eltern.isIdentischerZivilrechtlicherWohnsitz()) {
			return StringUtils.isNotEmpty(eltern.getIdentischerZivilrechtlicherWohnsitzPLZ()) && StringUtils.isNotEmpty(eltern.getIdentischerZivilrechtlicherWohnsitzOrt());
		}
		else {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE)
					.addConstraintViolation();
			return eltern.getIdentischerZivilrechtlicherWohnsitzPLZ() == null && eltern.getIdentischerZivilrechtlicherWohnsitzOrt() == null;
		}
	}
}
