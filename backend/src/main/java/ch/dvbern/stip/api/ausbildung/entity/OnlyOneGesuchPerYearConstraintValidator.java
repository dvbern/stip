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

package ch.dvbern.stip.api.ausbildung.entity;

import java.util.ArrayList;
import java.util.Comparator;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OnlyOneGesuchPerYearConstraintValidator
    implements ConstraintValidator<OnlyOneGesuchPerYearConstraint, Ausbildung> {
    @Override
    public boolean isValid(Ausbildung ausbildung, ConstraintValidatorContext context) {
        final var gesuche = ausbildung.getGesuchs();
        if (gesuche.size() <= 1) {
            return true;
        }
        var dateRanges = new ArrayList<DateRange>();
        for (var gesuch : gesuche) {
            dateRanges.add(getDateRangeOfGesuch(gesuch));
        }
        dateRanges.sort(Comparator.comparing(DateRange::getGueltigAb));
        for (var i = 0; i < dateRanges.size() - 1; i++) {
            if (dateRanges.get(i + 1).getGueltigAb().isBefore(dateRanges.get(i).getGueltigBis())) {
                return false;
            }
        }
        return false;
    }

    private static DateRange getDateRangeOfGesuch(Gesuch gesuch) {
        final var gesuchTranchen = gesuch.getGesuchTranchen();
        var dateRange = new DateRange()
            .setGueltigAb(gesuchTranchen.get(0).getGueltigkeit().getGueltigAb())
            .setGueltigBis(gesuchTranchen.get(0).getGueltigkeit().getGueltigBis());

        for (final var tranche : gesuchTranchen) {
            var gueltigkeit = tranche.getGueltigkeit();
            if (gueltigkeit.getGueltigAb().isBefore(dateRange.getGueltigAb())) {
                dateRange.setGueltigAb(gueltigkeit.getGueltigAb());
            }
            if (gueltigkeit.getGueltigBis().isAfter(dateRange.getGueltigBis())) {
                dateRange.setGueltigBis(gueltigkeit.getGueltigBis());
            }
        }
        return dateRange;
    }
}
