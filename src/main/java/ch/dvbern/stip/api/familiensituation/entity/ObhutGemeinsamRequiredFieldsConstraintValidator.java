package ch.dvbern.stip.api.familiensituation.entity;

import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ObhutGemeinsamRequiredFieldsConstraintValidator implements ConstraintValidator<ObhutGemeinsamRequiredFieldsConstraint, Familiensituation> {

	@Override
	public boolean isValid(
			Familiensituation familiensituation,
			ConstraintValidatorContext constraintValidatorContext) {
		if (familiensituation.getObhut() == Elternschaftsteilung.GEMEINSAM) {
			return familiensituation.getObhutVater() != null && familiensituation.getObhutMutter() != null;
		}
		return true;
	}
}
