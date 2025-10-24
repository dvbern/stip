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
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.Constants;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.InternalServerErrorException;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.AUSBILDUNGSBEITRAEGE_LINK;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_BOLD_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_MEDIUM;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_SMALL;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.LOGO_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.PAGE_SIZE;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_MEDIUM;

@ApplicationScoped
@RequiredArgsConstructor
public class DatenschutzbriefPdfService {
    private final GesuchTrancheService gesuchTrancheService;

    private PdfFont pdfFont = null;
    private PdfFont pdfFontBold = null;
    private Link ausbildungsbeitraegeUri = null;

    public ByteArrayOutputStream createDatenschutzbriefForElternteil(
        final Eltern elternteil,
        final UUID gesuchtrancheId
    ) {
        final var out = new ByteArrayOutputStream();
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

        final GesuchTranche gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchtrancheId);
        final Gesuch gesuch = gesuchTranche.getGesuch();

        final Locale locale = gesuchTranche
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache()
            .getLocale();

        final TL translator = TLProducer.defaultBundle().forAppLanguage(AppLanguages.fromLocale(locale));

        try (
            final var writer = new PdfWriter(out);
            final var pdfDocument = new PdfDocument(writer);
            final var document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            final Image logo = PdfUtils.getLogo(pdfDocument, LOGO_PATH);
            logo.setMarginLeft(-25);
            logo.setMarginTop(-35);
            document.add(logo);

            final var leftMargin = document.getLeftMargin();

            PdfUtils.header(
                gesuch,
                document,
                leftMargin,
                translator,
                false,
                pdfFont,
                pdfFontBold,
                ausbildungsbeitraegeUri,
                Optional.of(elternteil)
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFontBold,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.pdf.datenschutzbrief.title")
                )
            );

            final String translationKey = elternteil.getElternTyp().equals(ElternTyp.MUTTER)
                ? "pdf.begruessung.frau"
                : "pdf.begruessung.herr";

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate(translationKey) + " ",
                    elternteil.getNachname()
                )
            );

            final var pia = gesuchTranche.getGesuchFormular().getPersonInAusbildung();

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate(
                        "stip.pdf.datenschutzbrief.textBlock.eins",
                        "NAME_PIA",
                        String.format(" %s %s", pia.getVorname(), pia.getNachname())
                    )
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.pdf.datenschutzbrief.textBlock.zwei.title"),
                    translator.translate("stip.pdf.datenschutzbrief.textBlock.zwei.body")
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.pdf.datenschutzbrief.textBlock.drei.title"),
                    translator.translate("stip.pdf.datenschutzbrief.textBlock.drei.body")
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFontBold,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.pdf.datenschutzbrief.textBlock.vier")
                )
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.pdf.datenschutzbrief.textBlock.fuenf")
                )
            );

            PdfUtils.footer(gesuch, document, leftMargin, translator, pdfFont, false);

            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            document.add(logo);

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        return out;
    }

    public void sendBackLetterHeader(
        final Gesuch gesuch,
        final Document document,
        final float leftMargin,
        final TL translator,
        final boolean isDeckblatt,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold,
        final Link ausbildungsbeitraegeUri,
        final Optional<Eltern> elternteilOptional
    ) {
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

        final Paragraph uriParagraph = new Paragraph().add(ausbildungsbeitraegeUri);

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
                    leftMargin,
                    "Einschreiben"
                )
            );

            receiver.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_MEDIUM,
                    leftMargin,
                    elternteil.getFullName()
                )
            );

            receiver.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_MEDIUM,
                    leftMargin,
                    elternteil.getAdresse().getStrasse() + " " + elternteil.getAdresse().getHausnummer()
                )
            );

            receiver.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_MEDIUM,
                    leftMargin,
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
            final Locale locale = gesuch
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung()
                .getKorrespondenzSprache()
                .getLocale();

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
}
