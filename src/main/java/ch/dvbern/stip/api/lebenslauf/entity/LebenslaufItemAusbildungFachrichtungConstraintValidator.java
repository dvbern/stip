package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_NULL_MESSAGE;

public class LebenslaufItemAusbildungFachrichtungConstraintValidator
		implements ConstraintValidator<LebenslaufItemAusbildungFachrichtungConstraint, LebenslaufItem> {
	@Override
	public boolean isValid(LebenslaufItem lebenslaufItem, ConstraintValidatorContext constraintValidatorContext) {
		if (lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE
				|| lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.BACHELOR_HOCHSCHULE_UNI
				|| lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.MASTER) {
			return lebenslaufItem.getFachrichtung() != null;
		}

		if (lebenslaufItem.getFachrichtung() == null) {
			return true;
		}

		constraintValidatorContext.disableDefaultConstraintViolation();
		constraintValidatorContext.buildConstraintViolationWithTemplate(
						VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_NULL_MESSAGE)
				.addConstraintViolation();
		return false;
	}
}
