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
import java.util.Locale;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.InternalServerErrorException;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.AUSBILDUNGSBEITRAEGE_LINK;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_BOLD_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.LOGO_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.PAGE_SIZE;

@ApplicationScoped
@RequiredArgsConstructor
public class DarlehensVerfuegungPdfService {
    private PdfFont pdfFont = null;
    private PdfFont pdfFontBold = null;
    private Link ausbildungsbeitraegeUri = null;

    public ByteArrayOutputStream generateDarlehensVerfuegungPdf(final Darlehen darlehen) {
        final var out = new ByteArrayOutputStream();
        final FontProgram font;
        final FontProgram fontBold;
        try {
            final byte[] fontBytes = IOUtils.toByteArray(getClass().getResourceAsStream(FONT_PATH));
            final byte[] fontBoldBytes = IOUtils.toByteArray(getClass().getResourceAsStream(FONT_BOLD_PATH));
            font = FontProgramFactory.createFont(fontBytes);
            fontBold = FontProgramFactory.createFont(fontBoldBytes);
        } catch (IOException ex) {
            throw new InternalServerErrorException(ex);

        }
        pdfFont = PdfFontFactory.createFont(font);
        pdfFontBold = PdfFontFactory.createFont(fontBold);
        ausbildungsbeitraegeUri = new Link(AUSBILDUNGSBEITRAEGE_LINK, PdfAction.createURI(AUSBILDUNGSBEITRAEGE_LINK));
        final Gesuch gesuch = darlehen.getFall().getLatestGesuch();
        final Locale locale = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache()
            .getLocale();
        final TL translator = TLProducer.defaultBundle().forAppLanguage(AppLanguages.fromLocale(locale));
        try (
            final PdfWriter writer = new PdfWriter(out);
            final PdfDocument pdfDocument = new PdfDocument(writer);
            final Document document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            final float leftMargin = document.getLeftMargin();

            final Image logo = PdfUtils.getLogo(pdfDocument, LOGO_PATH);
            logo.setMarginLeft(-25);
            logo.setMarginTop(-35);

            document.add(logo);
            PdfUtils.header(gesuch, document, leftMargin, translator, true, pdfFont, ausbildungsbeitraegeUri);
            // todo: add main content
            PdfUtils.footer(gesuch, document, leftMargin, translator, pdfFont, true);
            PdfUtils.rechtsmittelbelehrung(translator, document, leftMargin, pdfFont, pdfFontBold);
            PdfUtils.makePageNumberEven(document);

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        return out;
    }

}
