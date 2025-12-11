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

package ch.dvbern.stip.api.darlehen.service;

import java.time.LocalDate;
import java.util.UUID;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.darlehen.entity.DarlehenDokument;
import ch.dvbern.stip.api.darlehen.repo.DarlehenDokumentRepository;
import ch.dvbern.stip.api.darlehen.repo.DarlehenRepository;
import ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType;
import ch.dvbern.stip.api.darlehen.type.DarlehenStatus;
import ch.dvbern.stip.api.darlehen.type.GetDarlehenSbQueryType;
import ch.dvbern.stip.api.darlehen.type.SbDarlehenDashboardColumn;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.NullableDarlehenDokumentDto;
import ch.dvbern.stip.generated.dto.PaginatedSbDarlehenDashboardDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@RequestScoped
@RequiredArgsConstructor
public class DarlehenService {
    public static final String DARLEHEN_DOKUMENT_PATH = "darlehen/";

    private final FallRepository fallRepository;
    private final DarlehenRepository darlehenRepository;
    private final DarlehenMapper darlehenMapper;
    private final DarlehenDokumentMapper darlehenDokumentMapper;
    private final DokumentUploadService dokumentUploadService;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final Antivirus antivirus;
    private final DarlehenDokumentRepository darlehenDokumentRepository;
    private final DokumentRepository dokumentRepository;
    private final DokumentDownloadService dokumentDownloadService;
    private final DarlehenDashboardQueryBuilder darlehenDashboardQueryBuilder;
    private final NotificationService notificationService;

    @Transactional
    public DarlehenDto createDarlehen(final UUID fallId) {
        final var fall = fallRepository.requireById(fallId);

        final var darlehen = new Darlehen();
        darlehen.setFall(fall);
        darlehen.setStatus(DarlehenStatus.IN_BEARBEITUNG_GS);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto getDarlehenGs(final UUID fallId) {
        final var darlehenList = darlehenRepository.findByFallId(fallId);
        final var darlehenActive = darlehenList.stream()
            .filter(
                d -> !d.getStatus().equals(DarlehenStatus.AKZEPTIERT) && !d.getStatus().equals(DarlehenStatus.ABGELEHNT)
            )
            .findFirst()
            .orElse(null);

        return darlehenMapper.toDto(darlehenActive);
    }

    @Transactional
    public PaginatedSbDarlehenDashboardDto getDarlehenSb(
        final GetDarlehenSbQueryType getDarlehenSbQueryType,
        final Integer page,
        final Integer pageSize,
        final String fallNummer,
        final String piaNachname,
        final String piaVorname,
        final LocalDate piaGeburtsdatum,
        final String status,
        final String bearbeiter,
        final LocalDate letzteAktivitaetFrom,
        final LocalDate letzteAktivitaetTo,
        final SbDarlehenDashboardColumn sortColumn,
        final SortOrder sortOrder
    ) {
        if (pageSize > configService.getMaxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var baseQuery = darlehenDashboardQueryBuilder.baseQuery(getDarlehenSbQueryType);

        if (fallNummer != null) {
            darlehenDashboardQueryBuilder.fallNummer(baseQuery, fallNummer);
        }

        if (piaNachname != null) {
            darlehenDashboardQueryBuilder.piaNachname(baseQuery, piaNachname);
        }

        if (piaVorname != null) {
            darlehenDashboardQueryBuilder.piaVorname(baseQuery, piaVorname);
        }

        if (piaGeburtsdatum != null) {
            darlehenDashboardQueryBuilder.piaGeburtsdatum(baseQuery, piaGeburtsdatum);
        }

        if (status != null) {
            darlehenDashboardQueryBuilder.status(baseQuery, status);
        }

        if (bearbeiter != null) {
            darlehenDashboardQueryBuilder.bearbeiter(baseQuery, bearbeiter);
        }

        if (letzteAktivitaetFrom != null && letzteAktivitaetTo != null) {
            darlehenDashboardQueryBuilder.letzteAktivitaet(baseQuery, letzteAktivitaetFrom, letzteAktivitaetTo);
        }

        final var countQuery = darlehenDashboardQueryBuilder.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            darlehenDashboardQueryBuilder.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            darlehenDashboardQueryBuilder.defaultOrder(baseQuery);
        }

        darlehenDashboardQueryBuilder.paginate(baseQuery, page, pageSize);
        final var results = baseQuery.stream()
            .map(darlehenMapper::toDashboardDto)
            .toList();

        return new PaginatedSbDarlehenDashboardDto(
            page,
            results.size(),
            Math.toIntExact(countQuery.fetchFirst()),
            results
        );
    }

    @Transactional
    public DarlehenDto darlehenAblehnen(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.ABGELEHNT);

        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenAbgelehntNotification(darlehen);

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenAkzeptieren(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.AKZEPTIERT);

        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenAkzeptiertNotification(darlehen);

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenEingeben(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.EINGEGEBEN);

        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenEingegebenNotification(darlehen);

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenFreigeben(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.IN_FREIGABE);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenZurueckweisen(final UUID darlehenId, final KommentarDto kommentar) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.IN_BEARBEITUNG_GS);

        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenZurueckgewiesenNotification(darlehen, kommentar.getText());

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenUpdateGs(final UUID darlehenId, final DarlehenUpdateGsDto darlehenUpdateGsDto) {
        final var darlehen = darlehenRepository.requireById(darlehenId);

        darlehenMapper.partialUpdate(darlehenUpdateGsDto, darlehen);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto darlehenUpdateSb(final UUID darlehenId, final DarlehenUpdateSbDto darlehenUpdateSbDto) {
        final var darlehen = darlehenRepository.requireById(darlehenId);

        darlehenMapper.partialUpdate(darlehenUpdateSbDto, darlehen);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public Uni<Response> uploadDarlehenDokument(
        final UUID darlehenId,
        final DarlehenDokumentType dokumentTyp,
        final FileUpload fileUpload
    ) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        final var darlehenDokument = getDarlehenDokument(darlehenId, dokumentTyp);
        if (darlehenDokument == null) {
            createDarlehenDokument(darlehen, dokumentTyp);
        }

        return dokumentUploadService.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            DARLEHEN_DOKUMENT_PATH,
            objectId -> uploadDokument(
                darlehenId,
                dokumentTyp,
                fileUpload,
                objectId
            )
        );
    }

    private void uploadDokument(
        final UUID darlehenId,
        final DarlehenDokumentType type,
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        final var darlehenDokument = darlehenDokumentRepository.findByDarlehenIdAndType(darlehenId, type)
            .orElseGet(() -> createDarlehenDokument(darlehen, type));

        final var dokument = new Dokument()
            .setFilename(fileUpload.fileName())
            .setFilesize(String.valueOf(fileUpload.size()))
            .setFilepath(DARLEHEN_DOKUMENT_PATH)
            .setObjectId(objectId);

        darlehenDokument.getDokumente().add(dokument);
        dokumentRepository.persistAndFlush(dokument);
    }

    private DarlehenDokument createDarlehenDokument(
        final Darlehen darlehen,
        final DarlehenDokumentType type
    ) {
        final var darlehenDokument = new DarlehenDokument()
            .setDarlehen(darlehen)
            .setDokumentType(type);

        darlehenDokumentRepository.persistAndFlush(darlehenDokument);
        return darlehenDokument;
    }

    @Transactional
    public NullableDarlehenDokumentDto getDarlehenDokument(
        final UUID darlehenId,
        final DarlehenDokumentType dokumentTyp
    ) {
        final var dokument = darlehenDokumentRepository.findByDarlehenIdAndType(darlehenId, dokumentTyp).orElse(null);
        return darlehenDokumentMapper.toDto(dokument);
    }

    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);

        return dokumentDownloadService.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            DARLEHEN_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }
}
