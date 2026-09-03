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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.beschwerdeentscheid.service.BeschwerdeEntscheidService;
import ch.dvbern.stip.api.beschwerdeverlauf.service.BeschwerdeverlaufService;
import ch.dvbern.stip.api.common.authorization.BeschwerdeEntscheidAuthorizer;
import ch.dvbern.stip.api.common.authorization.BeschwerdeVerlaufAuthorizer;
import ch.dvbern.stip.api.common.authorization.DelegierenAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.interceptors.PopulateCurrentBenutzerContext;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.resource.ReadOnlyEndpoint;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbGesucheDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
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
import ch.dvbern.stip.generated.dto.GesuchCreateResponseDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchHeaderDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.GesuchZurueckweisenResponseDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.NachfristAendernRequestDto;
import ch.dvbern.stip.generated.dto.PaginatedSbGesucheDashboardDto;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.OidcPermissions.ADMIN_GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.FREIGABESTELLE_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.JURIST_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.JURIST_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
@Validated
@PopulateCurrentBenutzerContext
public class GesuchResourceImpl implements GesuchResource {
    private final GesuchService gesuchService;
    private final GesuchTrancheService gesuchTrancheService;
    private final GesuchAuthorizer gesuchAuthorizer;
    private final GesuchTrancheAuthorizer gesuchTrancheAuthorizer;
    private final GesuchMapperUtil gesuchMapperUtil;
    private final StipConfig config;
    private final BenutzerService benutzerService;
    private final BeschwerdeverlaufService beschwerdeverlaufService;
    private final BeschwerdeVerlaufAuthorizer beschwerdeVerlaufAuthorizer;
    private final BeschwerdeEntscheidService beschwerdeEntscheidService;
    private final BeschwerdeEntscheidAuthorizer beschwerdeEntscheidAuthorizer;
    private final DelegierenAuthorizer delegierenAuthorizer;
    private final StatusprotokollService statusprotokollService;
    private final DokumentDownloadService dokumentDownloadService;

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto changeGesuchStatusToInBearbeitung(UUID gesuchTrancheId) {
        gesuchAuthorizer.sbCanChangeGesuchOfTrancheStatusToInBearbeitung(gesuchTrancheId);

        return gesuchService.gesuchStatusToInBearbeitung(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE, JURIST_GESUCH_UPDATE })
    public GesuchDto changeGesuchStatusToNegativeVerfuegung(
        UUID gesuchTrancheId,
        AusgewaehlterGrundDto ausgewaehlterGrundDto
    ) {
        gesuchAuthorizer.sbCanChangeGesuchStatusToNegativeVerfuegung(gesuchTrancheId);

        return gesuchService.changeGesuchStatusToNegativeVerfuegung(
            gesuchTrancheId,
            ausgewaehlterGrundDto
        );
    }

    @Blocking
    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE, JURIST_GESUCH_UPDATE })
    public GesuchWithChangesDto createManuelleVerfuegung(
        UUID gesuchTrancheId,
        FileUpload fileUpload,
        String kommentar
    ) {
        gesuchAuthorizer.sbCanCreateManuelleVerfuegungForGesuchOfTranche(gesuchTrancheId);

        return gesuchService.createManuelleVerfuegung(
            gesuchTrancheId,
            fileUpload,
            kommentar
        );
    }

    @Override
    @RolesAllowed(FREIGABESTELLE_GESUCH_UPDATE)
    public GesuchDto changeGesuchStatusToVerfuegt(UUID gesuchTrancheId) {
        gesuchAuthorizer.freigabestelleCanChangeGesuchOfTrancheStatusToVerfuegt(gesuchTrancheId);

        return gesuchService.changeGesuchOfTrancheStatusToVerfuegt(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto changeGesuchStatusToVerfuegungDruckbereit(UUID gesuchTrancheId) {
        gesuchAuthorizer.sbCanChangeGesuchOfTrancheStatusToVerfuegungDruckbereit(gesuchTrancheId);

        return gesuchService.changeGesuchOfTrancheStatusToVerfuegungDruckbereit(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto changeGesuchStatusToVersendet(UUID gesuchTrancheId) {
        gesuchAuthorizer.sbCanChangeGesuchOfTrancheStatusToVersendet(gesuchTrancheId);

        return gesuchService.changeToVersendentAndAnspruchOrKeinAnspruch(gesuchTrancheId);
    }

    @Blocking
    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE, JURIST_GESUCH_UPDATE })
    public Uni<Response> createBeschwerdeEntscheid(
        UUID gesuchId,
        String kommentar,
        Boolean isBeschwerdeErfolgreich,
        FileUpload fileUpload
    ) {
        beschwerdeEntscheidAuthorizer.canCreate(gesuchId);
        return beschwerdeEntscheidService.createBeschwerdeEntscheid(
            gesuchId,
            kommentar,
            isBeschwerdeErfolgreich,
            fileUpload
        );
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE, JURIST_GESUCH_UPDATE })
    public BeschwerdeVerlaufEntryDto createBeschwerdeVerlaufEntry(
        UUID gesuchId,
        BeschwerdeVerlaufEntryCreateDto beschwerdeVerlaufEntryCreateDto
    ) {
        beschwerdeVerlaufAuthorizer.canCreate();
        return beschwerdeverlaufService.createBeschwerdeVerlaufEntry(gesuchId, beschwerdeVerlaufEntryCreateDto);
    }

    @Override
    @RolesAllowed(GS_GESUCH_CREATE)
    public GesuchCreateResponseDto createGesuch(GesuchCreateDto gesuchCreateDto) {
        gesuchAuthorizer.gsCanCreate();
        return gesuchService.createGesuch(gesuchCreateDto);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_DELETE, ADMIN_GESUCH_DELETE })
    public void deleteGesuch(UUID gesuchId) {
        gesuchAuthorizer.gsOrAdminCanDelete(gesuchId);
        gesuchService.deleteGesuch(gesuchId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public EinreichedatumStatusDto canEinreichedatumAendern(UUID gesuchId) {
        gesuchAuthorizer.sbOrJuristCanRead();
        return gesuchService.canUpdateEinreichedatum(gesuchId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto einreichedatumManuellAendern(
        UUID gesuchId,
        EinreichedatumAendernRequestDto einreichedatumAendernRequestDto
    ) {
        gesuchAuthorizer.sbCanUpdateEinreichedatum(gesuchId);
        return gesuchService.einreichedatumManuellAendern(gesuchId, einreichedatumAendernRequestDto);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public List<BeschwerdeVerlaufEntryDto> getAllBeschwerdeVerlaufEntrys(UUID gesuchId) {
        beschwerdeVerlaufAuthorizer.canRead();
        return beschwerdeverlaufService.getAllBeschwerdeVerlaufEntriesByGesuchId(gesuchId);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_UPDATE })
    public GesuchDto gesuchEinreichenGs(UUID gesuchTrancheId) {
        gesuchAuthorizer.gsCanGesuchOfTrancheEinreichen(gesuchTrancheId);

        return gesuchService.gesuchOfTrancheEinreichenGs(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ JURIST_GESUCH_UPDATE })
    public GesuchDto gesuchManuellPruefenJur(UUID gesuchTrancheId) {
        gesuchAuthorizer.juristCanGesuchOfTrancheManuellPruefen(gesuchTrancheId);

        return gesuchService.gesuchManuellPruefenSbJur(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE })
    public GesuchDto gesuchManuellPruefenSB(UUID gesuchTrancheId) {
        gesuchAuthorizer.sbCanGesuchOfTrancheManuellPruefen(gesuchTrancheId);

        return gesuchService.gesuchManuellPruefenSbJur(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto gesuchFehlendeDokumenteUebermitteln(UUID gesuchTrancheId) {
        gesuchAuthorizer.sbCanGesuchOfTrancheFehlendeDokumenteUebermitteln(gesuchTrancheId);

        return gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public GesuchInfoDto getGesuchInfoGs(UUID gesuchId) {
        gesuchAuthorizer.gsCanRead(gesuchId);
        return gesuchService.getGesuchInfoGs(gesuchId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public GesuchInfoDto getGesuchInfoSb(UUID gesuchId) {
        gesuchAuthorizer.gsSbFreigabestelleOrJuristCanRead(gesuchId);
        return gesuchService.getGesuchInfoSb(gesuchId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public FallDashboardItemDto getGsDashboard() {
        gesuchAuthorizer.canGetGsDashboard();
        return gesuchService.getFallDashboardItemDtos();
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ, JURIST_GESUCH_READ })
    public GesuchDto getEingereichtTranche(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.gsSbFreigabestelleOrJuristCanRead(gesuchTrancheId);
        return gesuchService.getEingereichtGesuchByTrancheId(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ, JURIST_GESUCH_READ })
    public GesuchWithChangesDto getInitialTrancheChanges(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canReadInitialTranche(gesuchTrancheId);
        return gesuchService.getChangesByInitialTrancheId(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public List<GesuchDto> getGesucheGs() {
        gesuchAuthorizer.gsCanGetGesuche();
        return gesuchService.findGesucheGs();
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public PaginatedSbGesucheDashboardDto getGesucheSb(
        GetGesucheSBQueryType getGesucheSBQueryType,
        GesuchTrancheTyp trancheTyp,
        Integer page,
        Integer pageSize,
        Boolean bearbeitbar,
        Boolean zugewiesen,
        String fallNummer,
        String piaNachname,
        String piaVorname,
        LocalDate piaGeburtsdatum,
        String status,
        String bearbeiter,
        LocalDate letzteAktivitaetFrom,
        LocalDate letzteAktivitaetTo,
        SbGesucheDashboardColumn sortColumn,
        SortOrder sortOrder
    ) {
        gesuchAuthorizer.sbCanGetGesuche();
        return gesuchService.getDashboardSB(
            trancheTyp,
            getGesucheSBQueryType,
            bearbeitbar,
            zugewiesen,
            fallNummer,
            piaNachname,
            piaVorname,
            piaGeburtsdatum,
            status,
            bearbeiter,
            letzteAktivitaetFrom,
            letzteAktivitaetTo,
            page,
            pageSize,
            sortColumn,
            sortOrder
        );
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ, JURIST_GESUCH_READ })
    public List<StatusprotokollEntryDto> getStatusProtokoll(UUID gesuchId) {
        gesuchAuthorizer.gsSbFreigabestelleOrJuristCanRead(gesuchId);
        return statusprotokollService.getStatusprotokoll(gesuchId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public void updateGesuchGS(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        gesuchTrancheAuthorizer.canUpdateTrancheGS(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId());
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto, true);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public void updateGesuchSB(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        gesuchTrancheAuthorizer.canUpdateTrancheSB(gesuchUpdateDto.getGesuchTrancheToWorkWith().getId());
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto, false);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public void updateNachfristDokumente(UUID gesuchId, NachfristAendernRequestDto nachfristAendernRequestDto) {
        gesuchAuthorizer.canUpdateNachfrist(gesuchId);
        gesuchService.updateNachfristDokumente(gesuchId, nachfristAendernRequestDto.getNewNachfrist());
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public BerechnungsresultatDto getBerechnungForGesuchSb(UUID gesuchId) {
        gesuchAuthorizer.canGetBerechnungSb(gesuchId);
        return gesuchService.getBerechnungsresultatSb(gesuchId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ, GS_GESUCH_READ })
    public BerechnungsresultatDto getBerechnungForVerfuegung(UUID verfuegungId) {
        gesuchAuthorizer.canGetBerechnungOfVerfuegung(verfuegungId);
        return gesuchService.getBerechnungForVerfuegung(verfuegungId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public FileDownloadTokenDto getBerechnungsblattDownloadToken(UUID gesuchId) {
        gesuchAuthorizer.canGetBerechnungSb(gesuchId);

        return dokumentDownloadService.getFileDownloadToken(
            gesuchId,
            DokumentDownloadConstants.GESUCH_ID_CLAIM,
            benutzerService,
            config
        );
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public GesuchWithChangesDto getGesuchSB(UUID gesuchTrancheId) {
        gesuchAuthorizer.sbOrJuristCanRead();
        return gesuchTrancheService.getGesuchSB(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public GesuchDto getGesuchGS(UUID gesuchTrancheId) {
        gesuchAuthorizer.gsCanReadGesuchOfTranche(gesuchTrancheId);
        return gesuchService.getGesuchGS(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public GesuchHeaderDto getGesuchHeaderGs(UUID gesuchId) {
        gesuchAuthorizer.gsCanRead(gesuchId);
        return gesuchService.getGesuchTrancheHeaderGs(gesuchId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public GesuchHeaderDto getGesuchHeaderSb(UUID gesuchId) {
        gesuchAuthorizer.sbOrJuristCanRead();
        return gesuchService.getGesuchTrancheHeaderSb(gesuchId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    @ReadOnlyEndpoint
    public GesuchWithChangesDto getAenderungChangesGs(UUID aenderungId, Integer revision) {
        gesuchTrancheAuthorizer.gsCanRead(aenderungId);
        if (Objects.nonNull(revision)) {
            return gesuchService.getTrancheChangesWithRevision(aenderungId, revision);
        }
        return gesuchService.getGsTrancheChangesInBearbeitung(aenderungId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    @ReadOnlyEndpoint
    public GesuchWithChangesDto getAenderungChangesSb(UUID aenderungId, Integer revision) {
        gesuchTrancheAuthorizer.sbOrJuristCanRead();
        if (Objects.nonNull(revision)) {
            return gesuchService.getTrancheChangesWithRevision(aenderungId, revision);
        }
        return gesuchService.getSbTrancheChanges(aenderungId);
    }

    @RolesAllowed(GS_GESUCH_READ)
    @Override
    public FallDashboardItemDto getSozialdienstMitarbeiterDashboard(UUID fallId) {
        delegierenAuthorizer.canReadFallDashboard(fallId);
        return gesuchService.getSozialdienstMitarbeiterFallDashboardItemDtos(fallId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto bearbeitungAbschliessen(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canUpdateTrancheSB(gesuchTrancheId);
        gesuchAuthorizer.sbCanBearbeitungAbschliessenByTranche(gesuchTrancheId);
        return gesuchService.bearbeitungAbschliessen(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_UPDATE, FREIGABESTELLE_GESUCH_UPDATE })
    public GesuchWithChangesDto changeGesuchStatusToBereitFuerBearbeitung(
        UUID gesuchTrancheId
    ) {
        gesuchAuthorizer.sbCanChangeGesuchOfTrancheStatusToBereitFuerBearbeitung(gesuchTrancheId);
        return gesuchService.gesuchOfTrancheStatusToBereitFuerBearbeitung(gesuchTrancheId);
    }

    @RolesAllowed({ SB_GESUCH_UPDATE, JURIST_GESUCH_UPDATE })
    @Override
    public GesuchWithChangesDto changeGesuchStatusToDatenschutzbriefDruckbereit(
        UUID gesuchTrancheId,
        KommentarDto kommentarDto
    ) {
        gesuchAuthorizer
            .sbCanChangeGesuchOfTrancheStatusToDatenschutzBriefDruckbereitIfStatusChangeRequired(gesuchTrancheId);
        return gesuchService.gesuchStatusToDatenschutzbriefDruckbereit(gesuchTrancheId, kommentarDto);
    }

    @RolesAllowed(SB_GESUCH_UPDATE)
    @Override
    public GesuchWithChangesDto changeGesuchStatusToBearbeitungAsAenderung(
        UUID gesuchTrancheId,
        KommentarDto kommentarDto
    ) {
        gesuchAuthorizer
            .sbCanChangeGesuchOfTrancheStatusToBearbeitungAsAenderungIfStatusChangeRequired(gesuchTrancheId);
        return gesuchService.gesuchStatusToBearbeitungAsAenderung(gesuchTrancheId, kommentarDto);
    }

    @Transactional
    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchZurueckweisenResponseDto gesuchZurueckweisenAenderungUndo(
        UUID gesuchTrancheId,
        KommentarDto kommentarDto
    ) {
        gesuchAuthorizer.sbCanGesuchOfTrancheZurueckweisen(gesuchTrancheId);

        return gesuchService.gesuchOfTrancheZurueckweisen(gesuchTrancheId, kommentarDto);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public GesuchDto gesuchTrancheFehlendeDokumenteEinreichen(UUID gesuchTrancheId) {
        gesuchAuthorizer.gsCanFehlendeDokumenteEinreichen(gesuchTrancheId);

        return gesuchService.gesuchFehlendeDokumenteEinreichen(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchDto setGesuchsperiodeForGesuch(UUID gesuchTrancheId, @NotNull UUID gesuchsperiodeId) {
        gesuchAuthorizer.sbCanChangeGesuchsperiodeForGesuchOfTranche(gesuchTrancheId);

        return gesuchService.setGesuchsperiodeForGesuch(gesuchTrancheId, gesuchsperiodeId);
    }
}
