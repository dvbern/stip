package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator
implements ConstraintValidator<EinnahmenKostenSteuerjahrInPastOrCurrentConstraint, GesuchFormular> {
    private static final Integer CURRENT_YEAR = Year.now().getValue();

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if(gesuchFormular.getEinnahmenKosten() == null || gesuchFormular.getEinnahmenKosten().getSteuerjahr() == null){
            return true;
        }
        Integer steuerjahr = gesuchFormular.getEinnahmenKosten().getSteuerjahr();
        return steuerjahr <= CURRENT_YEAR;
    }
}
