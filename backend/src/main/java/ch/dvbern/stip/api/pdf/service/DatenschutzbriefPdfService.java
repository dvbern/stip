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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheService;
import ch.dvbern.stip.api.pdf.type.Anhangs;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_MEDIUM;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.LOGO_PATH;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.PAGE_SIZE;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_MEDIUM;

@ApplicationScoped
@RequiredArgsConstructor
public class DatenschutzbriefPdfService {
    private final GesuchTrancheService gesuchTrancheService;

    public ByteArrayOutputStream createDatenschutzbriefForElternteil(
        final Eltern elternteil,
        final UUID gesuchtrancheId
    ) {
        final var out = new ByteArrayOutputStream();

        final PdfFont pdfFont = PdfUtils.createFont();
        final PdfFont pdfFontBold = PdfUtils.createFontBold();

        final GesuchTranche gesuchTranche = gesuchTrancheService.getGesuchTranche(gesuchtrancheId);
        final Gesuch gesuch = gesuchTranche.getGesuch();

        final Locale locale = LocaleUtil.getLocale(gesuch);

        final TL translator = TLProducer.defaultBundle().forAppLanguage(AppLanguages.fromLocale(locale));

        try (
            final var writer = new PdfWriter(out);
            final var pdfDocument = new PdfDocument(writer);
            final var document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            final Image logo = PdfUtils.getLogo(pdfDocument, LOGO_PATH);

            document.add(logo);

            final var leftMargin = document.getLeftMargin();

            PdfUtils.header(
                gesuch,
                document,
                pdfDocument,
                leftMargin,
                translator,
                false,
                pdfFont,
                pdfFontBold,
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
                ? "stip.pdf.begruessung.frau"
                : "stip.pdf.begruessung.mann";

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
                    translator.translate(
                        "stip.pdf.datenschutzbrief.textBlock.vier",
                        "NAME_PIA",
                        pia.getFullName()
                    )
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

            final List<Anhangs> anhangs = List.of(Anhangs.RECHTSMITTELBELEHRUNG);
            PdfUtils.footer(gesuch, document, leftMargin, translator, pdfFont, anhangs, false);

            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            document.add(logo);

            returnLetter(gesuch, document, leftMargin, translator, pdfFont, pdfFontBold, elternteil, pia);

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        return out;
    }

    public void returnLetter(
        final Gesuch gesuch,
        final Document document,
        final float leftMargin,
        final TL translator,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold,
        final Eltern elternteil,
        final PersonInAusbildung pia
    ) {
        float[] columnWidths = { 50, 50 };
        final Table headerTable = PdfUtils.createTable(columnWidths, leftMargin);

        final Cell sender = PdfUtils.createCell(
            pdfFont,
            FONT_SIZE_MEDIUM,
            1,
            1,
            elternteil.getFullName(),
            String.format("%s %s", elternteil.getAdresse().getStrasse(), elternteil.getAdresse().getHausnummer()),
            String.format("%s %s", elternteil.getAdresse().getPlz(), elternteil.getAdresse().getOrt())
        ).setHeight(150).setVerticalAlignment(VerticalAlignment.BOTTOM);
        headerTable.addCell(sender);

        final Cell receiverNew = PdfUtils.createCell(
            pdfFont,
            FONT_SIZE_MEDIUM,
            2,
            1,
            translator.translate("stip.pdf.header.bkd"),
            translator.translate("stip.pdf.header.abteilung"),
            translator.translate("stip.pdf.header.strasse"),
            translator.translate("stip.pdf.header.plz")
        ).setHeight(200).setVerticalAlignment(VerticalAlignment.BOTTOM);
        headerTable.addCell(receiverNew);

        headerTable.setMarginBottom(SPACING_MEDIUM);

        document.add(headerTable);

        final var title = PdfUtils.createParagraph(
            pdfFontBold,
            FONT_SIZE_BIG,
            leftMargin,
            translator.translate("stip.pdf.datenschutzbrief.returnLetter.title")
        ).setMarginTop(SPACING_BIG * 2f);
        document.add(title);

        final var subtitle = PdfUtils.createParagraph(
            pdfFont,
            FONT_SIZE_BIG,
            leftMargin,
            String.format(
                "(%s; %s %s)",
                pia.getFullName(),
                translator.translate("stip.pdf.header.dossier.nr"),
                gesuch.getGesuchNummer()
            )
        );
        document.add(subtitle);

        final var einwilligung = PdfUtils.createParagraph(
            pdfFont,
            FONT_SIZE_BIG,
            leftMargin,
            translator.translate(
                "stip.pdf.datenschutzbrief.returnLetter.einwilligung",
                "NAME_PIA",
                pia.getFullName()
            )
        ).setMarginTop(SPACING_BIG);
        document.add(einwilligung);

        columnWidths = new float[] { 100 };
        final Table unterschrift = PdfUtils.createTable(columnWidths, leftMargin);
        unterschrift.setMarginRight(leftMargin);
        unterschrift.setMarginTop(SPACING_BIG);

        final String[] labels = {
            translator.translate("stip.pdf.datenschutzbrief.returnLetter.ortDatum"),
            translator.translate("stip.pdf.datenschutzbrief.returnLetter.name"),
            translator.translate("stip.pdf.datenschutzbrief.returnLetter.unterschrift"),
        };

        for (String label : labels) {

            final Cell cell = PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                label
            )
                .setBorderBottom(new DottedBorder(1))
                .setPaddingTop(SPACING_MEDIUM)
                .setPaddingBottom(0);
            unterschrift.addCell(cell);
        }

        document.add(unterschrift);
    }
}
