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
import java.util.Optional;

import ch.dvbern.stip.api.config.type.AdapterConfig;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.BusinessFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdaten;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.InfrastructureFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.InvalidArgumentsFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.PermissionDeniedFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.StipendienAuskunftPort;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.StipendienAuskunftService;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.type.NeskoSteuerdatenError;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenPortData;
import ch.dvbern.stip.integration.steuerdaten.domain.port.SteuerdatenPort;
import ch.dvbern.stip.integration.steuerdaten.domain.qualifier.SteuerdatenAdapterQualifier;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.service.SteuerdatenAccessService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@RequestScoped
@RequiredArgsConstructor
@SteuerdatenAdapterQualifier(SteuerdatenAdapterType.NESKO)
public class NeskoSteuerdatenAdapter implements SteuerdatenPort {

    @Inject
    @RestClient
    NeskoGetBearerTokenRequestService neskoGetBearerTokenRequestService;
    private final TenantService tenantService;
    private final SteuerdatenAccessService steuerdatenAccessService;

    @Override
    public SteuerdatenPortData getSteuerdaten(String svn, Integer jahr, SteuerdatenTyp steuerdatenTyp, String fallNr, String gesuchNr) {
        var request = new GetSteuerdaten();
        request.setSteuerjahr(jahr);
        request.setSozialversicherungsnummer(Long.valueOf(svn.replace(".", "")));
        final var port = getStipendienAuskunftPort();
        final Optional<GetSteuerdatenResponse> response;

        try {
            steuerdatenAccessService.logAccess(SteuerdatenAdapterType.NESKO, fallNr, gesuchNr, svn);
            response = Optional.ofNullable(port.getSteuerdaten(request));
        } catch (
        SOAPFaultException | InvalidArgumentsFault | PermissionDeniedFault | InfrastructureFault | BusinessFault e
        ) {
            NeskoSteuerdatenError.handleException(e);
            throw new InternalServerErrorException(e);
        }

        return response
            .map(
                getSteuerdatenResponse -> NeskoSteuerdatenMapper
                    .toSteuerdatenPortData(getSteuerdatenResponse, steuerdatenTyp)
            )
            .orElse(null);
    }

    public StipendienAuskunftPort getStipendienAuskunftPort() {
        final var config =
            tenantService.getConfigForCurrentTenant().adapter().steuerdaten().get(SteuerdatenAdapterType.NESKO);

        StipendienAuskunftService stipendienAuskunftService =
            new StipendienAuskunftService(toUrl(config.url().orElseThrow()));

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("authorization", Collections.singletonList("Bearer " + getToken(config)));
        var port = stipendienAuskunftService.getStipendienAuskunft();
        ((BindingProvider) port).getRequestContext()
            .put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        return port;
    }

    public String getToken(AdapterConfig.SteuerdatenAdapter config) {
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
