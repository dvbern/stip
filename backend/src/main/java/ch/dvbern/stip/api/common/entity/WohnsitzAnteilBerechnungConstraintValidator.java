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

package ch.dvbern.stip.api.common.entity;

import java.math.BigDecimal;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WohnsitzAnteilBerechnungConstraintValidator
    implements ConstraintValidator<WohnsitzAnteilBerechnungConstraint, AbstractFamilieEntity> {

    @Override
    public boolean isValid(
        AbstractFamilieEntity abstractFamilieEntity,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        if (
            abstractFamilieEntity.getWohnsitz() == Wohnsitz.MUTTER_VATER
            && abstractFamilieEntity.getWohnsitzAnteilVater() != null
            && abstractFamilieEntity.getWohnsitzAnteilMutter() != null
        ) {
            return abstractFamilieEntity.getWohnsitzAnteilVater()
                .add(abstractFamilieEntity.getWohnsitzAnteilMutter())
                .compareTo(new BigDecimal(100)) == 0;
        }
        return true;
    }
}
