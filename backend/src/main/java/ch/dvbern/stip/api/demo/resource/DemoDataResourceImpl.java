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

package ch.dvbern.stip.api.demo.resource;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.DemoDataAuthorizer;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.common.util.OidcPermissions;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.demo.service.DemoDataService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.generated.api.DemoDataResource;
import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Validated
@RequestScoped
@RequiredArgsConstructor
public class DemoDataResourceImpl implements DemoDataResource {
    private final DemoDataAuthorizer demoDataAuthorizer;
    private final DemoDataService demoDataService;
    private final BenutzerService benutzerService;
    private final ConfigService configService;
    private final JWTParser jwtParser;
    private final DokumentDownloadService dokumentDownloadService;

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public ApplyDemoDataResponseDto applyDemoData(UUID demoDataId) {
        demoDataAuthorizer.canRead();
        return demoDataService.applyDemoData(demoDataId);
    }

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public DemoDataListDto createNewDemoDataImport(String kommentar, FileUpload fileUpload) {
        demoDataAuthorizer.canCreateDemoDataList();
        return demoDataService.createNewDemoDataImport(
            kommentar,
            fileUpload
        );
    }

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public DemoDataListDto getAllDemoData() {
        demoDataAuthorizer.canRead();
        return demoDataService.getAllDemoData();
    }

    @Blocking
    @Override
    @PermitAll
    public RestMulti<Buffer> getDemoDataDokument(String token) {
        final var dokumentId = dokumentDownloadService.getClaimId(
            jwtParser,
            token,
            configService.getSecret(),
            DokumentDownloadConstants.DEMO_DATA_IMPORT_ID_CLAIM
        );
        return demoDataService.getDokument(dokumentId);
    }

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public FileDownloadTokenDto getDemoDataDokumentDownloadToken(UUID dokumentId) {
        demoDataAuthorizer.canRead();
        return dokumentDownloadService.getFileDownloadToken(
            dokumentId,
            DokumentDownloadConstants.DEMO_DATA_IMPORT_ID_CLAIM,
            benutzerService,
            configService
        );
    }
}
