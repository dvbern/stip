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

import java.util.Optional;

import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.BusinessFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdaten;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.GetSteuerdatenResponse;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.InfrastructureFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.InvalidArgumentsFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.PermissionDeniedFault;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.type.NeskoSteuerdatenError;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenPortData;
import ch.dvbern.stip.integration.steuerdaten.domain.port.SteuerdatenPort;
import ch.dvbern.stip.integration.steuerdaten.domain.qualifier.SteuerdatenAdapterQualifier;
import ch.dvbern.stip.integration.steuerdaten.domain.service.SteuerdatenAccessService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor(onConstructor_ = @Inject)
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@SteuerdatenAdapterQualifier(SteuerdatenAdapterType.NESKO)
public class NeskoSteuerdatenAdapter implements SteuerdatenPort {

    private final StipendienAuskunftPortFactory stipendienAuskunftPortFactory;
    private final SteuerdatenAccessService steuerdatenAccessService;
    private final TenantService tenantService;

    @Override
    public SteuerdatenPortData getSteuerdaten(
        String svn,
        Integer jahr,
        SteuerdatenTyp steuerdatenTyp,
        String fallNr,
        String gesuchNr
    ) {
        var request = new GetSteuerdaten();
        request.setSteuerjahr(jahr);
        request.setSozialversicherungsnummer(Long.valueOf(svn.replace(".", "")));
        final var config =
            tenantService.getConfigForCurrentTenant().adapter().steuerdaten().get(SteuerdatenAdapterType.NESKO);
        final var port = stipendienAuskunftPortFactory.create(config);
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
}
