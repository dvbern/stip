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

package ch.dvbern.stip.api.massendruck.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DatenschutzbriefOrVerfuegungSetConstraintValidator
    implements ConstraintValidator<DatenschutzbriefOrVerfuegungSetConstraint, MassendruckJob> {
    @Override
    public boolean isValid(MassendruckJob massendruckJob, ConstraintValidatorContext constraintValidatorContext) {
        if (massendruckJob.getVerfuegungMassendrucks().isEmpty()) {
            return !massendruckJob.getDatenschutzbriefMassendrucks().isEmpty();
        }

        // This could be simplified to just "true" but IMO that makes it harder to grok
        if (massendruckJob.getDatenschutzbriefMassendrucks().isEmpty()) {
            return !massendruckJob.getVerfuegungMassendrucks().isEmpty();
        }

        // There must be at least 1 entry
        return false;
    }
}
