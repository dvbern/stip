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

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.common.authorization.UnterschriftenblattAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentArt;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.generated.api.DokumentResource;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_ADMIN;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.GESUCH_UPDATE;

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
    private final UnterschriftenblattAuthorizer unterschriftenblattAuthorizer;

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    @AllowAll
    @Blocking
    public Uni<Response> createDokument(DokumentTyp dokumentTyp, UUID gesuchTrancheId, FileUpload fileUpload) {
        return gesuchDokumentService.getUploadDokumentUni(dokumentTyp, gesuchTrancheId, fileUpload);
    }

    @RolesAllowed(GESUCH_UPDATE)
    @Override
    @Blocking
    public Uni<Response> createUnterschriftenblatt(
        UnterschriftenblattDokumentTyp unterschriftenblattTyp,
        UUID gesuchId,
        FileUpload fileUpload
    ) {
        unterschriftenblattAuthorizer.canUpload(gesuchId);
        return unterschriftenblattService.getUploadUnterschriftenblattUni(unterschriftenblattTyp, gesuchId, fileUpload);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    @AllowAll
    public List<UnterschriftenblattDokumentDto> getUnterschriftenblaetterForGesuch(UUID gesuchId) {
        return unterschriftenblattService.getForGesuchAndType(gesuchId);
    }

    @RolesAllowed(GESUCH_DELETE)
    @Override
    @AllowAll
    @Blocking
    public void deleteUnterschriftenblattDokument(UUID dokumentId) {
        unterschriftenblattService.removeDokument(dokumentId);
    }

    @RolesAllowed(GESUCH_DELETE)
    @Override
    @AllowAll
    @Blocking
    public void deleteDokument(UUID dokumentId) {
        gesuchDokumentService.removeDokument(dokumentId);
    }

    @RolesAllowed({ ROLE_SACHBEARBEITER, ROLE_ADMIN })
    @Override
    @AllowAll
    public void gesuchDokumentAblehnen(
        UUID gesuchDokumentId,
        GesuchDokumentAblehnenRequestDto gesuchDokumentAblehnenRequestDto
    ) {
        gesuchDokumentService.gesuchDokumentAblehnen(gesuchDokumentId, gesuchDokumentAblehnenRequestDto);
    }

    @RolesAllowed({ ROLE_SACHBEARBEITER, ROLE_ADMIN })
    @Override
    @AllowAll
    public void gesuchDokumentAkzeptieren(UUID gesuchDokumentId) {
        gesuchDokumentService.gesuchDokumentAkzeptieren(gesuchDokumentId);
    }

    @Override
    @AllowAll
    @Blocking
    public RestMulti<Buffer> getDokument(String token, DokumentArt dokumentArt) {
        final var dokumentId = DokumentDownloadUtil.getDokumentId(jwtParser, token, configService.getSecret());

        return switch (dokumentArt) {
            case GESUCH_DOKUMENT -> gesuchDokumentService.getDokument(dokumentId);
            case UNTERSCHRIFTENBLATT -> unterschriftenblattService.getDokument(dokumentId);
        };
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    @AllowAll
    public FileDownloadTokenDto getDokumentDownloadToken(UUID dokumentId) {
        gesuchDokumentService.checkIfDokumentExists(dokumentId);

        return new FileDownloadTokenDto()
            .token(
                Jwt
                    .claims()
                    .upn(benutzerService.getCurrentBenutzername())
                    .claim(DokumentDownloadConstants.DOKUMENT_ID_CLAIM, dokumentId.toString())
                    .expiresIn(Duration.ofMinutes(configService.getExpiresInMinutes()))
                    .issuer(configService.getIssuer())
                    .jws()
                    .signWithSecret(configService.getSecret())
            );
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    @AllowAll
    public List<GesuchDokumentKommentarDto> getGesuchDokumentKommentare(DokumentTyp dokumentTyp, UUID gesuchId) {
        return gesuchDokumentService.getGesuchDokumentKommentarsByGesuchDokumentId(gesuchId, dokumentTyp);
    }

    @RolesAllowed(GESUCH_READ)
    @Override
    @AllowAll
    public NullableGesuchDokumentDto getGesuchDokumenteForTyp(DokumentTyp dokumentTyp, UUID gesuchTrancheId) {
        return gesuchDokumentService.findGesuchDokumentForTyp(gesuchTrancheId, dokumentTyp);
    }
}
