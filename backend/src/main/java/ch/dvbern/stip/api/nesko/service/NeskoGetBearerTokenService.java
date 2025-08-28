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

package ch.dvbern.stip.api.nesko.service;

import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequestScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class NeskoGetBearerTokenService {
    @ConfigProperty(name = "kstip.nesko.username")
    String username;

    @ConfigProperty(name = "kstip.nesko.password")
    String password;

    @Inject
    @RestClient
    NeskoGetBearerTokenRequestService neskoGetBearerTokenRequestService;

    public String getToken() {
        return neskoGetBearerTokenRequestService.post(
            neskoGetBearerTokenRequestService.getAuthorization(username, password),
            neskoGetBearerTokenRequestService.getGrantType()
        ).getAccess_token();
    }
}
