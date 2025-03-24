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

import java.util.Comparator;

import ch.dvbern.stip.api.common.scheduledtask.RunForTenant;
import ch.dvbern.stip.api.common.type.MandantIdentifier;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
@UnlessBuildProfile("test")
public class SeedingExecutor {
    private final Instance<Seeder> seeders;

    @Startup
    @RunForTenant(MandantIdentifier.BERN)
    public void seedForBern() {
        LOG.info("SeedingExecutor starting execution for Bern");
        seeders.stream().sorted(Comparator.comparing(Seeder::getPriority).reversed()).forEach(Seeder::seed);
        LOG.info("SeedingExecutor finished execution for Bern");
    }

    @Startup
    @RunForTenant(MandantIdentifier.DV)
    public void seedForDv() {
        LOG.info("SeedingExecutor starting execution for DV");
        seeders.stream().sorted(Comparator.comparing(Seeder::getPriority).reversed()).forEach(Seeder::seed);
        LOG.info("SeedingExecutor finished execution for DV");
    }
}
