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

import java.util.Objects;

import ch.dvbern.stip.api.common.util.DateUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AusbildungUnterbruchAntragGueltigkeitConstraintValidator
    implements ConstraintValidator<AusbildungUnterbruchAntragGueltigkeitConstraint, AusbildungUnterbruchAntrag> {

    @Override
    public boolean isValid(
        AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        final var gesuch = ausbildungUnterbruchAntrag.getGesuch();
        if (Objects.isNull(gesuch)) {
            return true;
        }

        final var gesuchRange = DateUtil.getGesuchDateRange(gesuch);

        return DateUtil
            .beforeOrEqual(gesuchRange.getGueltigAb(), ausbildungUnterbruchAntrag.getGueltigkeit().getGueltigAb())
        && DateUtil
            .afterOrEqual(gesuchRange.getGueltigBis(), ausbildungUnterbruchAntrag.getGueltigkeit().getGueltigBis());
    }
}
