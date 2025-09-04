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

package ch.dvbern.stip.api.unterschriftenblatt.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.DokumentDeleteUtil;
import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.common.util.DokumentUploadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchhistory.repo.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.api.unterschriftenblatt.entity.Unterschriftenblatt;
import ch.dvbern.stip.api.unterschriftenblatt.repo.UnterschriftenblattRepository;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.generated.dto.UnterschriftenblattDokumentDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class UnterschriftenblattService {
    public static final String UNTERSCHRIFTENBLATT_DOKUMENT_PATH = "unterschriftenblatt/";

    private final GesuchRepository gesuchRepository;
    private final UnterschriftenblattRepository unterschriftenblattRepository;
    private final DokumentRepository dokumentRepository;
    private final Antivirus antivirus;
    private final ConfigService configService;
    private final S3AsyncClient s3;
    private final GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    private final SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;
    private final UnterschriftenblattMapper unterschriftenblattMapper;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchHistoryRepository gesuchHistoryRepository;

    @Transactional
    public Uni<Response> getUploadUnterschriftenblattUni(
        final UnterschriftenblattDokumentTyp dokumentTyp,
        final UUID gesuchId,
        final FileUpload fileUpload
    ) {
        return DokumentUploadUtil.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            UNTERSCHRIFTENBLATT_DOKUMENT_PATH,
            objectId -> uploadDokument(
                gesuchId,
                dokumentTyp,
                fileUpload,
                objectId
            ),
            throwable -> LOG.error(throwable.getMessage())
        );
    }

    @Transactional
    public void uploadDokument(
        final UUID gesuchId,
        final UnterschriftenblattDokumentTyp dokumentTyp,
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var gesuch = gesuchRepository.requireById(gesuchId);
        final var unterschriftenblatt = unterschriftenblattRepository
            .findByGesuchAndDokumentTyp(gesuchId, dokumentTyp)
            .orElseGet(() -> createUnterschriftenblatt(gesuch, dokumentTyp));

        final var dokument = new Dokument()
            .setFilename(fileUpload.fileName())
            .setFilesize(String.valueOf(fileUpload.size()))
            .setFilepath(UNTERSCHRIFTENBLATT_DOKUMENT_PATH)
            .setObjectId(objectId);

        unterschriftenblatt.getDokumente().add(dokument);
        dokumentRepository.persist(dokument);
    }

    @Transactional
    public List<UnterschriftenblattDokumentDto> getForGesuchAndType(
        final UUID gesuchId
    ) {
        final var found = unterschriftenblattRepository.requireForGesuch(gesuchId);
        return found.map(unterschriftenblattMapper::toDto).toList();
    }

    @Transactional
    public List<UnterschriftenblattDokumentTyp> getUnterschriftenblaetterToUpload(final Gesuch gesuch) {
        final var existingTypes =
            gesuch.getUnterschriftenblaetter().stream().map(Unterschriftenblatt::getDokumentTyp).toList();

        return getRequiredUnterschriftenblaetter(gesuch)
            .filter(unterschriftenblattDokumentTyp -> !existingTypes.contains(unterschriftenblattDokumentTyp))
            .toList();
    }

    @Transactional
    public boolean areRequiredUnterschriftenblaetterUploaded(final Gesuch gesuch) {
        final var requiredUnterschriftenblaetter = new HashSet<>(getUnterschriftenblaetterToUpload(gesuch));

        unterschriftenblattRepository
            .requireForGesuch(gesuch.getId())
            .forEach(present -> requiredUnterschriftenblaetter.remove(present.getDokumentTyp()));

        return requiredUnterschriftenblaetter.isEmpty();
    }

    @Transactional
    public void checkForUnterschriftenblaetterOnAllGesuche() {
        final var gesuche = gesuchRepository.getAllWartenAufUnterschriftenblatt();

        // This is the list of historic Gesuche, select the actual ones
        final var changedSevenDaysAgo = new HashSet<>(
            gesuchHistoryRepository.getWhereStatusChangeHappenedBefore(
                gesuche.stream().map(AbstractEntity::getId).toList(),
                Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT,
                LocalDateTime.now().minusDays(7)
            ).map(AbstractEntity::getId).toList()
        );

        final var toUpdate = gesuche.stream().filter(gesuch -> changedSevenDaysAgo.contains(gesuch.getId())).toList();
        if (!toUpdate.isEmpty()) {
            gesuchStatusService.bulkTriggerStateMachineEvent(
                toUpdate,
                GesuchStatusChangeEvent.VERSANDBEREIT
            );
        }
    }

    @Transactional
    public boolean requiredUnterschriftenblaetterExistOrIsVerfuegt(final Gesuch gesuch) {
        if (gesuch.isVerfuegt()) {
            return true;
        }

        final var required = getUnterschriftenblaetterToUpload(gesuch);
        final var existing = unterschriftenblattRepository.findByGesuchAndDokumentTypes(gesuch.getId(), required);

        final var existingSet = existing.map(Unterschriftenblatt::getDokumentTyp).collect(Collectors.toSet());
        return existingSet.containsAll(required);
    }

    private Unterschriftenblatt createUnterschriftenblatt(
        final Gesuch gesuch,
        final UnterschriftenblattDokumentTyp dokumentTyp
    ) {
        final var unterschriftenblatt = new Unterschriftenblatt()
            .setGesuch(gesuch)
            .setDokumentTyp(dokumentTyp);

        unterschriftenblattRepository.persist(unterschriftenblatt);
        return unterschriftenblatt;
    }

    @Transactional
    public void removeDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);
        final var unterschriftenblatt = unterschriftenblattRepository.requireByDokumentId(dokumentId);

        dokumentRepository.delete(dokument);
        unterschriftenblatt.getDokumente().remove(dokument);

        DokumentDeleteUtil.executeDeleteDokumentFromS3(
            s3,
            configService.getBucketName(),
            UNTERSCHRIFTENBLATT_DOKUMENT_PATH + dokument.getObjectId()
        );

        if (unterschriftenblatt.getDokumente().isEmpty()) {
            unterschriftenblattRepository.delete(unterschriftenblatt);
        }
    }

    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);

        return DokumentDownloadUtil.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            UNTERSCHRIFTENBLATT_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    @Transactional
    public void deleteNotRequiredForGesuch(final Gesuch gesuch) {
        final var allTypes = EnumSet.allOf(UnterschriftenblattDokumentTyp.class);
        getRequiredUnterschriftenblaetter(gesuch).forEach(allTypes::remove);

        final var toDelete = unterschriftenblattRepository
            .findByGesuchAndDokumentTypes(gesuch.getId(), allTypes.stream().toList())
            .toList();
        final var toRemoveFromS3 = new ArrayList<String>();

        for (final var unterschriftenblatt : toDelete) {
            toRemoveFromS3.addAll(unterschriftenblatt.getDokumente().stream().map(Dokument::getObjectId).toList());
            gesuch.getUnterschriftenblaetter().remove(unterschriftenblatt);
            unterschriftenblattRepository.delete(unterschriftenblatt);
        }

        DokumentDeleteUtil.executeDeleteDokumentsFromS3(
            s3,
            configService.getBucketName(),
            toRemoveFromS3
        );
    }

    @Transactional
    public Set<UnterschriftenblattDokumentTyp> getExistingUnterschriftenblattTypsForGesuch(final UUID gesuchId) {
        return unterschriftenblattRepository.requireForGesuch(gesuchId)
            .filter(unterschriftenblatt -> !unterschriftenblatt.getDokumente().isEmpty())
            .map(Unterschriftenblatt::getDokumentTyp)
            .collect(Collectors.toSet());
    }

    private Stream<UnterschriftenblattDokumentTyp> getRequiredUnterschriftenblaetter(final Gesuch gesuch) {
        if (gesuch.isVerfuegt()) {
            return gesuch.getUnterschriftenblaetter().stream().map(Unterschriftenblatt::getDokumentTyp);
        }

        final var famsit = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getFamiliensituation();
        if (famsit == null) {
            return Stream.of();
        }

        return steuerdatenTabBerechnungsService
            .calculateTabs(famsit)
            .stream()
            .map(steuerdatenTyp -> switch (steuerdatenTyp) {
                case FAMILIE -> UnterschriftenblattDokumentTyp.GEMEINSAM;
                case VATER -> UnterschriftenblattDokumentTyp.VATER;
                case MUTTER -> UnterschriftenblattDokumentTyp.MUTTER;
            });
    }
}
