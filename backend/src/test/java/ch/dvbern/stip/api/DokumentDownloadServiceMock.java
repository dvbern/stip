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

import java.io.ByteArrayOutputStream;

import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import io.quarkus.test.Mock;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.RestMulti;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Mock
@ApplicationScoped
public class DokumentDownloadServiceMock extends DokumentDownloadService {
    @Override
    public RestMulti<Buffer> getWrapedDokument(
        final String fileName,
        final ByteArrayOutputStream byteStream
    ) {
        return RestMulti.fromMultiData(Uni.createFrom().item(Buffer.buffer()).toMulti()).build();
    }

    @Override
    public RestMulti<Buffer> getDokument(
        final S3AsyncClient s3,
        final String bucketName,
        final String objectId,
        final String dokumentPathPrefix,
        final String fileName
    ) {
        return RestMulti.fromMultiData(Uni.createFrom().item(Buffer.buffer().appendInt(0)).toMulti()).build();
    }
}
