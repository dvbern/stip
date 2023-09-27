package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator
		implements ConstraintValidator<LebenslaufItemAusbildungBerufsbezeichnungConstraint, LebenslaufItem> {
	@Override
	public boolean isValid(LebenslaufItem lebenslaufItem, ConstraintValidatorContext constraintValidatorContext) {
		if (lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST || lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS) {
			return lebenslaufItem.getBerufsbezeichnung() != null;
		}

		return lebenslaufItem.getBerufsbezeichnung() == null;
	}
}
