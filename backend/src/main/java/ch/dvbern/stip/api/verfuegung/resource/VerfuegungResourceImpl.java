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

package ch.dvbern.stip.api.verfuegung.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.VerfuegungAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.generated.api.VerfuegungResource;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.VerfuegungDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;

import static ch.dvbern.stip.api.common.util.OidcPermissions.GS_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.JURIST_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
@Validated
public class VerfuegungResourceImpl implements VerfuegungResource {

    private final BenutzerService benutzerService;
    private final ConfigService configService;
    private final JWTParser jwtParser;
    private final VerfuegungService verfuegungService;
    private final VerfuegungAuthorizer verfuegungAuthorizer;

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> getVerfuegungDokument(String token) {
        final var verfuegungDokumentId = DokumentDownloadUtil.getClaimId(
            jwtParser,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.VERFUEGUN_DOKUMENT_ID_CLAIM
        );
        return verfuegungService.getVerfuegungDokument(verfuegungDokumentId);
    }

    @Override
    @RolesAllowed({ GS_GESUCH_READ, SB_GESUCH_READ, JURIST_GESUCH_READ })
    public FileDownloadTokenDto getVerfuegungDokumentDownloadToken(UUID verfuegungDokumentId) {
        verfuegungAuthorizer.canGetVerfuegungDownloadToken(verfuegungDokumentId);

        return DokumentDownloadUtil.getFileDownloadToken(
            verfuegungDokumentId,
            DokumentDownloadConstants.VERFUEGUN_DOKUMENT_ID_CLAIM,
            benutzerService,
            configService
        );
    }

    @Override
    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    public List<VerfuegungDto> getVerfuegungen(UUID gesuchId) {
        verfuegungAuthorizer.canGetVerfuegungen();

        return verfuegungService.getVerfuegungen(gesuchId);
    }
}
