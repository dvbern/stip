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

package ch.dvbern.stip.api.pdf.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import org.jboss.resteasy.spi.InternalServerErrorException;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.PAGE_SIZE;

public class DatenschutzbriefService {
    public ByteArrayOutputStream createDatenschutzbriefForElternteil(final Eltern elternteil) {
        final var out = new ByteArrayOutputStream();

        try (
            final var writer = new PdfWriter(out);
            final var pdfDocument = new PdfDocument(writer);
            final var document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            // Dom nothing to the PDF, we just need 'document' to be closed so an empty PDF is created
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        return out;
    }
}
