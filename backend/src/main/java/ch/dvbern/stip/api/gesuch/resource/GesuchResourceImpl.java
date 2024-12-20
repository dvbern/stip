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
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.gesuch.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SbDashboardColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuch.util.GesuchMapperUtil;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.GesuchResource;
import ch.dvbern.stip.generated.dto.AusgewaehlterGrundDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.PaginatedSbDashboardDto;
import ch.dvbern.stip.generated.dto.StatusprotokollEntryDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_GESUCHSTELLER;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

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

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchDto changeGesuchStatusToInBearbeitung(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchStatusToInBearbeitung(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    // TODO KSTIP-1247: roles allowed
    @RolesAllowed({ ROLE_SACHBEARBEITER })
    @Override
    public GesuchDto changeGesuchStatusToNegativeVerfuegung(
        UUID gesuchTrancheId,
        AusgewaehlterGrundDto ausgewaehlterGrundDto
    ) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.changeGesuchStatusToNegativeVerfuegung(
            gesuchId,
            ausgewaehlterGrundDto.getDecisionId()
        );
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    // TODO KSTIP-1247: roles allowed
    @RolesAllowed({ ROLE_SACHBEARBEITER })
    @Override
    public GesuchDto changeGesuchStatusToVerfuegt(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchStatusToVerfuegt(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    // TODO KSTIP-1247: roles allowed
    @RolesAllowed({ ROLE_SACHBEARBEITER })
    @Override
    public GesuchDto changeGesuchStatusToVersendet(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchStatusToVersendet(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @RolesAllowed(GESUCH_CREATE)
    @Override
    public UUID createGesuch(GesuchCreateDto gesuchCreateDto) {
        gesuchAuthorizer.canCreate();
        final var created = gesuchService.createGesuch(gesuchCreateDto);
        return created.getId();
    }

    @RolesAllowed(GESUCH_DELETE)
    @Override
    public void deleteGesuch(UUID gesuchId) {
        gesuchAuthorizer.canDelete(gesuchId);
        gesuchService.deleteGesuch(gesuchId);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchDto gesuchEinreichen(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchEinreichen(gesuchId);
        gesuchService.stipendienAnspruchPruefen(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    // TODO KSTIP-1247: Update which roles can do this
    @RolesAllowed(GESUCH_UPDATE)
    @AllowAll
    @Override
    public GesuchDto gesuchFehlendeDokumenteUebermitteln(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchService.gesuchFehlendeDokumenteUebermitteln(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public GesuchDto getGesuch(UUID gesuchId, UUID gesuchTrancheId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchService.findGesuchWithTranche(gesuchId, gesuchTrancheId).orElseThrow(NotFoundException::new);
    }

    @Override
    public GesuchInfoDto getGesuchInfo(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchService.getGesuchInfo(gesuchId);
    }

    @RolesAllowed(GESUCH_READ)
    @AllowAll
    @Override
    public List<FallDashboardItemDto> getGsDashboard() {
        return gesuchService.getFallDashboardItemDtos();
    }

    @RolesAllowed(GESUCH_READ)
    @AllowAll
    @Override
    public GesuchWithChangesDto getGsAenderungChangesInBearbeitung(UUID aenderungId) {
        gesuchTrancheAuthorizer.canRead(aenderungId);
        return gesuchService.getGsTrancheChangesInBearbeitung(aenderungId);
    }

    @RolesAllowed({ GESUCH_READ, ROLE_GESUCHSTELLER })
    @AllowAll
    @Override
    public List<GesuchDto> getGesucheGs() {
        return gesuchService.findGesucheGs();
    }

    // TODO KSTIP-1247: Update which roles can do this
    @AllowAll
    @RolesAllowed(GESUCH_READ)
    @Override
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

    @RolesAllowed(GESUCH_READ)
    @Override
    public List<StatusprotokollEntryDto> getStatusProtokoll(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchHistoryService.getStatusprotokoll(gesuchId);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public void updateGesuch(UUID gesuchId, GesuchUpdateDto gesuchUpdateDto) {
        gesuchAuthorizer.canUpdate(gesuchId, gesuchUpdateDto);
        gesuchService.updateGesuch(gesuchId, gesuchUpdateDto, tenantService.getCurrentTenant().getIdentifier());
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public BerechnungsresultatDto getBerechnungForGesuch(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        gesuchAuthorizer.canGetBerechnung(gesuchId);
        return gesuchService.getBerechnungsresultat(gesuchId);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public GesuchWithChangesDto getInitialTrancheChangesByTrancheId(UUID trancheId) {
        gesuchTrancheAuthorizer.canReadInitialTrancheChanges(trancheId);
        return gesuchService.getChangesByTrancheId(trancheId);
    }

    // TODO KSTIP-1247: Update which roles can do this
    @RolesAllowed(GESUCH_READ)
    @AllowAll
    @Override
    public GesuchWithChangesDto getSbAenderungChanges(UUID aenderungId) {
        return gesuchService.getSbTrancheChanges(aenderungId);
    }

    // TODO KSTIP-1247: Only SB can execute these next 3
    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchDto bearbeitungAbschliessen(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.bearbeitungAbschliessen(gesuchId);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    // TODO KSTIP-1247: roles allowed
    @RolesAllowed({ ROLE_SACHBEARBEITER })
    @Override
    public GesuchDto changeGesuchStatusToBereitFuerBearbeitung(UUID gesuchTrancheId, KommentarDto kommentarDto) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchStatusToBereitFuerBearbeitung(gesuchId, kommentarDto);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchDto gesuchZurueckweisen(UUID gesuchTrancheId, KommentarDto kommentarDto) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchAuthorizer.canUpdate(gesuchId);
        gesuchService.gesuchZurueckweisen(gesuchId, kommentarDto);
        return gesuchMapperUtil.mapWithGesuchOfTranche(gesuchTranche);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchDto gesuchTrancheFehlendeDokumenteEinreichen(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canUpdate(gesuchTrancheId);
        return gesuchService.gesuchFehlendeDokumenteEinreichen(gesuchTrancheId);
    }
}
