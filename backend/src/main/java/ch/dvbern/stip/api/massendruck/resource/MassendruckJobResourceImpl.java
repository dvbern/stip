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

package ch.dvbern.stip.api.massendruck.resource;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.MassendruckJobAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.massendruck.service.MassendruckJobService;
import ch.dvbern.stip.api.massendruck.type.GetMassendruckJobQueryType;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp;
import ch.dvbern.stip.generated.api.MassendruckResource;
import ch.dvbern.stip.generated.dto.MassendruckDatenschutzbriefDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDetailDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDto;
import ch.dvbern.stip.generated.dto.MassendruckVerfuegungDto;
import ch.dvbern.stip.generated.dto.PaginatedMassendruckJobDto;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class MassendruckJobResourceImpl implements MassendruckResource {
    private final MassendruckJobAuthorizer authorizer;
    private final MassendruckJobService massendruckJobService;

    @Override
    public MassendruckJobDto createMassendruckJobForQueryType(GetGesucheSBQueryType getGesucheSBQueryType) {
        authorizer.permitAll();
        return null;
    }

    @Override
    @PermitAll
    public PaginatedMassendruckJobDto getAllMassendruckJobs(
        GetMassendruckJobQueryType getMassendruckJobs,
        Integer page,
        Integer pageSize,
        Integer massendruckJobNumber,
        String userErstellt,
        LocalDate timestampErstellt,
        MassendruckJobStatus massendruckJobStatus,
        MassendruckJobTyp massendruckJobTyp,
        MassendruckJobSortColumn sortColumn,
        SortOrder sortOrder
    ) {
        authorizer.permitAll();
        return massendruckJobService.getAllMassendruckJobs(
            getMassendruckJobs,
            page,
            pageSize,
            massendruckJobNumber,
            userErstellt,
            timestampErstellt,
            massendruckJobStatus,
            massendruckJobTyp,
            sortColumn,
            sortOrder
        );
    }

    @Override
    public MassendruckJobDetailDto getMassendruckJobDetail(UUID massendruckJobId) {
        authorizer.permitAll();
        return null;
    }

    @Override
    public MassendruckDatenschutzbriefDto massendruckDatenschutzbriefVersenden(UUID massendruckDatenschutzbriefId) {
        authorizer.permitAll();
        return null;
    }

    @Override
    public MassendruckVerfuegungDto massendruckVerfuegungVersenden(UUID massendruckVerfuegungId) {
        authorizer.permitAll();
        return null;
    }
}
