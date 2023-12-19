package ch.dvbern.stip.api.ausbildung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE;

public class AusbildungNichtGefundenRequiredFieldsConstraintValidator implements ConstraintValidator<AusbildungNichtGefundenRequiredFieldsConstraint, Ausbildung> {

	@Override
	public boolean isValid(
			Ausbildung ausbildung,
			ConstraintValidatorContext constraintValidatorContext) {
		if (ausbildung.isAusbildungNichtGefunden()) {
			return StringUtils.isNotEmpty(ausbildung.getAlternativeAusbildungsgang())
					&& StringUtils.isNotEmpty(ausbildung.getAlternativeAusbildungsstaette());
		}

		constraintValidatorContext.disableDefaultConstraintViolation();
		constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)
				.addConstraintViolation();
		return ausbildung.getAusbildungsgang() != null;
	}
}
