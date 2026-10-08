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
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.config.type.StipConfig;
import ch.dvbern.stip.api.dokument.service.DokumentDownloadService;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import io.quarkus.test.Mock;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.buffer.Buffer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import org.jboss.resteasy.reactive.RestMulti;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Mock
@ApplicationScoped
public class DokumentDownloadServiceMock extends DokumentDownloadService {

    public DokumentDownloadServiceMock() {
        super(null, null, null, null);
    }

    @Inject
    public DokumentDownloadServiceMock(S3AsyncClient s3, JWTParser jwtParser, StipConfig config, BenutzerService benutzerService) {
        super(s3, jwtParser, config, benutzerService);
    }

    @Override
    public RestMulti<Buffer> getWrapedDokument(
        final String fileName,
        final ByteArrayOutputStream byteStream
    ) {
        return RestMulti.fromMultiData(Uni.createFrom().item(Buffer.buffer()).toMulti()).build();
    }

    @Override
    public RestMulti<Buffer> getDokument(
        final String objectId,
        final String dokumentPathPrefix,
        final String fileName
    ) {
        return RestMulti.fromMultiData(Uni.createFrom().item(Buffer.buffer().appendInt(0)).toMulti()).build();
    }

    @Override
    public byte[] executeDownloadDocument(final String objectId, final String dokumentPathPrefix) {
        final var output = new ByteArrayOutputStream();
        try (final var pdf = new PdfDocument(new PdfWriter(output))) {
            pdf.addNewPage();
        }
        return output.toByteArray();
    }
}
