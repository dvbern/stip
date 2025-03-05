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

import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.BusinessFault;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.GetSteuerdaten;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.InfrastructureFault;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.InvalidArgumentsFault;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.PermissionDeniedFault;
import ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice.StipendienAuskunftService;
import ch.dvbern.stip.api.nesko.type.NeskoSteuerdatenError;
import io.quarkus.logging.Log;
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

    public GetSteuerdatenResponse getSteuerdatenResponse(String token, String ssvn, Integer steuerjahr) {
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
            return port.getSteuerdaten(request);
        } catch (
        SOAPFaultException | InvalidArgumentsFault | PermissionDeniedFault | InfrastructureFault | BusinessFault e
        ) {
            NeskoSteuerdatenError.handleException(e);
            Log.error(e.getMessage(), e);
            throw new InternalServerErrorException(e);
        }
    }
}
