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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.BusinessFault;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdaten;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.InfrastructureFault;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.InvalidArgumentsFault;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.PermissionDeniedFault;
import ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice.StipendienAuskunftService;
import ch.dvbern.stip.api.nesko.type.NeskoSteuerdatenError;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class NeskoGetSteuerdatenService {
    @ConfigProperty(name = "kstip.nesko-wsdl-url")
    String wsdlLocation;

    private final NeskoAccessLoggerService neskoAccessLoggerService;

    public GetSteuerdatenResponse getSteuerdatenResponse(
        String token,
        String ssvn,
        Integer steuerjahr,
        final String gesuchNummer,
        final String fallNr
    ) {
        neskoAccessLoggerService.logAccess(gesuchNummer, fallNr, ssvn);
        return placeNeskoGetSteuerdatenRequest(token, steuerjahr, ssvn);
    }

    // WARNING: Do not call directly. Always call neskoAccessLoggerService.logAccess before this method
    GetSteuerdatenResponse placeNeskoGetSteuerdatenRequest(
        final String token,
        final Integer steuerjahr,
        final String ssvn
    ) {
        StipendienAuskunftService stipendienAuskunftService = null;
        try {
            stipendienAuskunftService = new StipendienAuskunftService(new URL(wsdlLocation));
        } catch (MalformedURLException e) {
            throw new InternalServerErrorException(e);
        }

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("authorization", Collections.singletonList("Bearer " + token));
        var port = stipendienAuskunftService.getStipendienAuskunft();
        ((BindingProvider) port).getRequestContext()
            .put(MessageContext.HTTP_REQUEST_HEADERS, headers);

        var request = new GetSteuerdaten();
        request.setSteuerjahr(steuerjahr);
        request.setSozialversicherungsnummer(Long.valueOf(ssvn.replace(".", "")));
        try {
            final var steuerdaten = port.getSteuerdaten(request);
            return steuerdaten;
        } catch (
        SOAPFaultException | InvalidArgumentsFault | PermissionDeniedFault | InfrastructureFault | BusinessFault e
        ) {
            NeskoSteuerdatenError.handleException(e);
            throw new InternalServerErrorException(e);
        }
    }
}
