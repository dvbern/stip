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

package ch.dvbern.stip.api.config.service;

import java.util.List;

import ch.dvbern.stip.generated.dto.DeploymentConfigDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ConfigService {

    @ConfigProperty(name = "kstip.environment", defaultValue = "local")
    String environment;

    @ConfigProperty(name = "kstip.version")
    String version;

    @ConfigProperty(name = "bucket.name")
    String bucketName;

    @ConfigProperty(name = "kstip.allowed.mimetypes")
    List<String> allowedMimeTypes;

    public DeploymentConfigDto getDeploymentConfiguration() {
        return new DeploymentConfigDto().version(version).environment(environment).allowedMimeTypes(allowedMimeTypes);
    }

    public String getBucketName() {
        return bucketName;
    }

    public List<String> getAllowedMimeTypes() {
        return allowedMimeTypes;
    }
}
