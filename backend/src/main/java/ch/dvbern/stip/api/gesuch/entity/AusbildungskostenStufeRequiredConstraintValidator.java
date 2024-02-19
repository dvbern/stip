package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.type.Ausbildungsstufe.SEKUNDAR_2;
import static ch.dvbern.stip.api.common.type.Ausbildungsstufe.TERTIAER;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE;

public class AusbildungskostenStufeRequiredConstraintValidator
    implements ConstraintValidator<AusbildungskostenStufeRequiredConstraint, GesuchFormular> {
    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getAusbildung() == null || gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }
        if (gesuchFormular.getAusbildung().getAusbildungsgang().getAusbildungsrichtung().getAusbildungsstufe()
            == SEKUNDAR_2) {
            return gesuchFormular.getEinnahmenKosten().getAusbildungskostenSekundarstufeZwei() != null;
        }
        if (gesuchFormular.getAusbildung().getAusbildungsgang().getAusbildungsrichtung().getAusbildungsstufe()
            == TERTIAER) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE)
                .addConstraintViolation();
            return gesuchFormular.getEinnahmenKosten().getAusbildungskostenTertiaerstufe() != null;
        }
        return true;
    }
}
