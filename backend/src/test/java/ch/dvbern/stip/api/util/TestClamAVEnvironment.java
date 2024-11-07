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

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestClamAVEnvironment implements QuarkusTestResourceLifecycleManager {
    private final DockerImageName clamAvImageName =
        DockerImageName.parse("docker-registry.dvbern.ch/dockerhub/clamav/clamav");
    private final GenericContainer<?> clamAv = new GenericContainer<>(clamAvImageName);

    @Override
    public Map<String, String> start() {
        clamAv.withExposedPorts(3310).start();

        Map<String, String> systemProps = new HashMap<>();
        systemProps.put("quarkus.antivirus.clamav.host", clamAv.getHost());
        systemProps.put("quarkus.antivirus.clamav.port", String.valueOf(clamAv.getMappedPort(3310)));
        systemProps.put("quarkus.antivirus.clamav.enabled", "true");
        systemProps.put("quarkus.antivirus.clamav.health.enabled", "true");
        return systemProps;
    }

    @Override
    public void stop() {
        clamAv.stop();
    }
}
