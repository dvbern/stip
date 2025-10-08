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

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.MassendruckJobAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.massendruck.service.MassendruckJobPdfService;
import ch.dvbern.stip.api.massendruck.service.MassendruckJobService;
import ch.dvbern.stip.api.massendruck.type.GetMassendruckJobQueryType;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp;
import ch.dvbern.stip.generated.api.MassendruckResource;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.MassendruckDatenschutzbriefDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDetailDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDto;
import ch.dvbern.stip.generated.dto.MassendruckVerfuegungDto;
import ch.dvbern.stip.generated.dto.PaginatedMassendruckJobDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestMulti;

import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class MassendruckJobResourceImpl implements MassendruckResource {
    private final MassendruckJobAuthorizer authorizer;
    private final MassendruckJobService massendruckJobService;
    private final MassendruckJobPdfService massendruckJobPdfService;
    private final JWTParser jwtParser;
    private final BenutzerService benutzerService;
    private final ConfigService configService;

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE })
    public MassendruckJobDto createMassendruckJobForQueryType(GetGesucheSBQueryType getGesucheSBQueryType) {
        authorizer.canCreateMassendruckJob(getGesucheSBQueryType);
        final var massendruckJob = massendruckJobService.createMassendruckJobForQueryType(getGesucheSBQueryType);
        massendruckJobService.combineDocument(massendruckJob.getId());
        return massendruckJob;
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ })
    public PaginatedMassendruckJobDto getAllMassendruckJobs(
        GetMassendruckJobQueryType getMassendruckJobs,
        Integer page,
        Integer pageSize,
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
            userErstellt,
            timestampErstellt,
            massendruckJobStatus,
            massendruckJobTyp,
            sortColumn,
            sortOrder
        );
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ })
    public FileDownloadTokenDto getMassendruckDownloadToken(UUID massendruckId) {
        authorizer.permitAll();
        return DokumentDownloadUtil.getFileDownloadToken(
            massendruckId,
            DokumentDownloadConstants.MASSENDRUCK_JOB_ID_CLAIM,
            benutzerService,
            configService
        );
    }

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> downloadMassendruckDocument(String token) {
        final var massendruckJobId = DokumentDownloadUtil.getClaimId(
            jwtParser,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.MASSENDRUCK_JOB_ID_CLAIM
        );

        return massendruckJobPdfService.downloadMassendruckPdf(massendruckJobId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ })
    public MassendruckJobDetailDto getMassendruckJobDetail(UUID massendruckJobId) {
        authorizer.permitAll();
        return massendruckJobService.getMassendruckJobDetail(massendruckJobId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE })
    public MassendruckDatenschutzbriefDto massendruckDatenschutzbriefVersenden(UUID massendruckDatenschutzbriefId) {
        authorizer.canDatenschutzbriefVersenden(massendruckDatenschutzbriefId);
        return massendruckJobService.datenschutzbriefMassendruckVersenden(massendruckDatenschutzbriefId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE })
    public MassendruckVerfuegungDto massendruckVerfuegungVersenden(UUID massendruckVerfuegungId) {
        authorizer.canVerfuegungMassendruckVersenden(massendruckVerfuegungId);
        return massendruckJobService.verfuegungMassendruckVersenden(massendruckVerfuegungId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE })
    public void deleteMassendruckJob(UUID massendruckId) {
        authorizer.canDeleteMassendruckJob(massendruckId);
        massendruckJobService.deleteMassendruckJob(massendruckId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE })
    public MassendruckJobDetailDto retryMassendruckJob(UUID massendruckId) {
        authorizer.canRetryMassendruckJob(massendruckId);
        final var massendruckJobDetail = massendruckJobService.retryMassendruckJob(massendruckId);
        massendruckJobService.combineDocument(massendruckId);
        return massendruckJobDetail;
    }
}
