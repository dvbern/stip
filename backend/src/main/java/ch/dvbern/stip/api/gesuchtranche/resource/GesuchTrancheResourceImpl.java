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

import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
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
import ch.dvbern.stip.generated.dto.PatchAenderungsInfoRequestDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.common.util.OidcPermissions.AENDERUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AENDERUNG_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AENDERUNG_EINREICHEN;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_UPDATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.JURIST_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class GesuchTrancheResourceImpl implements GesuchTrancheResource {
    private final GesuchTrancheService gesuchTrancheService;
    private final GesuchService gesuchService;
    private final GesuchAuthorizer gesuchAuthorizer;
    private final GesuchTrancheAuthorizer gesuchTrancheAuthorizer;

    @Override
    @RolesAllowed(AENDERUNG_CREATE)
    public GesuchTrancheDto createAenderungsantrag(
        UUID gesuchId,
        CreateAenderungsantragRequestDto createAenderungsantragRequestDto
    ) {
        gesuchAuthorizer.gsCanCreateAenderung(gesuchId);
        return gesuchTrancheService.createAenderungsantrag(gesuchId, createAenderungsantragRequestDto);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public GesuchTrancheListDto getAllTranchenForGesuchGS(UUID gesuchId) {
        gesuchAuthorizer.gsCanRead(gesuchId);
        return gesuchTrancheService.getAllTranchenAndInitalTrancheForGesuchGS(gesuchId);
    }

    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    @Override
    public GesuchTrancheListDto getAllTranchenForGesuchSB(UUID gesuchId) {
        gesuchAuthorizer.sbOrJuristCanRead();
        return gesuchTrancheService.getAllTranchenAndInitalTrancheForGesuchSB(gesuchId);
    }

    @RolesAllowed(GS_GESUCH_READ)
    @Override
    public DokumenteToUploadDto getDocumentsToUploadGS(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.gsCanRead(gesuchTrancheId);
        return gesuchTrancheService.getDokumenteToUploadGS(gesuchTrancheId);
    }

    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    @Override
    public DokumenteToUploadDto getDocumentsToUploadSB(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.sbOrJuristCanRead();
        return gesuchTrancheService.getDokumenteToUploadSB(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchTrancheDto createGesuchTrancheCopy(
        UUID gesuchId,
        CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto
    ) {
        gesuchAuthorizer.sbCanCreateTranche(gesuchId);
        return gesuchTrancheService.createTrancheCopy(
            gesuchId,
            createGesuchTrancheRequestDto
        );
    }

    @Override
    @RolesAllowed(AENDERUNG_DELETE)
    public void deleteAenderung(UUID aenderungId) {
        gesuchTrancheAuthorizer.canDeleteAenderung(aenderungId);
        gesuchTrancheService.deleteAenderung(aenderungId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public ValidationReportDto gesuchTrancheEinreichenValidierenGS(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.gsCanRead(gesuchTrancheId);
        return gesuchTrancheService.einreichenValidierenGS(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public ValidationReportDto gesuchTrancheEinreichenValidierenSB(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.sbOrJuristCanRead();
        return gesuchTrancheService.einreichenValidierenSB(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public List<GesuchDokumentDto> getGesuchDokumenteGS(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.gsCanRead(gesuchTrancheId);
        return gesuchTrancheService.getAndCheckGesuchDokumentsForGesuchTrancheGS(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public List<GesuchDokumentDto> getGesuchDokumenteSB(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.sbOrJuristCanRead();
        return gesuchTrancheService.getAndCheckGesuchDokumentsForGesuchTrancheSB(gesuchTrancheId);
    }

    @RolesAllowed(SB_GESUCH_UPDATE)
    @Override
    public GesuchDto patchAenderungInfo(UUID aenderungId, PatchAenderungsInfoRequestDto patchAenderungsInfoRequestDto) {
        gesuchTrancheAuthorizer.sbCanUpdateGueltigkeitOfAenderung(aenderungId);
        return gesuchTrancheService.updateGueltigkeitOfAenderung(aenderungId, patchAenderungsInfoRequestDto);
    }

    @Override
    @RolesAllowed(GS_GESUCH_READ)
    public ValidationReportDto validateGesuchTranchePagesGS(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.gsCanRead(gesuchTrancheId);
        return gesuchTrancheService.validatePagesGS(gesuchTrancheId);
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public ValidationReportDto validateGesuchTranchePagesSB(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.sbOrJuristCanRead();
        return gesuchTrancheService.validatePagesSB(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(AENDERUNG_EINREICHEN)
    public void aenderungEinreichen(UUID aenderungId) {
        gesuchTrancheAuthorizer.canAenderungEinreichen(aenderungId);
        gesuchTrancheService.aenderungEinreichen(aenderungId);
    }

    @Override
    @RolesAllowed(GS_GESUCH_UPDATE)
    public GesuchDto aenderungFehlendeDokumenteEinreichen(UUID gesuchTrancheId) {
        gesuchTrancheAuthorizer.canFehlendeDokumenteEinreichen(gesuchTrancheId);
        return gesuchTrancheService.aenderungFehlendeDokumenteEinreichen(gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchWithChangesDto aenderungFehlendeDokumenteUebermitteln(UUID gesuchTrancheId) {
        final var gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchTrancheId);
        final var gesuchId = gesuchTrancheService.getGesuchIdOfTranche(gesuchTranche);
        gesuchTrancheAuthorizer.canFehlendeDokumenteUebermitteln(gesuchTrancheId);

        gesuchTrancheService.aenderungFehlendeDokumenteUebermitteln(gesuchTrancheId);
        return gesuchService.getGesuchSB(gesuchId, gesuchTrancheId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchTrancheDto aenderungAkzeptieren(UUID aenderungId) {
        gesuchTrancheAuthorizer.canAenderungAkzeptieren(aenderungId);
        return gesuchTrancheService.aenderungAkzeptieren(aenderungId);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchTrancheDto aenderungAblehnen(UUID aenderungId, KommentarDto kommentarDto) {
        gesuchTrancheAuthorizer.canAenderungAblehnen(aenderungId);
        return gesuchTrancheService.aenderungAblehnen(aenderungId, kommentarDto);
    }

    @Override
    @RolesAllowed(SB_GESUCH_UPDATE)
    public GesuchTrancheDto aenderungManuellAnpassen(UUID aenderungId) {
        gesuchTrancheAuthorizer.canAenderungManuellAnpassen(aenderungId);
        return gesuchTrancheService.aenderungManuellAnpassen(aenderungId);
    }
}
