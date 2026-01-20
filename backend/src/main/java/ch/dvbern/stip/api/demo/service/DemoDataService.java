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

import java.util.Comparator;
import java.util.UUID;

import ch.dvbern.stip.api.common.exception.DemoDataApplyException;
import ch.dvbern.stip.api.common.exception.DemoDataImportException;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.entity.DemoDataImport;
import ch.dvbern.stip.api.demo.repo.DemoDataImportRepository;
import ch.dvbern.stip.api.demo.repo.DemoDataRepository;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularService;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.zuordnung.service.ZuordnungService;
import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class DemoDataService {
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final Antivirus antivirus;
    private final Validator validator;
    private final DemoDataRepository demoDataRepository;
    private final DemoDataImportRepository demoDataImportRepository;
    private final DemoDataMapper demoDataMapper;
    private final ParseDemoDataService parseDemoDataService;
    private final DokumentUploadService dokumentUploadService;
    private final DokumentDownloadService dokumentDownloadService;
    private final GesuchRepository gesuchRepository;
    private final DokumentRepository dokumentRepository;
    private final GenerateDemoDataService generateDemoDataService;
    private final GesuchFormularService gesuchFormularService;
    private final ZuordnungService zuordnungService;
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
                configService.getTestcaseAllowedMimeTypes(),
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
                .indefinitely()
                .close();
            final var demoDataList = parseDemoDataService.parseList(fileUpload.uploadedFile());
            demoDataRepository.deleteAll();
            demoDataRepository.persist(demoDataList);
            demoDataImportRepository.persist(demoDataImport);

            return demoDataMapper.toDto(demoDataImport, demoDataList);
        } catch (Exception e) {
            LOG.error("Testcase Data Import Error", e);
            throw new DemoDataImportException(e);
        }
    }

    @Transactional
    public DemoDataListDto getAllDemoData() {
        final var demoDataList =
            demoDataRepository.findAll().stream().sorted(Comparator.comparing(DemoData::getTestFall));
        final var latestDemoDataImport = demoDataImportRepository.getLatest();

        return latestDemoDataImport
            .map(demoDataImport -> demoDataMapper.toDto(demoDataImport, demoDataList.toList()))
            .orElse(null);
    }

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

    @Transactional
    public ApplyDemoDataResponseDto applyDemoData(UUID demoDataId) {
        final var demoData = demoDataRepository.requireById(demoDataId);

        final var gesuchId = generateDemoDataService.createEingereicht(demoData.parseDemoDataDto());
        final var gesuch = gesuchRepository.requireById(gesuchId);

        final var preValidation =
            gesuchFormularService.validatePages(gesuch.getLatestGesuchTranche().getGesuchFormular());
        if (!preValidation.getValidationErrors().isEmpty()) {
            throw new DemoDataApplyException("ValidationError", preValidation.getValidationErrors());
        }

        generateDemoDataService.createDemoDokumentsForAllRequired(gesuch);
        zuordnungService.updateZuordnungOnGesuch(gesuch);

        final var violations =
            validator.validate(gesuchRepository.requireById(gesuchId), GesuchEinreichenValidationGroup.class);
        if (!violations.isEmpty()) {
            throw new ValidationsException(ValidationsException.ENTITY_NOT_VALID_MESSAGE, violations);
        }

        return demoDataMapper.toDto(gesuch);
    }
}
