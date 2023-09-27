package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator
		implements ConstraintValidator<LebenslaufItemAusbildungTitelDesAbschlussesConstraint, LebenslaufItem> {
	@Override
	public boolean isValid(LebenslaufItem lebenslaufItem, ConstraintValidatorContext constraintValidatorContext) {
		if (lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS) {
			return lebenslaufItem.getTitelDesAbschlusses() != null;
		}

		return lebenslaufItem.getTitelDesAbschlusses() == null;
	}
}
