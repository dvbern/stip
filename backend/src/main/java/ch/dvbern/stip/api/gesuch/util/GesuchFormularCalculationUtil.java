package ch.dvbern.stip.api.gesuch.util;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import lombok.experimental.UtilityClass;
import org.joda.time.Instant;
import org.joda.time.Years;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
@UtilityClass
public class GesuchFormularCalculationUtil {
    public int calculateNumberOfYearsBetween(Date date1, Date date2) {
        return Math.abs(Years.yearsBetween(Instant.ofEpochMilli(date1.getTime()),Instant.ofEpochMilli(date2.getTime())).getYears());
    }
    public int calculateAgeAtLocalDate(GesuchFormular gesuchFormular, LocalDate referenceDate){
        LocalDate geburtsDatumGS = gesuchFormular.getPersonInAusbildung().getGeburtsdatum();
        Date geburtsDatumGSAsDate = Date.from(geburtsDatumGS.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date vorjahrGesuchsjahrDatumAsDate = Date.from(referenceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return calculateNumberOfYearsBetween(vorjahrGesuchsjahrDatumAsDate, geburtsDatumGSAsDate);
    }

    public LocalDate getVorjahrGesuchsjahrAsLocalDate(GesuchFormular gesuchFormular){
        Integer vorjahrGesuchsjahr = gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() -1 ;
        LocalDate vorjahrGesuchsjahrDatum = LocalDate.of(vorjahrGesuchsjahr,12,31);
        return vorjahrGesuchsjahrDatum;
    }

    public boolean wasGSOlderThan18(GesuchFormular gesuchFormular){
        return calculateAgeAtLocalDate(gesuchFormular,getVorjahrGesuchsjahrAsLocalDate(gesuchFormular)) >= 18;
    }
}
