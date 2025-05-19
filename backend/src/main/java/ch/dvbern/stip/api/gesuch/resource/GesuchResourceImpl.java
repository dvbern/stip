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

package ch.dvbern.stip.api.gesuch.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.beschwerdeentscheid.service.BeschwerdeEntscheidAuthorizer;
import ch.dvbern.stip.api.beschwerdeentscheid.service.BeschwerdeEntscheidService;
import ch.dvbern.stip.api.beschwerdeverlauf.service.BeschwerdeverlaufService;
import ch.dvbern.stip.api.common.authorization.BeschwerdeVerlaufAuthorizer;
import ch.dvbern.stip.api.common.authorization.DelegierenAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuchhistory.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryCreateDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryDto;
import ch.dvbern.stip.generated.dto.EinreichedatumAendernRequestDto;
import ch.dvbern.stip.generated.dto.EinreichedatumStatusDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.GesuchZurueckweisenResponseDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.NachfristAendernRequestDto;
import ch.dvbern.stip.generated.dto.PaginatedSbDashboardDto;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import ch.dvbern.stip.generated.dto.VerfuegungDto;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.OidcPermissions.ADMIN_GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.JURIST_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
@Validated
public class GesuchResourceImpl implements GesuchResource {
    private final GesuchService gesuchService;
    private final GesuchTrancheService gesuchTrancheService;
    private final TenantService tenantService;
    private final GesuchHistoryService gesuchHistoryService;
    private final GesuchAuthorizer gesuchAuthorizer;
    private final GesuchTrancheAuthorizer gesuchTrancheAuthorizer;
    private final GesuchMapperUtil gesuchMapperUtil;
    private final ConfigService configService;
    private final JWTParser jwtParser;
    private final BenutzerService benutzerService;
    private final BeschwerdeverlaufService beschwerdeverlaufService;
    private final BeschwerdeVerlaufAuthorizer beschwerdeVerlaufAuthorizer;
    private final BeschwerdeEntscheidService beschwerdeEntscheidService;
    private final BeschwerdeEntscheidAuthorizer beschwerdeEntscheidAuthorizer;
    private final VerfuegungService verfuegungService;
    private final DelegierenAuthorizer delegierenAuthorizer;

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto changeGesuchStatusToInBearbeitung(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchService.gesuchStatusToInBearbeitung(gesuchId);
        return gesuchService.getGesuchSB(gesuchId, gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto changeGesuchStatusToNegativeVerfuegung(
        UUID gesuchTrancheId,
        AusgewaehlterGrundDto ausgewaehlterGrundDto
    ) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        verfuegungService.createVerfuegung(gesuchId, ausgewaehlterGrundDto.getDecisionId());
        gesuchService.changeGesuchStatusToNegativeVerfuegung(
            gesuchId,
            ausgewaehlterGrundDto.getDecisionId()
        );
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto changeGesuchStatusToVersandbereit(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchService.changeGesuchStatusToVersandbereit(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto changeGesuchStatusToVerfuegt(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchService.gesuchStatusToVerfuegt(gesuchId);
        gesuchService.gesuchStatusCheckUnterschriftenblatt(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto changeGesuchStatusToVersendet(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchService.gesuchStatusToVersendet(gesuchId);
        gesuchService.gesuchStatusToStipendienanspruch(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @Blocking
    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public Uni<Response> createBeschwerdeEntscheid(
        UUID gesuchId,
        String kommentar,
        Boolean isBeschwerdeErfolgreich,
        FileUpload fileUpload
    ) {
        beschwerdeEntscheidAuthorizer.canCreate(gesuchId);
        return beschwerdeEntscheidService
            .createBeschwerdeEntscheid(gesuchId, kommentar, isBeschwerdeErfolgreich, fileUpload);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public BeschwerdeVerlaufEntryDto createBeschwerdeVerlaufEntry(
        UUID gesuchId,
        BeschwerdeVerlaufEntryCreateDto beschwerdeVerlaufEntryCreateDto
    ) {
        beschwerdeVerlaufAuthorizer.canCreate();
        return beschwerdeverlaufService.createBeschwerdeVerlaufEntry(gesuchId, beschwerdeVerlaufEntryCreateDto);
    }

    @Override
    @RolesAllowed(GS_GESUCH_CREATE)
    public UUID createGesuch(GesuchCreateDto gesuchCreateDto) {
        gesuchAuthorizer.canCreate();
        final var created = gesuchService.createGesuch(gesuchCreateDto);
        return created.getId();
    }

    @Override
    @RolesAllowed({ GS_GESUCH_DELETE, ADMIN_GESUCH_DELETE })
    public void deleteGesuch(UUID gesuchId) {
        gesuchAuthorizer.canDelete(gesuchId);
        gesuchService.deleteGesuch(gesuchId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public EinreichedatumStatusDto canEinreichedatumAendern(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchService.canUpdateEinreichedatum(gesuchId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto einreichedatumManuellAendern(
        UUID gesuchId,
        EinreichedatumAendernRequestDto einreichedatumAendernRequestDto
    ) {
        gesuchAuthorizer.canUpdateEinreichedatum(gesuchId);
        return gesuchService.einreichedatumManuellAendern(gesuchId, einreichedatumAendernRequestDto);
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public List<BeschwerdeVerlaufEntryDto> getAllBeschwerdeVerlaufEntrys(UUID gesuchId) {
        beschwerdeVerlaufAuthorizer.canRead();
        return beschwerdeverlaufService.getAllBeschwerdeVerlaufEntriesByGesuchId(gesuchId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public List<VerfuegungDto> getAllVerfuegungen(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return verfuegungService.getVerfuegungenByGesuch(gesuchId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public GesuchDto gesuchEinreichen(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canGesuchEinreichen(gesuchId);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchService.gesuchEinreichen(gesuchId);
        gesuchService.stipendienAnspruchPruefen(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto gesuchFehlendeDokumenteUebermitteln(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);

        gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuchId);
        return gesuchService.getGesuchSB(gesuchId, gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ })
    public GesuchInfoDto getGesuchInfo(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchService.getGesuchInfo(gesuchId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public FallDashboardItemDto getGsDashboard() {
        gesuchAuthorizer.canGetGsDashboard();
        return gesuchService.getFallDashboardItemDtos();
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public GesuchWithChangesDto getGsAenderungChangesInBearbeitung(UUID aenderungId) {
        gesuchTrancheAuthorizer.canRead(aenderungId);
        return gesuchService.getGsTrancheChangesInBearbeitung(aenderungId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public List<GesuchDto> getGesucheGs() {
        gesuchAuthorizer.gsCanGetGesuche();
        return gesuchService.findGesucheGs();
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public PaginatedSbDashboardDto getGesucheSb(
        GetGesucheSBQueryType getGesucheSBQueryType,
        GesuchTrancheTyp typ,
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
        SbDashboardColumn sortColumn,
        SortOrder sortOrder
    ) {
        gesuchAuthorizer.sbCanGetGesuche();
        return gesuchService.findGesucheSB(
            getGesucheSBQueryType,
            fallNummer,
            piaNachname,
            piaVorname,
            piaGeburtsdatum,
            status,
            bearbeiter,
            letzteAktivitaetFrom,
            letzteAktivitaetTo,
            typ,
            page,
            pageSize,
            sortColumn,
            sortOrder
        );
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ, JURIST_GESUCH_READ })
    public List<StatusprotokollEntryDto> getStatusProtokoll(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchHistoryService.getStatusprotokoll(gesuchId);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_UPDATE, SB_GESUCH_UPDATE })
    public void updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        final var gesuchTranche =
            gesuchTrancheService.getGesuchTranche(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId());
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto, tenantService.getCurrentTenant().getIdentifier());
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public void updateNachfristDokumente(UUID gesuchId, NachfristAendernRequestDto nachfristAendernRequestDto) {
        gesuchAuthorizer.canUpdateEinreichefrist(gesuchId);
        gesuchService.updateNachfristDokumente(gesuchId, nachfristAendernRequestDto.getNewNachfrist());
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public BerechnungsresultatDto getBerechnungForGesuch(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        gesuchAuthorizer.canGetBerechnung(gesuchId);
        return gesuchService.getBerechnungsresultat(gesuchId);
    }

    @Override
    @Blocking
    @PermitAll
    public RestMulti<Buffer> getBerechnungsBlattForGesuch(String token) {
        JsonWebToken jwt;
        try {
            jwt = jwtParser.verify(token, configService.getSecret());
        } catch (ParseException e) {
            throw new UnauthorizedException();
        }

        final var gesuchId = UUID.fromString(
            (String) jwt.claim(DokumentDownloadConstants.GESUCH_ID_CLAIM)
                .orElseThrow(BadRequestException::new)
        );

        ByteArrayOutputStream byteStream = null;
        try {
            byteStream = gesuchService.getBerechnungsblattByteStream(gesuchId);
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        ByteArrayOutputStream finalByteStream = byteStream;
        return RestMulti.fromUniResponse(
            Uni.createFrom().item(finalByteStream),
            response -> Multi.createFrom()
                .items(response)
                .map(byteArrayOutputStream -> Buffer.buffer(byteArrayOutputStream.toByteArray())),
            response -> Map.of(
                "Content-Disposition",
                List.of("attachment;filename=" + gesuchService.getBerechnungsblattFileName(gesuchId)),
                "Content-Type",
                List.of("application/octet-stream")
            )
        );
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public FileDownloadTokenDto getBerechnungsblattDownloadToken(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        gesuchAuthorizer.canGetBerechnung(gesuchId);

        return new FileDownloadTokenDto()
            .token(
                Jwt
                    .claims()
                    .upn(benutzerService.getCurrentBenutzername())
                    .claim(DokumentDownloadConstants.GESUCH_ID_CLAIM, gesuchId.toString())
                    .expiresIn(Duration.ofMinutes(configService.getExpiresInMinutes()))
                    .issuer(configService.getIssuer())
                    .jws()
                    .signWithSecret(configService.getSecret())
            );
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public GesuchWithChangesDto getGesuchSB(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);

        gesuchAuthorizer.canReadChanges(gesuchId);
        return gesuchService.getGesuchSB(gesuchId, gesuchTrancheId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public GesuchDto getGesuchGS(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTrancheOrHistorical(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);

        gesuchAuthorizer.canRead(gesuchId);
        return gesuchService.getGesuchGS(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ })
    public GesuchWithChangesDto getInitialTrancheChangesByGesuchId(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchService.getChangesByGesuchId(gesuchId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_READ)
    public GesuchWithChangesDto getSbAenderungChanges(UUID aenderungId) {
        gesuchAuthorizer.sbCanReadTranche(aenderungId);
        return gesuchService.getSbTrancheChanges(aenderungId);
    }

    @RolesAllowed(GS_GESUCH_READ)
    @Override
    public FallDashboardItemDto getSozialdienstMitarbeiterDashboard(UUID fallId) {
        delegierenAuthorizer.canReadFallDashboard();
        return gesuchService.getSozialdienstMitarbeiterFallDashboardItemDtos(fallId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto bearbeitungAbschliessen(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchAuthorizer.canBearbeitungAbschliessen(gesuchId);
        gesuchService.bearbeitungAbschliessen(gesuchId);
        gesuchService.gesuchStatusCheckUnterschriftenblatt(gesuchId);
        return gesuchService.getGesuchSB(gesuchId, gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto changeGesuchStatusToBereitFuerBearbeitung(
        UUID gesuchTrancheId,
        KommentarDto kommentarDto
    ) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        gesuchService.gesuchStatusToBereitFuerBearbeitung(gesuchId, kommentarDto);
        return gesuchService.getGesuchSB(gesuchId, gesuchTrancheId);
    }

    @Transactional
    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchZurueckweisenResponseDto gesuchZurueckweisen(UUID gesuchTrancheId, KommentarDto kommentarDto) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canUpdateTranche(gesuchTranche);
        return gesuchService.gesuchZurueckweisen(gesuchId, kommentarDto);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public GesuchDto gesuchTrancheFehlendeDokumenteEinreichen(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canUpdateTrancheStatus(gesuchTrancheId);
        gesuchTrancheAuthorizer.canFehlendeDokumenteEinreichen(gesuchTrancheId);
        gesuchService.gesuchFehlendeDokumenteEinreichen(gesuchTrancheId);
        return gesuchService.getGesuchGS(gesuchTrancheId);
    }
}
