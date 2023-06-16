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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.config.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import ch.dvbern.stip.config.model.DeploymentConfigDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConfigService {

    @Inject
    @ConfigProperty(name = "kstip.environment", defaultValue = "local")
    String environment;
    @Inject
    @ConfigProperty(name = "kstip.version")
    String version;

    @Inject
    @ConfigProperty(name = "kstip.file.path")
    String filePath;

    public DeploymentConfigDTO getDeploymentConfiguration() {
        return new DeploymentConfigDTO(environment, version);
    }

    public String getFilePath() {
        return filePath;
    }
}
