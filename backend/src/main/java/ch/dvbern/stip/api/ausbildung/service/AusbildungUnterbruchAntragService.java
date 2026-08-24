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

package ch.dvbern.stip.api.ausbildung.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungUnterbruchAntragRepository;
import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.ausbildung.util.AusbildungUnterbruchAntragUtil;
import ch.dvbern.stip.api.common.util.GesuchUtil;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDeleteService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchDashboardSBDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchDashboardSBDtoBuilder;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchLimitsDto;
import ch.dvbern.stip.generated.dto.CreateAusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragSBDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class AusbildungUnterbruchAntragService {
    private final AusbildungUnterbruchAntragRepository ausbildungUnterbruchAntragRepository;
    private final AusbildungUnterbruchAntragMapper ausbildungUnterbruchAntragMapper;
    private final DokumentUploadService dokumentUploadService;
    private final S3AsyncClient s3;
    private final StipConfig config;
    private final Antivirus antivirus;
    private final GesuchRepository gesuchRepository;
    private final DokumentRepository dokumentRepository;
    private final DokumentDeleteService dokumentDeleteService;
    private final DokumentDownloadService dokumentDownloadService;
    private final NotificationService notificationService;
    private final GesuchStatusService gesuchStatusService;
    private final AusbildungService ausbildungService;
    private final StatusprotokollService statusprotokollService;

    public static final String AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH = "ausbildung_unterbruch_antrag/";

    public AusbildungUnterbruchAntrag requireById(final UUID ausbildungUnterbruchAntragId) {
        return ausbildungUnterbruchAntragRepository.requireById(ausbildungUnterbruchAntragId);
    }

    public AusbildungUnterbruchAntrag requireByDokumentId(final UUID dokumentId) {
        return ausbildungUnterbruchAntragRepository.requireByDokumentId(dokumentId);
    }

    @Transactional
    public void createStatusprotokollEntry(
        final AusbildungUnterbruchAntrag antrag,
        final String statusTo,
        final String statusFrom,
        final String comment
    ) {
        statusprotokollService.createStatusprotokoll(
            statusTo,
            statusFrom,
            StatusprotokollEntryTyp.AUSBILDUNG_UNTERBRUCH_ANTRAG,
            comment,
            antrag.getGesuch()
        );
    }

    @Transactional
    public void createStatusprotokollEntry(
        final AusbildungUnterbruchAntrag antrag,
        final String statusFrom,
        final String comment
    ) {
        createStatusprotokollEntry(antrag, antrag.getStatus().toString(), statusFrom, comment);
    }

    private void uploadDokument(
        final UUID ausbildungUnterbruchAntragId,
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var ausbildungUnterbruchAntrag = requireById(ausbildungUnterbruchAntragId);

        final var dokument = new Dokument()
            .setFilename(fileUpload.fileName())
            .setFilesize(String.valueOf(fileUpload.size()))
            .setFilepath(AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH)
            .setObjectId(objectId);

        ausbildungUnterbruchAntrag.getDokuments().add(dokument);
        dokumentRepository.persistAndFlush(dokument);
    }

    @Transactional
    public Uni<Response> uploadAusbildungUnterbruchAntragDokument(
        final UUID ausbildungUnterbruchAntragId,
        final FileUpload fileUpload
    ) {
        return dokumentUploadService.validateScanUploadDokument(
            fileUpload,
            s3,
            config,
            antivirus,
            AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH,
            objectId -> uploadDokument(
                ausbildungUnterbruchAntragId,
                fileUpload,
                objectId
            )
        );
    }

    @Transactional
    public void deleteAusbildungUnterbruchAntrag(final UUID ausbildungUnterbruchAntragId) {
        final var antrag = requireById(ausbildungUnterbruchAntragId);
        final List<String> objectIds =
            antrag.getDokuments().stream().map(dokument -> getFullPathObjectId(dokument.getObjectId())).toList();

        ausbildungUnterbruchAntragRepository.delete(antrag);
        dokumentDeleteService.executeDeleteDokumentsFromS3(s3, config.s3().bucketName(), objectIds);
    }

    @Transactional
    public void deleteAusbildungUnterbruchAntragDokument(final UUID dokumentId) {
        final var antrag = requireByDokumentId(dokumentId);
        final var dokument = dokumentRepository.requireById(dokumentId);
        antrag.getDokuments().remove(dokument);
        dokumentDeleteService.executeDeleteDokumentFromS3(
            s3,
            config.s3().bucketName(),
            getFullPathObjectId(dokument.getObjectId())
        );
    }

    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);

        return dokumentDownloadService.getDokument(
            s3,
            config.s3().bucketName(),
            dokument.getObjectId(),
            AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    private static String getFullPathObjectId(final String objectId) {
        return AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH + objectId;
    }

    @Transactional
    public Uni<Response> createAusbildungUnterbruchAntragGs(
        final UUID ausbildungId,
        final CreateAusbildungUnterbruchAntragGSDto createAusbildungUnterbruchAntragGSDto
    ) {
        final var ausbildung = ausbildungService.requireById(ausbildungId);
        final var gesuch = ausbildung.getLatestGesuch();
        final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag = new AusbildungUnterbruchAntrag();
        ausbildungUnterbruchAntrag.setGesuch(gesuch);
        ausbildungUnterbruchAntrag.setAusbildung(gesuch.getAusbildung());
        ausbildungUnterbruchAntragMapper
            .antragEinreichen(createAusbildungUnterbruchAntragGSDto, ausbildungUnterbruchAntrag);
        ausbildungUnterbruchAntragRepository.persistAndFlush(ausbildungUnterbruchAntrag);
        notificationService
            .createAusbildungUnterbruchAntragEingereichtNotificationAndSendStdMail(ausbildungUnterbruchAntrag);
        createStatusprotokollEntry(
            ausbildungUnterbruchAntrag,
            AusbildungUnterbruchAntragStatus.EINGEGEBEN.toString(),
            ausbildungUnterbruchAntrag.getStatus().toString(),
            ausbildungUnterbruchAntrag.getKommentarGS()
        );

        final var fileUploads = Stream.of(createAusbildungUnterbruchAntragGSDto.getFileUpload())
            .map(
                fileUpload -> dokumentUploadService.validateScanUploadDokument(
                    fileUpload,
                    s3,
                    config,
                    antivirus,
                    AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH,
                    objectId -> uploadDokument(
                        ausbildungUnterbruchAntrag.getId(),
                        fileUpload,
                        objectId
                    ),
                    throwable -> LOG.error(throwable.getMessage())
                )
            );

        return Uni.join()
            .all(fileUploads.toList())
            .usingConcurrencyOf(1)
            .andFailFast()
            .replaceWith(Uni.createFrom().item(Response.created(null).build()));
    }

    @Transactional
    public AusbildungUnterbruchDashboardSBDto getAusbildungUnterbruchAntragsByGesuchId(final UUID gesuchId) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var ausbildungUnterbruchs =
            ausbildungUnterbruchAntragRepository.getAusbildungUnterbruchAntragsByGesuchId(gesuchId)
                .stream()
                .map(ausbildungUnterbruchAntragMapper::toSbDto)
                .toList();

        return AusbildungUnterbruchDashboardSBDtoBuilder.ausbildungUnterbruchDashboardSBDto()
            .canCreateAusbildungUnterbruch(canCreateAusbildungUnterbruchAntrag(gesuch.getAusbildung()))
            .ausbildungUnterbruchs(ausbildungUnterbruchs)
            .build();
    }

    @Transactional
    public AusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntrag(
        final UUID ausbildungUnterbruchAntragId,
        final UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSBDto
    ) {
        var antrag = requireById(ausbildungUnterbruchAntragId);
        final var statusFrom = antrag.getStatus();
        antrag = ausbildungUnterbruchAntragMapper.partialUpdate(updateAusbildungUnterbruchAntragSBDto, antrag);
        createStatusprotokollEntry(antrag, statusFrom.toString(), antrag.getKommentarSB());
        if (AusbildungUnterbruchAntragStatus.IS_CLOSED.contains(antrag.getStatus())) {
            notificationService.createAusbildungUnterbruchAntragAkzeptiertAbgelehntNotificationAndSendStdMail(antrag);
            if (
                Objects.nonNull(antrag.getMonateOhneAnspruch())
                && antrag.getMonateOhneAnspruch() > 0
                && Gesuchstatus.GESUCH_VERFUEGUNG_ABGESCHLOSSEN.contains(antrag.getGesuch().getGesuchStatus())
            ) {
                gesuchStatusService.triggerStateMachineEvent(
                    antrag.getGesuch(),
                    GesuchStatusChangeEvent.AUSBILDUNG_UNTERBRUCH_ANTRAG_AKZEPTIEREN_REDUZIERTER_ANSPRUCH
                );
            }
        }

        return ausbildungUnterbruchAntragMapper
            .toSbDto(antrag);
    }

    @Transactional
    public void deleteAllByGesuchId(final UUID gesuchId) {
        final var antrags = ausbildungUnterbruchAntragRepository.getAusbildungUnterbruchAntragsByGesuchId(gesuchId);
        antrags.stream()
            .forEach(
                ausbildungUnterbruchAntrag -> {
                    final var dokuments = ausbildungUnterbruchAntrag.getDokuments();
                    ausbildungUnterbruchAntrag.getDokuments().remove(dokuments);
                    dokuments.forEach(dokumentRepository::delete);
                    ausbildungUnterbruchAntragRepository.delete(ausbildungUnterbruchAntrag);
                }
            );
    }

    public boolean canCreateAusbildungUnterbruchAntrag(final Ausbildung ausbildung) {
        final var gesuch = ausbildung.getLatestGesuch();
        final boolean openAenderungOnLatestGesuchExists =
            GesuchUtil.openAenderungAlreadyExists(gesuch);
        final boolean openAusbildungUnterbruchAntragExists =
            AusbildungUnterbruchAntragUtil.openAusbildungUnterbruchAntragExists(ausbildung);
        return !openAenderungOnLatestGesuchExists && !openAusbildungUnterbruchAntragExists
        && !ausbildung.isUnterbrochen() && gesuch.getGesuchStatus() != Gesuchstatus.IN_BEARBEITUNG_GS;
    }

    @Transactional
    public AusbildungUnterbruchLimitsDto getAusbildungUnterbruchLimits(UUID ausbildungId) {
        final var ausbildung = ausbildungService.requireById(ausbildungId);
        return ausbildungUnterbruchAntragMapper.toLimitsDto(ausbildung.getLatestGesuch());
    }
}
