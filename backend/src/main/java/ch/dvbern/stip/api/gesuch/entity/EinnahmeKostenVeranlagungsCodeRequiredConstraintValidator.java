package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class EinnahmeKostenVeranlagungsCodeRequiredConstraintValidator implements ConstraintValidator<EinnahmenKostenVeranlagungsCodeRequiredConstraint, GesuchFormular> {
    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        Integer veranlagungsCode = gesuchFormular.getEinnahmenKosten().getVeranlagungscode();
        return !(veranlagungsCode == null) && (veranlagungsCode >= 0 && veranlagungsCode <= 99) ;
    }
}
