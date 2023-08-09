package ch.dvbern.stip.api.familiensituation.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WerZahltAlimenteRequiredFieldConstraintValidator implements ConstraintValidator<WerZahltAlimenteRequiredFieldConstraint, Familiensituation> {

	@Override
	public boolean isValid(
			Familiensituation familiensituation,
			ConstraintValidatorContext constraintValidatorContext) {
		if (familiensituation.getGerichtlicheAlimentenregelung()) {
			return familiensituation.getWerZahltAlimente() != null;
		}
		return true;
	}
}
