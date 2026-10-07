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
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.massendruck.entity.DatenschutzbriefMassendruck;
import ch.dvbern.stip.api.massendruck.entity.VerfuegungMassendruck;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobRepository;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobPdfService {
    public static final String MASSENDRUCK_DOKUMENT_PATH = "massendruck/";

    private final MassendruckJobRepository massendruckJobRepository;
    private final GesuchStatusService gesuchStatusService;
    private final DokumentUploadService dokumentUploadService;
    private final DokumentDownloadService dokumentDownloadService;

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
        Iterator<PdfDocument> pdfsToMerge = switch (massendruckJob.getMassendruckTyp()) {
            case VERFUEGUNG -> getAllVerfuegungen(massendruckJob.getVerfuegungMassendrucks()).iterator();
            case DATENSCHUTZBRIEF -> generateAllDatenschutzbriefDocuments(
                massendruckJob.getDatenschutzbriefMassendrucks()
            ).iterator();
        };

        // Merge them using an iText PdfMerger
        if (!pdfsToMerge.hasNext()) {
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
            while (pdfsToMerge.hasNext()) {
                final var pdfToMerge = pdfsToMerge.next();
                merger.merge(pdfToMerge, 1, pdfToMerge.getNumberOfPages());
            }
        }

        LOG.info("Finished merging PDFs, creating and uploading Dokument");

        // Keep a stable object ID so retrying a job overwrites the previous upload.
        final var objectId = String.format("%s.pdf", massendruckJob.getId());
        final var fileName = String.format("massendruck_%s.pdf", massendruckJob.getMassendruckTyp());
        dokumentUploadService.executeUploadDocument(
            out.toByteArray(),
            fileName,
            objectId,
            MASSENDRUCK_DOKUMENT_PATH
        );

        final var dokument = new Dokument()
            .setFilename(fileName)
            .setFilepath(MASSENDRUCK_DOKUMENT_PATH)
            .setFilesize(Integer.toString(out.size()))
            .setObjectId(objectId);

        massendruckJob.setMergedPdf(dokument);

        // Once this is done, bulk trigger state change
        final var gesuche = massendruckJob.getAttachedGesuche();
        final var changeEvent = switch (massendruckJob.getMassendruckTyp()) {
            case VERFUEGUNG -> GesuchStatusChangeEvent.VERFUEGUNG_VERSANDBEREIT;
            case DATENSCHUTZBRIEF -> GesuchStatusChangeEvent.DATENSCHUTZBRIEF_VERSANDBEREIT;
        };

        LOG.info("Finished uploading document, bulk triggering state change");
        gesuchStatusService.bulkTriggerStateMachineEvent(gesuche, changeEvent);
        massendruckJob.setStatus(MassendruckJobStatus.SUCCESS);
    }

    public RestMulti<Buffer> downloadMassendruckPdf(final UUID massendruckJobId) {
        final var massendruckJob = massendruckJobRepository.requireById(massendruckJobId);
        final var dokument = massendruckJob.getMergedPdf();

        return dokumentDownloadService.getDokument(
            dokument.getObjectId(),
            MASSENDRUCK_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    private Stream<PdfDocument> generateAllDatenschutzbriefDocuments(
        final List<DatenschutzbriefMassendruck> datenschutzbriefMassendrucks
    ) {
        return datenschutzbriefMassendrucks.stream()
            .map(datenschutzbriefMassendruck -> {
                final var dokument = datenschutzbriefMassendruck.getDatenschutzbrief().getDokument();
                return getPdf(dokument.getObjectId(), dokument.getFilepath());
            });
    }

    private Stream<PdfDocument> getAllVerfuegungen(final List<VerfuegungMassendruck> verfuegungMassendrucks) {
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
                return getPdf(dokument.getObjectId(), dokument.getFilepath());
            });
    }

    private PdfDocument getPdf(final String objectId, final String dokumentPathPrefix) {
        try {
            final var bytes = dokumentDownloadService.executeDownloadDocument(objectId, dokumentPathPrefix);
            final var input = new ByteArrayInputStream(bytes);
            return new PdfDocument(new PdfReader(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
