package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LAND_CH_FIELD_REQUIRED_NULL_MESSAGE;

public class LandCHRequiredConstraintValidator implements ConstraintValidator<LandCHRequiredConstraint, PersonInAusbildung> {

	@Override
	public boolean isValid(
			PersonInAusbildung personInAusbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (personInAusbildung.getAdresse().getLand() == Land.CH) {
			return StringUtils.isNotEmpty(personInAusbildung.getHeimatort());
		}
		else {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_LAND_CH_FIELD_REQUIRED_NULL_MESSAGE)
					.addConstraintViolation();
			return personInAusbildung.getHeimatort() == null;
		}
	}
}
