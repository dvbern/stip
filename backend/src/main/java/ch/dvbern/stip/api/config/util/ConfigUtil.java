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

package ch.dvbern.stip.api.config.util;

import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.config.type.TenantConfig;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigUtil {
    public String getWelcomeMailURI(
        TenantConfig tenantConfig,
        StipConfig config,
        String tenantIdentifier,
        String redirectUri
    ) {
        return config.oidc().frontendUrl() +
        tenantConfig.welcomeMail().kcPath().replace("<TENANT>", tenantIdentifier) +
        tenantConfig.welcomeMail().kcQueryParameter().replace("<REDIRECT_URI>", redirectUri) +
        tenantConfig.welcomeMail().kcScope();
    }
}
