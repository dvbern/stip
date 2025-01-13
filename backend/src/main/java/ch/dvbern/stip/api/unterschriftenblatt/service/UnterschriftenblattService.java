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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DokumentDeleteUtil;
import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.common.util.DokumentUploadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheHistoryRepository;
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
        final var initialTranche = gesuchTrancheHistoryRepository
            .findOldestHistoricTrancheOfGesuchWhereStatusChangedTo(gesuch.getId(), Gesuchstatus.VERFUEGT);

        GesuchTranche toGetFor;
        if (initialTranche.isPresent()) {
            toGetFor = initialTranche.get();
        } else {
            if (gesuch.getGesuchTranchen().size() != 1) {
                throw new IllegalStateException("There are more than 1 Tranchen but none has been Verfuegt");
            }

            toGetFor = gesuch.getGesuchTranchen().get(0);
        }

        final var famsit = toGetFor.getGesuchFormular().getFamiliensituation();
        if (famsit == null) {
            return List.of();
        }

        final var existingTypes =
            gesuch.getUnterschriftenblaetter().stream().map(Unterschriftenblatt::getDokumentTyp).toList();

        return steuerdatenTabBerechnungsService
            .calculateTabs(famsit)
            .stream()
            .map(steuerdatenTyp -> switch (steuerdatenTyp) {
                case FAMILIE -> UnterschriftenblattDokumentTyp.GEMEINSAM;
                case VATER -> UnterschriftenblattDokumentTyp.VATER;
                case MUTTER -> UnterschriftenblattDokumentTyp.MUTTER;
            })
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
        final var gesucheToTransition = new ArrayList<Gesuch>();
        for (final var gesuch : gesuche) {
            if (areRequiredUnterschriftenblaetterUploaded(gesuch)) {
                gesucheToTransition.add(gesuch);
            }
        }

        if (!gesucheToTransition.isEmpty()) {
            gesuchStatusService.bulkTriggerStateMachineEvent(
                gesucheToTransition,
                GesuchStatusChangeEvent.VERSANDBEREIT
            );
        }
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
}
