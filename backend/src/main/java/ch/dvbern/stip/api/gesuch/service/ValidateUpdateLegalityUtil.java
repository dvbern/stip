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

package ch.dvbern.stip.api.gesuch.service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import ch.dvbern.stip.api.common.util.OidcConstants;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

@UtilityClass
public class ValidateUpdateLegalityUtil {
    public <T> T getAndValidateLegalityValue(
        final Set<String> benutzerRollenIdentifiers,
        final T dtoValue,
        final T existingValue,
        final T defaultValue
    ) {
        if (
            !CollectionUtils.containsAny(
                benutzerRollenIdentifiers,
                Arrays.asList(
                    OidcConstants.ROLE_SACHBEARBEITER,
                    OidcConstants.ROLE_ADMIN
                )
            )
        ) {
            return Objects.requireNonNullElse(
                existingValue,
                defaultValue
            );
        }

        return Objects.requireNonNullElse(
            dtoValue,
            defaultValue
        );
    }

    public <T> T getAndValidateLegalityNullableValue(
        final Set<String> benutzerRollenIdentifiers,
        final T dtoValue,
        final T existingValue,
        final T defaultValue
    ) {
        if (
            !CollectionUtils.containsAny(
                benutzerRollenIdentifiers,
                Arrays.asList(
                    OidcConstants.ROLE_SACHBEARBEITER,
                    OidcConstants.ROLE_ADMIN
                )
            )
        ) {
            return Optional.ofNullable(existingValue).orElse(defaultValue);
        }

        return Optional.ofNullable(dtoValue).orElse(defaultValue);
    }
}
