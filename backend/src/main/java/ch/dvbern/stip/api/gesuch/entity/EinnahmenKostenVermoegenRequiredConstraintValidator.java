package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchFormularCalculationUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.joda.time.Instant;
import org.joda.time.Years;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EinnahmenKostenVermoegenRequiredConstraintValidator implements ConstraintValidator<EinnahmenKostenVermoegenRequiredConstraint, GesuchFormular> {
    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        try{
            if(GesuchFormularCalculationUtil.getVorjahrGesuchsjahrAsLocalDate(gesuchFormular) != null){

            }
            LocalDate vorjahrGesuchsjahrDatum = GesuchFormularCalculationUtil.getVorjahrGesuchsjahrAsLocalDate(gesuchFormular);
            int numberOfYearsBetween = GesuchFormularCalculationUtil.calculateAgeAtLocalDate(gesuchFormular,vorjahrGesuchsjahrDatum);
            if(numberOfYearsBetween >= 18){
                return gesuchFormular.getEinnahmenKosten().getVermoegen() != null;
            }
            else {
                return gesuchFormular.getEinnahmenKosten().getVermoegen() == null;
            }
        }
        catch(NullPointerException ex){
            return false;
        }
    }
}
