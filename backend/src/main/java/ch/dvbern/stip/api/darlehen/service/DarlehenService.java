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

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.darlehen.entity.DarlehenDokument;
import ch.dvbern.stip.api.darlehen.repo.DarlehenDokumentRepository;
import ch.dvbern.stip.api.darlehen.repo.DarlehenRepository;
import ch.dvbern.stip.api.darlehen.type.DarlehenDokumentType;
import ch.dvbern.stip.api.darlehen.type.DarlehenStatus;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.generated.dto.DarlehenDokumentDto;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestMulti;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@RequestScoped
@RequiredArgsConstructor
public class DarlehenService {
    public static final String DARLEHEN_DOKUMENT_PATH = "unterschriftenblatt/";

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
    private final BenutzerService benutzerService;
    private final JWTParser jwtParser;

    public DarlehenDto createDarlehen(UUID fallId) {
        final var fall = fallRepository.requireById(fallId);

        var darlehen = new Darlehen();
        darlehen.setFall(fall);
        darlehen.setStatus(DarlehenStatus.IN_BEARBEITUNG_GS);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto getDarlehenGs(UUID fallId) {
        var darlehenList = darlehenRepository.findByFallId(fallId);
        var darlehenActive = darlehenList.stream()
            .filter(
                d -> !d.getStatus().equals(DarlehenStatus.AKZEPTIERT) && !d.getStatus().equals(DarlehenStatus.ABGELEHNT)
            )
            .findFirst()
            .orElse(null);

        return darlehenMapper.toDto(darlehenActive);
    }

    public DarlehenDto darlehenAblehen(UUID darlehenId) {
        var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.ABGELEHNT);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto darlehenAkzeptieren(UUID darlehenId) {
        var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.AKZEPTIERT);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto darlehenEingeben(UUID darlehenId) {
        var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.EINGEGEBEN);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto darlehenFreigeben(UUID darlehenId) {
        var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.IN_FREIGABE);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto darlehenZurueckweisen(UUID darlehenId) {
        var darlehen = darlehenRepository.requireById(darlehenId);
        darlehen.setStatus(DarlehenStatus.IN_BEARBEITUNG_GS);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto darlehenUpdateGs(UUID darlehenId, DarlehenUpdateGsDto darlehenUpdateGsDto) {
        var darlehen = darlehenRepository.requireById(darlehenId);

        darlehenMapper.partialUpdate(darlehenUpdateGsDto, darlehen);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public DarlehenDto darlehenUpdateSb(UUID darlehenId, DarlehenUpdateSbDto darlehenUpdateSbDto) {
        var darlehen = darlehenRepository.requireById(darlehenId);

        darlehenMapper.partialUpdate(darlehenUpdateSbDto, darlehen);

        darlehenRepository.persistAndFlush(darlehen);
        return darlehenMapper.toDto(darlehen);
    }

    public Uni<Response> uploadDarlehenDokument(
        UUID darlehenId,
        DarlehenDokumentType dokumentTyp,
        FileUpload fileUpload
    ) {
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
            .setDarlehenDokumentType(type);

        darlehenDokumentRepository.persistAndFlush(darlehenDokument);
        return darlehenDokument;
    }

    public DarlehenDokumentDto getDarlehenDokument(UUID darlehenId, DarlehenDokumentType dokumentTyp) {
        final var dokument = darlehenDokumentRepository.findByDarlehenIdAndType(darlehenId, dokumentTyp).orElseThrow();
        return darlehenDokumentMapper.toDto(dokument);
    }

    public RestMulti<Buffer> getDokument(UUID dokumentId) {
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
