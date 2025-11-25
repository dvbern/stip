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

package ch.dvbern.stip.api;

import java.util.List;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDeleteService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentstatusService;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@Mock
@RequestScoped
public class GesuchDokumentServiceMock extends GesuchDokumentService {
    GesuchDokumentServiceMock() {
        super(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Inject
    public GesuchDokumentServiceMock(
    GesuchDokumentMapper gesuchDokumentMapper,
    DokumentRepository dokumentRepository,
    GesuchDokumentRepository gesuchDokumentRepository,
    CustomDokumentTypRepository customDocumentTypRepository,
    GesuchRepository gesuchRepository,
    GesuchTrancheRepository gesuchTrancheRepository,
    S3AsyncClient s3,
    ConfigService configService,
    GesuchDokumentstatusService gesuchDokumentstatusService,
    RequiredDokumentService requiredDokumentService,
    Antivirus antivirus,
    GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository,
    DokumentHistoryRepository dokumentHistoryRepository,
    GesuchTrancheHistoryService gesuchTrancheHistoryService,
    DokumentUploadService dokumentUploadService,
    DokumentDownloadService dokumentDownloadService,
    DokumentDeleteService dokumentDeleteService
    ) {
        super(
            gesuchDokumentMapper,
            dokumentRepository,
            gesuchDokumentRepository,
            customDocumentTypRepository,
            gesuchRepository,
            gesuchTrancheRepository,
            s3,
            configService,
            gesuchDokumentstatusService,
            requiredDokumentService,
            antivirus,
            gesuchDokumentKommentarRepository,
            dokumentHistoryRepository,
            gesuchTrancheHistoryService,
            dokumentUploadService,
            dokumentDownloadService,
            dokumentDeleteService
        );
    }

    @Override
    public boolean canDeleteDokumentFromS3(final Dokument dokument, final GesuchTranche trancheToBeDeletedFrom) {
        return false;
    }

    @Override
    public void executeDeleteDokumentsFromS3(final List<String> objectIds) {}
}
