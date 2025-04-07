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

package ch.dvbern.stip.api.pdf;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.svg.converter.SvgConverter;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class PdfService {
    private static final String FONT_PATH = "src/main/resources/fonts/arial.ttf";
    private static final String FONT_BOLD_PATH = "src/main/resources/fonts/arial_bold.ttf";

    private static final float FONT_SIZE_BIG = 10.5f;
    private static final float FONT_SIZE_MEDIUM = 8.5f;
    private static final float FONT_SIZE_SMALL = 6.5f;

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(new Locale("de", "CH"));

    private static final PageSize PAGE_SIZE = PageSize.A4;

    PdfFont pdfFont = null;
    PdfFont pdfFontBold = null;

    public void onStart(@Observes StartupEvent event) {
        try {
            Locale locale = new Locale("de", "CH");

            ByteArrayOutputStream pdfOutput = createPdf(locale);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = now.format(formatter);

            String outputFileName = "generated-" + timestamp + ".pdf";
            File outputFile = new File(outputFileName);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                pdfOutput.writeTo(fos);
            }
            LOG.info("PDF created: {}", outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ByteArrayOutputStream createPdf(final Locale locale)
    throws Exception {
        FontProgram font = FontProgramFactory.createFont(FONT_PATH);
        FontProgram fontBold = FontProgramFactory.createFont(FONT_BOLD_PATH);

        pdfFont = PdfFontFactory.createFont(font);
        pdfFontBold = PdfFontFactory.createFont(fontBold);

        TL translator = TLProducer.defaultBundle()
            .forAppLanguage(
                AppLanguages.fromLocale(locale)
            );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PAGE_SIZE);

        float leftMargin = document.getLeftMargin();

        Image logo = getLogo(pdfDocument, "src/main/resources/images/bern_logo.svg");
        logo.setMarginLeft(-25);
        logo.setMarginTop(-35);
        document.add(logo);

        header(document, pdfFont, leftMargin);

        negativeVerfuegung(leftMargin, document);

        rechtsmittelbelehrung(locale, document, leftMargin);

        document.close();
        pdfDocument.close();
        writer.close();
        return out;
    }

    private void rechtsmittelbelehrung(Locale locale, Document document, float leftMargin) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        document.add(
            createParagraph(pdfFontBold, FONT_SIZE_BIG, leftMargin, "Rechtsmittelbelehrung".toUpperCase(locale))
                .setUnderline()
                .setPaddingTop(20)
        );

        Table mainTable = createTable(new float[] { 10, 90 }, leftMargin);
        mainTable.setMarginTop(20);

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "1."));
        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, "Gesetzliche Grundlagen"));
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Verfügungen der Abteilung Ausbildungsbeiträge (AAB) der kantonalen Bildungs- und Kultur-direktion des Kantons Bern stützen sich auf die einschlägigen gesetzlichen Grundlagen. "
                + "Gesetz vom 18. November 2004 über die Ausbildungsbeiträge (ABG; BSG 438.31) und Verordnung vom 5. April 2006 über die Ausbildungsbeiträge (ABV; BSG 438.312). "
                + "Diese Rechtsgrundlagen können unter www.be.ch/ausbildungsbeitraege eingesehen oder bei der Staatskanzlei des Kantons Bern bezogen werden."
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2."));
        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, "Rechtsmittelbelehrung"));
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Gegen Verfügungen der AAB kann innert 30 Tagen seit Erhalt des Entscheides bei der Bildungs- und Kulturdirektion des Kantons Bern, "
                + "Rechtsdienst, Sulgeneckstrasse 70, 3005 Bern, gemäss den Vorschriften des Gesetzes vom 23. Mai 1989 über die Verwaltungsrechtspflege (VRPG; BSG 155.21) "
                + "schriftlich und begründet Beschwerde geführt werden."
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.1."));
        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, "Beschwerdegründe gemäss Art. 66 VRPG"));
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Mit einer Beschwerde können die unrichtige oder unvollständige Feststellung des rechtserheblichen Sachverhaltes, unrichtige Anwendung der gesetzlichen Bestimmungen sowie "
                + "Unangemessenheit - soweit ein gesetzlicher Ermessensspielraum besteht - gerügt werden."
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.2."));
        mainTable.addCell(
            createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, "Form und Sprache einer Beschwerde gemäss Art. 32 VRPG")
        );
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Eingaben sind im Doppel in deutscher oder französischer Sprache einzureichen. Sie müssen einen Antrag, die Angabe von Tatsachen und Beweismitteln, eine Begründung "
                + "sowie eine Unterschrift enthalten; greifbare Beweismittel sind beizulegen."
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.3."));
        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, "Verfahrenskosten"));
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Die Verfahrenskosten werden grundsätzlich der unterliegenden Partei auferlegt und betragen gegenwärtig mindestens 200 Franken."
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "3."));
        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, "Revision"));
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Für Begehren, die sich aus Veränderungen der Berechnungsgrundlagen (Einnahmen, Ausgaben, etc.) ergeben, ist keine Beschwerde zu erheben, sondern ein Revisionsgesuch "
                + "an die Abteilung Ausbildungsbeiträge zu richten."
            )
        );

        document.add(mainTable);
    }

    private void negativeVerfuegung(float leftMargin, Document document) {
        Paragraph title = createParagraph(
            pdfFontBold,
            FONT_SIZE_BIG,
            leftMargin,
            "Vefügung für das Ausbildungsjahr ",
            "2024/2025 ",
            "(01.09.2024 - 31.08.2025)"
        );
        document.add(title);

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                "Sehr ",
                "geehrte Frau",
                " ",
                "Gesuchstellerin"
            )
        );

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                "Vielen Dank für Ihr Gesuch vom ",
                "14.12.2024",
                ", welches wir gerne geprüft haben."
            )
        );

        document.add(createParagraph(pdfFont, FONT_SIZE_BIG, leftMargin, "TEXTBAUSTEIN"));

        Link ausbildungsbeitraegeUri = new Link(
            "www.be.ch/ausbildungsbeitraege",
            PdfAction.createURI("www.be.ch/ausbildungsbeitraege")
        );
        Paragraph apology = createParagraph(pdfFont, FONT_SIZE_BIG, leftMargin)
            .add(
                "Wir bedauern, Ihnen keinen positiven Bericht geben zu können. Auf unserer Internetseite unter " +
                "«anderweitige Beiträge» finden Sie einige Informationen über Stiftungen und Fonds, welche unter " +
                "bestimmten Voraussetzungen Ausbildungsbeiträge gewähren: "
            )
            .add(ausbildungsbeitraegeUri)
            .add(".");
        document.add(apology);

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                "Für Ihre Ausbildung wünschen wir Ihnen viel Erfolg."
            ).setPaddingTop(10)
        );

        float[] columnWidths = { 50, 50 };
        Table signatureTable = createTable(columnWidths, leftMargin);
        signatureTable.setMarginTop(20);
        signatureTable.addCell(createCell(pdfFont, FONT_SIZE_BIG, 1, 1));
        signatureTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Freundliche Grüsse",
                "Abteilung Ausbildungs"
            )
        );
        signatureTable.addCell(createCell(pdfFont, FONT_SIZE_BIG, 1, 1));
        signatureTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                "Peter Muster",
                "Sachbearbeiter"
            )
        );
        signatureTable.setMarginBottom(20);
        document.add(signatureTable);

        String[] addons = { "Rechtsmittelbelehrung", "Kopie an: Eltern" };

        for (String addon : addons) {
            document.add(
                createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "- ",
                    addon
                )
            );
        }
    }

    private void header(Document document, PdfFont font, float leftMargin) {
        float[] columnWidths = { 50, 50 };
        Table headerTable = createTable(columnWidths, leftMargin);

        Cell sender = createCell(
            font,
            PdfService.FONT_SIZE_MEDIUM,
            1,
            1,
            "Bildungs- und Kulturdirektion",
            "Amt für zentrale Dienste",
            "Abteilung Ausbildungsbeiträge"
        );

        Cell address = createCell(
            font,
            FONT_SIZE_MEDIUM,
            1,
            1,
            "Sulgeneckstrasse 70",
            "3005 Bern"
        );

        Link ausbildungsbeitraegeUri =
            new Link("www.be.ch/ausbildungsbeitraege", PdfAction.createURI("www.be.ch/ausbildungsbeitraege"));
        Paragraph uriParagraph = new Paragraph()
            .add(ausbildungsbeitraegeUri);

        Cell url = createCell(font, FONT_SIZE_MEDIUM, 1, 1)
            .setPaddingBottom(0)
            .add(uriParagraph);

        Paragraph recieverHeaderParagraph = new Paragraph()
            .add("Bildungs- und Kulturdirektion, ")
            .add("Sulgenauackerstrasse 70, ")
            .add("3005 Bern");

        Cell recieverHeader = createCell(font, FONT_SIZE_SMALL, 3, 1)
            .setVerticalAlignment(VerticalAlignment.BOTTOM)
            .setBorderBottom(new SolidBorder(1))
            .setPaddingBottom(0);
        recieverHeader.add(recieverHeaderParagraph);

        Cell receiver = createCell(
            font,
            FONT_SIZE_MEDIUM,
            1,
            1,
            "Lisa Gesuchstellerin",
            "Beispielstrasse 5",
            "3012 Bern"
        )
            .setPaddingTop(20);

        Link email = new Link("peter.muster@be.ch", PdfAction.createURI("mailto:peter.muster@be.ch"));
        Paragraph emailParagraph = new Paragraph()
            .add(email);

        Cell sachbearbeiter = createCell(
            font,
            FONT_SIZE_MEDIUM,
            1,
            1,
            "Peter Muster",
            "031 300 30 30"
        ).setPaddingBottom(30)
            .setPaddingTop(20)
            .add(emailParagraph);

        Paragraph dossierNr = new Paragraph()
            .add("Dossier-Nr: ")
            .add("2024.BE.G.59959");

        Paragraph svNr = new Paragraph()
            .add("Sv-Nr: ")
            .add("756.8904.8005.37");

        Cell fallInformations = createCell(font, FONT_SIZE_MEDIUM, 1, 1);
        fallInformations.add(dossierNr);
        fallInformations.add(svNr);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMM yyyy");
        String timestamp = now.format(formatter);

        Cell date = createCell(
            font,
            FONT_SIZE_MEDIUM,
            1,
            1,
            timestamp
        );

        Cell ausbildungsgang = createCell(
            font,
            FONT_SIZE_MEDIUM,
            1,
            1,
            "Universität",
            "Bachelor"
        );

        headerTable.addCell(sender);
        headerTable.addCell(recieverHeader);
        headerTable.addCell(address);
        headerTable.addCell(url);
        headerTable.addCell(sachbearbeiter);
        headerTable.addCell(receiver);
        headerTable.addCell(fallInformations);
        headerTable.addCell(date);
        headerTable.addCell(ausbildungsgang);
        headerTable.setMarginBottom(20);

        document.add(headerTable);
    }

    private Image getLogo(PdfDocument pdfDocument, String pathToSvg) throws IOException {
        ByteArrayOutputStream svgOut = new ByteArrayOutputStream();
        try (InputStream svgStream = new FileInputStream(pathToSvg)) {
            SvgConverter.createPdf(svgStream, svgOut);
        }
        try (
            PdfReader reader = new PdfReader(new ByteArrayInputStream(svgOut.toByteArray()));
            PdfDocument tempSvgDoc = new PdfDocument(reader)
        ) {
            PdfFormXObject xObject = tempSvgDoc.getFirstPage().copyAsFormXObject(pdfDocument);
            return new Image(xObject).scaleToFit(150, 87);
        }
    }

    private Paragraph createParagraph(PdfFont font, float fontSize, float leftMargin, String... texts) {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(font);
        paragraph.setFontSize(fontSize);
        paragraph.setMultipliedLeading(1f);
        paragraph.setMarginLeft(leftMargin);
        for (String text : texts) {
            paragraph.add(new Text(text));
        }
        return paragraph;
    }

    private Table createTable(float[] columnWidths, float leftMargin) {
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginLeft(leftMargin);
        return table;
    }

    private Cell createCell(PdfFont font, float fontSize, int rowSpan, int colSpan, String... paragraphs) {
        Cell cell = new Cell(rowSpan, colSpan)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .setFont(font)
            .setFontSize(fontSize)
            .setPaddingBottom(10);
        for (String text : paragraphs) {
            cell.add(createParagraph(font, fontSize, 0, text));
        }
        return cell;
    }
}
