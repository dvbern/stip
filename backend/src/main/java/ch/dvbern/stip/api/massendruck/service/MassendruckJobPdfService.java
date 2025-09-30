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

package ch.dvbern.stip.api.massendruck.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.massendruck.entity.DatenschutzbriefMassendruck;
import ch.dvbern.stip.api.massendruck.entity.VerfuegungMassendruck;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobRepository;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.pdf.service.DatenschutzbriefService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobPdfService {
    public static final String MASSENDRUCK_DOKUMENT_PATH = "massendruck/";

    private final MassendruckJobRepository massendruckJobRepository;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final GesuchStatusService gesuchStatusService;

    // TODO KSTIP-2294: Replace this with an impl on the service once 2709 is merged
    private final DatenschutzbriefService datenschutzbriefPdfService;

    @Transactional
    public void setFailedStatusOnJob(final UUID massendruckJobId) {
        final var massendruck = massendruckJobRepository.requireById(massendruckJobId);
        massendruck.setStatus(MassendruckJobStatus.FAILED);
    }

    @Transactional
    public void downloadCombineAndSaveForJob(final UUID massendruckJobId) {
        final var massendruckJob = massendruckJobRepository.requireById(massendruckJobId);

        // Download all PDFs from S3 if Verfuegung, otherwise generate the Datenschutzbriefe PDFs
        LOG.info("Gathering PDFs to merge");
        final List<PdfDocument> pdfsToMerge = switch (massendruckJob.getMassendruckTyp()) {
            case VERFUEGUNG -> getAllVerfuegungen(massendruckJob.getVerfuegungMassendrucks());
            case DATENSCHUTZBRIEF -> generateAllDatenschutzbriefDocuments(
                massendruckJob.getDatenschutzbriefMassendrucks()
            );
        };

        // Merge them using an iText PdfMerger
        if (pdfsToMerge.isEmpty()) {
            // This should never happen, just a sanity check
            LOG.warn(
                "Massendruck Job of type {} with ID {} tried to merge PDFs but no PDFs were found",
                massendruckJob.getMassendruckTyp(),
                massendruckJob.getId()
            );
            return;
        }

        LOG.info("Finished loading PDFs to merge, starting to combine");

        final var out = new ByteArrayOutputStream();
        try (final var targetPdf = new PdfDocument(new PdfWriter(out))) {
            final var merger = new PdfMerger(targetPdf);
            for (final var pdfToMerge : pdfsToMerge) {
                merger.merge(pdfToMerge, 1, pdfToMerge.getNumberOfPages());
            }
        }

        LOG.info("Finished merging PDFs, creating Dokument and uploading to S3");

        // Set the Document for MassendruckJob
        final var objectId = String.format("%s/%s.pdf", MASSENDRUCK_DOKUMENT_PATH, massendruckJob.getId());
        final var fileName = String.format("massendruck_%s.pdf", massendruckJob.getMassendruckJobNumber());

        final var dokument = new Dokument()
            .setFilename(fileName)
            .setFilepath(fileName)
            .setFilesize("")
            .setObjectId(objectId);

        massendruckJob.setMergedPdf(dokument);

        // Upload the merged PDF to S3
        final var future = s3.putObject(
            PutObjectRequest.builder()
                .bucket(configService.getBucketName())
                .key(objectId)
                .contentType("application/pdf")
                .build(),
            AsyncRequestBody.fromBytes(out.toByteArray())
        );

        LOG.info("Uploading document to S3");
        Uni.createFrom()
            .completionStage(future)
            .await()
            .indefinitely();

        // Once this is done, bulk trigger state change
        final var gesuche = massendruckJob.getAttachedGesuche();
        final var changeEvent = switch (massendruckJob.getMassendruckTyp()) {
            case VERFUEGUNG -> GesuchStatusChangeEvent.VERFUEGUNG_VERSANDBEREIT;
            case DATENSCHUTZBRIEF -> GesuchStatusChangeEvent.DATENSCHUTZBRIEF_VERSANDBEREIT;
        };

        LOG.info("Finished uploading document to S3, bulk triggering state change");
        gesuchStatusService.bulkTriggerStateMachineEvent(gesuche, changeEvent);
        massendruckJob.setStatus(MassendruckJobStatus.SUCCESS);
    }

    private List<PdfDocument> generateAllDatenschutzbriefDocuments(
        final List<DatenschutzbriefMassendruck> datenschutzbriefMassendrucks
    ) {
        return datenschutzbriefMassendrucks.stream()
            .map(datenschutzbriefMassendruck -> {
                try (
                    final var out = datenschutzbriefPdfService.createDatenschutzbriefForElternteil(new Eltern());
                ) {
                    final var in = new ByteArrayInputStream(out.toByteArray());
                    final var reader = new PdfReader(in);
                    return new PdfDocument(reader);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }

    private List<PdfDocument> getAllVerfuegungen(final List<VerfuegungMassendruck> verfuegungMassendrucks) {
        return verfuegungMassendrucks.stream()
            .map(verfuegungMassendruck -> {
                return (PdfDocument) null; // TODO KSTIP-2294
            })
            .toList();
    }
}
