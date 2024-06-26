package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator
implements ConstraintValidator<EinnahmenKostenSteuerjahrInPastOrCurrentConstraint, GesuchFormular> {

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if(gesuchFormular.getEinnahmenKosten() == null || gesuchFormular.getEinnahmenKosten().getSteuerjahr() == null){
            return true;
        }
        return gesuchFormular.getEinnahmenKosten().getSteuerjahr() <= gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() -1;

    }
}
