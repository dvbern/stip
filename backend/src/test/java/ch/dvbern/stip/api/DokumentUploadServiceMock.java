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

package ch.dvbern.stip.api;

import java.util.UUID;
import java.util.function.Consumer;

import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.dokument.service.DokumentUploadService;
import io.quarkiverse.antivirus.runtime.Antivirus;
import io.quarkus.test.Mock;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Mock
@Slf4j
@ApplicationScoped
public class DokumentUploadServiceMock extends DokumentUploadService {

    @Override
    public Uni<Response> uploadDokument(
        final FileUpload fileUpload,
        final S3AsyncClient s3,
        final ConfigService configService,
        final String dokumentPathPrefix,
        final Consumer<String> serviceCallback,
        final @Nullable Consumer<Throwable> onFailure
    ) {
        serviceCallback.accept(UUID.randomUUID().toString());
        return Uni.createFrom().item(Response.created(null).build());
    }

    @Override
    public Uni<Response> uploadDokument(
        final FileUpload fileUpload,
        final S3AsyncClient s3,
        final ConfigService configService,
        final String dokumentPathPrefix,
        final Consumer<String> serviceCallback
    ) {
        serviceCallback.accept(UUID.randomUUID().toString());
        return Uni.createFrom().item(Response.created(null).build());
    }

    @Override
    public String executeUploadDocument(
        final byte[] byteArray,
        final String fileName,
        final S3AsyncClient s3,
        final ConfigService configService,
        final String documentPathPrefix
    ) {
        return FileUtil.generateUUIDWithFileExtension(fileName);
    }

    @Override
    public void scanDokument(final Antivirus antivirus, final FileUpload fileUpload) {
        LOG.debug("Would scan file: {}", fileUpload.fileName());
    }
}
