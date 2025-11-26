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

package ch.dvbern.stip.api.verfuegung.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.pdf.service.VerfuegungPdfService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.api.verfuegung.repo.VerfuegungDokumentRepository;
import ch.dvbern.stip.api.verfuegung.repo.VerfuegungRepository;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.generated.dto.VerfuegungDto;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import ch.dvbern.stip.stipdecision.type.Kanton;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class VerfuegungService {

    static final String VERFUEGUNG_DOKUMENT_PATH = "verfuegung/";
    static final String FILENAME_PREFIX_BERECHNUNGSBLATT_PIA = "Berechnungsblatt_%s_PIA_%s%s";
    static final String FILENAME_PREFIX_BERECHNUNGSBLATT_MUTTER = "Berechnungsblatt_%s_Mutter_%s%s";
    static final String FILENAME_PREFIX_BERECHNUNGSBLATT_VATER = "Berechnungsblatt_%s_Vater_%s%s";
    static final String FILENAME_PREFIX_BERECHNUNGSBLATT_FAMILIE = "Berechnungsblatt_%s_Familie_%s%s";
    static final String FILENAME_PREFIX_VERFUEGUNG = "Verfügung_%s_%s%s";
    static final String FILENAME_PREFIX_VERFUEGUNGSBRIEF = "Verfügungsbrief_%s_%s%s";
    static final String FILENAME_EXTENSION_PDF = ".pdf";

    private final VerfuegungRepository verfuegungRepository;
    private final GesuchRepository gesuchRepository;
    private final StipDecisionTextRepository stipDecisionTextRepository;
    private final VerfuegungMapper verfuegungMapper;
    private final VerfuegungPdfService verfuegungPdfService;
    private final Antivirus antivirus;
    private final ConfigService configService;
    private final S3AsyncClient s3;
    private final VerfuegungDokumentRepository verfuegungDokumentRepository;
    private final DokumentUploadService dokumentUploadService;
    private final DokumentDownloadService dokumentDownloadService;

    public List<VerfuegungDto> getVerfuegungen(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return gesuch.getVerfuegungs()
            .stream()
            .map(verfuegungMapper::toDto)
            .toList();
    }

    @Transactional
    public void createVerfuegung(final UUID gesuchId) {
        final Verfuegung verfuegung = new Verfuegung();
        verfuegung.setGesuch(gesuchRepository.requireById(gesuchId));
        verfuegungRepository.persistAndFlush(verfuegung);
    }

    @Transactional
    public void createNegativeVerfuegungManuell(final UUID gesuchId, final FileUpload fileUpload) {
        final var response = dokumentUploadService.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            VERFUEGUNG_DOKUMENT_PATH,
            objectId -> {
                final var verfuegung = new Verfuegung();
                verfuegung.setGesuch(gesuchRepository.requireById(gesuchId));
                verfuegung.setNegativeVerfuegung(true);

                final var verfuegungsDokument = new VerfuegungDokument();
                verfuegungsDokument.setVerfuegung(verfuegung);
                verfuegungsDokument.setTyp(VerfuegungDokumentTyp.VERFUEGUNGSBRIEF);
                verfuegungsDokument.setObjectId(objectId);
                verfuegungsDokument.setFilename(fileUpload.fileName());
                verfuegungsDokument.setFilepath(VERFUEGUNG_DOKUMENT_PATH);
                verfuegungsDokument.setFilesize(String.valueOf(fileUpload.size()));
                verfuegung.getDokumente().add(verfuegungsDokument);

                verfuegungRepository.persistAndFlush(verfuegung);
                verfuegungDokumentRepository.persistAndFlush(verfuegungsDokument);

            }
        );
        response.await().indefinitely();
    }

    @Transactional
    public void createPdfForNegtativeVerfuegung(final Verfuegung verfuegung) {
        final ByteArrayOutputStream out = verfuegungPdfService.createNegativeVerfuegungPdf(verfuegung);

        createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERFUEGUNGSBRIEF,
            out
        );
    }

    @Transactional
    public void createPdfForVerfuegungMitAnspruch(final Verfuegung verfuegung) {
        final ByteArrayOutputStream out = verfuegungPdfService.createVerfuegungMitAnspruchPdf(verfuegung);

        createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERFUEGUNGSBRIEF,
            out
        );
    }

    @Transactional
    public void createPdfForVerfuegungOhneAnspruch(final Verfuegung verfuegung) {
        final ByteArrayOutputStream out = verfuegungPdfService.createVerfuegungOhneAnspruchPdf(verfuegung);

        createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERFUEGUNGSBRIEF,
            out
        );
    }

    @Transactional
    public void createNegativeVerfuegungWithDecision(
        final UUID gesuchId,
        final UUID stipDecisionId,
        final Optional<Kanton> kanton
    ) {
        final var stipDecision = stipDecisionTextRepository.requireById(stipDecisionId);

        final Verfuegung verfuegung = new Verfuegung();
        verfuegung.setStipDecision(stipDecision.getStipDecision());
        verfuegung.setGesuch(gesuchRepository.requireById(gesuchId));
        verfuegung.setKanton(kanton.orElse(null));
        verfuegung.setNegativeVerfuegung(true);
        verfuegungRepository.persistAndFlush(verfuegung);
    }

    public Verfuegung getLatestVerfuegung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return gesuch.getVerfuegungs()
            .stream()
            .max(Comparator.comparing(Verfuegung::getTimestampErstellt))
            .orElseThrow();
    }

    public RestMulti<Buffer> getVerfuegungDokument(UUID verfuegungDokumentId) {
        final var verfuegungDokument = verfuegungDokumentRepository.requireById(verfuegungDokumentId);

        return dokumentDownloadService.getDokument(
            s3,
            configService.getBucketName(),
            verfuegungDokument.getObjectId(),
            verfuegungDokument.getFilepath(),
            verfuegungDokument.getFilename()
        );
    }

    @Transactional
    public void createAndStoreVerfuegungDokument(
        final Verfuegung verfuegung,
        final VerfuegungDokumentTyp dokumentTyp,
        final ByteArrayOutputStream pdfContent
    ) {
        final String filename =
            generateFilename(dokumentTyp, verfuegung.getGesuch().getAusbildung().getFall().getFallNummer());
        final String objectId = dokumentUploadService.executeUploadDocument(
            pdfContent.toByteArray(),
            filename,
            s3,
            configService,
            VERFUEGUNG_DOKUMENT_PATH
        );

        final VerfuegungDokument dokument = new VerfuegungDokument();
        dokument.setVerfuegung(verfuegung);
        dokument.setTyp(dokumentTyp);
        dokument.setObjectId(objectId);
        dokument.setFilename(filename);
        dokument.setFilepath(VERFUEGUNG_DOKUMENT_PATH);
        dokument.setFilesize(String.valueOf(pdfContent.size()));

        verfuegung.getDokumente().add(dokument);
        verfuegungDokumentRepository.persistAndFlush(dokument);
    }

    public VerfuegungDokument getBerechnungsblattByType(
        final Verfuegung verfuegung,
        final VerfuegungDokumentTyp typ
    ) {
        return verfuegung.getDokumente()
            .stream()
            .filter(d -> d.getTyp() == typ)
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Berechnungsblatt not found: " + typ));
    }

    private String generateFilename(VerfuegungDokumentTyp typ, String fallNr) {
        final String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // todo KSTIP-2845: also use correct format for darlehensverfuegung
        return switch (typ) {
            case BERECHNUNGSBLATT_PIA -> String
                .format(FILENAME_PREFIX_BERECHNUNGSBLATT_PIA, fallNr, formattedDate, FILENAME_EXTENSION_PDF);
            case BERECHNUNGSBLATT_MUTTER -> String
                .format(FILENAME_PREFIX_BERECHNUNGSBLATT_MUTTER, fallNr, formattedDate, FILENAME_EXTENSION_PDF);
            case BERECHNUNGSBLATT_VATER -> String
                .format(FILENAME_PREFIX_BERECHNUNGSBLATT_VATER, fallNr, formattedDate, FILENAME_EXTENSION_PDF);
            case BERECHNUNGSBLATT_FAMILIE -> String
                .format(FILENAME_PREFIX_BERECHNUNGSBLATT_FAMILIE, fallNr, formattedDate, FILENAME_EXTENSION_PDF);
            case VERSENDETE_VERFUEGUNG -> String
                .format(FILENAME_PREFIX_VERFUEGUNG, fallNr, formattedDate, FILENAME_EXTENSION_PDF);
            case VERFUEGUNGSBRIEF -> String
                .format(FILENAME_PREFIX_VERFUEGUNGSBRIEF, fallNr, formattedDate, FILENAME_EXTENSION_PDF);
        };
    }

    public byte[] getVerfuegungDokumentFromS3(final VerfuegungDokument dokument) {
        try {
            final var bytes = s3.getObject(
                GetObjectRequest.builder()
                    .bucket(configService.getBucketName())
                    .key(dokument.getFilepath() + dokument.getObjectId())
                    .build(),
                AsyncResponseTransformer.toBytes()
            );
            return bytes.get().asByteArray();
        } catch (InterruptedException | ExecutionException e) {
            throw new InternalServerErrorException("Failed to download VerfuegungDokument", e);
        }
    }
}
