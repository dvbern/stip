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

import java.util.Base64;

import ch.dvbern.stip.api.nesko.entity.NeskoBearerTokenResponse;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/token")
@RegisterRestClient(configKey = "NeskoGetBearerToken-api")
public interface NeskoGetBearerTokenRequestService {
    default String getAuthorization(final String username, final String password) {
        final String userPassword = String.format("%s:%s", username, password);

        return "Basic " + Base64.getEncoder().encodeToString(userPassword.getBytes());
    }

    default String getGrantType() {
        return "client_credentials";
    }

    @POST
    NeskoBearerTokenResponse post(
        @HeaderParam("Authorization") String authorization,
        @FormParam("grant_type") String grantType
    );
}
