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

package ch.dvbern.stip.api.config.resource;

import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.generated.api.ConfigurationResource;
import ch.dvbern.stip.generated.dto.DeploymentConfigDto;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class ConfigResourceImpl implements ConfigurationResource {
    private final ConfigService configService;

    @Override
    @PermitAll
    public DeploymentConfigDto getDeploymentConfig() {
        return configService.getDeploymentConfiguration();
    }
}
