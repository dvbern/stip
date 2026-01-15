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

package ch.dvbern.stip.api.pdf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.util.Constants;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.pdf.type.Anhangs;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.svg.converter.SvgConverter;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.InternalServerErrorException;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.AUSBILDUNGSBEITRAEGE_LINK;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_MEDIUM;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_SMALL;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.LOGO_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.NUMBER_FORMAT;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.RECHTSMITTELBELEHRUNG_TITLE_KEY;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_MEDIUM;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_SMALL;

@UtilityClass
public class PdfUtils {
    public static final Link AUSBILDUNGSBEITRAEGE_URI =
        new Link(AUSBILDUNGSBEITRAEGE_LINK, PdfAction.createURI(AUSBILDUNGSBEITRAEGE_LINK));

    public static String formatNumber(Number number) {
        return NUMBER_FORMAT.format(number);
    }

    public boolean isPageNumberEven(final Document document) {
        return document.getPdfDocument().getNumberOfPages() % 2 == 0;
    }

    public void makePageNumberEven(Document document) {
        if (isPageNumberEven(document)) {
            return;
        }
        document.getPdfDocument().addNewPage();
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
    }

    public String formatAusbildungsjahr(final LocalDate von, final LocalDate bis) {
        return String.format(
            " %d/%d",
            von.getYear(),
            bis.getYear()
        );
    }

    public Paragraph getAnredeParagraph(
        final PersonInAusbildung pia,
        final PdfFont pdfFont,
        final TL translator,
        float fontSize,
        float leftMargin
    ) {
        final String anredeTlKey = pia
            .getAnrede()
            .equals(Anrede.HERR)
                ? "stip.pdf.begruessung.mann"
                : "stip.pdf.begruessung.frau";

        return PdfUtils.createParagraph(
            pdfFont,
            fontSize,
            leftMargin,
            translator.translate(anredeTlKey) + " ",
            pia.getNachname()
        );
    }

    public PdfFont createFont() {
        return getPdfFont(PdfConstants.FONT_PATH);
    }

    public PdfFont createFontBold() {
        return getPdfFont(PdfConstants.FONT_BOLD_PATH);
    }

    private static PdfFont getPdfFont(String fontPath) {
        final FontProgram font;
        try {
            final byte[] fontBytes =
                IOUtils.toByteArray(Objects.requireNonNull(PdfUtils.class.getResourceAsStream(fontPath)));
            font = FontProgramFactory.createFont(fontBytes);
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        return PdfFontFactory.createFont(font);
    }

    public Image getLogo(
        final PdfDocument pdfDocument,
        final String pathToSvg
    ) {
        final ByteArrayOutputStream svgOut = new ByteArrayOutputStream();
        try (final InputStream svgStream = PdfUtils.class.getResourceAsStream(pathToSvg)) {
            SvgConverter.createPdf(svgStream, svgOut);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        try (
            final PdfReader reader = new PdfReader(new ByteArrayInputStream(svgOut.toByteArray()));
            final PdfDocument tempSvgDoc = new PdfDocument(reader)
        ) {
            final PdfFormXObject xObject = tempSvgDoc.getFirstPage().copyAsFormXObject(pdfDocument);
            final Image logo = new Image(xObject);
            logo.scaleToFit(150, 87);
            logo.setMarginLeft(-25);
            logo.setMarginTop(-35);
            return logo;
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public Paragraph createParagraph(
        final PdfFont font,
        float fontSize,
        float leftMargin,
        final String... texts
    ) {
        final Paragraph paragraph = new Paragraph();
        paragraph.setFont(font);
        paragraph.setFontSize(fontSize);
        paragraph.setMultipliedLeading(1f);
        paragraph.setMarginLeft(leftMargin);
        for (final String text : texts) {
            paragraph.add(new Text(text));
        }
        return paragraph;
    }

    public Paragraph createParagraph(
        float fontSize,
        float leftMargin
    ) {
        final Paragraph paragraph = new Paragraph();
        paragraph.setFontSize(fontSize);
        paragraph.setMultipliedLeading(1f);
        paragraph.setMarginLeft(leftMargin);
        return paragraph;
    }

    public Text createText(
        final PdfFont font,
        float fontSize,
        final String text
    ) {
        final Text textOut = new Text(text);
        textOut.setFontSize(fontSize);
        textOut.setFont(font);
        return textOut;
    }

    public Table createTable(
        final float[] columnWidths,
        final float leftMargin
    ) {
        final Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginLeft(leftMargin);
        return table;
    }

    public Cell createCell(
        final PdfFont font,
        final float fontSize,
        final int rowSpan,
        final int colSpan,
        final String... paragraphs
    ) {
        final Cell cell = new Cell(rowSpan, colSpan)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setFont(font)
            .setFontSize(fontSize)
            .setPaddingBottom(SPACING_SMALL);
        for (final String text : paragraphs) {
            cell.add(createParagraph(font, fontSize, 0, text));
        }
        return cell;
    }

    public void header(
        final Gesuch gesuch,
        final Document document,
        final PdfDocument pdfDocument,
        final float leftMargin,
        final TL translator,
        final boolean isDeckblatt,
        final PdfFont pdfFont
    ) {
        header(
            gesuch,
            document,
            pdfDocument,
            leftMargin,
            translator,
            isDeckblatt,
            pdfFont,
            null,
            Optional.empty()
        );
    }

    public void header(
        final Gesuch gesuch,
        final Document document,
        final PdfDocument pdfDocument,
        final float leftMargin,
        final TL translator,
        final boolean isDeckblatt,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold,
        final Optional<Eltern> elternteilOptional
    ) {

        final Image logo = PdfUtils.getLogo(pdfDocument, LOGO_PATH);
        document.add(logo);
        final float[] columnWidths = { 50, 50 };
        final Table headerTable = PdfUtils.createTable(columnWidths, leftMargin);

        final GesuchFormular gesuchFormular = gesuch.getLatestGesuchTranche().getGesuchFormular();

        final Sachbearbeiter sachbearbeiterBenutzer = gesuch
            .getAusbildung()
            .getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter();

        final Cell sender = PdfUtils.createCell(
            pdfFont,
            FONT_SIZE_MEDIUM,
            1,
            1,
            translator.translate("stip.pdf.header.bkd"),
            translator.translate("stip.pdf.header.amt"),
            translator.translate("stip.pdf.header.abteilung")
        );
        headerTable.addCell(sender);

        final Paragraph recieverHeaderParagraph = new Paragraph()
            .add(translator.translate("stip.pdf.header.bkd") + ", ")
            .add(translator.translate("stip.pdf.header.strasse") + ", ")
            .add(translator.translate("stip.pdf.header.plz"));

        final Cell recieverHeader = PdfUtils.createCell(pdfFont, FONT_SIZE_SMALL, 3, 1)
            .setVerticalAlignment(VerticalAlignment.BOTTOM)
            .setBorderBottom(new SolidBorder(1))
            .setPaddingBottom(0);
        recieverHeader.add(recieverHeaderParagraph);
        headerTable.addCell(recieverHeader);

        final Cell address = PdfUtils.createCell(
            pdfFont,
            FONT_SIZE_MEDIUM,
            1,
            1,
            translator.translate("stip.pdf.header.strasse"),
            translator.translate("stip.pdf.header.plz")
        );
        headerTable.addCell(address);

        final Paragraph uriParagraph = new Paragraph().add(AUSBILDUNGSBEITRAEGE_URI);

        final Cell url = PdfUtils.createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1).setPaddingBottom(0).add(uriParagraph);
        headerTable.addCell(url);

        final String mail = sachbearbeiterBenutzer.getEmail() != null
            ? sachbearbeiterBenutzer.getEmail()
            : Constants.DVB_MAILBUCKET_MAIL;

        final Link email = new Link(mail, PdfAction.createURI(String.format("mailto:%s", mail)));
        final Paragraph emailParagraph = new Paragraph().add(email);

        final Cell sachbearbeiter = PdfUtils.createCell(
            pdfFont,
            FONT_SIZE_MEDIUM,
            1,
            1,
            String.format("%s %s", sachbearbeiterBenutzer.getVorname(), sachbearbeiterBenutzer.getNachname()),
            sachbearbeiterBenutzer.getTelefonnummer()
        )
            .setPaddingBottom(SPACING_BIG)
            .setPaddingTop(SPACING_MEDIUM)
            .add(emailParagraph);
        headerTable.addCell(sachbearbeiter);

        if (!isDeckblatt && elternteilOptional.isEmpty()) {
            final Cell receiver = PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_MEDIUM,
                1,
                1,
                gesuchFormular.getPersonInAusbildung().getFullName(),
                gesuchFormular.getPersonInAusbildung().getAdresse().getStrasse(),
                gesuchFormular.getPersonInAusbildung().getAdresse().getPlz() +
                " " +
                gesuchFormular.getPersonInAusbildung().getAdresse().getOrt()
            ).setPaddingTop(SPACING_MEDIUM);
            headerTable.addCell(receiver);
        }

        if (isDeckblatt && elternteilOptional.isEmpty()) {
            final var sozialdienst = gesuch.getAusbildung().getFall().getDelegierung().getSozialdienst();
            final Cell receiver = PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_MEDIUM,
                1,
                1,
                sozialdienst.getName(),
                sozialdienst.getZahlungsverbindung().getVorname() + " "
                + sozialdienst.getZahlungsverbindung().getNachname(),
                sozialdienst.getZahlungsverbindung().getAdresse().getStrasse(),
                sozialdienst.getZahlungsverbindung().getAdresse().getPlz() +
                " " +
                sozialdienst.getZahlungsverbindung().getAdresse().getOrt()
            ).setPaddingTop(SPACING_MEDIUM);
            headerTable.addCell(receiver);
        }

        if (elternteilOptional.isPresent()) {
            final var elternteil = elternteilOptional.get();
            final Cell receiver = PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_MEDIUM,
                1,
                1
            ).setPaddingTop(SPACING_MEDIUM);

            receiver.add(
                PdfUtils.createParagraph(
                    pdfFontBold,
                    FONT_SIZE_MEDIUM,
                    0,
                    "Einschreiben"
                )
            );

            receiver.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_MEDIUM,
                    0,
                    elternteil.getFullName()
                )
            );

            receiver.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_MEDIUM,
                    0,
                    elternteil.getAdresse().getStrasse() + " " + elternteil.getAdresse().getHausnummer()
                )
            );

            receiver.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_MEDIUM,
                    0,
                    elternteil.getAdresse().getPlz() + " " + elternteil.getAdresse().getOrt()
                )
            );

            headerTable.addCell(receiver);
        }

        final Paragraph dossierNr = new Paragraph()
            .add(translator.translate("stip.pdf.header.dossier.nr") + " ")
            .add(gesuch.getGesuchNummer());

        final Paragraph svNr = new Paragraph()
            .add(translator.translate("stip.pdf.header.svNr") + " ")
            .add(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());

        final Cell fallInformations = PdfUtils.createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1);
        fallInformations.add(dossierNr);
        fallInformations.add(svNr);
        headerTable.addCell(fallInformations);

        final Cell date = PdfUtils.createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1, DateUtil.formatDate(LocalDate.now()));
        headerTable.addCell(date);

        if (!isDeckblatt) {
            final Locale locale = LocaleUtil.getLocale(gesuch);

            final var ausbildungsStaette = gesuch.getAusbildung().getAusbildungsstaetteOrAlternative(locale);
            final var ausbildungsGang = gesuch.getAusbildung().getAusbildungsgangOrAlternative(locale);

            final Cell ausbildung =
                PdfUtils.createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1, ausbildungsStaette, ausbildungsGang);

            headerTable.addCell(ausbildung);
        }

        headerTable.setMarginBottom(SPACING_MEDIUM);

        document.add(headerTable);

        if (isDeckblatt) {
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        }
    }

    public void footer(
        final Gesuch gesuch,
        final Document document,
        float leftMargin,
        final TL translator,
        final PdfFont pdfFont,
        final List<Anhangs> anhangs,
        final boolean addKopieAn
    ) {
        final float[] columnWidths = { 50, 50 };
        final Table signatureTable = PdfUtils.createTable(columnWidths, leftMargin);
        signatureTable.setMarginTop(SPACING_MEDIUM);
        signatureTable.addCell(PdfUtils.createCell(pdfFont, FONT_SIZE_BIG, 1, 1));
        signatureTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.footer.gruesse"),
                translator.translate("stip.pdf.header.abteilung")
            )
        );
        signatureTable.addCell(PdfUtils.createCell(pdfFont, FONT_SIZE_BIG, 1, 1));

        final var sachbearbeiterBenutzer =
            gesuch.getAusbildung().getFall().getSachbearbeiterZuordnung().getSachbearbeiter();

        final var locale = LocaleUtil.getLocale(gesuch);

        signatureTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                String.format("%s %s", sachbearbeiterBenutzer.getVorname(), sachbearbeiterBenutzer.getNachname()),
                sachbearbeiterBenutzer.getFunktion(locale)
            )
        );
        signatureTable.setMarginBottom(SPACING_MEDIUM);
        document.add(signatureTable);

        anhangs.forEach(anhang -> {
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "- ",
                    translator.translate(anhang.getTranslationKey())
                )
            );
        });

        if (addKopieAn) {
            addCopieAnParagraph(gesuch, translator, leftMargin, document, pdfFont);
        }
    }

    private void addCopieAnParagraph(
        final Gesuch gesuch,
        final TL translator,
        final float leftMargin,
        final Document document,
        final PdfFont pdfFont
    ) {
        if (gesuch.getAusbildung().getFall().getDelegierung() == null) {
            return;
        }

        final List<String> kopieAn = new ArrayList<>();
        final var sozialdienstName = gesuch.getAusbildung().getFall().getDelegierung().getSozialdienst().getName();
        final var sozialdienstAdresse = gesuch.getAusbildung()
            .getFall()
            .getDelegierung()
            .getSozialdienst()
            .getZahlungsverbindung()
            .getAdresse();

        kopieAn.add(sozialdienstName);
        if (Objects.nonNull(sozialdienstAdresse.getCoAdresse())) {
            kopieAn.add(sozialdienstAdresse.getCoAdresse());
        }
        kopieAn.add(sozialdienstAdresse.getStrasse().concat(" ").concat(sozialdienstAdresse.getHausnummer()));
        kopieAn.add(sozialdienstAdresse.getPlz().concat(" ").concat(sozialdienstAdresse.getOrt()));

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                "- ",
                translator.translate("stip.pdf.footer.kopieAn") + " ",
                String.join(", ", kopieAn)
            )
        );
    }

    public void rechtsmittelbelehrung(
        final TL translator,
        final Document document,
        final float leftMargin,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold
    ) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        document.add(
            PdfUtils.createParagraph(
                pdfFontBold,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(RECHTSMITTELBELEHRUNG_TITLE_KEY).toUpperCase()
            )
                .setUnderline()
                .setPaddingTop(SPACING_MEDIUM)
        );

        final Table mainTable = PdfUtils.createTable(new float[] { 10, 90 }, leftMargin);
        mainTable.setMarginTop(SPACING_MEDIUM);

        mainTable.addCell(PdfUtils.createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "1."));
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.gesetzlicheGrundlagen.title")
            )
        );
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.gesetzlicheGrundlagen.text")
            )
        );

        mainTable.addCell(PdfUtils.createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2."));
        mainTable.addCell(
            PdfUtils.createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, translator.translate(RECHTSMITTELBELEHRUNG_TITLE_KEY))
        );
        mainTable.addCell(
            PdfUtils
                .createCell(pdfFont, FONT_SIZE_BIG, 1, 1, translator.translate("stip.pdf.rechtsmittelbelehrung.text"))
        );

        mainTable.addCell(PdfUtils.createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.1."));
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.beschwerdeGruende.title")
            )
        );
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.beschwerdeGruende.text")
            )
        );

        mainTable.addCell(PdfUtils.createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.2."));
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.formUndSprache.title")
            )
        );
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.formUndSprache.text")
            )
        );

        mainTable.addCell(PdfUtils.createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.3."));
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.verfahrenskosten.title")
            )
        );
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.verfahrenskosten.text")
            )
        );

        mainTable.addCell(PdfUtils.createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "3."));
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.revision.title")
            )
        );
        mainTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.revision.text")
            )
        );

        document.add(mainTable);
    }
}
