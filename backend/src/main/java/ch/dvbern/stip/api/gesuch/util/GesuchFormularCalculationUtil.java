package ch.dvbern.stip.api.gesuch.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchFormularCalculationUtil {
    public int calculateNumberOfYearsBetween(final LocalDate date1, final LocalDate date2) {
        // Casting is ok, there have been more than 2147483647 years, but we just truncate to that
        return (int) ChronoUnit.YEARS.between(date1, date2);
    }

    public LocalDate getVorjahrGesuchsjahrAsLocalDate(final GesuchFormular gesuchFormular) {
        int vorjahrGesuchsjahr = LocalDate.now().getYear() - 1;
        if (gesuchFormular.getTranche() != null) {
            final var vorjahrGesuchsperiode = gesuchFormular
                .getTranche()
                .getGesuch()
                .getGesuchsperiode();

            if (vorjahrGesuchsperiode != null) {
                vorjahrGesuchsjahr = gesuchFormular
                    .getTranche()
                    .getGesuch()
                    .getGesuchsperiode()
                    .getGesuchsjahr()
                    .getTechnischesJahr() - 1;
            }
        }
        return LocalDate.of(vorjahrGesuchsjahr, 12, 31);
    }

    public boolean wasGSOlderThan18(final GesuchFormular gesuchFormular) {
        if (gesuchFormular.getPersonInAusbildung() == null) {
            return true;
        }

        return calculateNumberOfYearsBetween(
            gesuchFormular.getPersonInAusbildung().getGeburtsdatum(),
            getVorjahrGesuchsjahrAsLocalDate(gesuchFormular)
        ) >= 18;
    }
}
