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

import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoOverlapInAusbildungenConstraintValidator
    implements ConstraintValidator<NoOverlapInAusbildungenConstraint, GesuchFormular> {
    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getAusbildung() == null) {
            return true;
        }

        final var ausbildung = gesuchFormular.getAusbildung();
        if (ausbildung.getAusbildungBegin() == null || ausbildung.getAusbildungEnd() == null) {
            return true;
        }

        if (gesuchFormular.getLebenslaufItems() == null) {
            return true;
        }

        return !hasOverlap(ausbildung, gesuchFormular.getLebenslaufItems().stream());
    }

    public boolean hasOverlap(final Ausbildung ausbildung, final Stream<LebenslaufItem> lebenslaufItems) {
        final var ausbildungStart = ausbildung.getAusbildungBegin();
        final var ausbildungEnd = ausbildung.getAusbildungEnd();
        return lebenslaufItems.filter(x -> x.getBildungsart() != null).anyMatch(item -> {
            if (
                (ausbildungStart.isBefore(item.getBis()) || ausbildungStart.isEqual(item.getBis())) &&
                (ausbildungEnd.isAfter(item.getVon()) || ausbildungEnd.isEqual(item.getVon()))
            ) {
                return true;
            }

            return false;
        });
    }
}
