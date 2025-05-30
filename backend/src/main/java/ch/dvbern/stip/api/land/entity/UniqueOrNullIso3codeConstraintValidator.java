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

package ch.dvbern.stip.api.land.entity;

import ch.dvbern.stip.api.land.service.LandService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueOrNullIso3codeConstraintValidator
    implements ConstraintValidator<UniqueOrNullIso3codeConstraint, Land> {

    @Inject
    LandService landService;

    @Override
    public boolean isValid(Land land, ConstraintValidatorContext context) {
        if (land.getIso3code() == null) {
            return true;
        }

        // TODO KSTIP-1968: For some reason this throws a StackOverflow on startup?
        // final var duplicate = landService.getByIso3code(land.getIso3code());
        // if (duplicate.isPresent() && !duplicate.get().getId().equals(land.getId())) {
        // return false;
        // }

        return true;
    }
}
