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

import java.time.LocalDate;

import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AusbildungEndDateMustBeAfterEarliestActiveGesuchsperiodeValidator
    implements ConstraintValidator<AusbildungEndDateMustBeAfterEarliestActiveGesuchsperiodeConstraint, Ausbildung> {

    @Inject
    GesuchsperiodeRepository gesuchsperiodeRepository;

    @Override
    public boolean isValid(Ausbildung value, ConstraintValidatorContext context) {
        if (value == null || value.getAusbildungEnd() == null) {
            return true;
        }

        final var earliestActiveOpt = gesuchsperiodeRepository.findEarliestActive(LocalDate.now());
        if (earliestActiveOpt.isEmpty()) {
            return true;
        }

        final var earliestActiveStart = earliestActiveOpt.get().getGesuchsperiodeStart();
        return !value.getAusbildungEnd().isBefore(earliestActiveStart);
    }
}
