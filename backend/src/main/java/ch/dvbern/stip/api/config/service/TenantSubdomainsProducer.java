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

package ch.dvbern.stip.api.config.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.common.exception.AppFailureMessage;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantSubdomainsProducer {
    private static final String SUBDOMAINS_FORMAT = "kstip.tenancy.%s.subdomains";

    @Produces
    public List<PerTenantSubdomains> getAllSubdomains() {
        final var allSubdomains = new ArrayList<PerTenantSubdomains>();

        for (final var tenant : MandantIdentifier.values()) {
            final var value = getSubdomainsForTenant(tenant.getIdentifier());
            allSubdomains.add(new PerTenantSubdomains(tenant.getIdentifier(), new HashSet<>(value)));
        }

        return allSubdomains;
    }

    private List<String> getSubdomainsForTenant(final String tenant) {
        final var config = ConfigProvider.getConfig();
        final var path = String.format(SUBDOMAINS_FORMAT, tenant);

        return config.getOptionalValues(path, String.class)
            .orElseThrow(() -> AppFailureMessage.missingTenantConfig(path, tenant).create());
    }

    public record PerTenantSubdomains(String tenant, Set<String> subdomains) {
    }
}
