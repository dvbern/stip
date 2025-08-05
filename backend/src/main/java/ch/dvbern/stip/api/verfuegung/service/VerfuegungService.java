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
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.common.util.DokumentUploadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.pdf.service.PdfService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.repo.VerfuegungRepository;
import ch.dvbern.stip.generated.dto.VerfuegungDto;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import ch.dvbern.stip.stipdecision.type.Kanton;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class VerfuegungService {

    private static final String VERFUEGUNG_DOKUMENT_PATH = "verfuegung/";
    private static final String NEGATIVE_VERFUEGUNG_DOKUMENT_NAME = "Negative_Verfuegung.pdf";
    private static final String VERFUEGUNG_DOKUMENT_NAME = "Verfuegung.pdf";

    private final PdfService pdfService;
    private final ConfigService configService;
    private final S3AsyncClient s3;
    private final VerfuegungRepository verfuegungRepository;
    private final GesuchRepository gesuchRepository;
    private final StipDecisionTextRepository stipDecisionTextRepository;
    private final VerfuegungMapper verfuegungMapper;
    private final Antivirus antivirus;

    @Transactional
    public void createVerfuegung(final UUID gesuchId) {
        final Verfuegung verfuegung = new Verfuegung();
        verfuegung.setGesuch(gesuchRepository.requireById(gesuchId));
        verfuegungRepository.persistAndFlush(verfuegung);
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

    @Transactional
    public void createNegativeVerfuegungManuell(final UUID gesuchId, final FileUpload fileUpload) {
        final var response = DokumentUploadUtil.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            VERFUEGUNG_DOKUMENT_PATH,
            objectId -> {
                final var verfuegung = new Verfuegung();
                verfuegung.setGesuch(gesuchRepository.requireById(gesuchId));
                verfuegung.setNegativeVerfuegung(true);
                verfuegung.setObjectId(objectId);
                verfuegung.setFilename(fileUpload.fileName());
                verfuegung.setFilepath(VERFUEGUNG_DOKUMENT_PATH);
                verfuegungRepository.persistAndFlush(verfuegung);
            },
            throwable -> {
                throw new InternalServerErrorException(throwable);
            }
        );
        response.await().indefinitely();
    }

    @Transactional
    public void createPdfForNegtativeVerfuegung(final Verfuegung verfuegung) {
        final ByteArrayOutputStream out = pdfService.createNegativeVerfuegungPdf(verfuegung);

        final String objectId = DokumentUploadUtil.executeUploadDocument(
            out.toByteArray(),
            NEGATIVE_VERFUEGUNG_DOKUMENT_NAME,
            s3,
            configService,
            VERFUEGUNG_DOKUMENT_PATH
        );
        verfuegung.setObjectId(objectId);
        verfuegung.setFilename(NEGATIVE_VERFUEGUNG_DOKUMENT_NAME);
        verfuegung.setFilepath(VERFUEGUNG_DOKUMENT_PATH);
    }

    @Transactional
    public void createPdfForVerfuegungOhneAnspruch(final Verfuegung verfuegung) throws IOException {
        final ByteArrayOutputStream verfuegungOut = pdfService.createVerfuegungOhneAnspruchPdf(verfuegung);
        final String objectId = DokumentUploadUtil.executeUploadDocument(
            verfuegungOut.toByteArray(),
            VERFUEGUNG_DOKUMENT_NAME,
            s3,
            configService,
            VERFUEGUNG_DOKUMENT_PATH
        );
        verfuegung.setObjectId(objectId);
        verfuegung.setFilename(VERFUEGUNG_DOKUMENT_NAME);
        verfuegung.setFilepath(VERFUEGUNG_DOKUMENT_PATH);
    }

    public List<VerfuegungDto> getVerfuegungenByGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        return gesuch.getVerfuegungs().stream().map(verfuegungMapper::toDto).toList();
    }

    public Verfuegung getLatestVerfuegung(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        return gesuch.getVerfuegungs()
            .stream()
            .sorted(Comparator.comparing(Verfuegung::getTimestampErstellt).reversed())
            .findFirst()
            .orElseThrow();
    }

    public RestMulti<Buffer> getVerfuegung(final UUID verfuegungId) {
        final var verfuegung = verfuegungRepository.requireById(verfuegungId);

        return DokumentDownloadUtil.getDokument(
            s3,
            configService.getBucketName(),
            verfuegung.getObjectId(),
            verfuegung.getFilepath(),
            verfuegung.getFilename()
        );
    }
}
