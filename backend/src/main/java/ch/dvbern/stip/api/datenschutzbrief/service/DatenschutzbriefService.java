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

package ch.dvbern.stip.api.datenschutzbrief.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.api.datenschutzbrief.entity.DatenschutzbriefBuilder;
import ch.dvbern.stip.api.datenschutzbrief.repo.DatenschutzbriefRepository;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.DokumentBuilder;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.repo.ElternRepository;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.pdf.service.DatenschutzbriefPdfService;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.generated.dto.DatenschutzbriefOverviewDto;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CaseUtils;
import org.jboss.resteasy.reactive.RestMulti;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class DatenschutzbriefService {

    static final String DATENSCHUTZBRIEF_DOKUMENT_PATH = "datenschutzbrief/";
    static final String FILENAME_DATENSCHUTZBRIEF = "Datenschutzbrief_%s_%s.pdf";
    private final DatenschutzbriefPdfService datenschutzbriefPdfService;
    private final DatenschutzbriefRepository datenschutzbriefRepository;
    private final SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;
    private final DokumentRepository dokumentRepository;
    private final DokumentDownloadService dokumentDownloadService;
    private final DokumentUploadService dokumentUploadService;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final ElternRepository elternRepository;
    private final GesuchRepository gesuchRepository;
    private final DatenschutzbriefMapper datenschutzbriefMapper;

    public RestMulti<Buffer> getDatenschutzbriefDokument(final UUID datenschutzbriefId) {
        final var dokument = datenschutzbriefRepository.requireById(datenschutzbriefId).getDokument();
        return dokumentDownloadService.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            DATENSCHUTZBRIEF_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    @Transactional
    public UUID createDatenschutzbrief(final UUID gesuchId, final UUID elternteilId) {
        final var elternteil = elternRepository.requireById(elternteilId);
        return createDatenschutzbrief(gesuchId, elternteil, true).getId();
    }

    @Transactional
    public Datenschutzbrief createDatenschutzbrief(
        final UUID gesuchId,
        final Eltern elternteil,
        final boolean isVersendet
    ) {
        final var dokumentByteArray = datenschutzbriefPdfService.createDatenschutzbriefForElternteil(
            elternteil,
            gesuchId
        );
        final var dokument = storeDatenschutzbriefDokument(elternteil.getElternTyp(), dokumentByteArray);
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var datenschutzbrief = DatenschutzbriefBuilder.datenschutzbrief()
            .isVersendet(isVersendet)
            .datenschutzbriefEmpfaenger(elternteil.getElternTyp())
            .sozialversicherungsnummer(elternteil.getSozialversicherungsnummer())
            .nachname(elternteil.getNachname())
            .vorname(elternteil.getVorname())
            .gesuch(gesuch)
            .dokument(dokument)
            .build();
        gesuch.getDatenschutzbriefs().add(datenschutzbrief);
        dokumentRepository.persist(dokument);
        datenschutzbriefRepository.persist(datenschutzbrief);
        return datenschutzbrief;
    }

    private String generateFilename(ElternTyp elternTyp) {
        final String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format(FILENAME_DATENSCHUTZBRIEF, CaseUtils.toCamelCase(elternTyp.name(), true), formattedDate);
    }

    private Dokument storeDatenschutzbriefDokument(final ElternTyp elternTyp, final ByteArrayOutputStream pdfContent) {
        final String filename = generateFilename(elternTyp);
        final String objectId = dokumentUploadService.executeUploadDocument(
            pdfContent.toByteArray(),
            filename,
            s3,
            configService,
            DATENSCHUTZBRIEF_DOKUMENT_PATH
        );

        return DokumentBuilder.dokument()
            .filename(filename)
            .filepath(DATENSCHUTZBRIEF_DOKUMENT_PATH)
            .filesize(String.valueOf(pdfContent.size()))
            .objectId(objectId)
            .build();
    }

    @Transactional
    public void deleteDatenschutzbriefeOfGesuch(final UUID gesuchId) {
        datenschutzbriefRepository.deleteAllByGesuchId(gesuchId);
    }

    @Transactional
    public void createAllRequiredDatenschutzbriefeForGesuch(final Gesuch gesuch) {
        if (gesuch.getGesuchTranchen().size() != 1) {
            LOG.error("Trying to create Datenschutzbriefe for a Gesuch with more than 1 Tranche");
            return;
        }

        final var trancheToUse = gesuch.getGesuchTranchen().getFirst();
        final var requiredEmpfaenger =
            getRequiredDatenschutzbriefEmpfaenger(trancheToUse.getGesuchFormular().getFamiliensituation());
        for (final var empfaengerToCreate : requiredEmpfaenger) {
            final var empfaenger = trancheToUse.getGesuchFormular()
                .getElternteilOfTyp(empfaengerToCreate)
                .orElseThrow(IllegalStateException::new);
            final var datenschutzbrief = createDatenschutzbrief(gesuch.getId(), empfaenger, false);

            gesuch.getDatenschutzbriefs().add(datenschutzbrief);
        }
    }

    private List<ElternTyp> getRequiredDatenschutzbriefEmpfaenger(
        final Familiensituation familiensituation
    ) {
        return steuerdatenTabBerechnungsService.calculateTabs(familiensituation)
            .stream()
            .flatMap(steuerdatenTyp -> switch (steuerdatenTyp) {
                case MUTTER -> Stream.of(ElternTyp.MUTTER);
                case VATER -> Stream.of(ElternTyp.VATER);
                case FAMILIE -> Stream.of(ElternTyp.MUTTER, ElternTyp.VATER);
            })
            .toList();
    }

    public List<DatenschutzbriefOverviewDto> getDatenschutzbriefs(UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);

        return gesuch.getDatenschutzbriefs()
            .stream()
            .sorted(Comparator.comparing(Datenschutzbrief::getTimestampErstellt).reversed())
            .map(datenschutzbriefMapper::toDto)
            .toList();
    }
}
