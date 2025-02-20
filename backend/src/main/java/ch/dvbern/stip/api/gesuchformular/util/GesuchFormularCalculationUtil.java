/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuchformular.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
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
                    .getTechnischesJahr()
                - 1;
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

    public boolean isPersonInAusbildungVolljaehrig(final GesuchFormular gesuchFormular) {
        if (
            gesuchFormular.getPersonInAusbildung() == null
            || gesuchFormular.getPersonInAusbildung().getGeburtsdatum() == null
        ) {
            return false;
        }

        return isDateOfBirthGreaterThanOrEquals18(gesuchFormular.getPersonInAusbildung().getGeburtsdatum());
    }

    public boolean isPersonInAusbildungVolljaehrig(final GesuchFormularUpdateDto gesuchFormular) {
        if (
            gesuchFormular.getPersonInAusbildung() == null
            || gesuchFormular.getPersonInAusbildung().getGeburtsdatum() == null
        ) {
            return false;
        }

        return isDateOfBirthGreaterThanOrEquals18(gesuchFormular.getPersonInAusbildung().getGeburtsdatum());
    }

    public boolean isDateOfBirthGreaterThanOrEquals18(final LocalDate birthdate) {
        if (birthdate == null) {
            return false;
        }
        return Math.abs(
            calculateNumberOfYearsBetween(
                LocalDate.now(),
                birthdate
            )
        ) >= 18;
    }
}
