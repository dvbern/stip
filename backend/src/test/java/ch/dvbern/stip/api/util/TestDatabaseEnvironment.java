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

package ch.dvbern.stip.api.util;

import java.util.HashMap;
import java.util.Map;

import io.quarkus.logging.Log;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestDatabaseEnvironment implements QuarkusTestResourceLifecycleManager {

    private final JdbcDatabaseContainer postgres =
        new PostgreSQLContainer<>(
            DockerImageName.parse("docker-registry.dvbern.ch/dockerhub/library/postgres:15.3")
                .asCompatibleSubstituteFor("postgres")
        );

    @Override
    public Map<String, String> start() {
        String dbName = "stip";
        String dbUser = "stip";
        String dbPassword = "stip";

        postgres
            .withDatabaseName(dbName)
            .withUsername(dbUser)
            .withPassword(dbPassword)
            .withEnv("TZ", "Europe/Zurich")
            .start();

        Map<String, String> systemProps = new HashMap<>();
        systemProps.put("quarkus.datasource.jdbc.url", postgres.getJdbcUrl());
        systemProps.put("quarkus.datasource.username", dbUser);
        systemProps.put("quarkus.datasource.password", dbPassword);
        Log.info(
            "TestDatabaseEnvironment start \n"
            + " - url: '" + postgres.getJdbcUrl() + "'\n"
            + " - user: '" + dbUser + "'\n"
            + " - password: '" + dbPassword + "'"
        );

        return systemProps;
    }

    @Override
    public void stop() {
        postgres.stop();
    }
}
