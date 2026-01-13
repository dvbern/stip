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
import java.util.List;
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
import ch.dvbern.stip.api.pdf.type.Anhangs;
import ch.dvbern.stip.api.pdf.util.DarlehenPdfUtils;
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
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
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
    private static final float[] TABLE_WIDTH_PERCENTAGES = { 30, 60 };

    private PdfFont pdfFont = null;
    private PdfFont pdfFontBold = null;
    private Link ausbildungsbeitraegeUri = null;

    private void generateFonts() {
        final FontProgram font;
        final FontProgram fontBold;
        try {
            final byte[] fontBytes =
                IOUtils.toByteArray(Objects.requireNonNull(getClass().getResourceAsStream(FONT_PATH)));
            final byte[] fontBoldBytes = IOUtils.toByteArray(
                Objects.requireNonNull(
                    getClass().getResourceAsStream(
                        FONT_BOLD_PATH
                    )
                )
            );
            font = FontProgramFactory.createFont(fontBytes);
            fontBold = FontProgramFactory.createFont(fontBoldBytes);
        } catch (IOException ex) {
            throw new InternalServerErrorException(ex);
        }
        pdfFont = PdfFontFactory.createFont(font);
        pdfFontBold = PdfFontFactory.createFont(fontBold);
    }

    private TL getTranslator(final Gesuch gesuch) {
        final Locale locale = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache()
            .getLocale();
        return TLProducer.defaultBundle().forAppLanguage(AppLanguages.fromLocale(locale));
    }

    private void addHeader(
        final Gesuch gesuch,
        final PdfDocument pdfDocument,
        final Document document,
        final float leftMargin,
        final TL translator
    ) throws IOException {
        final Image logo = PdfUtils.getLogo(pdfDocument, LOGO_PATH);
        logo.setMarginLeft(-25);
        logo.setMarginTop(-35);

        document.add(logo);
        PdfUtils.header(gesuch, document, leftMargin, translator, false, pdfFont, ausbildungsbeitraegeUri);
    }

    private String getAusbildungsJahr(final Gesuch gesuch) {
        final LocalDate ausbildungsjahrVon = gesuch
            .getGesuchTranchen()
            .stream()
            .map(GesuchTranche::getGueltigkeit)
            .min(Comparator.comparing(DateRange::getGueltigAb))
            .get()
            .getGueltigAb();

        final LocalDate ausbildungsjahrBis = gesuch
            .getGesuchTranchen()
            .stream()
            .map(GesuchTranche::getGueltigkeit)
            .max(Comparator.comparing(DateRange::getGueltigBis))
            .get()
            .getGueltigBis();

        return String.format(
            " %d/%d",
            ausbildungsjahrVon.getYear(),
            ausbildungsjahrBis.getYear()
        );
    }

    public ByteArrayOutputStream generatePositiveDarlehensVerfuegungPdf(final Darlehen darlehen) {

        generateFonts();
        ausbildungsbeitraegeUri = new Link(AUSBILDUNGSBEITRAEGE_LINK, PdfAction.createURI(AUSBILDUNGSBEITRAEGE_LINK));
        final Gesuch gesuch = darlehen.getFall().getLatestGesuch();
        final TL translator = getTranslator(gesuch);

        final var out = new ByteArrayOutputStream();
        try (
            final PdfWriter writer = new PdfWriter(out);
            final PdfDocument pdfDocument = new PdfDocument(writer);
            final Document document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            final float leftMargin = document.getLeftMargin();

            addHeader(
                gesuch,
                pdfDocument,
                document,
                leftMargin,
                translator
            );

            final String ausbildungsjahr = getAusbildungsJahr(gesuch);

            final var titelZeile1 =
                String.format(translator.translate("stip.darlehen.verfuegung.positiv.titel.zeile1"), ausbildungsjahr);
            final var titelZeile2 =
                String.format(
                    translator.translate("stip.darlehen.verfuegung.positiv.titel.zeile2"),
                    darlehen.getDarlehenNr()
                );
            document.add(
                PdfUtils.createParagraph(
                    pdfFontBold,
                    FONT_SIZE_BIG,
                    leftMargin,
                    String.format(
                        "%s \n %s",
                        titelZeile1,
                        titelZeile2
                    )
                )
            );

            final PersonInAusbildung personInAusbildung = gesuch
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

            final var text1 = String.format(
                translator.translate("stip.darlehen.verfuegung.positiv.textBlock.eins"),
                DateUtil.formatDate(darlehen.getEingabedatum())
            );
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    text1
                )
            );

            // todo KSTIP-2697: display darlehen detail table correctly
            addDetailsForDarlehenTable(document, darlehen, translator);

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.darlehen.verfuegung.positiv.textBlock.zwei"),
                    "\n",
                    translator.translate("stip.darlehen.verfuegung.positiv.textBlock.drei"),
                    "\n"
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.darlehen.verfuegung.positiv.textBlock.vier")
                )
            );

            final List<Anhangs> anhangs = List.of(Anhangs.RECHTSMITTELBELEHRUNG);

            PdfUtils.footer(gesuch, document, leftMargin, translator, pdfFont, anhangs, false);

            final var kopieAnTextZeile2 = DarlehenPdfUtils.getKopieAnSozialdienstRezipientString(
                darlehen.getFall(),
                translator.translate("stip.darlehen.verfuegung.positiv.textBlock.kopieAn.zeile2")
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "- ",
                    translator.translate("stip.darlehen.verfuegung.positiv.textBlock.kopieAn.zeile1"),
                    "\n",
                    kopieAnTextZeile2,
                    "\n"
                )
            );
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "- ",
                    translator.translate("stip.darlehen.verfuegung.positiv.wichtigerHinweis")
                )
            );

            // todo KSTIP-2697: add wichtige infos to rechtsmittelbelehurng (flag)
            PdfUtils.rechtsmittelbelehrung(translator, document, leftMargin, pdfFont, pdfFontBold);
            PdfUtils.makePageNumberEven(document);

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        return out;
    }

    public ByteArrayOutputStream generateNegativeDarlehensVerfuegungPdf(final Darlehen darlehen) {
        final var out = new ByteArrayOutputStream();
        generateFonts();
        ausbildungsbeitraegeUri = new Link(AUSBILDUNGSBEITRAEGE_LINK, PdfAction.createURI(AUSBILDUNGSBEITRAEGE_LINK));
        final Gesuch gesuch = darlehen.getFall().getLatestGesuch();
        final TL translator = getTranslator(gesuch);
        try (
            final PdfWriter writer = new PdfWriter(out);
            final PdfDocument pdfDocument = new PdfDocument(writer);
            final Document document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            final float leftMargin = document.getLeftMargin();

            addHeader(
                gesuch,
                pdfDocument,
                document,
                leftMargin,
                translator
            );

            final String ausbildungsjahr = getAusbildungsJahr(gesuch);

            final var titel =
                String.format(translator.translate("stip.darlehen.verfuegung.negativ.titel.zeile1"), ausbildungsjahr);
            document.add(
                PdfUtils.createParagraph(
                    pdfFontBold,
                    FONT_SIZE_BIG,
                    leftMargin,
                    titel
                )
            );

            final PersonInAusbildung personInAusbildung = gesuch
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

            final var text1 = String.format(
                translator.translate("stip.darlehen.verfuegung.negativ.textBlock.eins"),
                DateUtil.formatDate(darlehen.getEingabedatum())
            );
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    text1
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.darlehen.verfuegung.negativ.textBlock.zwei")
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.darlehen.verfuegung.negativ.textBlock.drei")
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.darlehen.verfuegung.negativ.textBlock.vier")
                )
            );

            final List<Anhangs> anhangs = List.of(Anhangs.RECHTSMITTELBELEHRUNG);

            PdfUtils.footer(gesuch, document, leftMargin, translator, pdfFont, anhangs, false);

            final var kopieAnText = DarlehenPdfUtils.getKopieAnSozialdienstRezipientString(
                darlehen.getFall(),
                String.format("- %s", translator.translate("stip.darlehen.verfuegung.negativ.textBlock.kopieAn"))
            );
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    kopieAnText
                )
            );

            PdfUtils.rechtsmittelbelehrung(translator, document, leftMargin, pdfFont, pdfFontBold);
            PdfUtils.makePageNumberEven(document);

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        return out;
    }

    private void addDetailsForDarlehenTable(Document document, final Darlehen darlehen, final TL translator) {
        final var leftMargin = document.getLeftMargin();
        final Table calculationTable = PdfUtils.createTable(TABLE_WIDTH_PERCENTAGES, leftMargin);
        calculationTable.useAllAvailableWidth();
        calculationTable.setMarginTop(SPACING_MEDIUM);
        calculationTable.setMarginBottom(SPACING_MEDIUM);
        calculationTable.setPaddingRight(SPACING_MEDIUM);

        calculationTable.addCell(
            new Cell().setBorder(Border.NO_BORDER)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                String.format("CHF %s.-", PdfUtils.formatNumber(Objects.requireNonNullElse(darlehen.getBetrag(), 0)))
            ).setPadding(1).setTextAlignment(TextAlignment.LEFT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                String.format("%s:", translator.translate("stip.darlehen.verfuegung.positiv.geburtsdatum"))
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
            ).setPadding(1).setTextAlignment(TextAlignment.LEFT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                String.format("%s:", translator.translate("stip.darlehen.verfuegung.positiv.heimatort"))
            ).setPadding(1).setTextAlignment(TextAlignment.LEFT)
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
            ).setPadding(1).setTextAlignment(TextAlignment.LEFT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                String.format("%s:", translator.translate("stip.darlehen.verfuegung.positiv.verfall"))
            ).setPadding(1).setTextAlignment(TextAlignment.LEFT)
        );

        // todo KSTIP-2697: refine definition of "ende gesuchsjahr" for gueltigkeit of darlehen
        final var gueltigBis = DateUtil
            .formatDate(DateUtil.getGesuchDateRange(darlehen.getFall().getLatestGesuch()).getGueltigBis());

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                gueltigBis
            ).setPadding(1).setTextAlignment(TextAlignment.LEFT)
        );

        document.add(calculationTable);
    }
}
