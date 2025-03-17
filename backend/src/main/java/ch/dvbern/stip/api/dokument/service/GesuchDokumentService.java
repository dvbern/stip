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

package ch.dvbern.stip.api.dokument.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DokumentDeleteUtil;
import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.common.util.DokumentUploadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentKommentarDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_ADMIN;
import static ch.dvbern.stip.api.common.util.OidcConstants.ROLE_SACHBEARBEITER;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
public class GesuchDokumentService {
    private static final String GESUCH_DOKUMENT_PATH = "gesuch/";

    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final DokumentMapper dokumentMapper;
    private final DokumentRepository dokumentRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final CustomDokumentTypRepository customDocumentTypRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final DokumentstatusService dokumentstatusService;
    private final RequiredDokumentService requiredDokumentService;
    private final Antivirus antivirus;
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;

    @Transactional
    public List<GesuchDokumentKommentarDto> getGesuchDokumentKommentarsByGesuchDokumentId(
        UUID gesuchDokumentId
    ) {
        return dokumentstatusService.getGesuchDokumentKommentareByGesuchDokumentId(gesuchDokumentId);
    }

    @Transactional
    public Uni<Response> getUploadCustomDokumentUni(
        final UUID customDokumentTypId,
        final FileUpload fileUpload
    ) {
        final var customDokumentTyp = customDocumentTypRepository.findById(customDokumentTypId);
        customDokumentTyp.getGesuchDokument().getGesuchTranche();
        return DokumentUploadUtil.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            GESUCH_DOKUMENT_PATH,
            objectId -> uploadCustomDokument(
                customDokumentTypId,
                fileUpload,
                objectId
            ),
            throwable -> LOG.error(throwable.getMessage())
        );
    }

    @Transactional
    public Uni<Response> getUploadDokumentUni(
        final DokumentTyp dokumentTyp,
        final UUID gesuchTrancheId,
        final FileUpload fileUpload
    ) {
        return DokumentUploadUtil.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            GESUCH_DOKUMENT_PATH,
            objectId -> uploadDokument(
                gesuchTrancheId,
                dokumentTyp,
                fileUpload,
                objectId
            ),
            throwable -> LOG.error(throwable.getMessage())
        );
    }

    @Transactional
    public void uploadDokument(
        final UUID gesuchTrancheId,
        final DokumentTyp dokumentTyp,
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var gesuchTranche = gesuchTrancheRepository
            .findByIdOptional(gesuchTrancheId)
            .orElseThrow(NotFoundException::new);

        final var gesuchDokument = gesuchDokumentRepository
            .findByGesuchTrancheAndDokumentTyp(gesuchTranche.getId(), dokumentTyp)
            .orElseGet(() -> createGesuchDokument(gesuchTranche, dokumentTyp));

        final var dokument = new Dokument()
            .setFilename(fileUpload.fileName())
            .setFilesize(String.valueOf(fileUpload.size()))
            .setFilepath(GESUCH_DOKUMENT_PATH)
            .setObjectId(objectId);

        dokument.getGesuchDokumente().add(gesuchDokument);
        gesuchDokument.getDokumente().add(dokument);

        dokumentRepository.persist(dokument);
    }

    @Transactional
    public void uploadCustomDokument(
        final UUID customDokumentTypId,
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var gesuchDokument =
            gesuchDokumentRepository
                .findByCustomDokumentTyp(customDokumentTypId)
                .orElseThrow(NotFoundException::new);
        final var dokument = new Dokument();
        dokument.getGesuchDokumente().add(gesuchDokument);
        gesuchDokument.getDokumente().add(dokument);
        dokument.setFilename(fileUpload.fileName());
        dokument.setObjectId(objectId);
        dokument.setFilesize(String.valueOf(fileUpload.size()));
        dokument.setFilepath(GESUCH_DOKUMENT_PATH);
        dokumentRepository.persist(dokument);
    }

    @Transactional
    public boolean customDokumentHasGesuchDokuments(UUID customDokumentTypeId) {
        return gesuchDokumentRepository.customDokumentHasGesuchDokuments(customDokumentTypeId);
    }

    @Transactional
    public NullableGesuchDokumentDto findGesuchDokumentForCustomTyp(
        final UUID customDokumentTypId
    ) {
        final var gesuchDokument =
            gesuchDokumentRepository.findByCustomDokumentTyp(customDokumentTypId);

        final var dto = gesuchDokument.map(gesuchDokumentMapper::toDto).orElse(null);
        return new NullableGesuchDokumentDto(dto);
    }

    @Transactional
    public NullableGesuchDokumentDto findGesuchDokumentForTyp(
        final UUID gesuchTrancheId,
        final DokumentTyp dokumentTyp
    ) {
        final var gesuchDokument =
            gesuchDokumentRepository.findByGesuchTrancheAndDokumentTyp(gesuchTrancheId, dokumentTyp);
        final var dto = gesuchDokument.map(gesuchDokumentMapper::toDto).orElse(null);
        return new NullableGesuchDokumentDto(dto);
    }

    public void removeAllGesuchDokumentsForGesuch(final UUID gesuchId) {
        gesuchRepository.requireById(gesuchId)
            .getGesuchTranchen()
            .forEach(gesuchTranche -> removeAllDokumentsForGesuchTranche(gesuchTranche.getId()));
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void removeAllDokumentsForGesuchTranche(final UUID gesuchTrancheId) {
        gesuchDokumentRepository.findAllForGesuchTranche(gesuchTrancheId)
            .forEach(
                gesuchDokument -> {
                    gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(gesuchDokument.getId());
                    removeGesuchDokument(gesuchDokument.getId());
                }
            );
    }

    private static void checkGesuchOrAenderungStatusOrElseThrow(final GesuchDokument gesuchDokument) {
        if (gesuchDokument.getGesuchTranche().getTyp() == GesuchTrancheTyp.AENDERUNG) {
            aenderungIsNotOrElseThrow(gesuchDokument.getGesuchTranche(), GesuchTrancheStatus.UEBERPRUEFEN);
        } else {
            gesuchstatusIsNotOrElseThrow(gesuchDokument.getGesuchTranche().getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB);
        }
    }

    private static void gesuchstatusIsNotOrElseThrow(final Gesuch gesuch, final Gesuchstatus statusToVerify) {
        if (gesuch.getGesuchStatus() != statusToVerify) {
            throw new IllegalStateException(
                "Gesuchstatus " + gesuch.getGesuchStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    private static void aenderungIsNotOrElseThrow(
        final GesuchTranche aenderung,
        final GesuchTrancheStatus statusToVerify
    ) {
        if (aenderung.getStatus() != statusToVerify) {
            throw new IllegalStateException(
                "GesuchTrancheStatus " + aenderung.getStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    @RolesAllowed({ ROLE_SACHBEARBEITER, ROLE_ADMIN })
    @Transactional
    public void gesuchDokumentAblehnen(final UUID gesuchDokumentId, final GesuchDokumentAblehnenRequestDto dto) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        checkGesuchOrAenderungStatusOrElseThrow(gesuchDokument);
        dokumentstatusService.triggerStatusChangeWithComment(
            gesuchDokument,
            DokumentstatusChangeEvent.ABGELEHNT,
            dto.getKommentar()
        );
    }

    @Transactional
    public void gesuchDokumentAkzeptieren(final UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        checkGesuchOrAenderungStatusOrElseThrow(gesuchDokument);
        dokumentstatusService.triggerStatusChange(
            gesuchDokument,
            DokumentstatusChangeEvent.AKZEPTIERT
        );
    }

    public void executeDeleteDokumentsFromS3(final List<String> objectIds) {
        DokumentDeleteUtil.executeDeleteDokumentsFromS3(
            s3,
            configService.getBucketName(),
            objectIds.stream()
                .map(objectId -> GESUCH_DOKUMENT_PATH + objectId)
                .toList()
        );
    }

    private void dropGesuchDokumentIfNotRequredAnymore(GesuchDokument gesuchDokument) {
        if (!requiredDokumentService.isGesuchDokumentRequired(gesuchDokument)) {
            gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(gesuchDokument.getId());
            gesuchDokumentRepository.dropGesuchDokumentIfNoDokumente(gesuchDokument.getId());
        }
    }

    @Transactional
    public String deleteDokument(final UUID dokumentId, final UUID trancheId) {
        Dokument dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var dokumentObjectId = dokument.getObjectId();
        for (
            final var gesuchDokument : dokument.getGesuchDokumente()
                .stream()
                .filter(dok -> dok.getGesuchTranche().getId().equals(trancheId))
                .toList()
        ) {
            gesuchDokument.getDokumente().remove(dokument);
        }

        return dokumentObjectId;
    }

    @Transactional
    public void removeDokument(final UUID dokumentId) {
        Dokument dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        final var dokumentObjectId = dokument.getObjectId();

        for (final var gesuchDokument : dokument.getGesuchDokumente()) {
            gesuchDokument.getDokumente().remove(dokument);
            dropGesuchDokumentIfNotRequredAnymore(gesuchDokument);
        }
        if (dokument.getGesuchDokumente().isEmpty()) {
            executeDeleteDokumentsFromS3(List.of(dokumentObjectId));
        }
    }

    @Transactional
    public void removeGesuchDokument(final UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        removeGesuchDokument(gesuchDokument);
    }

    @Transactional
    public void removeGesuchDokument(final GesuchDokument gesuchDokument) {
        final var dokuments = gesuchDokument.getDokumente();

        List<String> dokumentObjectIds = new ArrayList<>();
        // Using Iterator to be able to remove while looping
        for (Iterator<Dokument> iterator = dokuments.iterator(); iterator.hasNext();) {
            final var dokument = iterator.next();
            iterator.remove();
            if (dokument.getGesuchDokumente().isEmpty()) {
                dokumentRepository.delete(dokument);
                dokumentObjectIds.add(dokument.getObjectId());
            }
        }
        if (gesuchDokument.getDokumente().isEmpty()) {
            gesuchDokumentRepository.delete(gesuchDokument);
        }
        if (!dokumentObjectIds.isEmpty()) {
            executeDeleteDokumentsFromS3(dokumentObjectIds);
        }
    }

    @Transactional
    public void setAbgelehnteDokumenteToAusstehendForGesuch(final Gesuch gesuch) {
        // Query for these instead of iterating "in memory" because abgelehnteGesuchDokumente are lazy loaded
        // and this results in only loading the ones we need instead of all
        final var abgelehnteGesuchDokumente = gesuchDokumentRepository
            .getAllForGesuchInStatus(gesuch, Dokumentstatus.ABGELEHNT)
            .toList();

        for (var gesuchdokument : abgelehnteGesuchDokumente) {
            dokumentstatusService.triggerStatusChangeNoComment(gesuchdokument, DokumentstatusChangeEvent.AUSSTEHEND);
        }
    }

    public void deleteFilesOfAbgelehnteGesuchDokumenteForGesuch(List<GesuchDokument> abgelehnteGesuchDokumente) {
        // Query for these instead of iterating "in memory" because filteredGesuchDokumente are lazy loaded
        // and this results in only loading the ones we need instead of all
        final var dokumenteToDeleteFromS3 = new ArrayList<String>();
        for (var gesuchdokument : abgelehnteGesuchDokumente) {
            final var dokumentList = gesuchdokument.getDokumente().stream().toList();
            for (var dokument : dokumentList) {
                dokument.getGesuchDokumente().remove(gesuchdokument);
                gesuchdokument.getDokumente().remove(dokument);

                // If no other references to this physical document exist, delete it as well
                if (dokument.getGesuchDokumente().isEmpty()) {
                    dokumenteToDeleteFromS3.add(dokument.getObjectId());
                    dokumentRepository.delete(dokument);
                }
            }
        }

        executeDeleteDokumentsFromS3(dokumenteToDeleteFromS3);
    }

    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);

        return DokumentDownloadUtil.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            GESUCH_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    public void checkIfDokumentExists(final UUID dokumentId) {
        dokumentRepository.requireById(dokumentId);
    }

    private GesuchDokument createGesuchDokument(final GesuchTranche gesuchTranche, final DokumentTyp dokumentTyp) {
        GesuchDokument gesuchDokument =
            new GesuchDokument().setGesuchTranche(gesuchTranche).setDokumentTyp(dokumentTyp);
        gesuchDokumentRepository.persist(gesuchDokument);
        return gesuchDokument;
    }

    @Transactional
    public GesuchDokument createGesuchDokument(
        final GesuchTranche gesuchTranche,
        final CustomDokumentTyp customDokumentTyp
    ) {
        GesuchDokument gesuchDokument =
            new GesuchDokument().setGesuchTranche(gesuchTranche).setCustomDokumentTyp(customDokumentTyp);
        gesuchDokumentRepository.persist(gesuchDokument);
        return gesuchDokument;
    }
}
