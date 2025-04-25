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

package ch.dvbern.stip.api.delegieren.resource;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.DelegierenAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.delegieren.service.DelegierenService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.api.DelegierenResource;
import ch.dvbern.stip.generated.dto.DelegierungCreateDto;
import ch.dvbern.stip.generated.dto.GetDelegierungSozQueryTypeDto;
import ch.dvbern.stip.generated.dto.PaginatedSozDashboardDto;
import ch.dvbern.stip.generated.dto.SozDashboardColumnDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_UPDATE;

@Validated
@RequestScoped
@RequiredArgsConstructor
public class DelegierenResourceImpl implements DelegierenResource {
    private final DelegierenAuthorizer delegierenAuthorizer;
    private final DelegierenService delegierenService;

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public void fallDelegieren(UUID fallId, UUID sozialdienstId, DelegierungCreateDto delegierungCreateDto) {
        delegierenAuthorizer.canDelegate(fallId);
        delegierenService.delegateFall(fallId, sozialdienstId, delegierungCreateDto);
    }

    @Override
    public PaginatedSozDashboardDto getDelegierungSoz(
        GetDelegierungSozQueryTypeDto getDelegierungSozQueryType,
        @NotNull Integer page,
        @NotNull Integer pageSize,
        String fallNummer,
        String piaNachname,
        String piaVorname,
        LocalDate piaGeburtsdatum,
        String piaWohnort,
        String status,
        LocalDate letzteAktivitaetFrom,
        LocalDate letzteAktivitaetTo,
        SozDashboardColumnDto sortColumn,
        SortOrder sortOrder
    ) {

        // TODO Auto-generated method stub
        return new PaginatedSozDashboardDto();
    }
}
