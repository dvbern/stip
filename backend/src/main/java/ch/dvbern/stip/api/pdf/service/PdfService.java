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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
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
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.InternalServerErrorException;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class PdfService {

    private static final String FONT_PATH = "/fonts/arial.ttf";
    private static final String FONT_BOLD_PATH = "/fonts/arial_bold.ttf";

    private static final int SPACING_BIG = 30;
    private static final int SPACING_MEDIUM = 20;
    private static final int SPACING_SMALL = 10;
    private static final float FONT_SIZE_BIG = 10.5f;
    private static final float FONT_SIZE_MEDIUM = 8.5f;
    private static final float FONT_SIZE_SMALL = 6.5f;
    private static final PageSize PAGE_SIZE = PageSize.A4;

    private static final String AUSBILDUNGSBEITRAEGE_LINK = "www.be.ch/ausbildungsbeitraege";

    private final StipDecisionTextRepository stipDecisionTextRepository;
    private final SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;

    private PdfFont pdfFont = null;
    private PdfFont pdfFontBold = null;
    private Link ausbildungsbeitraegeUri = null;

    public ByteArrayOutputStream createNegativeVerfuegungPdf(final Verfuegung verfuegung) {
        final PdfSection negativeVerfuegungSection = this::negativeVerfuegung;
        return this.createPdf(verfuegung, negativeVerfuegungSection);
    }

    public ByteArrayOutputStream createVerfuegungOhneAnspruchPdf(
        final Verfuegung verfuegung
    ) {
        final PdfSection negativeVerfuegungSection =
            this::verfuegungOhneAnspruch;
        return this.createPdf(verfuegung, negativeVerfuegungSection);
    }

    private ByteArrayOutputStream createPdf(
        final Verfuegung verfuegung,
        final PdfSection section
    ) {
        final Gesuch gesuch = verfuegung.getGesuch();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final FontProgram font;
        final FontProgram fontBold;

        try {
            final byte[] fontBytes = IOUtils.toByteArray(getClass().getResourceAsStream(FONT_PATH));
            final byte[] fontBoldBytes = IOUtils.toByteArray(getClass().getResourceAsStream(FONT_BOLD_PATH));
            font = FontProgramFactory.createFont(fontBytes);
            fontBold = FontProgramFactory.createFont(fontBoldBytes);
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        pdfFont = PdfFontFactory.createFont(font);
        pdfFontBold = PdfFontFactory.createFont(fontBold);

        ausbildungsbeitraegeUri = new Link(AUSBILDUNGSBEITRAEGE_LINK, PdfAction.createURI(AUSBILDUNGSBEITRAEGE_LINK));

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

            final Image logo = getLogo(pdfDocument, "/images/bern_logo.svg");
            logo.setMarginLeft(-25);
            logo.setMarginTop(-35);
            document.add(logo);

            header(gesuch, document, leftMargin, translator);
            section.render(verfuegung, document, leftMargin, translator);
            rechtsmittelbelehrung(translator, document, leftMargin);
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        return out;
    }

    private void header(final Gesuch gesuch, final Document document, float leftMargin, TL translator) {
        final float[] columnWidths = { 50, 50 };
        final Table headerTable = createTable(columnWidths, leftMargin);

        final GesuchFormular gesuchFormular = gesuch.getLatestGesuchTranche().getGesuchFormular();

        final Sachbearbeiter sachbearbeiterBenutzer = gesuch
            .getAusbildung()
            .getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter();

        final Cell sender = createCell(
            pdfFont,
            PdfService.FONT_SIZE_MEDIUM,
            1,
            1,
            translator.translate("stip.pdf.header.bkd"),
            translator.translate("stip.pdf.header.amt"),
            translator.translate("stip.pdf.header.abteilung")
        );

        final Cell address = createCell(
            pdfFont,
            FONT_SIZE_MEDIUM,
            1,
            1,
            translator.translate("stip.pdf.header.strasse"),
            translator.translate("stip.pdf.header.plz")
        );

        final Paragraph uriParagraph = new Paragraph().add(ausbildungsbeitraegeUri);

        final Cell url = createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1).setPaddingBottom(0).add(uriParagraph);

        final Paragraph recieverHeaderParagraph = new Paragraph()
            .add(translator.translate("stip.pdf.header.bkd") + ", ")
            .add(translator.translate("stip.pdf.header.strasse") + ", ")
            .add(translator.translate("stip.pdf.header.plz"));

        final Cell recieverHeader = createCell(pdfFont, FONT_SIZE_SMALL, 3, 1)
            .setVerticalAlignment(VerticalAlignment.BOTTOM)
            .setBorderBottom(new SolidBorder(1))
            .setPaddingBottom(0);
        recieverHeader.add(recieverHeaderParagraph);

        final Cell receiver = createCell(
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

        final Link email =
            new Link(sachbearbeiterBenutzer.getEmail(), PdfAction.createURI("mailto:peter.muster@be.ch"));
        final Paragraph emailParagraph = new Paragraph().add(email);

        final Cell sachbearbeiter = createCell(
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

        final Paragraph dossierNr = new Paragraph()
            .add(translator.translate("stip.pdf.header.dossier.nr") + " ")
            .add(gesuch.getGesuchNummer());

        final Paragraph svNr = new Paragraph()
            .add(translator.translate("stip.pdf.header.svNr") + " ")
            .add(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());

        final Cell fallInformations = createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1);
        fallInformations.add(dossierNr);
        fallInformations.add(svNr);

        final Cell date = createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1, DateUtil.formatDate(LocalDate.now()));

        final Locale locale = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache()
            .getLocale();

        final var ausbildungsStaette = gesuch.getAusbildung().getAusbildungsstaetteOrAlternative(locale);
        final var ausbildungsGang = gesuch.getAusbildung().getAusbildungsgangOrAlternative(locale);

        final Cell ausbildungsgang = createCell(pdfFont, FONT_SIZE_MEDIUM, 1, 1, ausbildungsStaette, ausbildungsGang);

        headerTable.addCell(sender);
        headerTable.addCell(recieverHeader);
        headerTable.addCell(address);
        headerTable.addCell(url);
        headerTable.addCell(sachbearbeiter);
        headerTable.addCell(receiver);
        headerTable.addCell(fallInformations);
        headerTable.addCell(date);
        headerTable.addCell(ausbildungsgang);
        headerTable.setMarginBottom(SPACING_MEDIUM);

        document.add(headerTable);
    }

    private void rechtsmittelbelehrung(TL translator, Document document, float leftMargin) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        document.add(
            createParagraph(
                pdfFontBold,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.rechtsmittelbelehrung.title").toUpperCase()
            )
                .setUnderline()
                .setPaddingTop(SPACING_MEDIUM)
        );

        final Table mainTable = createTable(new float[] { 10, 90 }, leftMargin);
        mainTable.setMarginTop(SPACING_MEDIUM);

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "1."));
        mainTable.addCell(
            createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.gesetzlicheGrundlagen.title")
            )
        );
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.gesetzlicheGrundlagen.text")
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2."));
        mainTable.addCell(
            createCell(pdfFontBold, FONT_SIZE_BIG, 1, 1, translator.translate("stip.pdf.rechtsmittelbelehrung.title"))
        );
        mainTable.addCell(
            createCell(pdfFont, FONT_SIZE_BIG, 1, 1, translator.translate("stip.pdf.rechtsmittelbelehrung.text"))
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.1."));
        mainTable.addCell(
            createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.beschwerdeGruende.title")
            )
        );
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.beschwerdeGruende.text")
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.2."));
        mainTable.addCell(
            createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.formUndSprache.title")
            )
        );
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.formUndSprache.text")
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "2.3."));
        mainTable.addCell(
            createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.verfahrenskosten.title")
            )
        );
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.verfahrenskosten.text")
            )
        );

        mainTable.addCell(createCell(pdfFontBold, FONT_SIZE_BIG, 2, 1, "3."));
        mainTable.addCell(
            createCell(
                pdfFontBold,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.revision.title")
            )
        );
        mainTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.rechtsmittelbelehrung.revision.text")
            )
        );

        document.add(mainTable);
    }

    private void verfuegungOhneAnspruch(
        final Verfuegung verfuegung,
        final Document document,
        final float leftMargin,
        final TL translator
    ) throws IOException {
        final LocalDate ausbildungsjahrVon = verfuegung.getGesuch()
            .getGesuchTranchen()
            .stream()
            .map(GesuchTranche::getGueltigkeit)
            .min(Comparator.comparing(DateRange::getGueltigAb))
            .get()
            .getGueltigAb();

        final LocalDate ausbildungsjahrBis = verfuegung.getGesuch()
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
        document.add(
            createParagraph(
                pdfFontBold,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegung.ausbildungsjahr"
                ),
                ausbildungsjahr,
                String.format(
                    " (%s - %s)",
                    DateUtil.formatDate(ausbildungsjahrVon),
                    DateUtil.formatDate(ausbildungsjahrBis)
                )
            )
        );

        final PersonInAusbildung personInAusbildung = verfuegung.getGesuch()
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String translateKey = personInAusbildung
            .getAnrede()
            .equals(Anrede.HERR)
                ? "stip.pdf.verfuegung.begruessung.mann"
                : "stip.pdf.verfuegung.begruessung.frau";

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(translateKey) + " ",
                personInAusbildung.getNachname()
            )
        );

        final String einreichedatum = DateUtil.formatDate(
            Objects.requireNonNull(verfuegung.getGesuch().getEinreichedatum())
        );
        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegung.bedankung",
                    "DATUM",
                    einreichedatum
                )
            )
        );

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.eins"
                )
            )
        );

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.zwei"
                )
            )
        );

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.drei"
                )
            )
        );

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.vier"
                )
            ).add(ausbildungsbeitraegeUri)
        );
    }

    private void negativeVerfuegung(
        final Verfuegung verfuegung,
        final Document document,
        final float leftMargin,
        final TL translator
    ) throws IOException {
        final var gesuch = verfuegung.getGesuch();

        final Locale locale = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache()
            .getLocale();

        final Sachbearbeiter sachbearbeiterBenutzer = gesuch
            .getAusbildung()
            .getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter();

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

        final String ausbildungsjahr = String.format(
            " %d/%d",
            ausbildungsjahrVon.getYear(),
            ausbildungsjahrBis.getYear()
        );
        document.add(
            createParagraph(
                pdfFontBold,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.verfuegung.ausbildungsjahr"),
                ausbildungsjahr,
                String.format(
                    " (%s - %s)",
                    DateUtil.formatDate(ausbildungsjahrVon),
                    DateUtil.formatDate(ausbildungsjahrBis)
                )
            )
        );

        final PersonInAusbildung personInAusbildung = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung();
        final String translateKey = personInAusbildung.getAnrede().equals(Anrede.HERR)
            ? "stip.pdf.verfuegung.begruessung.mann"
            : "stip.pdf.verfuegung.begruessung.frau";

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(translateKey) + " ",
                personInAusbildung.getNachname()
            )
        );

        final String einreichedatum = DateUtil.formatDate(Objects.requireNonNull(gesuch.getEinreichedatum()));
        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.verfuegung.bedankung", "DATUM", einreichedatum)
            )
        );

        String decision = locale.getLanguage().equals("de")
            ? stipDecisionTextRepository.getTextByStipDecision(verfuegung.getStipDecision()).getTextDe()
            : stipDecisionTextRepository.getTextByStipDecision(verfuegung.getStipDecision()).getTextFr();

        decision = decision.replace("{AUSBILDUNGSJAHR}", ausbildungsjahr);
        decision = decision.replace("{EINREICHEDATUM}", einreichedatum);
        final var kanton = Optional.ofNullable(verfuegung.getKanton());

        if (kanton.isPresent()) {
            final var wohnsitzKantonName = translator.translate(kanton.get().getTlKey());
            decision = decision.replace("{WOHNSITZKANTON}", wohnsitzKantonName);
        }

        document.add(createParagraph(pdfFont, FONT_SIZE_BIG, leftMargin, decision));

        document.add(
            createParagraph(pdfFont, FONT_SIZE_BIG, leftMargin)
                .add(translator.translate("stip.pdf.verfuegung.entschuldigung") + " ")
                .add(ausbildungsbeitraegeUri)
                .add(".")
        );

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.verfuegung.glueckWunsch")
            ).setPaddingTop(SPACING_SMALL)
        );

        final float[] columnWidths = { 50, 50 };
        final Table signatureTable = createTable(columnWidths, leftMargin);
        signatureTable.setMarginTop(SPACING_MEDIUM);
        signatureTable.addCell(createCell(pdfFont, FONT_SIZE_BIG, 1, 1));
        signatureTable.addCell(
            createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.footer.gruesse"),
                translator.translate("stip.pdf.header.abteilung")
            )
        );
        signatureTable.addCell(createCell(pdfFont, FONT_SIZE_BIG, 1, 1));

        signatureTable.addCell(
            createCell(
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

        document.add(
            createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                "- ",
                translator.translate("stip.pdf.rechtsmittelbelehrung.title")
            )
        );

        final List<String> kopieAn = new ArrayList<>();

        steuerdatenTabBerechnungsService
            .calculateTabs(gesuch.getLatestGesuchTranche().getGesuchFormular().getFamiliensituation())
            .forEach(steuerdatenTyp -> {
                switch (steuerdatenTyp) {
                    case FAMILIE -> kopieAn.add(translator.translate("stip.pdf.footer.kopieAn.eltern"));
                    case MUTTER -> kopieAn.add(translator.translate("stip.pdf.footer.kopieAn.mutter"));
                    case VATER -> kopieAn.add(translator.translate("stip.pdf.footer.kopieAn.vater"));
                }
            });

        if (gesuch.getAusbildung().getFall().getDelegierung() != null) {
            kopieAn.add(gesuch.getAusbildung().getFall().getDelegierung().getSozialdienst().getName());
        }

        if (!kopieAn.isEmpty()) {
            document.add(
                createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    "- ",
                    translator.translate("stip.pdf.footer.kopieAn") + " ",
                    String.join(", ", kopieAn)
                )
            );
        }
    }

    private Image getLogo(PdfDocument pdfDocument, String pathToSvg) throws IOException {
        final ByteArrayOutputStream svgOut = new ByteArrayOutputStream();
        try (final InputStream svgStream = getClass().getResourceAsStream(pathToSvg)) {
            SvgConverter.createPdf(svgStream, svgOut);
        }
        try (
            final PdfReader reader = new PdfReader(new ByteArrayInputStream(svgOut.toByteArray()));
            final PdfDocument tempSvgDoc = new PdfDocument(reader)
        ) {
            final PdfFormXObject xObject = tempSvgDoc.getFirstPage().copyAsFormXObject(pdfDocument);
            return new Image(xObject).scaleToFit(150, 87);
        }
    }

    private Paragraph createParagraph(PdfFont font, float fontSize, float leftMargin, String... texts) {
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

    private Table createTable(float[] columnWidths, float leftMargin) {
        final Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginLeft(leftMargin);
        return table;
    }

    private Cell createCell(PdfFont font, float fontSize, int rowSpan, int colSpan, String... paragraphs) {
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

    @FunctionalInterface
    private static interface PdfSection {
        void render(Verfuegung verfuegung, Document document, float leftMargin, TL translator)
        throws IOException;
    }

}
