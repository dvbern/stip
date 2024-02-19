package ch.dvbern.stip.api.familiensituation.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_NULL_MESSAGE;

public class WerZahltAlimenteRequiredFieldConstraintValidator
		implements ConstraintValidator<WerZahltAlimenteRequiredFieldConstraint, Familiensituation> {

	@Override
	public boolean isValid(
			Familiensituation familiensituation,
			ConstraintValidatorContext constraintValidatorContext) {
		if (familiensituation.getGerichtlicheAlimentenregelung() != null && familiensituation.getGerichtlicheAlimentenregelung()) {
			return familiensituation.getWerZahltAlimente() != null;
		}
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                        VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_NULL_MESSAGE)
                .addConstraintViolation();
        return familiensituation.getWerZahltAlimente() == null;
    }
}
