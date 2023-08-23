package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE;

public class AusbildungskostenStufeRequiredConstraintValidator
		implements ConstraintValidator<AusbildungskostenStufeRequiredConstraint, GesuchFormular> {
	@Override
	public boolean isValid(
			GesuchFormular gesuchFormular,
			ConstraintValidatorContext constraintValidatorContext) {
		if (gesuchFormular.getAusbildung() == null || gesuchFormular.getEinnahmenKosten() == null) {
			return true;
		} else if (gesuchFormular.getAusbildung().getAusbildungsgang().getAusbildungsrichtung().getLevel() == 2) {
			return gesuchFormular.getEinnahmenKosten().getAusbildungskostenSekundarstufeZwei() != null;
		} else if (gesuchFormular.getAusbildung().getAusbildungsgang().getAusbildungsrichtung().getLevel() == 3) {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(
							VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE)
					.addConstraintViolation();
			return gesuchFormular.getEinnahmenKosten().getAusbildungskostenTertiaerstufe() != null;
		}
		return true;
	}
}
