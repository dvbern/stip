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

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.exception.DemoDataApplyException;
import ch.dvbern.stip.api.common.exception.DemoDataImportException;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.service.seeding.GesuchsperiodeSeeding;
import ch.dvbern.stip.api.common.service.seeding.GesuchsperiodeSeeding.Season;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.entity.DemoDataImport;
import ch.dvbern.stip.api.demo.repo.DemoDataImportRepository;
import ch.dvbern.stip.api.demo.repo.DemoDataRepository;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularService;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchsjahr.repo.GesuchsjahrRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.zuordnung.service.ZuordnungService;
import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungResultatDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class DemoDataService {
    private final S3AsyncClient s3;
    private final StipConfig config;
    private final Antivirus antivirus;
    private final Validator validator;
    private final DemoDataRepository demoDataRepository;
    private final DemoDataImportRepository demoDataImportRepository;
    private final DemoDataMapper demoDataMapper;
    private final DokumentUploadService dokumentUploadService;
    private final DokumentDownloadService dokumentDownloadService;
    private final GesuchRepository gesuchRepository;
    private final DokumentRepository dokumentRepository;
    private final GesuchsperiodeRepository gesuchsperiodeRepository;
    private final GesuchsjahrRepository gesuchsjahrRepository;
    private final GenerateDemoDataService generateDemoDataService;
    private final GesuchFormularService gesuchFormularService;
    private final ZuordnungService zuordnungService;
    private final TransactionManager transactionManager;
    public static final String DEMODATA_DOKUMENT_PATH = "demo_data/";
    public static final int DEMODATA_TEST_ALL_YEAR = 2026;

    @Transactional
    public DemoDataListDto createNewDemoDataImport(
        final String kommentar,
        final FileUpload fileUpload,
        final Boolean ignoreBerechnungErrors
    ) {
        var demoDataImport = new DemoDataImport();
        demoDataImport.setKommentar(kommentar);

        try {
            dokumentUploadService.validateScanUploadDokument(
                fileUpload,
                s3,
                config,
                antivirus,
                config.upload().allowedTestcaseMimetypes(),
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
            final var demoDataList = ParseDemoDataService.parseList(fileUpload.uploadedFile(), ignoreBerechnungErrors);
            demoDataRepository.deleteAll();
            demoDataRepository.persist(demoDataList);
            demoDataImportRepository.persist(demoDataImport);

            return demoDataMapper.toDto(demoDataImport, demoDataList);
        } catch (Exception e) {
            LOG.error("Testcase Data Import Error", e);
            throw new DemoDataImportException(e);
        }
    }

    @SneakyThrows
    public List<DemoDataTestBerechnungResultatDto> testAllDemoDataBerechnung() {
        transactionManager.begin();
        final var demoDataList =
            demoDataRepository.findAll().stream().sorted(Comparator.comparing(DemoData::getTestFall));
        final var gesuchsperiode = createTestGesuchsperiode();
        final var resultatList = demoDataList.map(demoData -> {
            try {
                final var gesuch =
                    generateDemoDataService.createEinreichableGesuch(demoData, new Fall(), Optional.of(gesuchsperiode));
                return generateDemoDataService.getBerechnungResultatDto(gesuch, demoData);
            } catch (Exception e) {
                return new DemoDataTestBerechnungResultatDto()
                    .message(e.getMessage())
                    .testFall(demoData.getTestFall())
                    .demoDataId(demoData.getId());
            }
        }
        ).toList();
        transactionManager.rollback();
        return resultatList;
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
            config.s3().bucketName(),
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

        UUID gesuchId;
        try {
            gesuchId = generateDemoDataService.createAndPersistEinreichableGesuch(demoData);
        } catch (NullPointerException e) {
            LOG.error("DemoDataApplyError", e);
            throw new DemoDataApplyException("A required value was not set", e);
        }

        final var gesuch = gesuchRepository.requireById(gesuchId);

        final var preValidation =
            gesuchFormularService.validatePagesSb(gesuch.getLatestGesuchTranche().getGesuchFormular());
        if (!preValidation.getValidationErrors().isEmpty()) {
            throw new DemoDataApplyException("ValidationError", preValidation.getValidationErrors());
        }

        final var allDokuments =
            generateDemoDataService.createDemoDokumentsForAllRequired(gesuch.getLatestGesuchTranche());
        generateDemoDataService.createS3EntriesForDokumente(allDokuments);
        zuordnungService.updateZuordnungOnGesuch(gesuch);

        final var violations =
            validator.validate(gesuchRepository.requireById(gesuchId), GesuchEinreichenValidationGroup.class);
        if (!violations.isEmpty()) {
            throw new ValidationsException(ValidationsException.ENTITY_NOT_VALID_MESSAGE, violations);
        }

        DemoDataTestBerechnungResultatDto stipendienanspruchDto;
        try {
            stipendienanspruchDto = generateDemoDataService.getBerechnungResultatDto(gesuch, demoData);
        } catch (Exception e) {
            stipendienanspruchDto = new DemoDataTestBerechnungResultatDto()
                .demoDataId(demoData.getId())
                .testFall(demoData.getTestFall())
                .message(e.getMessage());
            LOG.error("Unable to calculate berechnung for{}\n{}", demoData.getTestFall(), e.getMessage());
        }

        return demoDataMapper.toDto(gesuch, stipendienanspruchDto);
    }

    private Gesuchsperiode createTestGesuchsperiode() {
        final GesuchsperiodeSeeding gesuchsperiodeSeeding = new GesuchsperiodeSeeding(
            null, null, null
        );
        final var gesuchsjahr = gesuchsperiodeSeeding.getJahrForSeeding(DEMODATA_TEST_ALL_YEAR);
        final var gesuchsperiode = gesuchsperiodeSeeding.getPeriodeForSeeding(
            "Frühling",
            "Printemps",
            gesuchsjahr,
            Season.FALL,
            GueltigkeitStatus.PUBLIZIERT,
            LocalDate.of(DEMODATA_TEST_ALL_YEAR, 7, 1),
            LocalDate.of(DEMODATA_TEST_ALL_YEAR + 1, 6, 30),
            LocalDate.of(DEMODATA_TEST_ALL_YEAR, 7, 15),
            LocalDate.of(DEMODATA_TEST_ALL_YEAR, 12, 31),
            LocalDate.of(DEMODATA_TEST_ALL_YEAR + 1, 3, 31),
            LocalDate.of(DEMODATA_TEST_ALL_YEAR, 12, 31)
        );
        gesuchsjahrRepository.persist(gesuchsjahr);
        gesuchsperiodeRepository.persist(gesuchsperiode);
        return gesuchsperiode;
    }
}
