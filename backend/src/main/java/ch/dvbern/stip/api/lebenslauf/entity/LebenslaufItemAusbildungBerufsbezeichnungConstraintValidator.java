package ch.dvbern.stip.api.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_BERUFSBEZEICHNUNG_NULL_MESSAGE;

public class LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator
    implements ConstraintValidator<LebenslaufItemAusbildungBerufsbezeichnungConstraint, LebenslaufItem> {
    @Override
    public boolean isValid(LebenslaufItem lebenslaufItem, ConstraintValidatorContext constraintValidatorContext) {
        if (lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST
            || lebenslaufItem.getBildungsart() == LebenslaufAusbildungsArt.EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS) {
            return lebenslaufItem.getBerufsbezeichnung() != null;
        }

        if (lebenslaufItem.getBerufsbezeichnung() == null) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
                VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_BERUFSBEZEICHNUNG_NULL_MESSAGE)
            .addConstraintViolation();
        return false;
    }
}
