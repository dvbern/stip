package ch.dvbern.stip.api.lebenslauf.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE;

public class LebenslaufItemArtRequiredFieldsConstraintValidator
		implements ConstraintValidator<LebenslaufItemArtRequiredFieldsConstraint, LebenslaufItem> {

	@Override
	public boolean isValid(
			LebenslaufItem lebenslaufItem,
			ConstraintValidatorContext constraintValidatorContext) {
		if (lebenslaufItem.getBildungsart() == null && lebenslaufItem.getTaetigskeitsart() == null) {
			return false;
		} else if (lebenslaufItem.getBildungsart() != null && lebenslaufItem.getTaetigskeitsart() != null) {
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(
							VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE)
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
