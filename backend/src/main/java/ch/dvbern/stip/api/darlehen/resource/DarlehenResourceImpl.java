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

package ch.dvbern.stip.api.darlehen.resource;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.DarlehenAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.darlehen.service.DarlehenService;
import ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType;
import ch.dvbern.stip.api.darlehen.type.GetDarlehenSbQueryType;
import ch.dvbern.stip.api.darlehen.type.SbDarlehenDashboardColumn;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.api.DarlehenResource;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.NullableDarlehenDokumentDto;
import ch.dvbern.stip.generated.dto.PaginatedSbDarlehenDashboardDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.OidcPermissions.FREIGABESTELLE_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.JURIST_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class DarlehenResourceImpl implements DarlehenResource {

    private final DarlehenService darlehenService;
    private final DokumentDownloadService dokumentDownloadService;
    private final BenutzerService benutzerService;
    private final ConfigService configService;
    private final JWTParser jwtPar;
    private final DarlehenAuthorizer darlehenAuthorizer;

    @Override
    @RolesAllowed(GS_GESUCH_CREATE)
    public DarlehenDto getDarlehenGs(UUID fallId) {
        darlehenAuthorizer.canGetDarlehenGs(fallId);
        return darlehenService.getDarlehenGs(fallId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public DarlehenDto getDarlehenSb(UUID darlehenId) {
        darlehenAuthorizer.canGetDarlehenSb();
        return darlehenService.getDarlehenSb(darlehenId);
    }

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> getDarlehensVerfuegungDokument(String token, UUID darlehenId) {
        dokumentDownloadService.getClaimId(
            jwtPar,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.DOKUMENT_ID_CLAIM
        );
        return darlehenService.getDarlehensVerfuegungDokument(darlehenId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public PaginatedSbDarlehenDashboardDto getDarlehenDashboardSb(
        GetDarlehenSbQueryType getDarlehenSbQueryType,
        Integer page,
        Integer pageSize,
        String fallNummer,
        String piaNachname,
        String piaVorname,
        LocalDate piaGeburtsdatum,
        String status,
        String bearbeiter,
        LocalDate letzteAktivitaetFrom,
        LocalDate letzteAktivitaetTo,
        SbDarlehenDashboardColumn sortColumn,
        SortOrder sortOrder
    ) {
        darlehenAuthorizer.canGetDarlehenDashboardSb();
        return darlehenService.getDarlehenDashboardSb(
            getDarlehenSbQueryType,
            page,
            pageSize,
            fallNummer,
            piaNachname,
            piaVorname,
            piaGeburtsdatum,
            status,
            bearbeiter,
            letzteAktivitaetFrom,
            letzteAktivitaetTo,
            sortColumn,
            sortOrder
        );
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public DarlehenDto createDarlehen(UUID fallId) {
        darlehenAuthorizer.canCreateDarlehen(fallId);
        return darlehenService.createDarlehen(fallId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE, FREIGABESTELLE_GESUCH_UPDATE })
    public DarlehenDto darlehenAblehen(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenAblehenen(darlehenId);
        return darlehenService.darlehenAblehnen(darlehenId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE, FREIGABESTELLE_GESUCH_UPDATE })
    public DarlehenDto darlehenAkzeptieren(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenAkzeptieren(darlehenId);
        return darlehenService.darlehenAkzeptieren(darlehenId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public DarlehenDto darlehenEingeben(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenEingeben(darlehenId);
        return darlehenService.darlehenEingeben(darlehenId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public DarlehenDto darlehenFreigeben(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenFreigeben(darlehenId);
        return darlehenService.darlehenFreigeben(darlehenId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public DarlehenDto darlehenZurueckweisen(UUID darlehenId, KommentarDto kommentar) {
        darlehenAuthorizer.canDarlehenZurueckweisen(darlehenId);
        return darlehenService.darlehenZurueckweisen(darlehenId, kommentar);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public DarlehenDto darlehenUpdateGs(UUID darlehenId, DarlehenUpdateGsDto darlehenUpdateGsDto) {
        darlehenAuthorizer.canDarlehenUpdateGs(darlehenId);
        return darlehenService.darlehenUpdateGs(darlehenId, darlehenUpdateGsDto);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public DarlehenDto darlehenUpdateSb(UUID darlehenId, DarlehenUpdateSbDto darlehenUpdateSbDto) {
        darlehenAuthorizer.canDarlehenUpdateSb(darlehenId);
        return darlehenService.darlehenUpdateSb(darlehenId, darlehenUpdateSbDto);
    }

    @Blocking
    @Override
    @RolesAllowed(GS_GESUCH_CREATE)
    public Uni<Response> createDarlehenDokument(
        UUID darlehenId,
        DarlehenDokumentType dokumentTyp,
        FileUpload fileUpload
    ) {
        darlehenAuthorizer.canCreateDarlehenDokument(darlehenId);
        return darlehenService.uploadDarlehenDokument(darlehenId, dokumentTyp, fileUpload);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ, JURIST_GESUCH_READ })
    public NullableDarlehenDokumentDto getDarlehenDokument(UUID darlehenId, DarlehenDokumentType dokumentTyp) {
        darlehenAuthorizer.canGetDarlehenDokument();
        return darlehenService.getDarlehenDokument(darlehenId, dokumentTyp);
    }

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> downloadDarlehenDokument(String token) {
        final var dokumentId = dokumentDownloadService.getClaimId(
            jwtPar,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.DARLEHEN_ID_CLAIM
        );
        return darlehenService.getDokument(dokumentId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public List<DarlehenDto> getAllDarlehenSb(UUID gesuchId) {
        darlehenAuthorizer.canGetDarlehenSb();
        return darlehenService.getDarlehenAllSb(gesuchId);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ, JURIST_GESUCH_READ })
    public FileDownloadTokenDto getDarlehenDownloadToken(UUID dokumentId) {
        darlehenAuthorizer.canGetDarlehenDokument();

        return dokumentDownloadService.getFileDownloadToken(
            dokumentId,
            DokumentDownloadConstants.DARLEHEN_ID_CLAIM,
            benutzerService,
            configService
        );
    }

    @Blocking
    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public void deleteDarlehenDokument(UUID dokumentId) {
        darlehenAuthorizer.canDeleteDarlehenDokument(dokumentId);
        darlehenService.removeDokument(dokumentId);
    }
}
