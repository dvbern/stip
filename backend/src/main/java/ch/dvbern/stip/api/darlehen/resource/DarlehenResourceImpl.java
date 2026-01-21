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
import ch.dvbern.stip.api.darlehen.type.GetFreiwilligDarlehenSbQueryType;
import ch.dvbern.stip.api.darlehen.type.SbFreiwilligDarlehenDashboardColumn;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.generated.api.DarlehenResource;
import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungEntryDto;
import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungOverviewDto;
import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungSaldokorrekturDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.FreiwilligDarlehenUpdateSbDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.NullableDarlehenDokumentDto;
import ch.dvbern.stip.generated.dto.PaginatedSbFreiwilligDarlehenDashboardDto;
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

import static ch.dvbern.stip.api.common.util.OidcPermissions.DARLEHEN_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DARLEHEN_FREIGABESTELLE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DARLEHEN_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DARLEHEN_UPDATE_GS;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DARLEHEN_UPDATE_SB;

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
    @RolesAllowed(DARLEHEN_READ)
    public FreiwilligDarlehenDto getFreiwilligDarlehenGs(UUID darlehenId) {
        darlehenAuthorizer.canGetDarlehenGs(darlehenId);
        return darlehenService.getDarlehen(darlehenId);
    }

    @Override
    @RolesAllowed(DARLEHEN_READ)
    public FreiwilligDarlehenDto getFreiwilligDarlehenSb(UUID darlehenId) {
        darlehenAuthorizer.canGetDarlehenSb();
        return darlehenService.getDarlehen(darlehenId);
    }

    @Override
    @RolesAllowed(DARLEHEN_READ)
    public PaginatedSbFreiwilligDarlehenDashboardDto getFreiwilligDarlehenDashboardSb(
        GetFreiwilligDarlehenSbQueryType getFreiwilligDarlehenSbQueryType,
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
        SbFreiwilligDarlehenDashboardColumn sortColumn,
        SortOrder sortOrder
    ) {
        darlehenAuthorizer.canGetDarlehenSb();
        return darlehenService.getDarlehenDashboardSb(
            getFreiwilligDarlehenSbQueryType,
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
    @RolesAllowed(DARLEHEN_UPDATE_GS)
    public FreiwilligDarlehenDto createFreiwilligDarlehen(UUID fallId) {
        darlehenAuthorizer.canCreateDarlehen(fallId);
        return darlehenService.createDarlehen(fallId);
    }

    @Override
    @RolesAllowed(DARLEHEN_FREIGABESTELLE)
    public FreiwilligDarlehenDto freiwilligDarlehenAblehen(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenAblehenenAkzeptieren(darlehenId);
        return darlehenService.darlehenAblehnen(darlehenId);
    }

    @Override
    @RolesAllowed(DARLEHEN_FREIGABESTELLE)
    public FreiwilligDarlehenDto freiwilligDarlehenAkzeptieren(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenAblehenenAkzeptieren(darlehenId);
        return darlehenService.darlehenAkzeptieren(darlehenId);
    }

    @Override
    @RolesAllowed(DARLEHEN_UPDATE_GS)
    public FreiwilligDarlehenDto freiwilligDarlehenEingeben(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenEingeben(darlehenId);
        return darlehenService.darlehenEingeben(darlehenId);
    }

    @Override
    @RolesAllowed(DARLEHEN_UPDATE_SB)
    public FreiwilligDarlehenDto freiwilligDarlehenFreigeben(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenFreigeben(darlehenId);
        return darlehenService.darlehenFreigeben(darlehenId);
    }

    @Override
    @RolesAllowed(DARLEHEN_UPDATE_SB)
    public FreiwilligDarlehenDto freiwilligDarlehenZurueckweisen(UUID darlehenId, KommentarDto kommentar) {
        darlehenAuthorizer.canDarlehenZurueckweisen(darlehenId);
        return darlehenService.darlehenZurueckweisen(darlehenId, kommentar);
    }

    @Override
    @RolesAllowed(DARLEHEN_UPDATE_GS)
    public FreiwilligDarlehenDto freiwilligDarlehenUpdateGs(
        UUID darlehenId,
        FreiwilligDarlehenUpdateGsDto darlehenUpdateGsDto
    ) {
        darlehenAuthorizer.canDarlehenUpdateGs(darlehenId);
        return darlehenService.darlehenUpdateGs(darlehenId, darlehenUpdateGsDto);
    }

    @Override
    @RolesAllowed(DARLEHEN_DELETE)
    public void deleteFreiwilligDarlehenGs(UUID darlehenId) {
        darlehenAuthorizer.canDarlehenUpdateGs(darlehenId);
        darlehenService.deleteDarlehen(darlehenId);
    }

    @Override
    @RolesAllowed(DARLEHEN_UPDATE_SB)
    public FreiwilligDarlehenDto freiwilligDarlehenUpdateSb(
        UUID darlehenId,
        FreiwilligDarlehenUpdateSbDto darlehenUpdateSbDto
    ) {
        darlehenAuthorizer.canDarlehenUpdateSb(darlehenId);
        return darlehenService.darlehenUpdateSb(darlehenId, darlehenUpdateSbDto);
    }

    @Override
    @RolesAllowed(DARLEHEN_UPDATE_SB)
    public DarlehenBuchhaltungEntryDto createDarlehenBuchhaltungSaldokorrektur(
        UUID gesuchId,
        DarlehenBuchhaltungSaldokorrekturDto darlehenBuchhaltungSaldokorrekturDto
    ) {
        darlehenAuthorizer.canCreateDarlehenBuchhaltungEntry();
        return darlehenService.createDarlehenBuchhaltungSaldokorrektur(gesuchId, darlehenBuchhaltungSaldokorrekturDto);
    }

    @Blocking
    @Override
    @RolesAllowed(DARLEHEN_UPDATE_GS)
    public Uni<Response> createDarlehenDokument(
        UUID darlehenId,
        DarlehenDokumentType dokumentTyp,
        FileUpload fileUpload
    ) {
        darlehenAuthorizer.canCreateDarlehenDokument(darlehenId);
        return darlehenService.uploadDarlehenDokument(darlehenId, dokumentTyp, fileUpload);
    }

    @Override
    @RolesAllowed(DARLEHEN_READ)
    public NullableDarlehenDokumentDto getDarlehenDokument(UUID darlehenId, DarlehenDokumentType dokumentTyp) {
        darlehenAuthorizer.canGetDarlehenDokument(darlehenId);
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
    @RolesAllowed(DARLEHEN_READ)
    public List<FreiwilligDarlehenDto> getAllFreiwilligDarlehenSb(UUID gesuchId) {
        darlehenAuthorizer.canGetDarlehenSb();
        return darlehenService.getDarlehenAllSb(gesuchId);
    }

    @Override
    @RolesAllowed(DARLEHEN_READ)
    public DarlehenBuchhaltungOverviewDto getDarlehenBuchhaltungEntrys(UUID gesuchId) {
        darlehenAuthorizer.canGetDarlehenBuchhaltungEntrys();
        return darlehenService.getDarlehenBuchhaltungEntryOverviewByFallId(gesuchId);
    }

    @Override
    @RolesAllowed(DARLEHEN_READ)
    public List<FreiwilligDarlehenDto> getAllFreiwilligDarlehenGs(UUID fallId) {
        darlehenAuthorizer.canGetDarlehenByFallId(fallId);
        return darlehenService.getDarlehenAllGs(fallId);
    }

    @Override
    @RolesAllowed(DARLEHEN_READ)
    public FileDownloadTokenDto getDarlehenDownloadToken(UUID dokumentId) {
        darlehenAuthorizer.canGetDarlehenDokumentByDokumentId(dokumentId);

        return dokumentDownloadService.getFileDownloadToken(
            dokumentId,
            DokumentDownloadConstants.DARLEHEN_ID_CLAIM,
            benutzerService,
            configService
        );
    }

    @Blocking
    @Override
    @RolesAllowed(DARLEHEN_UPDATE_GS)
    public void deleteDarlehenDokument(UUID dokumentId) {
        darlehenAuthorizer.canDeleteDarlehenDokument(dokumentId);
        darlehenService.removeDokument(dokumentId);
    }
}
