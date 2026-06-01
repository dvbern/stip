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

package ch.dvbern.stip.api.datenschutzbrief.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.interceptors.PopulateCurrentBenutzerContext;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.datenschutzbrief.auth.DatenschutzbriefAuthorizer;
import ch.dvbern.stip.api.datenschutzbrief.service.DatenschutzbriefService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.generated.api.DatenschutzbriefResource;
import ch.dvbern.stip.generated.dto.DatenschutzbriefCreateDto;
import ch.dvbern.stip.generated.dto.DatenschutzbriefOverviewDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestMulti;

import static ch.dvbern.stip.api.common.util.OidcPermissions.JURIST_GESUCH_READ;
import static ch.dvbern.stip.api.common.util.OidcPermissions.SB_GESUCH_READ;

@Validated
@RequestScoped
@RequiredArgsConstructor
@PopulateCurrentBenutzerContext
public class DatenschutzbriefRessourceImpl implements DatenschutzbriefResource {
    private final DatenschutzbriefService datenschutzbriefService;
    private final BenutzerService benutzerService;
    private final ConfigService configService;
    private final JWTParser jwtParser;
    private final DatenschutzbriefAuthorizer authorizer;
    private final DokumentDownloadService dokumentDownloadService;

    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    @Override
    public List<DatenschutzbriefOverviewDto> getAllDatenschutzbriefs(UUID gesuchId) {
        authorizer.canGetDatenschutzbriefs();
        return datenschutzbriefService.getDatenschutzbriefs(gesuchId);
    }

    @Blocking
    @PermitAll
    @Override
    public RestMulti<Buffer> getDatenschutzbrief(final String token) {
        final var datenschutzbriefId = dokumentDownloadService.getClaimId(
            jwtParser,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.DOKUMENT_ID_CLAIM
        );

        return datenschutzbriefService.getDatenschutzbriefDokument(datenschutzbriefId);
    }

    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    @Override
    public FileDownloadTokenDto getDatenschutzbriefDownloadToken(final UUID gesuchId, final UUID datenschutzbriefId) {
        authorizer.canGetDokumentDownloadToken();
        return dokumentDownloadService.getFileDownloadToken(
            datenschutzbriefId,
            DokumentDownloadConstants.DOKUMENT_ID_CLAIM,
            benutzerService,
            configService
        );
    }

    @RolesAllowed({ SB_GESUCH_READ, JURIST_GESUCH_READ })
    @Override
    public FileDownloadTokenDto createAndGetDatenschutzbriefDownloadToken(
        UUID gesuchId,
        DatenschutzbriefCreateDto datenschutzbriefCreateDto
    ) {
        authorizer.canGetDokumentDownloadToken();
        final var datenschutzbriefId =
            datenschutzbriefService.createDatenschutzbrief(gesuchId, datenschutzbriefCreateDto.getElternId());
        return dokumentDownloadService.getFileDownloadToken(
            datenschutzbriefId,
            DokumentDownloadConstants.DOKUMENT_ID_CLAIM,
            benutzerService,
            configService
        );
    }
}
