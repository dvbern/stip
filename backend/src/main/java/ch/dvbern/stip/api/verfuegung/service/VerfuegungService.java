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
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.common.util.DokumentUploadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.pdf.service.PdfService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.repo.VerfuegungRepository;
import ch.dvbern.stip.generated.dto.VerfuegungDto;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class VerfuegungService {

    private static final String VERFUEGUNG_DOKUMENT_PATH = "verfuegung/";
    private static final String NEGATIVE_VERFUEGUNG_DOKUMENT_NAME = "Negative_Verfuegung.pdf";

    private final PdfService pdfService;
    private final ConfigService configService;
    private final S3AsyncClient s3;
    private final VerfuegungRepository verfuegungRepository;
    private final GesuchRepository gesuchRepository;
    private final StipDecisionTextRepository stipDecisionTextRepository;
    private final VerfuegungMapper verfuegungMapper;

    @Transactional
    public void createVerfuegung(final UUID gesuchId, final UUID stipDecisionId) {
        final var stipDecision = stipDecisionTextRepository.requireById(stipDecisionId);

        final Verfuegung verfuegung = new Verfuegung();
        verfuegung.setStipDecision(stipDecision.getStipDecision());
        verfuegung.setGesuch(gesuchRepository.requireById(gesuchId));
        verfuegungRepository.persistAndFlush(verfuegung);
    }

    @Transactional
    public void createNegtativeVerfuegung(final Gesuch gesuch, final Verfuegung verfuegung) {
        final ByteArrayOutputStream out = pdfService.createNegativeVerfuegungPdf(gesuch, verfuegung.getStipDecision());

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

    public List<VerfuegungDto> getVerfuegungenByGesuch(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        return gesuch.getVerfuegungs().stream().map(verfuegungMapper::toDto).toList();
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
