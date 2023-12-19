package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALIMENTE_NULL_WHEN_NO_ALIMENTEREGELUNG;

public class AlimenteRequiredWhenAlimenteRegelungConstraintValidator
		implements ConstraintValidator<AlimenteRequiredWhenAlimenteregelungConstraint, GesuchFormular> {
	@Override
	public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
		if (gesuchFormular.getFamiliensituation() == null || gesuchFormular.getEinnahmenKosten() == null) {
			return true;
		}

		if (gesuchFormular.getFamiliensituation().getGerichtlicheAlimentenregelung()) {
			return gesuchFormular.getEinnahmenKosten().getAlimente() != null;
		}

		if (gesuchFormular.getEinnahmenKosten().getAlimente() != null) {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(
							VALIDATION_ALIMENTE_NULL_WHEN_NO_ALIMENTEREGELUNG)
					.addConstraintViolation();
			return false;
		}

		return true;
	}

}