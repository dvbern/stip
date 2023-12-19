package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NULL_MESSAGE;

public class LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator
		implements ConstraintValidator<LebenslaufItemAusbildungTitelDesAbschlussesConstraint, LebenslaufItem> {
	@Override
	public boolean isValid(LebenslaufItem lebenslaufItem, ConstraintValidatorContext constraintValidatorContext) {
		if (lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS) {
			return lebenslaufItem.getTitelDesAbschlusses() != null;
		}

		if (lebenslaufItem.getTitelDesAbschlusses() == null) {
			return true;
		}

		constraintValidatorContext.disableDefaultConstraintViolation();
		constraintValidatorContext.buildConstraintViolationWithTemplate(
						VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_TITEL_DES_ABSCHLUSSES_NULL_MESSAGE)
				.addConstraintViolation();
		return false;
	}
}
