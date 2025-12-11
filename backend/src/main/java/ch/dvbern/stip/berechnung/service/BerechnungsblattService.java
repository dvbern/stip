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

package ch.dvbern.stip.berechnung.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class BerechnungsblattService {
    private final BerechnungService berechnungService;
    private final UnterschriftenblattService unterschriftenblattService;

    private static final String FONT = StandardFonts.HELVETICA;
    private static final String FONT_BOLD = StandardFonts.HELVETICA_BOLD;

    private static final int FONT_SIZE = 10;
    private static final int FONT_SIZE_SMALL = 8;

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.of("de", "CH"));

    private static final PageSize PAGE_SIZE = PageSize.A4;

    private static final UnitValue[] TABLE_WIDTH_PERCENTAGES = UnitValue.createPercentArray(new float[] { 85, 15 });

    PdfFont pdfFont = null;
    PdfFont pdfFontBold = null;

    private static final Map<SteuerdatenTyp, UnterschriftenblattDokumentTyp> UNTERSCHRIFTENBLATT_DOKUMENT_TYP_STEUERDATEN_TYP_MAP =
        Map.of(
            SteuerdatenTyp.FAMILIE,
            UnterschriftenblattDokumentTyp.GEMEINSAM,
            SteuerdatenTyp.VATER,
            UnterschriftenblattDokumentTyp.VATER,
            SteuerdatenTyp.MUTTER,
            UnterschriftenblattDokumentTyp.MUTTER
        );

    public void addBerechnungsblattToDocument(
        final Gesuch gesuch,
        final Locale locale,
        Document document,
        final boolean addAllBerechnungsblaetter
    )
    throws IOException {
        pdfFont = PdfFontFactory.createFont(FONT);
        pdfFontBold = PdfFontFactory.createFont(FONT_BOLD);

        TL translator = TLProducer.defaultBundle()
            .forAppLanguage(
                AppLanguages.fromLocale(locale)
            );
        PersonInAusbildung pia = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();

        var berechnungsResultat = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        boolean firstTranche = true;
        for (var tranchenBerechnungsResultat : berechnungsResultat.getTranchenBerechnungsresultate()) {
            if (!firstTranche) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
            firstTranche = false;

            addHeaderParagraph(
                document,
                pia,
                String.format(
                    "%s %s %s",
                    translator.translate("stip.berechnung.persoenlich.title"),
                    pia.getVorname(),
                    pia.getNachname()
                ),
                tranchenBerechnungsResultat.getGueltigAb(),
                tranchenBerechnungsResultat.getGueltigBis(),
                translator
            );
            document.add(getNewLineParagraph());

            Table persoenlichesBudgetTableEinnahmen = getPersoenlichesBudgetTableEinnahmen(
                tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                tranchenBerechnungsResultat.getBerechnungsStammdaten(),
                translator
            );

            document.add(persoenlichesBudgetTableEinnahmen);
            document.add(getNewLineParagraph());

            Table persoenlichesBudgetTableKosten =
                getPersoenlichesBudgetTableKosten(
                    tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                    translator
                );

            document.add(persoenlichesBudgetTableKosten);
            document.add(getNewLineParagraph());

            var persoenlichTotalCell = new Cell();
            persoenlichTotalCell.add(
                getDefaultParagraphTranslated("stip.berechnung.persoenlich.total", translator).setFont(pdfFontBold)
            );
            if (
                !(tranchenBerechnungsResultat.getBerechnungsanteilKinder() != null
                && tranchenBerechnungsResultat.getBerechnungsanteilKinder().compareTo(BigDecimal.ONE) == 0)
            ) {
                persoenlichTotalCell.add(
                    getDefaultParagraphSmall(
                        translator.translate(
                            "stip.berechnung.persoenlich.geteilteBerechnung",
                            "berechnungsanteilKinder",
                            tranchenBerechnungsResultat.getBerechnungsanteilKinder()
                        )
                    ).setFontColor(ColorConstants.WHITE)
                );
            }

            Table persoenlichesBudgetTableTotal = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
            persoenlichesBudgetTableTotal.addCell(persoenlichTotalCell).setBackgroundColor(ColorConstants.LIGHT_GRAY);

            persoenlichesBudgetTableTotal.addCell(
                getDefaultParagraphNumber(
                    tranchenBerechnungsResultat.getBerechnung()
                ).setFont(pdfFontBold).setBackgroundColor(ColorConstants.LIGHT_GRAY)
            );

            document.add(persoenlichesBudgetTableTotal);
            document.add(getNewLineParagraph());

            addFooterParagraph1(document, 40, translator);
            addFooterParagraph2(document, 15, translator);

            var existingUnterschriftenblaetterTyps =
                unterschriftenblattService.getExistingUnterschriftenblattTypsForGesuch(gesuch.getId());

            for (var fammilienBudgetResultat : tranchenBerechnungsResultat.getFamilienBudgetresultate()) {
                var steuerdatentyp = fammilienBudgetResultat.getSteuerdatenTyp();
                var requiredUnterschriftenblatttyp =
                    UNTERSCHRIFTENBLATT_DOKUMENT_TYP_STEUERDATEN_TYP_MAP.get(steuerdatentyp);

                if (
                    !addAllBerechnungsblaetter
                    && !existingUnterschriftenblaetterTyps.contains(requiredUnterschriftenblatttyp)
                ) {
                    continue;
                }
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                var budgetTypText = switch (fammilienBudgetResultat.getSteuerdatenTyp()) {
                    case FAMILIE -> translator.translate("stip.berechnung.familien.typ.FAMILIE");
                    case VATER -> translator.translate("stip.berechnung.familien.typ.VATER");
                    case MUTTER -> translator.translate("stip.berechnung.familien.typ.MUTTER");
                };

                addHeaderParagraph(
                    document,
                    pia,
                    String.format(
                        "%s %s",
                        translator.translate("stip.berechnung.familien.title"),
                        budgetTypText
                    ),
                    tranchenBerechnungsResultat.getGueltigAb(),
                    tranchenBerechnungsResultat.getGueltigBis(),
                    translator
                );
                document.add(getNewLineParagraph());

                Table familienBudgetTableEinnahmen = getFamilienBudgetTableEinnahmen(
                    fammilienBudgetResultat,
                    tranchenBerechnungsResultat.getBerechnungsStammdaten(),
                    translator
                );

                document.add(familienBudgetTableEinnahmen);
                document.add(getNewLineParagraph());

                Table familienBudgetTableKosten = getFamilienBudgetTableKosten(
                    fammilienBudgetResultat,
                    translator
                );

                document.add(familienBudgetTableKosten);
                document.add(getNewLineParagraph());
                addFooterParagraph1(document, 15, translator);
            }
        }
    }

    public ByteArrayOutputStream getBerechnungsblattFromGesuch(final Gesuch gesuch, final Locale locale)
    throws IOException {
        pdfFont = PdfFontFactory.createFont(FONT);
        pdfFontBold = PdfFontFactory.createFont(FONT_BOLD);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PAGE_SIZE);
        addBerechnungsblattToDocument(gesuch, locale, document, true);
        PdfUtils.makePageNumberEven(document);
        document.close();
        pdfDocument.close();
        writer.close();
        return out;
    }

    private void addHeaderParagraph(
        Document document,
        PersonInAusbildung pia,
        String budgetTypeText,
        LocalDate trancheVon,
        LocalDate trancheBis,
        TL tl
    ) {

        float leftMargin = document.getLeftMargin();
        Paragraph line1Paragraph = new Paragraph(
            budgetTypeText
        )
            .setFont(pdfFontBold)
            .setFontSize(10)
            .setHorizontalAlignment(HorizontalAlignment.LEFT)
            .setFixedPosition(
                leftMargin,
                PAGE_SIZE.getHeight() - 25,
                PAGE_SIZE.getWidth() - leftMargin * 2
            )
            .setTextAlignment(TextAlignment.LEFT);

        document.add(line1Paragraph);

        Paragraph line2Part1Paragraph = new Paragraph(
            String.format(
                "%s %s, AHV Nr.: %s, %s",
                pia.getNachname(),
                pia.getVorname(),
                pia.getSozialversicherungsnummer(),
                pia.getGeburtsdatum().toString()
            )
        ).setFont(pdfFont)
            .setFontSize(10)
            .setHorizontalAlignment(HorizontalAlignment.LEFT)
            .setFixedPosition(
                leftMargin,
                PAGE_SIZE.getHeight() - 40,
                PAGE_SIZE.getWidth() - leftMargin * 2
            )
            .setTextAlignment(TextAlignment.LEFT);

        document.add(line2Part1Paragraph);

        Paragraph line2Part2Paragraph = new Paragraph(
            String.format(
                "%s: %s, %s: %s, %s: %d",
                tl.translate("stip.berechnung.von"),
                trancheVon.toString(),
                tl.translate("stip.berechnung.bis"),
                trancheBis.toString(),
                tl.translate("stip.berechnung.monate"),
                DateUtil.getMonthsBetween(
                    trancheVon,
                    trancheBis
                )
            )
        ).setFont(pdfFont)
            .setFontSize(10)
            .setHorizontalAlignment(HorizontalAlignment.LEFT)
            .setFixedPosition(
                leftMargin,
                PAGE_SIZE.getHeight() - 40,
                PAGE_SIZE.getWidth() - leftMargin * 2
            )
            .setTextAlignment(TextAlignment.RIGHT);

        document.add(line2Part2Paragraph);
    }

    private void addFooterParagraph1(
        Document document,
        int verticalOffset,
        TL tl
    ) {
        float leftMargin = document.getLeftMargin();
        document.add(
            new Paragraph(tl.translate("stip.berechnung.footer.info"))
                .setFont(pdfFont)
                .setFontSize(7)
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setFixedPosition(
                    leftMargin,
                    verticalOffset,
                    PAGE_SIZE.getWidth() - leftMargin * 2
                )
                .setTextAlignment(TextAlignment.LEFT)
        );
    }

    private void addFooterParagraph2(
        Document document,
        int verticalOffset,
        TL tl
    ) {
        float leftMargin = document.getLeftMargin();
        document.add(
            new Paragraph(tl.translate("stip.berechnung.footer.kosten"))
                .setFont(pdfFont)
                .setFontSize(7)
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setFixedPosition(
                    leftMargin,
                    verticalOffset,
                    PAGE_SIZE.getWidth() - leftMargin * 2
                )
                .setTextAlignment(TextAlignment.LEFT)
        );
    }

    private Cell getDefaultParagraphCellTranslatedWithInfo(final String key, final TL tl) {
        var cell = new Cell();
        cell.add(getDefaultParagraphTranslated(key, tl));
        cell.add(getDefaultParagraphTranslatedSmall(key + ".info", tl));
        return cell;
    }

    private Paragraph getDefaultParagraphTranslated(final String key, final TL tl) {
        return getDefaultParagraph(tl.translate(key));
    }

    private Paragraph getDefaultParagraphTranslatedSmall(final String key, final TL tl) {
        return getDefaultParagraphSmall(tl.translate(key));
    }

    private Paragraph getDefaultParagraphNumber(final Number number) {
        return getDefaultParagraph(NUMBER_FORMAT.format(number)).setTextAlignment(TextAlignment.RIGHT);

    }

    private Paragraph getDefaultParagraphSmall(final String text) {
        return new Paragraph(text).setFont(pdfFont).setFontSize(FONT_SIZE_SMALL).setFontColor(ColorConstants.GRAY);
    }

    private Paragraph getDefaultParagraph(final String text) {
        return new Paragraph(text).setFont(pdfFont).setFontSize(FONT_SIZE);
    }

    private Paragraph getNewLineParagraph() {
        return new Paragraph("");
    }

    private Table getPersoenlichesBudgetTableEinnahmen(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        final TL tl
    ) {
        Table persoenlichesBudgetTableEinnahmen = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addPersoenlichesBudgetEinnahmenToTable(
            persoenlichesBudgetresultat,
            berechnungsStammdaten,
            persoenlichesBudgetTableEinnahmen,
            tl
        );
        return persoenlichesBudgetTableEinnahmen;
    }

    private Table getPersoenlichesBudgetTableKosten(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final TL tl
    ) {
        Table persoenlichesBudgetTableKosten = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addPersoenlichesBudgetKostenToTable(persoenlichesBudgetresultat, persoenlichesBudgetTableKosten, tl);
        return persoenlichesBudgetTableKosten;
    }

    private void addPersoenlichesBudgetEinnahmenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        Table persoenlichesBudgetTableEinnahmen,
        TL tl
    ) {
        // Einnahmen
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.title", tl).setFont(pdfFontBold)
        );
        persoenlichesBudgetTableEinnahmen.addCell(new Paragraph(""));

        // TODO: Update Help texts
        // TODO: Update FR Translation texts
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen", tl)
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getNettoerwerbseinkommen()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.einnahmenBGSA", tl)
            );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getEinnahmenBGSA()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo(
                    "stip.berechnung.persoenlich.einnahmen.kinderAusbildungszulagen",
                    tl
                )
            );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getKinderAusbildungszulagen()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.unterhaltsbeitraege", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getUnterhaltsbeitraege()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.eoLeistungen", tl)
            );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getEoLeistungen()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.taggelderAHVIV", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getTaggelderAHVIV()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.renten", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getRenten()
            )
        );

        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.ergaenzungsleistungen", tl)
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat
                    .getEinnahmen()
                    .getErgaenzungsleistungen()

            )
        );

        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.beitraegeGemeindeInstitutionen", tl)
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getBeitraegeGemeindeInstitutionen()
            )
        );

        var anrechenbaresVermoegenCell = new Cell();
        anrechenbaresVermoegenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen", tl));
        anrechenbaresVermoegenCell.add(
            getDefaultParagraphSmall(
                tl.translate(
                    "stip.berechnung.persoenlich.einnahmen.anrechenbaresVermoegen.info",
                    "vermoegensanteilInProzent",
                    berechnungsStammdaten.getVermoegensanteilInProzent(),
                    "steuerbaresVermoegen",
                    persoenlichesBudgetresultat.getEinnahmen().getSteuerbaresVermoegen()
                )
            )
        );
        persoenlichesBudgetTableEinnahmen.addCell(anrechenbaresVermoegenCell);
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getAnrechenbaresVermoegen()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.elterlicheLeistung", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getElterlicheLeistung()
            )
        );

        var persoenlichEinnahmenTitleTotalCell = new Cell();
        persoenlichEinnahmenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableEinnahmen.addCell(persoenlichEinnahmenTitleTotalCell);

        var persoenlichEinnahmenTotalCell = new Cell();
        persoenlichEinnahmenTotalCell.add(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getTotal()
            ).setFont(pdfFontBold)
        ).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableEinnahmen.addCell(persoenlichEinnahmenTotalCell);
    }

    private void addPersoenlichesBudgetKostenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        Table persoenlichesBudgetTableKosten,
        TL tl
    ) {
        // Kosten
        persoenlichesBudgetTableKosten.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.title", tl).setFont(pdfFontBold)
        );
        persoenlichesBudgetTableKosten.addCell(new Paragraph(""));

        // TODO: Update Help texts
        var ausbildungskostenCell = new Cell();
        ausbildungskostenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.ausbildungskosten", tl));
        ausbildungskostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(ausbildungskostenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getAusbildungskosten()));

        var fahrkostenCell = new Cell();
        fahrkostenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.fahrkosten", tl));
        fahrkostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(fahrkostenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getFahrkosten()));

        var verpflegungskostenCell = new Cell();
        verpflegungskostenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.verpflegungskosten", tl));
        verpflegungskostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(verpflegungskostenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getVerpflegungskosten()));

        var grundbedarfCell = new Cell();
        grundbedarfCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.grundbedarf", tl));
        grundbedarfCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(grundbedarfCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getGrundbedarf()));

        var wohnkostenCell = new Cell();
        wohnkostenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.wohnkosten", tl));
        wohnkostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(wohnkostenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getWohnkosten()));

        var medizinischeGrundversorgungCell = new Cell();
        medizinischeGrundversorgungCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.medizinischeGrundversorgung", tl));
        medizinischeGrundversorgungCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        // TODO: add custom list with personValue
        persoenlichesBudgetTableKosten.addCell(medizinischeGrundversorgungCell);
        persoenlichesBudgetTableKosten
            .addCell(
                getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getMedizinischeGrundversorgungTotal())
            );

        var fahrkostenPartnerCellCell = new Cell();
        fahrkostenPartnerCellCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.fahrkostenPartnerCell", tl));
        fahrkostenPartnerCellCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(fahrkostenPartnerCellCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getFahrkostenPartner()));

        var verpflegungPartnerCell = new Cell();
        verpflegungPartnerCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.verpflegungPartner", tl));
        verpflegungPartnerCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(verpflegungPartnerCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getFahrkostenPartner()));

        var betreuungskostenKinderCell = new Cell();
        betreuungskostenKinderCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.betreuungskostenKinder", tl));
        betreuungskostenKinderCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(betreuungskostenKinderCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getBetreuungskostenKinder()));

        var kantonsGemeindesteuernCell = new Cell();
        kantonsGemeindesteuernCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.kantonsGemeindesteuern", tl));
        kantonsGemeindesteuernCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(kantonsGemeindesteuernCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getKantonsGemeindesteuern()));

        var bundessteuernCell = new Cell();
        bundessteuernCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.bundessteuern", tl));
        bundessteuernCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(bundessteuernCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getBundessteuern()));

        var anteilLebenshaltungskostenCell = new Cell();
        anteilLebenshaltungskostenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.anteilLebenshaltungskosten", tl));
        anteilLebenshaltungskostenCell
            .add(
                getDefaultParagraphTranslatedSmall(
                    "stip.berechnung.persoenlich.kosten.anteilLebenshaltungskosten.info",
                    tl
                )
            );
        persoenlichesBudgetTableKosten.addCell(anteilLebenshaltungskostenCell);
        persoenlichesBudgetTableKosten
            .addCell(
                getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getAnteilLebenshaltungskosten())
            );

        var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableKosten.addCell(persoenlichKostenTitleTotalCell);

        var persoenlichKostenTotalCell = new Cell();
        persoenlichKostenTotalCell.add(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getKosten().getTotal()
            ).setFont(pdfFontBold)
        ).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableKosten.addCell(persoenlichKostenTotalCell);
    }

    private Table getFamilienBudgetTableEinnahmen(
        final FamilienBudgetresultatDto familienBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        final TL tl
    ) {
        Table familienBudgetTableEinnahmen = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addFamilienBudgetEinnahmenToTable(
            familienBudgetresultat,
            berechnungsStammdaten,
            familienBudgetTableEinnahmen,
            tl
        );
        return familienBudgetTableEinnahmen;
    }

    private Table getFamilienBudgetTableKosten(
        final FamilienBudgetresultatDto familienBudgetresultat,
        final TL tl
    ) {
        Table familienBudgetTableKosten = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addFamilienBudgetKostenToTable(familienBudgetresultat, familienBudgetTableKosten, tl);
        return familienBudgetTableKosten;
    }

    private void addFamilienBudgetEinnahmenToTable(
        final FamilienBudgetresultatDto familienBudgetResultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        Table familienBudgetTableEinnahmen,
        TL tl
    ) {
        // Einnahmen
        familienBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.title", tl).setFont(pdfFontBold)
        );
        familienBudgetTableEinnahmen.addCell(new Paragraph(""));

        // TODO: Update Help texts
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.einnahmenBGSA", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getEinnahmenBGSA()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.ergaenzungsleistungen", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getErgaenzungsleistungen()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.andereEinnahmen", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getAndereEinnahmen()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.eigenmietwert", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getEigenmietwert()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.unterhaltsbeitraege", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getUnterhaltsbeitraege()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.sauele3", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getSauele3()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.sauele2", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getSauele2()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.renten", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getRenten()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.einkommensfreibetrag", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getEinkommensfreibetrag()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.zwischentotal", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getZwischentotal()));

        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.anrechenbaresVermoegen", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEinnahmen().getAnrechenbaresVermoegen()));

        var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(
                getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.totalEinkuenfte", tl)
                    .setFont(pdfFontBold)
            )
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableEinnahmen.addCell(persoenlichKostenTitleTotalCell);

        var persoenlichKostenTotalCell = new Cell();
        persoenlichKostenTotalCell.add(
            getDefaultParagraphNumber(
                familienBudgetResultat.getEinnahmen().getTotal()
            ).setFont(pdfFontBold)
        ).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableEinnahmen.addCell(persoenlichKostenTotalCell);
    }

    private void addFamilienBudgetKostenToTable(
        final FamilienBudgetresultatDto familienBudgetResultat,
        Table familienBudgetTableKosten,
        TL tl
    ) {
        familienBudgetTableKosten.addCell(
            getDefaultParagraphTranslated("stip.berechnung.familien.kosten.title", tl).setFont(pdfFontBold)
        );
        familienBudgetTableKosten.addCell(new Paragraph(""));

        // TODO: Update Help texts
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.grundbedarf", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getGrundbedarf()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.wohnkosten", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getWohnkosten()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.medizinischeGrundversorgung", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getMedizinischeGrundversorgung()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.integrationszulage", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getIntegrationszulage()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.kantonsGemeindesteuern", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getKantonsGemeindesteuern()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.bundessteuern", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getBundessteuern()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.fahrkosten", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getFahrkostenTotal()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.verpflegung", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getKosten().getVerpflegungTotal()));

        var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableKosten.addCell(persoenlichKostenTitleTotalCell);

        var persoenlichKostenTotalCell = new Cell();
        persoenlichKostenTotalCell.add(
            getDefaultParagraphNumber(
                familienBudgetResultat.getKosten().getTotal()
            ).setFont(pdfFontBold)
        ).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableKosten.addCell(persoenlichKostenTotalCell);
    }
}
