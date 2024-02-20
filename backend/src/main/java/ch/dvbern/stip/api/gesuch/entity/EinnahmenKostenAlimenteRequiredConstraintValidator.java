package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenAlimenteRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenAlimenteRequiredConstraint, GesuchFormular> {
    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getFamiliensituation() == null || gesuchFormular.getEinnahmenKosten() == null) {
            return true;
        }
        if (gesuchFormular.getFamiliensituation().getGerichtlicheAlimentenregelung()) {
            return gesuchFormular.getEinnahmenKosten().getAlimente() != null;
        }
        return true;
    }

}
