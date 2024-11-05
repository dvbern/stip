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

package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LebenslaufLuckenlosConstraintValidator
    implements ConstraintValidator<LebenslaufLuckenlosConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(LebenslaufLuckenlosConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getPersonInAusbildung() == null || gesuchFormular.getAusbildung() == null) {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }

        final LocalDate start = gesuchFormular.getPersonInAusbildung()
            .getGeburtsdatum()
            .plusYears(16)
            .withMonth(8)
            .withDayOfMonth(1);
        final LocalDate stop = gesuchFormular.getAusbildung().getAusbildungBegin().withDayOfMonth(1);

        // If PIA is younger than 16 no items need to be present
        if (stop.isBefore(start)) {
            return true;
        }

        List<DateRange> dateRanges = new ArrayList<>();
        gesuchFormular.getLebenslaufItems()
            .forEach(
                lebenslaufItem -> dateRanges.add(
                    new DateRange(lebenslaufItem.getVon(), lebenslaufItem.getBis())
                )
            );
        // Sort the Lebenslaufsitem Daterange
        Collections.sort(dateRanges, Comparator.comparing(DateRange::getGueltigAb));

        if (dateRanges.isEmpty()) {
            LOG.warn("No Lebenslauf Items present");
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        // Check if first Lebenslaufitem is After birth
        if (gesuchFormular.getPersonInAusbildung().getGeburtsdatum().isAfter(dateRanges.get(0).getGueltigAb())) {
            LOG.warn("Lebenslauf Item start bevor " + start);
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }

        LocalDate currentDate = start;
        for (DateRange range : dateRanges) {
            if (range.getGueltigAb().isAfter(currentDate)) {
                LOG.warn("Lebenslauf Lücke found between " + currentDate + " and " + range.getGueltigAb());
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
            if (range.getGueltigBis().isAfter(stop)) {
                LOG.warn("Lebenslauf Item end after " + currentDate);
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
            currentDate = range.getGueltigBis().plusDays(1);
            if (currentDate.isAfter(stop)) {
                break;
            }
        }

        if (currentDate.isBefore(stop)) {
            LOG.warn("Lebenslauf Lücke found between " + currentDate + " and " + stop);
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }

        return true;
    }
}
