package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.bildungsart.type.Bildungsstufe.SEKUNDAR_2;
import static ch.dvbern.stip.api.bildungsart.type.Bildungsstufe.TERTIAER;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE;

public class AusbildungskostenStufeRequiredConstraintValidator
    implements ConstraintValidator<AusbildungskostenStufeRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(AusbildungskostenStufeRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getAusbildung() == null || gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }
        if (gesuchFormular.getAusbildung().getAusbildungsgang().getBildungsart().getBildungsstufe()
            == SEKUNDAR_2) {
			if (gesuchFormular.getEinnahmenKosten().getAusbildungskostenSekundarstufeZwei() == null) {
				return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
			} else {
				return true;
			}
        }
        if (gesuchFormular.getAusbildung().getAusbildungsgang().getBildungsart().getBildungsstufe()
            == TERTIAER) {
            if (gesuchFormular.getEinnahmenKosten().getAusbildungskostenTertiaerstufe() == null) {
                return GesuchValidatorUtil.addProperty(
                    constraintValidatorContext,
                    VALIDATION_EINNAHMEN_KOSTEN_AUSBILDUNGSKOSTEN_STUFE3_REQUIRED_MESSAGE,
                    property
                );
            } else {
                return true;
            }
        }
        return true;
    }
}
