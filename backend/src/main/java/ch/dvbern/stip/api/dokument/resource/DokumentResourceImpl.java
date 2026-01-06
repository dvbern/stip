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

package ch.dvbern.stip.api.dokument.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.beschwerdeentscheid.service.BeschwerdeEntscheidService;
import ch.dvbern.stip.api.common.authorization.CustomGesuchDokumentTypAuthorizer;
import ch.dvbern.stip.api.common.authorization.DokumentAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchDokumentAuthorizer;
import ch.dvbern.stip.api.common.authorization.UnterschriftenblattAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.CustomDokumentTypService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentKommentarService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentArt;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheOverrideDokumentService;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.generated.api.DokumentResource;
import ch.dvbern.stip.generated.dto.CustomDokumentTypCreateDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.OidcPermissions.CUSTOM_DOKUMENT_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.CUSTOM_DOKUMENT_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.CUSTOM_DOKUMENT_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DOKUMENT_ABLEHNEN_AKZEPTIEREN;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DOKUMENT_DELETE_GS;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DOKUMENT_DELETE_SB;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DOKUMENT_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DOKUMENT_UPLOAD_GS;
import static ch.dvbern.stip.api.common.util.OidcPermissions.DOKUMENT_UPLOAD_SB;
import static ch.dvbern.stip.api.common.util.OidcPermissions.UNTERSCHRIFTENBLATT_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.UNTERSCHRIFTENBLATT_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.UNTERSCHRIFTENBLATT_UPLOAD;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
@Validated
public class DokumentResourceImpl implements DokumentResource {

    private final GesuchDokumentService gesuchDokumentService;
    private final UnterschriftenblattService unterschriftenblattService;
    private final ConfigService configService;
    private final JWTParser jwtParser;
    private final BenutzerService benutzerService;
    private final CustomDokumentTypService customDokumentTypService;
    private final UnterschriftenblattAuthorizer unterschriftenblattAuthorizer;
    private final DokumentAuthorizer dokumentAuthorizer;
    private final CustomGesuchDokumentTypAuthorizer customGesuchDokumentTypAuthorizer;
    private final GesuchDokumentAuthorizer gesuchDokumentAuthorizer;
    private final GesuchDokumentKommentarService gesuchDokumentKommentarService;
    private final GesuchTrancheOverrideDokumentService gesuchTrancheOverrideDokumentService;
    private final BeschwerdeEntscheidService beschwerdeEntscheidService;
    private final DokumentDownloadService dokumentDownloadService;

    @Override
    @RolesAllowed(CUSTOM_DOKUMENT_CREATE)
    public GesuchDokumentDto createCustomDokumentTyp(CustomDokumentTypCreateDto customDokumentTypCreateDto) {
        customGesuchDokumentTypAuthorizer.canCreateCustomDokumentTyp(customDokumentTypCreateDto.getTrancheId());
        final var createdCustomTyp = customDokumentTypService.createCustomDokumentTyp(customDokumentTypCreateDto);
        return gesuchDokumentService.findGesuchDokumentForCustomTypSB(createdCustomTyp.getId()).getValue();
    }

    @Blocking
    @Override
    @RolesAllowed(DOKUMENT_UPLOAD_GS)
    public Uni<Response> createDokumentGS(DokumentTyp dokumentTyp, UUID gesuchTrancheId, FileUpload fileUpload) {
        gesuchDokumentAuthorizer.canGsUploadDokument(gesuchTrancheId, dokumentTyp);
        return gesuchDokumentService.getUploadDokumentUni(dokumentTyp, gesuchTrancheId, fileUpload);
    }

    @Blocking
    @Override
    @RolesAllowed(DOKUMENT_UPLOAD_SB)
    public Uni<Response> createDokumentSB(DokumentTyp dokumentTyp, UUID gesuchTrancheId, FileUpload fileUpload) {
        gesuchDokumentAuthorizer.assertSbCanModifyDokumentOfTranche(gesuchTrancheId);
        gesuchDokumentService.setGesuchDokumentOfDokumentTypToAusstehend(gesuchTrancheId, dokumentTyp);
        return gesuchDokumentService.getUploadDokumentUni(dokumentTyp, gesuchTrancheId, fileUpload);
    }

    @Override
    @Blocking
    @RolesAllowed(UNTERSCHRIFTENBLATT_UPLOAD)
    public Uni<Response> createUnterschriftenblatt(
        UnterschriftenblattDokumentTyp unterschriftenblattTyp,
        UUID gesuchId,
        FileUpload fileUpload
    ) {
        unterschriftenblattAuthorizer.canUpload(gesuchId);
        return unterschriftenblattService.getUploadUnterschriftenblattUni(unterschriftenblattTyp, gesuchId, fileUpload);
    }

    @Override
    @RolesAllowed(UNTERSCHRIFTENBLATT_READ)
    public List<UnterschriftenblattDokumentDto> getUnterschriftenblaetterForGesuch(UUID gesuchId) {
        unterschriftenblattAuthorizer.canGetUnterschriftenblaetter();
        return unterschriftenblattService.getForGesuchAndType(gesuchId);
    }

    @Blocking
    @Override
    @RolesAllowed(DOKUMENT_UPLOAD_GS)
    public Uni<Response> uploadCustomGesuchDokumentGS(UUID customDokumentTypId, FileUpload fileUpload) {
        customGesuchDokumentTypAuthorizer.assertGsCanModifyCustomDokumentOfTranche(customDokumentTypId);
        return gesuchDokumentService.getUploadCustomDokumentUni(customDokumentTypId, fileUpload);
    }

    @Blocking
    @Override
    @RolesAllowed(DOKUMENT_UPLOAD_SB)
    public Uni<Response> uploadCustomGesuchDokumentSB(UUID customDokumentTypId, FileUpload fileUpload) {
        customGesuchDokumentTypAuthorizer.assertSbCanModifyCustomDokumentOfTranche(customDokumentTypId);
        gesuchDokumentService.setGesuchDokumentOfCustomDokumentTypToAusstehend(customDokumentTypId);
        return gesuchDokumentService.getUploadCustomDokumentUni(customDokumentTypId, fileUpload);
    }

    @Blocking
    @Override
    @RolesAllowed(UNTERSCHRIFTENBLATT_DELETE)
    public void deleteUnterschriftenblattDokument(UUID dokumentId) {
        unterschriftenblattAuthorizer.canDeleteUnterschriftenblattDokument(dokumentId);
        unterschriftenblattService.removeDokument(dokumentId);
    }

    @Blocking
    @Override
    @RolesAllowed(CUSTOM_DOKUMENT_DELETE)
    public void deleteCustomDokumentTyp(UUID customDokumentTypId) {
        customGesuchDokumentTypAuthorizer.canDeleteTyp(customDokumentTypId);
        customDokumentTypService.deleteCustomDokumentTyp(customDokumentTypId);
    }

    @Blocking
    @Override
    @RolesAllowed(DOKUMENT_DELETE_GS)
    public void deleteDokumentGS(UUID dokumentId) {
        gesuchDokumentAuthorizer.assertGsCanDeleteDokumentOfTranche(dokumentId);
        gesuchDokumentService.removeDokument(dokumentId);
    }

    @Blocking
    @Override
    @RolesAllowed(DOKUMENT_DELETE_SB)
    public void deleteDokumentSB(UUID dokumentId) {
        gesuchDokumentAuthorizer.assertSbCanDeleteDokumentOfTranche(dokumentId);
        gesuchDokumentService.removeDokument(dokumentId);
    }

    @Override
    @RolesAllowed(DOKUMENT_ABLEHNEN_AKZEPTIEREN)
    public void gesuchDokumentAblehnen(
        UUID gesuchDokumentId,
        GesuchDokumentAblehnenRequestDto gesuchDokumentAblehnenRequestDto
    ) {
        gesuchDokumentAuthorizer.canUpdateGesuchDokument(gesuchDokumentId);
        gesuchDokumentService.gesuchDokumentAblehnen(gesuchDokumentId, gesuchDokumentAblehnenRequestDto);
        gesuchTrancheOverrideDokumentService
            .jahresfeldGesuchDokumentAblehnen(gesuchDokumentId, gesuchDokumentAblehnenRequestDto);
    }

    @Override
    @RolesAllowed(DOKUMENT_ABLEHNEN_AKZEPTIEREN)
    public void gesuchDokumentAkzeptieren(UUID gesuchDokumentId) {
        gesuchDokumentAuthorizer.canUpdateGesuchDokument(gesuchDokumentId);
        gesuchDokumentService.gesuchDokumentAkzeptieren(gesuchDokumentId);
        gesuchTrancheOverrideDokumentService.jahresfeldGesuchDokumentAkzeptieren(gesuchDokumentId);
    }

    @Override
    @RolesAllowed(CUSTOM_DOKUMENT_READ)
    public NullableGesuchDokumentDto getCustomGesuchDokumentForTypGS(UUID customDokumentTypId) {
        customGesuchDokumentTypAuthorizer.canReadCustomDokumentOfTyp(customDokumentTypId);
        return gesuchDokumentService.findGesuchDokumentForCustomTypGS(customDokumentTypId);
    }

    @Override
    @RolesAllowed(CUSTOM_DOKUMENT_READ)
    public NullableGesuchDokumentDto getCustomGesuchDokumentForTypSB(UUID customDokumentTypId) {
        customGesuchDokumentTypAuthorizer.canReadCustomDokumentOfTyp(customDokumentTypId);
        return gesuchDokumentService.findGesuchDokumentForCustomTypSB(customDokumentTypId);
    }

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> getDokument(String token, DokumentArt dokumentArt) {
        final var dokumentId = dokumentDownloadService.getClaimId(
            jwtParser,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.DOKUMENT_ID_CLAIM
        );
        return switch (dokumentArt) {
            case GESUCH_DOKUMENT, CUSTOM_DOKUMENT -> gesuchDokumentService.getDokument(dokumentId);
            case UNTERSCHRIFTENBLATT -> unterschriftenblattService.getDokument(dokumentId);
            case BESCHWERDE_ENTSCHEID -> beschwerdeEntscheidService.getDokument(dokumentId);
        };
    }

    @Override
    @RolesAllowed({ CUSTOM_DOKUMENT_READ, DOKUMENT_READ, UNTERSCHRIFTENBLATT_READ })
    public FileDownloadTokenDto getDokumentDownloadToken(UUID dokumentId) {
        dokumentAuthorizer.canGetDokumentDownloadToken(dokumentId);
        gesuchDokumentService.checkIfDokumentExists(dokumentId);

        return dokumentDownloadService.getFileDownloadToken(
            dokumentId,
            DokumentDownloadConstants.DOKUMENT_ID_CLAIM,
            benutzerService,
            configService
        );
    }

    @Override
    @RolesAllowed(DOKUMENT_READ)
    public List<GesuchDokumentKommentarDto> getGesuchDokumentKommentareGS(UUID gesuchDokumentId) {
        gesuchDokumentAuthorizer.canGetGesuchDokumentKommentar(gesuchDokumentId);
        return gesuchDokumentKommentarService.getAllKommentareForGesuchDokumentGS(gesuchDokumentId);
    }

    @Override
    @RolesAllowed(DOKUMENT_READ)
    public List<GesuchDokumentKommentarDto> getGesuchDokumentKommentareSB(UUID gesuchDokumentId) {
        gesuchDokumentAuthorizer.canGetGesuchDokumentKommentar(gesuchDokumentId);
        return gesuchDokumentKommentarService.getAllKommentareForGesuchDokumentSB(gesuchDokumentId);
    }

    @Override
    @RolesAllowed(DOKUMENT_READ)
    public NullableGesuchDokumentDto getGesuchDokumentForTypGS(DokumentTyp dokumentTyp, UUID gesuchTrancheId) {
        gesuchDokumentAuthorizer.canGetGesuchDokumentForTrancheGS(gesuchTrancheId);
        return gesuchDokumentService.findGesuchDokumentForTypGS(gesuchTrancheId, dokumentTyp);
    }

    @Override
    @RolesAllowed(DOKUMENT_READ)
    public NullableGesuchDokumentDto getGesuchDokumentForTypSB(DokumentTyp dokumentTyp, UUID gesuchTrancheId) {
        gesuchDokumentAuthorizer.canGetGesuchDokumentForTrancheSB(gesuchTrancheId);
        return gesuchDokumentService.findGesuchDokumentForTypSB(gesuchTrancheId, dokumentTyp);
    }
}
