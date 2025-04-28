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

package ch.dvbern.stip.api.beschwerdeentscheid.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.beschwerdeentscheid.entity.BeschwerdeEntscheid;
import ch.dvbern.stip.api.beschwerdeentscheid.repo.BeschwerdeEntscheidRepository;
import ch.dvbern.stip.api.beschwerdeverlauf.service.BeschwerdeverlaufService;
import ch.dvbern.stip.api.common.util.DokumentDownloadUtil;
import ch.dvbern.stip.api.common.util.DokumentUploadUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.generated.dto.BeschwerdeEntscheidDto;
import ch.dvbern.stip.generated.dto.BeschwerdeVerlaufEntryCreateDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class BeschwerdeEntscheidService {
    private final BeschwerdeEntscheidRepository beschwerdeEntscheidRepository;
    private final BeschwerdeEntscheidMapper beschwerdeEntscheidMapper;

    public static final String BESCHWERDEENTSCHEID_DOKUMENT_PATH = "beschwerdeentscheid/";
    private static final String BESCHWERDEVERLAUF_TITEL = "Beschwerdeentscheid hochgeladen";

    private final GesuchRepository gesuchRepository;
    private final DokumentRepository dokumentRepository;
    private final Antivirus antivirus;
    private final ConfigService configService;
    private final S3AsyncClient s3;
    private final BeschwerdeverlaufService beschwerdeverlaufService;

    private final GesuchStatusService gesuchStatusService;

    @Transactional
    public Uni<Response> createBeschwerdeEntscheid(
        final UUID gesuchId,
        final String kommentar,
        final Boolean isBeschwerdeErfolgreich,
        final FileUpload fileUpload
    ) {
        var beschwerdeEntscheid = new BeschwerdeEntscheid();
        beschwerdeEntscheid.setBeschwerdeErfolgreich(isBeschwerdeErfolgreich);
        beschwerdeEntscheid.setKommentar(kommentar);
        beschwerdeEntscheid.setGesuch(gesuchRepository.requireById(gesuchId));
        return DokumentUploadUtil.validateScanUploadDokument(
            fileUpload,
            s3,
            configService,
            antivirus,
            BESCHWERDEENTSCHEID_DOKUMENT_PATH,
            objectId -> uploadDokument(
                beschwerdeEntscheid,
                fileUpload,
                objectId
            ),
            throwable -> LOG.error(throwable.getMessage())
        );
    }

    @Transactional
    public List<BeschwerdeEntscheidDto> getAllBeschwerdeEntscheids(final UUID gesuchId) {
        return beschwerdeEntscheidRepository.findByGesuchId(gesuchId)
            .stream()
            .map(beschwerdeEntscheidMapper::toDto)
            .toList();
    }

    @Transactional
    public void uploadDokument(
        final BeschwerdeEntscheid beschwerdeEntscheid,
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var beschwerdeentscheid = createNewBeschwerdeEntscheid(beschwerdeEntscheid);

        final var dokument = new Dokument()
            .setFilename(fileUpload.fileName())
            .setFilesize(String.valueOf(fileUpload.size()))
            .setFilepath(BESCHWERDEENTSCHEID_DOKUMENT_PATH)
            .setObjectId(objectId);

        beschwerdeentscheid.getDokumente().add(dokument);
        dokumentRepository.persist(dokument);

        if (beschwerdeEntscheid.isBeschwerdeErfolgreich()) {
            setGesuchToBereitFuerBearbeitung(beschwerdeentscheid.getGesuch());
        }

        createBeschwerdeverlaufEntry(beschwerdeEntscheid);
    }

    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);
        return DokumentDownloadUtil.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            BESCHWERDEENTSCHEID_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    private void createBeschwerdeverlaufEntry(BeschwerdeEntscheid beschwerdeEntscheid) {
        var createDto = new BeschwerdeVerlaufEntryCreateDto();
        createDto.setBeschwerdeSetTo(beschwerdeEntscheid.getGesuch().isBeschwerdeHaengig());
        createDto.setKommentar(beschwerdeEntscheid.getKommentar());
        beschwerdeverlaufService
            .createBeschwerdeVerlaufEntryIgnoreBeschwerdeHaengigFlag(
                beschwerdeEntscheid.getGesuch().getId(),
                createDto
            );
    }

    private BeschwerdeEntscheid createNewBeschwerdeEntscheid(final BeschwerdeEntscheid beschwerdeEntscheid) {
        beschwerdeEntscheidRepository.persistAndFlush(beschwerdeEntscheid);
        return beschwerdeEntscheid;
    }

    private void setGesuchToBereitFuerBearbeitung(Gesuch gesuch) {
        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
    }

}
