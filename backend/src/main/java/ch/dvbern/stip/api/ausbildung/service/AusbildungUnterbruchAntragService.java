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
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungUnterbruchAntragRepository;
import ch.dvbern.stip.api.ausbildung.util.AusbildungUnterbruchAntragUtil;
import ch.dvbern.stip.api.common.util.GesuchUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.service.DokumentDeleteService;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.api.dokument.service.DokumentMapper;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDto;
import ch.dvbern.stip.generated.dto.DokumentDto;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragGSDto;
import ch.dvbern.stip.generated.dto.UpdateAusbildungUnterbruchAntragSBDto;
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
public class AusbildungUnterbruchAntragService {
    private final AusbildungUnterbruchAntragRepository ausbildungUnterbruchAntragRepository;
    private final AusbildungUnterbruchAntragMapper ausbildungUnterbruchAntragMapper;
    private final GesuchService gesuchService;
    private final DokumentUploadService dokumentUploadService;
    private final S3AsyncClient s3;
    private final ConfigService configService;
    private final Antivirus antivirus;
    private final DokumentRepository dokumentRepository;
    private final DokumentDeleteService dokumentDeleteService;
    private final DokumentDownloadService dokumentDownloadService;
    private final DokumentMapper dokumentMapper;

    public static final String AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH = "ausbildung_unterbruch_antrag/";

    public AusbildungUnterbruchAntrag requireById(final UUID ausbildungUnterbruchAntragId) {
        return ausbildungUnterbruchAntragRepository.requireById(ausbildungUnterbruchAntragId);
    }

    public AusbildungUnterbruchAntrag requireByDokumentId(final UUID dokumentId) {
        return ausbildungUnterbruchAntragRepository.requireByDokumentId(dokumentId);
    }

    @Transactional
    public AusbildungUnterbruchAntrag createAntrag(final Gesuch gesuch) {
        final AusbildungUnterbruchAntrag ausbildungUnterbruchAntrag = new AusbildungUnterbruchAntrag();
        ausbildungUnterbruchAntrag.setGesuch(gesuch);
        ausbildungUnterbruchAntrag.setAusbildung(gesuch.getAusbildung());
        ausbildungUnterbruchAntragRepository.persistAndFlush(ausbildungUnterbruchAntrag);
        return ausbildungUnterbruchAntrag;
    }

    @Transactional
    public AusbildungUnterbruchAntragGSDto createAusbildungUnterbruchAntrag(UUID gesuchId) {
        return ausbildungUnterbruchAntragMapper.toGsDto(createAntrag(gesuchService.getGesuchById(gesuchId)));
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
            configService,
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
        dokumentDeleteService.executeDeleteDokumentsFromS3(s3, configService.getBucketName(), objectIds);
    }

    @Transactional
    public void deleteAusbildungUnterbruchAntragDokument(final UUID dokumentId) {
        final var antrag = requireByDokumentId(dokumentId);
        final var dokument = dokumentRepository.requireById(dokumentId);
        antrag.getDokuments().remove(dokument);
        dokumentDeleteService.executeDeleteDokumentFromS3(
            s3,
            configService.getBucketName(),
            getFullPathObjectId(dokument.getObjectId())
        );
    }

    @Transactional
    public RestMulti<Buffer> getDokument(final UUID dokumentId) {
        final var dokument = dokumentRepository.requireById(dokumentId);

        return dokumentDownloadService.getDokument(
            s3,
            configService.getBucketName(),
            dokument.getObjectId(),
            AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH,
            dokument.getFilename()
        );
    }

    private static String getFullPathObjectId(final String objectId) {
        return AUSBILDUNG_UNTERBRUCH_ANTRAG_DOKUMENT_PATH + objectId;
    }

    @Transactional
    public AusbildungUnterbruchAntragGSDto einreichenAusbildungUnterbruchAntrag(
        final UUID ausbildungUnterbruchAntragId,
        final UpdateAusbildungUnterbruchAntragGSDto updateAusbildungUnterbruchAntragGSDto
    ) {
        final var antrag = requireById(ausbildungUnterbruchAntragId);
        return ausbildungUnterbruchAntragMapper
            .toGsDto(ausbildungUnterbruchAntragMapper.partialUpdate(updateAusbildungUnterbruchAntragGSDto, antrag));
    }

    @Transactional
    public List<AusbildungUnterbruchAntragSBDto> getAusbildungUnterbruchAntragsByGesuchId(final UUID gesuchId) {
        return ausbildungUnterbruchAntragRepository.getAusbildungUnterbruchAntragsByGesuchId(gesuchId)
            .stream()
            .map(ausbildungUnterbruchAntragMapper::toSbDto)
            .toList();
    }

    @Transactional
    public AusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntrag(
        final UUID ausbildungUnterbruchAntragId,
        final UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSBDto
    ) {
        final var antrag = requireById(ausbildungUnterbruchAntragId);
        return ausbildungUnterbruchAntragMapper
            .toSbDto(ausbildungUnterbruchAntragMapper.partialUpdate(updateAusbildungUnterbruchAntragSBDto, antrag));
    }

    @Transactional
    public List<DokumentDto> getDokumentsOfAusbildungUnterbruchAntrag(final UUID ausbildungUnterbruchAntragId) {
        final var antrag = requireById(ausbildungUnterbruchAntragId);
        return antrag.getDokuments().stream().map(dokumentMapper::toDto).toList();
    }

    public boolean canCreateAusbildungUnterbruchAntrag(final Ausbildung ausbildung) {
        final boolean latestGesuchHasNoOpenAenderung =
            GesuchUtil.openAenderungAlreadyExists(ausbildung.getLatestGesuch());
        final boolean openAusbildungUnterbruchAntragExists =
            !AusbildungUnterbruchAntragUtil.openAusbildungUnterbruchAntragExists(ausbildung);
        return latestGesuchHasNoOpenAenderung && !openAusbildungUnterbruchAntragExists && !ausbildung.isUnterbrochen();
    }

}
