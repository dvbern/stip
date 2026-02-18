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

package ch.dvbern.stip.api.ausbildung.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.service.AusbildungService;
import ch.dvbern.stip.api.ausbildung.service.AusbildungUnterbruchAntragService;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.api.common.authorization.AusbildungUnterbruchAntragAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.generated.api.AusbildungResource;
import ch.dvbern.stip.generated.dto.AusbildungCreateResponseDto;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragSBDto;
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

import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_CREATE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_DELETE;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.AUSBILDUNG_UPDATE;

@RequestScoped
@RequiredArgsConstructor
@Validated
public class AusbildungResourceImpl implements AusbildungResource {
    private final AusbildungService ausbildungService;
    private final AusbildungAuthorizer ausbildungAuthorizer;
    private final AusbildungUnterbruchAntragAuthorizer ausbildungUnterbruchAntragAuthorizer;
    private final AusbildungUnterbruchAntragService ausbildungUnterbruchAntragService;
    private final DokumentDownloadService dokumentDownloadService;
    private final JWTParser jwtParser;
    private final ConfigService configService;
    private final BenutzerService benutzerService;

    @Override
    @RolesAllowed(AUSBILDUNG_CREATE)
    public AusbildungCreateResponseDto createAusbildung(AusbildungUpdateDto ausbildungUpdateDto) {
        ausbildungAuthorizer.canCreate(ausbildungUpdateDto.getFallId());
        return ausbildungService.createAusbildung(ausbildungUpdateDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    public AusbildungDto getAusbildung(UUID ausbildungId) {
        ausbildungAuthorizer.canRead(ausbildungId);
        return ausbildungService.getAusbildungById(ausbildungId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    public AusbildungDto updateAusbildung(UUID ausbildungId, AusbildungUpdateDto ausbildungUpdateDto) {
        ausbildungAuthorizer.canUpdate(ausbildungId);
        return ausbildungService.patchAusbildung(ausbildungId, ausbildungUpdateDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_CREATE)
    public AusbildungUnterbruchAntragGSDto createAusbildungUnterbruchAntrag(UUID ausbildungId) {
        ausbildungUnterbruchAntragAuthorizer.gsCanCreate(ausbildungId);
        return ausbildungUnterbruchAntragService.createAusbildungUnterbruchAntrag(ausbildungId);
    }

    @Blocking
    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    public Uni<Response> createAusbildungUnterbruchAntragDokument(
        UUID ausbildungUnterbruchAntragId,
        FileUpload fileUpload
    ) {
        ausbildungUnterbruchAntragAuthorizer.gsCanUpdate(ausbildungUnterbruchAntragId);
        return ausbildungUnterbruchAntragService
            .uploadAusbildungUnterbruchAntragDokument(ausbildungUnterbruchAntragId, fileUpload);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_DELETE)
    public void deleteAusbildungUnterbruchAntrag(UUID ausbildungUnterbruchAntragId) {
        ausbildungUnterbruchAntragAuthorizer.gsCanUpdate(ausbildungUnterbruchAntragId);
        ausbildungUnterbruchAntragService.deleteAusbildungUnterbruchAntrag(ausbildungUnterbruchAntragId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_DELETE)
    public void deleteAusbildungUnterbruchAntragDokument(UUID dokumentId) {
        ausbildungUnterbruchAntragAuthorizer.gsCanDeleteDokument(dokumentId);
        ausbildungUnterbruchAntragService.deleteAusbildungUnterbruchAntragDokument(dokumentId);
    }

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> downloadAusbildungUnterbruchAntragDokument(String token) {
        final var dokumentId = dokumentDownloadService.getClaimId(
            jwtParser,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_ID_CLAIM
        );
        return ausbildungUnterbruchAntragService.getDokument(dokumentId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    public AusbildungUnterbruchAntragGSDto einreichenAusbildungUnterbruchAntrag(
        UUID ausbildungUnterbruchAntragId,
        UpdateAusbildungUnterbruchAntragGSDto updateAusbildungUnterbruchAntragGSDto
    ) {
        ausbildungUnterbruchAntragAuthorizer.gsCanUpdate(ausbildungUnterbruchAntragId);
        return ausbildungUnterbruchAntragService
            .einreichenAusbildungUnterbruchAntrag(ausbildungUnterbruchAntragId, updateAusbildungUnterbruchAntragGSDto);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    public FileDownloadTokenDto getAusbildungUnterbruchAntragDokumentDownloadToken(UUID dokumentId) {
        ausbildungUnterbruchAntragAuthorizer.canReadDokument(dokumentId);
        return dokumentDownloadService.getFileDownloadToken(
            dokumentId,
            DokumentDownloadConstants.AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_ID_CLAIM,
            benutzerService,
            configService
        );
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    public AusbildungUnterbruchAntragGSDto getAusbildungUnterbruchAntragGS(UUID ausbildungUnterbruchAntragId) {
        ausbildungUnterbruchAntragAuthorizer.gsCanRead(ausbildungUnterbruchAntragId);
        return ausbildungUnterbruchAntragService.getAusbildungUnterbruchAntrag(ausbildungUnterbruchAntragId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_READ)
    public List<AusbildungUnterbruchAntragSBDto> getAusbildungUnterbruchAntragsByGesuchId(UUID gesuchId) {
        ausbildungUnterbruchAntragAuthorizer.sbCanRead();
        return ausbildungUnterbruchAntragService.getAusbildungUnterbruchAntragsByGesuchId(gesuchId);
    }

    @Override
    @RolesAllowed(AUSBILDUNG_UPDATE)
    public AusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSB(
        UUID ausbildungUnterbruchAntragId,
        UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSBDto
    ) {
        ausbildungUnterbruchAntragAuthorizer.sbCanWrite(ausbildungUnterbruchAntragId);
        return ausbildungUnterbruchAntragService
            .updateAusbildungUnterbruchAntrag(ausbildungUnterbruchAntragId, updateAusbildungUnterbruchAntragSBDto);
    }
}
