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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.ValidatorUtil;
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
import ch.dvbern.stip.api.dokument.service.DokumentDeleteService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuchformular.validation.DarlehenEinreichenValidationGroup;
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
import jakarta.validation.Validator;
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
    private final DokumentDeleteService dokumentDeleteService;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final Antivirus antivirus;
    private final DarlehenDokumentRepository darlehenDokumentRepository;
    private final DokumentRepository dokumentRepository;
    private final DokumentDownloadService dokumentDownloadService;
    private final DarlehenDashboardQueryBuilder darlehenDashboardQueryBuilder;
    private final NotificationService notificationService;
    private final Validator validator;
    private final GesuchRepository gesuchRepository;

    @Transactional
    public DarlehenDto createDarlehen(final UUID fallId) {
        final var fall = fallRepository.requireById(fallId);
        final var gesuchs = gesuchRepository.findAllForFall(fallId);
        final var relatedGesuch =
            gesuchs.filter(gesuch -> gesuch.getGesuchStatus().isEingereicht())
                .max(Comparator.comparing(gesuch -> gesuch.getGesuchsperiode().getGesuchsperiodeStopp()))
                .orElseThrow();

        final var darlehen = new Darlehen();
        darlehen.setFall(fall);
        darlehen.setStatus(DarlehenStatus.IN_BEARBEITUNG_GS);
        darlehen.setRelatedGesuch(relatedGesuch);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto getDarlehen(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public List<DarlehenDto> getDarlehenAllSb(final UUID gesuchId) {
        final var darlehenList = darlehenRepository.findByGesuchId(gesuchId);
        return darlehenList.stream().map(darlehenMapper::toDto).toList();
    }

    @Transactional
    public List<DarlehenDto> getDarlehenAllGs(final UUID fallId) {
        final var darlehenList = darlehenRepository.findByFallId(fallId);
        return darlehenList.stream().map(darlehenMapper::toDto).toList();
    }

    @Transactional
    public PaginatedSbDarlehenDashboardDto getDarlehenDashboardSb(
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
        final var results = baseQuery.distinct()
            .stream()
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
        assertDarlehenStatus(darlehen, DarlehenStatus.IN_FREIGABE);
        darlehen.setStatus(DarlehenStatus.ABGELEHNT);

        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenAbgelehntNotification(darlehen);

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenAkzeptieren(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        assertDarlehenStatus(darlehen, DarlehenStatus.IN_FREIGABE);
        darlehen.setStatus(DarlehenStatus.AKZEPTIERT);

        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenAkzeptiertNotification(darlehen);

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenEingeben(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        assertDarlehenStatus(darlehen, DarlehenStatus.IN_BEARBEITUNG_GS);
        removeSuperfluousDokumentsForDarlehen(darlehen);
        darlehen.setStatus(DarlehenStatus.EINGEGEBEN);

        ValidatorUtil.validate(validator, darlehen, DarlehenEinreichenValidationGroup.class);
        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenEingegebenNotification(darlehen);

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenFreigeben(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        assertDarlehenStatus(darlehen, DarlehenStatus.EINGEGEBEN);
        darlehen.setStatus(DarlehenStatus.IN_FREIGABE);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenZurueckweisen(final UUID darlehenId, final KommentarDto kommentar) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        assertDarlehenStatus(darlehen, DarlehenStatus.EINGEGEBEN);
        darlehen.setStatus(DarlehenStatus.IN_BEARBEITUNG_GS);

        darlehenRepository.persistAndFlush(darlehen);

        notificationService.createDarlehenZurueckgewiesenNotification(darlehen, kommentar.getText());

        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenUpdateGs(final UUID darlehenId, final DarlehenUpdateGsDto darlehenUpdateGsDto) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        assertDarlehenStatus(darlehen, DarlehenStatus.IN_BEARBEITUNG_GS);

        darlehenMapper.partialUpdate(darlehenUpdateGsDto, darlehen);
        removeSuperfluousDokumentsForDarlehen(darlehen);

        ValidatorUtil.validate(validator, darlehen, DarlehenEinreichenValidationGroup.class);
        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    @Transactional
    public DarlehenDto darlehenUpdateSb(final UUID darlehenId, final DarlehenUpdateSbDto darlehenUpdateSbDto) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        assertDarlehenStatus(darlehen, Set.of(DarlehenStatus.EINGEGEBEN, DarlehenStatus.IN_FREIGABE));

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

    @Transactional
    public Set<DarlehenDokumentType> getRequiredDokumentsForDarlehen(final UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        final var requiredDokumentTypes = new HashSet<DarlehenDokumentType>();

        requiredDokumentTypes.add(DarlehenDokumentType.BETREIBUNGS_AUSZUG);
        for (var grund : darlehen.getGruende()) {
            final var documentType = switch (grund) {
                case NICHT_BERECHTIGT -> DarlehenDokumentType.AUFSTELLUNG_KOSTEN_ELTERN;
                case HOHE_GEBUEHREN -> DarlehenDokumentType.KOPIE_SCHULGELDRECHNUNG;
                case ANSCHAFFUNGEN_FUER_AUSBILDUNG -> DarlehenDokumentType.BELEGE_ANSCHAFFUNGEN;
                case AUSBILDUNG_ZWOELF_JAHRE, ZWEITAUSBILDUNG -> null;
            };
            if (Objects.nonNull(documentType)) {
                requiredDokumentTypes.add(documentType);
            }
        }
        return requiredDokumentTypes;
    }

    private void removeSuperfluousDokumentsForDarlehen(final Darlehen darlehen) {
        final var requiredDokumentTypes = getRequiredDokumentsForDarlehen(darlehen.getId());
        darlehen.getDokumente().removeIf(darlehenDokument -> {
            if (!requiredDokumentTypes.contains(darlehenDokument.getDokumentType())) {
                // Create a copy of the dokumente list to preserve the iteration count
                darlehenDokument.getDokumente().stream().toList().forEach(this::removeDokumentAndFromS3);
                return true;
            }
            return false;
        });
        darlehenRepository.persistAndFlush(darlehen);
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

    @Transactional
    public void removeDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);
        final var darlehen = darlehenRepository.requireByDokumentId(dokumentId);
        final var darlehenDokumente = darlehen.getDokumente();

        for (var darlehenDokument : darlehenDokumente) {
            if (darlehenDokument.getDokumente().remove(dokument)) {
                break;
            }
        }

        removeDokumentAndFromS3(dokument);
    }

    private void removeDokumentAndFromS3(final Dokument dokument) {
        dokumentRepository.delete(dokument);
        dokumentDeleteService.executeDeleteDokumentFromS3(
            s3,
            configService.getBucketName(),
            DARLEHEN_DOKUMENT_PATH + dokument.getObjectId()
        );
    }

    @Transactional
    public void deleteDarlehen(UUID darlehenId) {
        final var darlehen = darlehenRepository.requireById(darlehenId);
        assertDarlehenStatus(darlehen, DarlehenStatus.IN_BEARBEITUNG_GS);
        darlehenRepository.delete(darlehen);
    }

    private static void assertDarlehenStatus(final Darlehen darlehen, final DarlehenStatus darlehenStatus) {
        if (darlehen.getStatus() != darlehenStatus) {
            throw new IllegalStateException(String.format("Darlehen not in status %s", darlehenStatus.name()));
        }
    }

    private static void assertDarlehenStatus(final Darlehen darlehen, final Set<DarlehenStatus> darlehenStatuss) {
        if (!darlehenStatuss.contains(darlehen.getStatus())) {
            throw new IllegalStateException(String.format("Darlehen not in statuss %s", darlehenStatuss.toString()));
        }
    }
}
