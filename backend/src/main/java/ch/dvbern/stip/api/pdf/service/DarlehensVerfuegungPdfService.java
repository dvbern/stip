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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
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
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.InternalServerErrorException;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.AUSBILDUNGSBEITRAEGE_LINK;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_BOLD_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.LOGO_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.PAGE_SIZE;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_MEDIUM;

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
            PdfUtils.header(gesuch, document, leftMargin, translator, false, pdfFont, ausbildungsbeitraegeUri);
            // todo: add main content

            final LocalDate ausbildungsjahrVon = darlehen.getFall()
                .getLatestGesuch()
                .getGesuchTranchen()
                .stream()
                .map(GesuchTranche::getGueltigkeit)
                .min(Comparator.comparing(DateRange::getGueltigAb))
                .get()
                .getGueltigAb();

            final LocalDate ausbildungsjahrBis = darlehen.getFall()
                .getLatestGesuch()
                .getGesuchTranchen()
                .stream()
                .map(GesuchTranche::getGueltigkeit)
                .max(Comparator.comparing(DateRange::getGueltigBis))
                .get()
                .getGueltigBis();

            final String ausbildungsjahr = String.format(
                " %d/%d",
                ausbildungsjahrVon.getYear(),
                ausbildungsjahrBis.getYear()
            );

            // überschrift
            document.add(
                PdfUtils.createParagraph(
                    pdfFontBold,
                    FONT_SIZE_BIG,
                    leftMargin,
                    String.format(
                        "Verfügung für das Ausbildungsjahr %s\n"
                        + "Ausbildungsdarlehen %s",
                        ausbildungsjahr,
                        darlehen.getId()
                    )
                )
            );
            // anrede/begrüssung
            final PersonInAusbildung personInAusbildung = darlehen.getFall()
                .getLatestGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung();

            final String translateKey = personInAusbildung
                .getAnrede()
                .equals(Anrede.HERR)
                    ? "stip.pdf.begruessung.mann"
                    : "stip.pdf.begruessung.frau";

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate(translateKey) + " ",
                    personInAusbildung.getNachname()
                )
            );

            // einleitungstext
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    String.format(
                        "Vielen Dank für Ihren Antrag vom %s. Wir haben diesen geprüft und gewähren Ihnen folgendes Ausbildungsdarlehen: ",
                        DateUtil.formatDate(darlehen.getTimestampErstellt().toLocalDate())
                    )
                )
            );

            // todo: display table correctly
            addTable(document, darlehen);

            // mail to text
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "Die Auszahlung erfolgt durch die Berner Kantonalbank BEKB. Um die Akten anzufordern, bitten wir Sie, sich 10 Tage vor Auszahlungstermin direkt mit der BEKB, Team Ausbildungsdarlehen,\n"
                    + "Email Ausbildungsdarlehen@bekb.ch, in Verbindung zu setzen. \n"
                )
            );

            // erfolgswunsch
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "Wir wünschen Ihnen viel Erfolg für Ihre Ausbildung. "
                )
            );

            PdfUtils.footer(gesuch, document, leftMargin, translator, pdfFont, false);
            // kopie an bkd
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "- ",
                    "Kopie an: Berner Kantonalbank, Team Ausbildungsdarlehen\n"
                    + "  und «e_dor_Verteiler»\n"
                )
            );
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "- ",
                    "Hinweis: Bitte beachten Sie unbedingt die wichtigen Hinweise auf der Rückseite. "
                )
            );

            // todo: add wichtige infos to rechtsmittelbelehurng (flag)
            PdfUtils.rechtsmittelbelehrung(translator, document, leftMargin, pdfFont, pdfFontBold);
            PdfUtils.makePageNumberEven(document);

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        return out;
    }

    private void addTable(Document document, final Darlehen darlehen) {
        final float[] columnWidths = { 50, 50 };
        final var leftMargin = document.getLeftMargin();
        final Table calculationTable = PdfUtils.createTable(columnWidths, leftMargin);
        calculationTable.setMarginTop(SPACING_MEDIUM);
        calculationTable.setMarginBottom(SPACING_MEDIUM);
        calculationTable.setPaddingRight(SPACING_MEDIUM);
        // betrag -> CHF <total>
        // geburtsdatum
        // heimatort
        // verfall (verfuegungs-ende)

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                String.format("CHF %d", Objects.requireNonNullElse(darlehen.getBetrag(), 0))
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Geburtsdatum"
            ).setPadding(1).setTextAlignment(TextAlignment.LEFT)
        );

        final var geburtsdatum = DateUtil.formatDate(
            darlehen.getFall()
                .getLatestGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung()
                .getGeburtsdatum()
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                geburtsdatum
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Heimatort"
            ).setPadding(1)
        );

        final var heimatort = darlehen.getFall()
            .getLatestGesuch()
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getHeimatort();

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                heimatort
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Verfall"
            ).setPadding(1)
        );

        final var verfuegungBis = "todo";

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                verfuegungBis
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        document.add(calculationTable);
    }
}
