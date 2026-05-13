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

package ch.dvbern.stip.integration.steuerdaten.adapter.nesko.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.config.type.TenantAdapterConfig;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.StipendienAuskunftPort;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.StipendienAuskunftService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.MessageContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class StipendienAuskunftPortFactory {

    @Inject
    @RestClient
    NeskoGetBearerTokenRequestService neskoGetBearerTokenRequestService;

    public StipendienAuskunftPort create(TenantAdapterConfig.SteuerdatenAdapter config) {
        StipendienAuskunftService stipendienAuskunftService =
            new StipendienAuskunftService(toUrl(config.url().orElseThrow()));

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("authorization", Collections.singletonList("Bearer " + getToken(config)));
        var port = stipendienAuskunftService.getStipendienAuskunft();
        ((BindingProvider) port).getRequestContext()
            .put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        return port;
    }

    public String getToken(TenantAdapterConfig.SteuerdatenAdapter config) {
        return neskoGetBearerTokenRequestService.post(
            neskoGetBearerTokenRequestService
                .getAuthorization(config.username().orElseThrow(), config.password().orElseThrow()),
            neskoGetBearerTokenRequestService.getGrantType()
        ).getAccessToken();
    }

    private static URL toUrl(String url) {
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
