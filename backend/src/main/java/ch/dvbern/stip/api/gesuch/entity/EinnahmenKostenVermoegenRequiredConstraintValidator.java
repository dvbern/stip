package ch.dvbern.stip.api.gesuch.entity;

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
            Integer vorjahrGesuchsjahr = gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() -1 ;
            LocalDate geburtsDatumGS = gesuchFormular.getPersonInAusbildung().getGeburtsdatum();
            Date geburtsDatumGSAsDate = Date.from(geburtsDatumGS.atStartOfDay(ZoneId.systemDefault()).toInstant());
            LocalDate vorjahrGesuchsjahrDatum = LocalDate.of(vorjahrGesuchsjahr,12,31);
            Date vorjahrGesuchsjahrDatumAsDate = Date.from(vorjahrGesuchsjahrDatum.atStartOfDay(ZoneId.systemDefault()).toInstant());
            long numberOfYearsBetween = calculateNumberOfYearsBetween(vorjahrGesuchsjahrDatumAsDate, geburtsDatumGSAsDate);
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

    private int calculateNumberOfYearsBetween(Date date1, Date date2) {
        return Math.abs(Years.yearsBetween(Instant.ofEpochMilli(date1.getTime()),Instant.ofEpochMilli(date2.getTime())).getYears());
    }
}
