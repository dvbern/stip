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

package ch.dvbern.stip.api.gesuchformular.entity;

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
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.api.common.util.BusinessDateConstants.PIA_GEBURTSDATUM_STICHTAG_DAY;
import static ch.dvbern.stip.api.common.util.BusinessDateConstants.PIA_GEBURTSDATUM_STICHTAG_MIN_AGE;
import static ch.dvbern.stip.api.common.util.BusinessDateConstants.PIA_GEBURTSDATUM_STICHTAG_MONTH;

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
            .plusYears(PIA_GEBURTSDATUM_STICHTAG_MIN_AGE)
            .withMonth(PIA_GEBURTSDATUM_STICHTAG_MONTH)
            .withDayOfMonth(PIA_GEBURTSDATUM_STICHTAG_DAY);
        final LocalDate stop = gesuchFormular.getAusbildung().getAusbildungBegin().withDayOfMonth(1);

        // If PIA is younger than 16 no items need to be present
        if (start.equals(stop) || stop.isBefore(start)) {
            return true;
        }

        final ArrayList<DateRange> merged = new ArrayList<>();
        final var dateRanges = gesuchFormular
            .getLebenslaufItems()
            .stream()
            .map(lebenslaufItem -> new DateRange(lebenslaufItem.getVon(), lebenslaufItem.getBis()))
            .sorted(Comparator.comparing(DateRange::getGueltigAb))
            .toList();

        if (dateRanges.isEmpty()) {
            LOG.warn("No Lebenslauf Items present");
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        // Check if first Lebenslaufitem is before birth
        if (gesuchFormular.getPersonInAusbildung().getGeburtsdatum().isAfter(dateRanges.get(0).getGueltigAb())) {
            LOG.warn("Lebenslauf Item start bevor {}", start);
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        final var firstRange = dateRanges.getFirst().getGueltigAb();
        if (firstRange.isAfter(start)) {
            LOG.warn("Lebenslauf Lücke found between {} and {}", firstRange, start);
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        final var lastRange = dateRanges.getLast().getGueltigBis().plusDays(1);
        if (lastRange.isBefore(stop)) {
            LOG.warn("Lebenslauf Lücke found between {} and {}", lastRange, stop);
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }

        merged.add(dateRanges.getFirst());
        dateRanges.forEach(range -> {
            final DateRange lastMerged = merged.getLast();
            if (lastMerged.getGueltigBis().isAfter(range.getGueltigAb().minusDays(2))) {
                lastMerged.setGueltigBis(Collections.max(List.of(range.getGueltigBis(), lastMerged.getGueltigBis())));
            } else {
                merged.add(range);
            }
        });

        if (merged.size() > 1) {
            LOG.warn("Lebenslauf Lücke found, {}", StringUtils.joinWith(", ", merged));
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        return true;
    }
}
