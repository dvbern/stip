package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenWohnkostenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenWohnkostenRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(EinnahmenKostenWohnkostenRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext context) {
        if (gesuchFormular.getPersonInAusbildung() == null || gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }

        if (gesuchFormular.getPersonInAusbildung().getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT &&
            gesuchFormular.getEinnahmenKosten().getWohnkosten() == null) {
            return GesuchValidatorUtil.addProperty(context, property);
        } else {
            return true;
        }
    }
}
