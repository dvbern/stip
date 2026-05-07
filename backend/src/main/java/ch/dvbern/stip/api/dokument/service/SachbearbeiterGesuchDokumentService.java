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

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.DokumentDownloadConstants;
import ch.dvbern.stip.api.config.StipConfig;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.SachbearbeiterGesuchDokumentRepository;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterGesuchDokumentCreateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterGesuchDokumentDto;
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
public class SachbearbeiterGesuchDokumentService {
    public static final String SACHBEARBEITER_GESUCHDOKUMENT_DOKUMENT_PATH = "sachbearbeiterGesuchDokument/";

    private final Antivirus antivirus;
    private final S3AsyncClient s3;

    private final StipConfig config;
    private final DokumentUploadService dokumentUploadService;
    private final DokumentDownloadService dokumentDownloadService;
    private final DokumentDeleteService dokumentDeleteService;

    private final SachbearbeiterGesuchDokumentMapper sachbearbeiterGesuchDokumentMapper;

    private final SachbearbeiterGesuchDokumentRepository sachbearbeiterGesuchDokumentRepository;
    private final DokumentRepository dokumentRepository;
    private final BenutzerService benutzerService;

    @Transactional
    public SachbearbeiterGesuchDokumentDto createSachbearbeiterGesuchDokument(
        UUID gesuchId,
        SachbearbeiterGesuchDokumentCreateDto sachbearbeiterGesuchDokumentCreateDto
    ) {
        final var sachbearbeiterGesuchDokument =
            sachbearbeiterGesuchDokumentMapper.toEntity(gesuchId, sachbearbeiterGesuchDokumentCreateDto);

        sachbearbeiterGesuchDokumentRepository.persist(sachbearbeiterGesuchDokument);
        return sachbearbeiterGesuchDokumentMapper.toDto(sachbearbeiterGesuchDokument);
    }

    @Transactional
    public Uni<Response> getUploadSachbearbeiterGesuchDokumentDokumentUni(
        final UUID sachbearbeiterGesuchDokumentId,
        final FileUpload fileUpload
    ) {
        return dokumentUploadService.validateScanUploadDokument(
            fileUpload,
            s3,
            config,
            antivirus,
            SACHBEARBEITER_GESUCHDOKUMENT_DOKUMENT_PATH,
            objectId -> uploadDokument(
                sachbearbeiterGesuchDokumentId,
                fileUpload,
                objectId
            ),
            throwable -> LOG.error(throwable.getMessage())
        );
    }

    @Transactional
    public void uploadDokument(
        final UUID sachbearbeiterGesuchDokumentId,
        final FileUpload fileUpload,
        final String objectId
    ) {
        final var sachbearbeiterGesuchDokument =
            sachbearbeiterGesuchDokumentRepository.requireById(sachbearbeiterGesuchDokumentId);

        final var dokument = new Dokument()
            .setFilename(fileUpload.fileName())
            .setFilesize(String.valueOf(fileUpload.size()))
            .setFilepath(SACHBEARBEITER_GESUCHDOKUMENT_DOKUMENT_PATH)
            .setObjectId(objectId);

        sachbearbeiterGesuchDokument.getDokumente().add(dokument);
        dokumentRepository.persist(dokument);
    }

    @Transactional
    public void deleteSachbearbeiterGesuchDokument(UUID sachbearbeiterGesuchDokumentId) {
        final var sachbearbeiterGesuchDokument =
            sachbearbeiterGesuchDokumentRepository.requireById(sachbearbeiterGesuchDokumentId);

        sachbearbeiterGesuchDokument.getDokumente()
            .forEach(dokument -> deleteSachbearbeiterGesuchDokumentDokument(dokument.getId()));

        sachbearbeiterGesuchDokument.getGesuch()
            .getSachbearbeiterGesuchDokuments()
            .remove(sachbearbeiterGesuchDokument);
        sachbearbeiterGesuchDokumentRepository.delete(sachbearbeiterGesuchDokument);
    }

    @Transactional
    public void deleteSachbearbeiterGesuchDokumentDokument(UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);
        final var sachbearbeiterGesuchDokument = sachbearbeiterGesuchDokumentRepository.requireByDokumentId(dokumentId);

        dokumentRepository.delete(dokument);
        sachbearbeiterGesuchDokument.getDokumente().remove(dokument);

        dokumentDeleteService.executeDeleteDokumentFromS3(
            s3,
            config.s3().bucketName(),
            SACHBEARBEITER_GESUCHDOKUMENT_DOKUMENT_PATH + dokument.getObjectId()
        );
    }

    @Transactional
    public List<SachbearbeiterGesuchDokumentDto> getAllByGesuchId(final UUID gesuchId) {
        return sachbearbeiterGesuchDokumentRepository.getAllByGesuchId(gesuchId)
            .map(sachbearbeiterGesuchDokumentMapper::toDto)
            .toList();
    }

    public FileDownloadTokenDto getSachbearbeiterGesuchDokumentDokumentDownloadToken(
        UUID dokumentId
    ) {
        return dokumentDownloadService.getFileDownloadToken(
            dokumentId,
            DokumentDownloadConstants.SACHBEARBEITER_GESUCHDOKUMENT_DOKUMENT_ID_CLAIM,
            benutzerService,
            config
        );
    }

    public RestMulti<Buffer> getSachbearbeiterGesuchDokumentDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);

        return dokumentDownloadService.getDokument(
            s3,
            config.s3().bucketName(),
            dokument.getObjectId(),
            SACHBEARBEITER_GESUCHDOKUMENT_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }
}
