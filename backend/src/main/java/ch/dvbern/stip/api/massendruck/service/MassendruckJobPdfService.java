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
import java.util.concurrent.ExecutionException;

import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.massendruck.entity.DatenschutzbriefMassendruck;
import ch.dvbern.stip.api.massendruck.entity.VerfuegungMassendruck;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobRepository;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.pdf.service.DatenschutzbriefPdfService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobPdfService {
    public static final String MASSENDRUCK_DOKUMENT_PATH = "massendruck/";

    private final MassendruckJobRepository massendruckJobRepository;
    private final S3AsyncClient s3async;
    private final ConfigService configService;
    private final GesuchStatusService gesuchStatusService;

    // TODO KSTIP-2294: Replace this with an impl on the service once 2709 is merged
    private final DatenschutzbriefPdfService datenschutzbriefPdfService;

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
        // Set the objectId to MassendruckJob ID for easy document downloading
        final var objectId = String.format("%s.pdf", massendruckJob.getId());
        final var fileName = String.format("massendruck_%s.pdf", massendruckJob.getMassendruckTyp());

        final var dokument = new Dokument()
            .setFilename(fileName)
            .setFilepath(fileName)
            .setFilesize(Integer.toString(out.size()))
            .setObjectId(objectId);

        massendruckJob.setMergedPdf(dokument);

        // Upload the merged PDF to S3
        final var future = s3async.putObject(
            PutObjectRequest.builder()
                .bucket(configService.getBucketName())
                .key(MASSENDRUCK_DOKUMENT_PATH + objectId)
                .contentType("application/pdf")
                .build(),
            AsyncRequestBody.fromBytes(out.toByteArray())
        );

        LOG.info("Uploading document to S3");
        Uni.createFrom()
            .completionStage(future)
            .onItem()
            .invoke(response -> LOG.info("S3 upload completed, uploaded size: {}", response.size()))
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

    public RestMulti<Buffer> downloadMassendruckPdf(final UUID massendruckJobId) {
        final var massendruckJob = massendruckJobRepository.requireById(massendruckJobId);
        final var dokument = massendruckJob.getMergedPdf();

        return DokumentDownloadUtil.getDokument(
            s3async,
            configService.getBucketName(),
            dokument.getObjectId(),
            MASSENDRUCK_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    private List<PdfDocument> generateAllDatenschutzbriefDocuments(
        final List<DatenschutzbriefMassendruck> datenschutzbriefMassendrucks
    ) {
        return datenschutzbriefMassendrucks.stream()
            .map(datenschutzbriefMassendruck -> {
                final var tranche =
                    datenschutzbriefMassendruck.getDatenschutzbrief().getGesuch().getLatestGesuchTranche();
                final var elternteil = tranche.getGesuchFormular()
                    .getElternteilOfTyp(
                        datenschutzbriefMassendruck.getDatenschutzbrief().getDatenschutzbriefEmpfaenger()
                    )
                    .orElseThrow(IllegalStateException::new);
                try (
                    final var out =
                        datenschutzbriefPdfService.createDatenschutzbriefForElternteil(elternteil, tranche.getId());
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
                final var verfuegung = verfuegungMassendruck.getVerfuegung();
                final var dokument = verfuegung.getDokumente()
                    .stream()
                    .filter(
                        verfuegungDokument -> verfuegungDokument.getTyp()
                            .equals(VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG)
                    )
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);

                final var bytes = s3async.getObject(
                    GetObjectRequest.builder()
                        .bucket(configService.getBucketName())
                        .key(dokument.getFilepath() + dokument.getObjectId())
                        .build(),
                    AsyncResponseTransformer.toBytes()
                );

                try {
                    final var input = bytes.get().asInputStream();
                    return new PdfDocument(new PdfReader(input));
                } catch (InterruptedException | ExecutionException | IOException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
    }
}
