package ch.dvbern.stip.api.gesuch.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import lombok.experimental.UtilityClass;
import org.joda.time.Instant;
import org.joda.time.Years;
@UtilityClass
public class GesuchFormularCalculationUtil {
    public int calculateNumberOfYearsBetween(final Date date1, final Date date2) {
        return Math.abs(Years.yearsBetween(Instant.ofEpochMilli(date1.getTime()),Instant.ofEpochMilli(date2.getTime())).getYears());
    }
    public int calculateAgeAtLocalDate(final GesuchFormular gesuchFormular, final LocalDate referenceDate) {
        LocalDate geburtsDatumGS = gesuchFormular.getPersonInAusbildung().getGeburtsdatum();
        Date geburtsDatumGSAsDate = Date.from(geburtsDatumGS.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date vorjahrGesuchsjahrDatumAsDate = Date.from(referenceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return calculateNumberOfYearsBetween(vorjahrGesuchsjahrDatumAsDate, geburtsDatumGSAsDate);
    }

    public LocalDate getVorjahrGesuchsjahrAsLocalDate(final GesuchFormular gesuchFormular) {
        int vorjahrGesuchsjahr = LocalDate.now().getYear() - 1;
        if (gesuchFormular.getTranche() != null) {
            vorjahrGesuchsjahr = gesuchFormular.getTranche().getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1;
        }
        return LocalDate.of(vorjahrGesuchsjahr, 12, 31);
    }

    public boolean wasGSOlderThan18(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getPersonInAusbildung() == null) {
            return true;
        }
        return calculateAgeAtLocalDate(gesuchFormular, getVorjahrGesuchsjahrAsLocalDate(gesuchFormular)) >= 18;
    }
}
