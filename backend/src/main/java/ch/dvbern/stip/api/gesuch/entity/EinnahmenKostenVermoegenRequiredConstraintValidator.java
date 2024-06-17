package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchFormularCalculationUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenVermoegenRequiredConstraintValidator implements ConstraintValidator<EinnahmenKostenVermoegenRequiredConstraint, GesuchFormular> {
    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }
        if(GesuchFormularCalculationUtil.wasGSOlderThan18(gesuchFormular)){
            return gesuchFormular.getEinnahmenKosten().getVermoegen() != null;
        }
        return gesuchFormular.getEinnahmenKosten().getVermoegen() == null;
    }
}
