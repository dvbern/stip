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

package ch.dvbern.stip.api.common.service.seeding;

import java.util.List;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.stipdecision.decider.StipDeciderTenant;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StipDecisionTextSeeding extends Seeder {
    private final ConfigService configService;
    private final TenantService tenantService;
    private final StipDecisionTextRepository decisionTextRepository;
    private final Instance<BaseStipDecisionTextProvider> decisionTextProviders;

    @Override
    protected void doSeed() {
        final var decisionTextProvider = getDecisionTextProviderForTenantId(
            getTenant()
        );
        decisionTextRepository.deleteAll();
        LOG.info("Starting stip decision text seeding seeding");
        final var decisionTexts = decisionTextProvider.getDecisionTexts();
        decisionTexts.forEach(decisionTextRepository::persistAndFlush);
        LOG.info("Finished stip decision text seeding seeding");

    }

    @Override
    protected List<String> getProfiles() {
        return configService.getSeedAllProfiles();
    }

    private BaseStipDecisionTextProvider getDecisionTextProviderForTenantId(final String tenantId) {
        final var textProvider = decisionTextProviders.stream()
            .filter(provider -> {
                final var annotation = provider.getClass().getAnnotation(StipDeciderTenant.class);
                return annotation != null
                && annotation.value().name().toLowerCase().equals(tenantId);
            })
            .findFirst();
        if (textProvider.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a StipDecisionTextProvider for tenant " + tenantId
            );
        }
        return textProvider.get();
    }

}
