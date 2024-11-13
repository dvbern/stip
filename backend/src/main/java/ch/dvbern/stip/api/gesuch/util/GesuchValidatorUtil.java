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

package ch.dvbern.stip.api.gesuch.util;

import java.util.Collection;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.validation.ConstraintValidatorContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GesuchValidatorUtil {
    public boolean addProperty(final @Nullable ConstraintValidatorContext context, @Nonnull final String property) {
        if (context != null) {
            return addProperty(context, context.getDefaultConstraintMessageTemplate(), property);
        }
        return false;
    }

    public boolean addProperty(
        final @Nullable ConstraintValidatorContext context,
        final @Nonnull String template,
        final @Nonnull String property
    ) {
        if (context == null || property.trim().isEmpty() || template.trim().isEmpty()) {
            return false;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(template)
            .addPropertyNode(property)
            .addConstraintViolation();

        return false;
    }

    public boolean addProperties(
        final @Nullable ConstraintValidatorContext context,
        final @Nonnull String template,
        final @Nonnull Collection<String> properties
    ) {
        for (final var property : properties) {
            addProperty(context, template, property);
        }

        return false;
    }
}
