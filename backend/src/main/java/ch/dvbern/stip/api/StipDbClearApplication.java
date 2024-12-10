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

package ch.dvbern.stip.api;

import ch.dvbern.stip.api.config.service.ConfigService;
import io.quarkus.liquibase.LiquibaseFactory;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class StipDbClearApplication implements QuarkusApplication {
    private final ConfigService configService;
    private final LiquibaseFactory liquibaseFactory;

    @Override
    public int run(String... args) throws Exception {
        // Sanity check that nothing accidentally calls this
        if (!configService.getShouldClearDatabase()) {
            throw new IllegalStateException("StipDbClearApplication should only be ran if kstip.should-clear-database");
        }

        try (final var liquibase = liquibaseFactory.createLiquibase()) {
            LOG.info("Clearing database...");
            liquibase.dropAll();
            LOG.info("Done clearing database, exiting");
        }

        Quarkus.asyncExit(0);
        return 0;
    }
}
