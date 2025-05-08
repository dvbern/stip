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

package ch.dvbern.stip.api.common.util;

import java.util.HashSet;
import java.util.List;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import jakarta.validation.Validator;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorUtil {
    public <T> void validate(final Validator validator, final T toValidate, final Class<?> validationGroup) {
        validate(validator, toValidate, List.of(validationGroup));
    }

    public <T> void validate(final Validator validator, final T toValidate) {
        validate(validator, toValidate, List.of());
    }

    public <T> void validate(final Validator validator, final T toValidate, final List<Class<?>> validationGroups) {
        final var concatenatedViolations = new HashSet<>(validator.validate(toValidate));

        if (!validationGroups.isEmpty()) {
            concatenatedViolations.addAll(validator.validate(toValidate, validationGroups.toArray(new Class<?>[0])));
        }

        if (!concatenatedViolations.isEmpty()) {
            throw new ValidationsException(
                String.format(
                    "Validation of class %s with validation groups %s failed",
                    toValidate.getClass(),
                    validationGroups
                ),
                concatenatedViolations
            );
        }
    }

    public <T> void throwIfEntityNotValid(final Validator validator, final T entity) {
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ValidationsException(ValidationsException.ENTITY_NOT_VALID_MESSAGE, violations);
        }
    }
}
