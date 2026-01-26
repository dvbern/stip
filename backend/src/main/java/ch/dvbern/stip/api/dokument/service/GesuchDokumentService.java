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
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatusChangeEvent;
import ch.dvbern.stip.api.dokument.util.DokumentOfJahreswertUtil;
import ch.dvbern.stip.api.dokument.util.GesuchDokumentCopyUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.generated.dto.GesuchDokumentAblehnenRequestDto;
import ch.dvbern.stip.generated.dto.NullableGesuchDokumentDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import static ch.dvbern.stip.api.common.util.DokumentDownloadConstants.GESUCH_DOKUMENT_PATH;

@Slf4j
@RequestScoped
@RequiredArgsConstructor
@UnlessBuildProfile("test")
public class GesuchDokumentService {
    private final GesuchDokumentMapper gesuchDokumentMapper;
    private final DokumentRepository dokumentRepository;
    private final GesuchDokumentRepository gesuchDokumentRepository;
    private final CustomDokumentTypRepository customDocumentTypRepository;
    private final GesuchRepository gesuchRepository;
    private final GesuchTrancheRepository gesuchTrancheRepository;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final GesuchDokumentstatusService gesuchDokumentstatusService;
    private final RequiredDokumentService requiredDokumentService;
    private final Antivirus antivirus;
    private final GesuchDokumentKommentarRepository gesuchDokumentKommentarRepository;
    private final DokumentHistoryRepository dokumentHistoryRepository;
    private final GesuchTrancheHistoryService gesuchTrancheHistoryService;
    private final DokumentUploadService dokumentUploadService;
    private final DokumentDownloadService dokumentDownloadService;
    private final DokumentDeleteService dokumentDeleteService;

    @Transactional
    public void setGesuchDokumentOfDokumentTypToAusstehend(final UUID gesuchTrancheId, final DokumentTyp dokumentTyp) {
        final var gesuchTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);

        final var gesuchDokumentOpt = gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(gDok -> gDok.getDokumentTyp() == dokumentTyp)
            .findFirst();

        if (gesuchDokumentOpt.isEmpty()) {
            return;
        }
        final var gesuchDokument = gesuchDokumentOpt.get();

        gesuchDokument.setStatus(GesuchDokumentStatus.AUSSTEHEND);
        gesuchDokumentRepository.persist(gesuchDokument);
    }

    @Transactional
    public void setGesuchDokumentOfCustomDokumentTypToAusstehend(final UUID customDokumentTypId) {
        customDocumentTypRepository.requireById(customDokumentTypId)
            .getGesuchDokument()
            .setStatus(GesuchDokumentStatus.AUSSTEHEND);
    }

    @Transactional
    public Uni<Response> getUploadCustomDokumentUni(
        final UUID customDokumentTypId,
        final FileUpload fileUpload
    ) {
        final var customDokumentTyp = customDocumentTypRepository.findById(customDokumentTypId);
        customDokumentTyp.getGesuchDokument().getGesuchTranche();
        return dokumentUploadService.validateScanUploadDokument(
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
        return dokumentUploadService.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            GESUCH_DOKUMENT_PATH,
            objectId -> {
                uploadDokument(
                    gesuchTrancheId,
                    dokumentTyp,
                    fileUpload,
                    objectId
                );

                synchroniseGesuchDokumente(gesuchTrancheId, dokumentTyp);
            },
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

        gesuchDokument.addDokument(dokument);

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
        gesuchDokument.addDokument(dokument);
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
    public NullableGesuchDokumentDto findGesuchDokumentForCustomTypGS(
        final UUID customDokumentTypId
    ) {
        var gesuchDokumentOptional =
            gesuchDokumentRepository.findByCustomDokumentTyp(customDokumentTypId);

        if (gesuchDokumentOptional.isPresent()) {
            final var gesuchDokument = gesuchDokumentOptional.get();

            final var gesuchTranche = gesuchTrancheHistoryService
                .getCurrentOrHistoricalTrancheForGS(gesuchDokument.getGesuchTranche().getId());
            final var dto = gesuchTranche.getGesuchDokuments()
                .stream()
                .filter(
                    gDok -> Objects.equals(
                        gDok.getId(),
                        gesuchDokument.getId()
                    )
                )
                .findFirst()
                .map(gesuchDokumentMapper::toDto)
                .orElse(null);
            return new NullableGesuchDokumentDto(dto);

        }
        return new NullableGesuchDokumentDto(null);
    }

    @Transactional
    public NullableGesuchDokumentDto findGesuchDokumentForCustomTypSB(
        final UUID customDokumentTypId
    ) {
        final var gesuchDokument =
            gesuchDokumentRepository.findByCustomDokumentTyp(customDokumentTypId);

        final var dto = gesuchDokument.map(gesuchDokumentMapper::toDto).orElse(null);
        return new NullableGesuchDokumentDto(dto);
    }

    @Transactional
    public NullableGesuchDokumentDto findGesuchDokumentForTypGS(
        final UUID gesuchTrancheId,
        final DokumentTyp dokumentTyp
    ) {
        final var gesuchTranche = gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(gesuchTrancheId);

        final var gesuchDokumentOpt = gesuchTranche.getGesuchDokuments()
            .stream()
            .filter(gDok -> gDok.getDokumentTyp() == dokumentTyp)
            .findFirst();
        final var dto = gesuchDokumentOpt.map(gesuchDokumentMapper::toDto).orElse(null);
        return new NullableGesuchDokumentDto(dto);
    }

    @Transactional
    public NullableGesuchDokumentDto findGesuchDokumentForTypSB(
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

    @Transactional
    public void removeAllDokumentsForGesuchTranche(final UUID gesuchTrancheId) {
        gesuchDokumentRepository.findAllForGesuchTranche(gesuchTrancheId)
            .forEach(
                gesuchDokument -> {
                    gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(gesuchDokument.getId());
                    removeGesuchDokument(gesuchDokument.getId());
                }
            );
    }

    @Transactional
    public void removeAllDokumentsOfTypeAndObjectIdForAllTranchen(
        final Gesuch gesuch,
        final DokumentTyp dokumentTyp,
        final String objectId
    ) {
        gesuchDokumentRepository.getAllOfTypeForGesuch(gesuch.getId(), dokumentTyp)
            .forEach(
                gesuchDokument -> {
                    gesuchDokument.getGesuchDokumentKommentare().clear();
                    gesuchDokumentKommentarRepository.deleteAllByGesuchDokumentId(gesuchDokument.getId());

                    final var dokumenteToDelete = new ArrayList<Dokument>();
                    for (final var dokument : gesuchDokument.getDokumente()) {
                        if (!dokument.getObjectId().equals(objectId)) {
                            continue;
                        }

                        // On removal of a Dokument we always reset the Status to AUSSTEHEND
                        dokument.getGesuchDokument().setStatus(GesuchDokumentStatus.AUSSTEHEND);
                        dokumenteToDelete.add(dokument);
                    }

                    dokumenteToDelete.forEach(this::removeDokument);

                    if (gesuchDokument.getDokumente().isEmpty()) {
                        removeGesuchDokument(gesuchDokument.getId());
                    }
                }
            );
    }

    private static void validateGesuchAndTrancheAreInCorrectStateOrElseThrow(final GesuchDokument gesuchDokument) {
        if (gesuchDokument.getGesuchTranche().getTyp() == GesuchTrancheTyp.AENDERUNG) {
            gesuchTrancheStatusIsOrElseThrow(gesuchDokument.getGesuchTranche(), GesuchTrancheStatus.UEBERPRUEFEN);
        } else {
            gesuchstatusIsOrElseThrow(gesuchDokument.getGesuchTranche().getGesuch(), Gesuchstatus.IN_BEARBEITUNG_SB);
        }
    }

    private static void gesuchstatusIsOrElseThrow(final Gesuch gesuch, final Gesuchstatus statusToVerify) {
        if (gesuch.getGesuchStatus() != statusToVerify) {
            throw new IllegalStateException(
                "Gesuchstatus " + gesuch.getGesuchStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    private static void gesuchTrancheStatusIsOrElseThrow(
        final GesuchTranche aenderung,
        final GesuchTrancheStatus statusToVerify
    ) {
        if (aenderung.getStatus() != statusToVerify) {
            throw new IllegalStateException(
                "GesuchTrancheStatus " + aenderung.getStatus().toString() + " not " + statusToVerify.toString()
            );
        }
    }

    @Transactional
    public void gesuchDokumentAblehnen(final UUID gesuchDokumentId, final GesuchDokumentAblehnenRequestDto dto) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        gesuchDokumentAblehnen(gesuchDokument, dto);
    }

    @Transactional
    public void gesuchDokumentAblehnen(
        final GesuchDokument gesuchDokument,
        final GesuchDokumentAblehnenRequestDto dto
    ) {
        validateGesuchAndTrancheAreInCorrectStateOrElseThrow(gesuchDokument);
        gesuchDokumentstatusService.triggerStatusChangeWithComment(
            gesuchDokument,
            GesuchDokumentStatusChangeEvent.ABGELEHNT,
            dto.getKommentar()
        );
    }

    @Transactional
    public void gesuchDokumentAkzeptieren(final UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.requireById(gesuchDokumentId);
        gesuchDokumentAkzeptieren(gesuchDokument);
    }

    @Transactional
    public void gesuchDokumentAkzeptieren(final GesuchDokument gesuchDokument) {
        validateGesuchAndTrancheAreInCorrectStateOrElseThrow(gesuchDokument);
        gesuchDokumentstatusService.triggerStatusChange(
            gesuchDokument,
            GesuchDokumentStatusChangeEvent.AKZEPTIERT
        );
        removeNachfristDokumenteIfAllAccepted(gesuchDokument);
    }

    private void removeNachfristDokumenteIfAllAccepted(GesuchDokument gesuchDokument) {
        final var gesuch = gesuchDokument.getGesuchTranche().getGesuch();
        final var allGesuchDokuments = new ArrayList<GesuchDokument>();
        gesuch.getGesuchTranchen().stream().map(GesuchTranche::getGesuchDokuments).forEach(allGesuchDokuments::addAll);
        final var allAccepted = allGesuchDokuments.stream()
            .allMatch(dok -> dok.getStatus().equals(GesuchDokumentStatus.AKZEPTIERT));
        if (!allAccepted) {
            return;
        }

        gesuch.setNachfristDokumente(null);
    }

    public boolean canDeleteDokumentFromS3(final Dokument dokument, final GesuchTranche trancheToBeDeletedFrom) {
        // Count distinct historical Tranchen that reference the dokument
        final var distinctHistoricalGesuchTranchenCount = dokumentHistoryRepository
            .countDistinctTranchenThatReferenceDokumentByObjectId(dokument.getObjectId());

        // If more than 1 distinct Tranche reference it, we cannot delete the dokument from S3
        if (distinctHistoricalGesuchTranchenCount > 1) {
            return false;
        }

        return switch (trancheToBeDeletedFrom.getTyp()) {
            case TRANCHE -> trancheToBeDeletedFrom.getGesuch().getGesuchStatus() == Gesuchstatus.IN_BEARBEITUNG_GS;
            case AENDERUNG -> trancheToBeDeletedFrom.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS;
        };
    }

    public void executeDeleteDokumentsFromS3(final List<String> objectIds) {
        dokumentDeleteService.executeDeleteDokumentsFromS3(
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
    public String deleteDokument(final UUID dokumentId) {
        Dokument dokument = dokumentRepository.findByIdOptional(dokumentId).orElseThrow(NotFoundException::new);
        dokument.getGesuchDokument().getDokumente().remove(dokument);
        dokument.setGesuchDokument(null);
        final var dokumentObjectId = dokument.getObjectId();
        dokumentRepository.delete(dokument);

        return dokumentObjectId;
    }

    @Transactional
    public void deleteDokumenteForTranche(final UUID trancheId, final List<DokumentTyp> dokumentTyps) {
        final var gesuchDokumente = gesuchDokumentRepository.findByDokumentTyps(trancheId, dokumentTyps).toList();
        final var dokumente = gesuchDokumente.stream()
            .flatMap(gesuchDokument -> gesuchDokument.getDokumente().stream())
            .toList();

        dokumente.forEach(this::removeDokument);

        for (final var gesuchDokument : gesuchDokumente) {
            if (gesuchDokument.getStatus() != GesuchDokumentStatus.AUSSTEHEND) {
                gesuchDokumentstatusService
                    .triggerStatusChange(gesuchDokument, GesuchDokumentStatusChangeEvent.AUSSTEHEND);
            }
        }
    }

    @Transactional
    public void removeDokument(final UUID dokumentId) {
        // Set GesuchDokument to ausstehend
        final var dokument = dokumentRepository.requireById(dokumentId);
        dokument.getGesuchDokument().setStatus(GesuchDokumentStatus.AUSSTEHEND);

        // Remove Dokument on all Tranchen if Jahreswert Dokument, otherwise only this
        final var gesuchDokument = dokument.getGesuchDokument();
        if (DokumentOfJahreswertUtil.isDokumentOfJahreswert(gesuchDokument.getDokumentTyp())) {
            removeAllDokumentsOfTypeAndObjectIdForAllTranchen(
                gesuchDokument.getGesuchTranche().getGesuch(),
                gesuchDokument.getDokumentTyp(),
                dokument.getObjectId()
            );
        } else {
            removeDokument(dokument);
        }
    }

    @Transactional
    public void removeDokument(final Dokument dokument) {
        final var dokumentObjectIds = new ArrayList<String>();

        if (
            canDeleteDokumentFromS3(dokument, dokument.getGesuchDokument().getGesuchTranche())
        ) {
            dokumentObjectIds.add(dokument.getObjectId());
        }

        dokumentRepository.delete(dokument);

        dokument.getGesuchDokument().getDokumente().remove(dokument);
        dropGesuchDokumentIfNotRequredAnymore(dokument.getGesuchDokument());
        executeDeleteDokumentsFromS3(dokumentObjectIds);
    }

    @Transactional
    public void removeGesuchDokument(final UUID gesuchDokumentId) {
        final var gesuchDokument = gesuchDokumentRepository.findByIdOptional(gesuchDokumentId);
        gesuchDokument.ifPresent(this::removeGesuchDokument);
    }

    @Transactional
    public void removeGesuchDokument(final GesuchDokument gesuchDokument) {
        final var dokuments = gesuchDokument.getDokumente();

        List<String> dokumentObjectIds = new ArrayList<>();

        // Using Iterator to be able to remove while looping
        for (Iterator<Dokument> iterator = dokuments.iterator(); iterator.hasNext();) {
            final var dokument = iterator.next();
            iterator.remove();
            dokumentRepository.delete(dokument);
            if (
                canDeleteDokumentFromS3(dokument, gesuchDokument.getGesuchTranche())
            ) {
                dokumentObjectIds.add(dokument.getObjectId());
            }
        }
        if (gesuchDokument.getDokumente().isEmpty()) {
            gesuchDokumentRepository.deleteById(gesuchDokument.getId());
            gesuchDokument.getGesuchTranche().getGesuchDokuments().remove(gesuchDokument);
        }
        if (!dokumentObjectIds.isEmpty()) {
            executeDeleteDokumentsFromS3(dokumentObjectIds);
        }
    }

    public GesuchDokument getGesuchDokumentOfDokument(UUID dokumentId) {
        return dokumentRepository.requireById(dokumentId).getGesuchDokument();
    }

    @Transactional
    public void setAbgelehnteDokumenteToAusstehendForGesuch(final Gesuch gesuch) {
        // Query for these instead of iterating "in memory" because abgelehnteGesuchDokumente are lazy loaded
        // and this results in only loading the ones we need instead of all
        final var abgelehnteGesuchDokumente = gesuchDokumentRepository
            .getAllForGesuchInStatus(gesuch, GesuchDokumentStatus.ABGELEHNT)
            .toList();

        for (var gesuchdokument : abgelehnteGesuchDokumente) {
            gesuchDokumentstatusService
                .triggerStatusChangeNoComment(gesuchdokument, GesuchDokumentStatusChangeEvent.AUSSTEHEND);
        }
    }

    public void deleteFilesOfAbgelehnteGesuchDokumenteForGesuch(List<GesuchDokument> abgelehnteGesuchDokumente) {
        // Query for these instead of iterating "in memory" because filteredGesuchDokumente are lazy loaded
        // and this results in only loading the ones we need instead of all
        final var dokumenteToDeleteFromS3 = new ArrayList<String>();
        for (var gesuchdokument : abgelehnteGesuchDokumente) {
            final var dokumentList = gesuchdokument.getDokumente().stream().toList();
            for (var dokument : dokumentList) {
                gesuchdokument.getDokumente().remove(dokument);

                // If no other references to this physical document exist, delete it as well
                dokumentRepository.delete(dokument);
                if (canDeleteDokumentFromS3(dokument, gesuchdokument.getGesuchTranche())) {
                    dokumenteToDeleteFromS3.add(dokument.getObjectId());
                }
            }
        }

        executeDeleteDokumentsFromS3(dokumenteToDeleteFromS3);
    }

    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentHistoryRepository.findInHistoryById(dokumentId);

        return dokumentDownloadService.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            GESUCH_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    public void checkIfDokumentExists(final UUID dokumentId) {
        var dokument = dokumentRepository.findById(dokumentId);
        if (Objects.isNull(dokument)) {
            dokument = dokumentHistoryRepository.findInHistoryById(dokumentId);
        }
        if (Objects.isNull(dokument)) {
            throw new NotFoundException();
        }
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

    @Transactional
    public void synchroniseGesuchDokumente(final UUID gesuchTrancheId, final DokumentTyp dokumentTyp) {
        if (!isDokumentOfJahreswert(dokumentTyp)) {
            // No synchronisation is needed if not a Dokument on a Jahreswert field
            return;
        }

        final var sourceTranche = gesuchTrancheRepository.requireById(gesuchTrancheId);
        if (sourceTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            // No synchronisation is needed for Aenderungen
            return;
        }

        final var targetTranchen = sourceTranche.getGesuch()
            .getTranchenTranchen()
            .filter(gesuchTranche -> !gesuchTranche.getId().equals(sourceTranche.getId()))
            .toList();

        final var sourceGesuchDokumentOpt = gesuchDokumentRepository.findByGesuchTrancheAndDokumentTyp(
            gesuchTrancheId,
            dokumentTyp
        );

        for (final var targetTranche : targetTranchen) {
            final var targetGesuchDokument = targetTranche.getGesuchDokuments()
                .stream()
                .filter(gesuchDokument -> gesuchDokument.getDokumentTyp() == dokumentTyp)
                .findFirst();

            targetGesuchDokument.ifPresent(gesuchDokument -> {
                // This is needed, otherwise we're modifying the same list during iteration
                final var dokumente = gesuchDokument.getDokumente().stream().toList();
                dokumente.forEach(this::removeDokument);
            });

            sourceGesuchDokumentOpt.ifPresent(sourceGesuchDokument -> {
                // Find GesuchDokument for this type on targetTranche, if none exists create a new one
                final var newGesuchDokument = targetGesuchDokument
                    .orElseGet(() -> GesuchDokumentCopyUtil.createCopy(sourceGesuchDokument, targetTranche));

                GesuchDokumentCopyUtil.copyDokumente(newGesuchDokument, sourceGesuchDokument.getDokumente());
                targetTranche.getGesuchDokuments().add(newGesuchDokument);
            });
        }
    }

    public GesuchDokument copyGesuchDokument(final GesuchDokument sourceDokument, final GesuchTranche forTranche) {
        final var newDokument = GesuchDokumentCopyUtil.createCopy(sourceDokument, forTranche);
        gesuchDokumentRepository.persistAndFlush(newDokument);
        return newDokument;
    }

    public List<Dokument> copyDokumente(final GesuchDokument targetGesuchDokument, List<Dokument> toCopy) {
        return GesuchDokumentCopyUtil.copyDokumente(targetGesuchDokument, toCopy);
    }

    public boolean isDokumentOfJahreswert(final DokumentTyp dokumentTyp) {
        return DokumentOfJahreswertUtil.isDokumentOfJahreswert(dokumentTyp);
    }

    public Set<DokumentTyp> getDokumentTypsOfJahreswerte() {
        return DokumentOfJahreswertUtil.getDokumentTypsOfJahreswerte();
    }
}
