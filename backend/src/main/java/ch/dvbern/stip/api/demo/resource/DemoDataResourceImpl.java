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

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.DemoDataAuthorizer;
import ch.dvbern.stip.api.common.interceptors.PopulateCurrentBenutzerContext;
import ch.dvbern.stip.api.common.interceptors.Validated;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.common.util.OidcPermissions;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.demo.service.DemoDataService;
import ch.dvbern.stip.api.demo.service.GenerateMultipleDemoDataService;
import ch.dvbern.stip.api.demo.type.DemoDataDefaults;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.generated.api.DemoDataResource;
import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungResultatDto;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Multi;
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
@PopulateCurrentBenutzerContext
public class DemoDataResourceImpl implements DemoDataResource {
    private final DemoDataAuthorizer demoDataAuthorizer;
    private final DemoDataService demoDataService;
    private final GenerateMultipleDemoDataService generateMultipleDemoDataService;
    private final BenutzerService benutzerService;
    private final StipConfig config;
    private final JWTParser jwtParser;
    private final DokumentDownloadService dokumentDownloadService;

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public ApplyDemoDataResponseDto applyDemoData(UUID demoDataId) {
        demoDataAuthorizer.canGenerate();
        return demoDataService.applyDemoData(demoDataId);
    }

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public DemoDataListDto createNewDemoDataImport(
        String kommentar,
        Boolean ignoreBerechnungErrors,
        FileUpload fileUpload
    ) {
        demoDataAuthorizer.canCreateDemoDataList();
        return demoDataService.createNewDemoDataImport(
            kommentar,
            fileUpload,
            ignoreBerechnungErrors
        );
    }

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public void generateAllGesucheAsVerfuegt() {
        demoDataAuthorizer.canGenerate();
        generateMultipleDemoDataService.generateAllGesucheAsVerfuegt(false, DemoDataDefaults.MASS_GESUCH_FALL_PREFIX);
    }

    @Blocking
    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public Multi<Buffer> getStatistikXmlWithAllTestcases() {
        demoDataAuthorizer.canGenerate();
        return generateMultipleDemoDataService.generateStatistikXmlWithAllTestcases();
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
            config.preSignedRequest().secret(),
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
            config
        );
    }

    @Override
    @RolesAllowed(OidcPermissions.DEMO_DATA_APPLY)
    public List<DemoDataTestBerechnungResultatDto> testAllDemoDataBerechnung() {
        demoDataAuthorizer.canRead();
        return demoDataService.testAllDemoDataBerechnung();
    }
}
