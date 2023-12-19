package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_HEIMATORT_FIELD_REQUIRED_NULL_MESSAGE;

public class LandCHRequiredConstraintValidator implements ConstraintValidator<LandCHRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (personInAusbildung.getNationalitaet() == Land.CH) {
			return StringUtils.isNotEmpty(personInAusbildung.getHeimatort());
		}
		else {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_HEIMATORT_FIELD_REQUIRED_NULL_MESSAGE)
					.addConstraintViolation();
			return personInAusbildung.getHeimatort() == null;
		}
	}
}
