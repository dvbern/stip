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

package ch.dvbern.stip.api.gesuchtranche.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.generated.api.GesuchTrancheResource;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class GesuchTrancheResourceImpl implements GesuchTrancheResource {
    private final GesuchTrancheService gesuchTrancheService;
    private final GesuchService gesuchService;
    private final GesuchAuthorizer gesuchAuthorizer;
    private final GesuchTrancheAuthorizer gesuchTrancheAuthorizer;

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchTrancheDto createAenderungsantrag(
        UUID gesuchId,
        CreateAenderungsantragRequestDto createAenderungsantragRequestDto
    ) {
        gesuchAuthorizer.canUpdate(gesuchId, true);
        return gesuchTrancheService.createAenderungsantrag(gesuchId, createAenderungsantragRequestDto);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public GesuchTrancheListDto getAllTranchenForGesuch(UUID gesuchId) {
        gesuchAuthorizer.canRead(gesuchId);
        return gesuchTrancheService.getAllTranchenAndInitalTrancheForGesuch(gesuchId);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchTrancheDto createGesuchTrancheCopy(
        UUID gesuchId,
        CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto
    ) {
        gesuchAuthorizer.canCreateTranche(gesuchId);
        return gesuchTrancheService.createTrancheCopy(
            gesuchId,
            createGesuchTrancheRequestDto
        );
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public void deleteAenderung(UUID aenderungId) {
        gesuchTrancheAuthorizer.canDeleteAenderung(aenderungId);
        gesuchTrancheService.deleteAenderung(aenderungId);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public List<GesuchDokumentDto> getGesuchDokumente(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        return gesuchTrancheService.getAndCheckGesuchDokumentsForGesuchTranche(gesuchTrancheId);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public GesuchDokumentDto getGesuchDokument(UUID gesuchTrancheId, DokumentTyp dokumentTyp) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        return gesuchTrancheService.getGesuchDokument(gesuchTrancheId, dokumentTyp);
    }

    @Override
    @RolesAllowed(GESUCH_READ)
    public DokumenteToUploadDto getDocumentsToUpload(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        return gesuchTrancheService.getDokumenteToUpload(gesuchTrancheId);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public ValidationReportDto validateGesuchTranchePages(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canRead(gesuchTrancheId);
        return gesuchTrancheService.validatePages(gesuchTrancheId);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public void aenderungEinreichen(UUID aenderungId) {
        gesuchTrancheAuthorizer.canAenderungEinreichen(aenderungId);
        gesuchTrancheService.aenderungEinreichen(aenderungId);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchDto aenderungFehlendeDokumenteEinreichen(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canUpdate(gesuchTrancheId);
        gesuchTrancheAuthorizer.canFehlendeDokumenteEinreichen(gesuchTrancheId);
        return gesuchTrancheService.aenderungFehlendeDokumenteEinreichen(gesuchTrancheId);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @AllowAll
    @Override
    public GesuchWithChangesDto aenderungFehlendeDokumenteUebermitteln(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheService.aenderungFehlendeDokumenteUebermitteln(gesuchTrancheId);
        return gesuchService.getGesuchSB(gesuchId, gesuchTrancheId);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    public ValidationReportDto gesuchTrancheEinreichenValidieren(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canUpdate(gesuchTrancheId);
        return gesuchTrancheService.einreichenValidieren(gesuchTrancheId);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchTrancheDto aenderungAkzeptieren(UUID aenderungId) {
        gesuchTrancheAuthorizer.canUpdate(aenderungId);
        return gesuchTrancheService.aenderungAkzeptieren(aenderungId);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchTrancheDto aenderungAblehnen(UUID aenderungId, KommentarDto kommentarDto) {
        gesuchTrancheAuthorizer.canUpdate(aenderungId);
        return gesuchTrancheService.aenderungAblehnen(aenderungId, kommentarDto);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    public GesuchTrancheDto aenderungManuellAnpassen(UUID aenderungId) {
        gesuchTrancheAuthorizer.canUpdate(aenderungId);
        return gesuchTrancheService.aenderungManuellAnpassen(aenderungId);
    }
}
