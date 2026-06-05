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

package ch.dvbern.stip.api.config.validation;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.type.TenantIdentifier;
import ch.dvbern.stip.api.config.type.StipConfig;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantConfigValidator {

    private final StipConfig stipConfig;

    void onStart(@Observes StartupEvent event) {
        final Set<TenantIdentifier> configured = stipConfig.tenant().keySet();
        final Set<TenantIdentifier> missing = EnumSet.allOf(TenantIdentifier.class)
            .stream()
            .filter(t -> !configured.contains(t))
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(TenantIdentifier.class)));

        if (!missing.isEmpty()) {
            final String missingIdentifiers = missing.stream()
                .map(TenantIdentifier::getIdentifier)
                .collect(Collectors.joining(", "));
            throw new IllegalStateException(
                String.format(
                    "%s%s%s",
                    "Missing tenant configuration under 'kstip.tenant' for: ",
                    missingIdentifiers,
                    ". Every TenantIdentifier must have a corresponding configuration entry."
                )
            );
        }
    }
}
