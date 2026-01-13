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

package ch.dvbern.stip.api.demo.service;

import java.util.UUID;

import ch.dvbern.stip.api.common.exception.DemoDataImportException;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.demo.entity.DemoDataImport;
import ch.dvbern.stip.api.demo.repo.DemoDataImportRepository;
import ch.dvbern.stip.api.demo.repo.DemoDataRepository;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class DemoDataService {
    private final DemoDataRepository demoDataRepository;
    private final DemoDataImportRepository demoDataImportRepository;
    private final DemoDataMapper demoDataMapper;
    private final ParseDemoDataService parseDemoDataService;
    private final DokumentUploadService dokumentUploadService;
    private final DokumentDownloadService dokumentDownloadService;
    private final DokumentRepository dokumentRepository;
    private final Antivirus antivirus;
    private final ConfigService configService;
    private final S3AsyncClient s3;
    public static final String DEMODATA_DOKUMENT_PATH = "demo_data/";

    @Transactional
    public DemoDataListDto createNewDemoDataImport(
        final String kommentar,
        final FileUpload fileUpload
    ) {
        var demoDataImport = new DemoDataImport();
        demoDataImport.setKommentar(kommentar);

        try {
            dokumentUploadService.validateScanUploadDokument(
                fileUpload,
                s3,
                configService,
                antivirus,
                DEMODATA_DOKUMENT_PATH,
                objectId -> demoDataImport.setDokument(
                    uploadDokument(
                        fileUpload,
                        objectId
                    )
                ),
                throwable -> LOG.error(throwable.getMessage())
            )
                .await()
                .indefinitely();
            final var demoDataList = parseDemoDataService.parseList(fileUpload.uploadedFile());

            return demoDataMapper.toDto(demoDataImport, demoDataList);
        } catch (Exception e) {
            throw new DemoDataImportException(e);
        }
    }

    @Transactional
    public DemoDataListDto getAllDemoData() {
        final var demoDataList = demoDataRepository.findAll();
        final var latestDemoDataImport = demoDataImportRepository.getLatest();

        return latestDemoDataImport
            .map(demoDataImport -> demoDataMapper.toDto(demoDataImport, demoDataList.stream().toList()))
            .orElse(null);
    }

    @Transactional
    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);
        return dokumentDownloadService.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            DEMODATA_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    private Dokument uploadDokument(
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var dokument = new Dokument()
            .setFilename(fileUpload.fileName())
            .setFilesize(String.valueOf(fileUpload.size()))
            .setFilepath(DEMODATA_DOKUMENT_PATH)
            .setObjectId(objectId);

        dokumentRepository.persist(dokument);
        return dokument;
    }
}
