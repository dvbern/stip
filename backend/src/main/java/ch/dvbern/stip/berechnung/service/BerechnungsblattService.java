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
import java.time.LocalDate;
import java.util.Locale;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
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

    private static final PdfFont FONT;
    private static final PdfFont FONT_BOLD;

    private static final int FONT_SIZE = 11;
    private static final int FONT_SIZE_SMALL = 9;

    static {
        try {
            FONT = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            FONT_BOLD = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final PageSize PAGE_SIZE = PageSize.A4;

    private static final UnitValue[] TABLE_WIDTH_PERCENTAGES = UnitValue.createPercentArray(new float[] { 85, 15 });

    public ByteArrayOutputStream getBerechnungsblattFromGesuch(final Gesuch gesuch, final Locale locale) {
        TL translator = TLProducer.defaultBundle()
            .forAppLanguage(
                AppLanguages.fromLocale(locale)
            );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PAGE_SIZE);

        var berechnungsResultat = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);
        boolean firstTranche = true;
        for (var tranchenBerechnungsResultat : berechnungsResultat.getTranchenBerechnungsresultate()) {
            if (!firstTranche) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
            firstTranche = false;

            document.add(
                getHeaderParagraph(
                    tranchenBerechnungsResultat.getGueltigAb(),
                    tranchenBerechnungsResultat.getGueltigBis(),
                    translator
                )
            );

            document.add(
                new Paragraph(
                    String.format(
                        "%s %s",
                        translator.translate("stip.berechnung.persoenlich.title"),
                        tranchenBerechnungsResultat.getNameGesuchsteller()
                    )
                ).setFont(FONT_BOLD)
            );

            Table persoenlichesBudgetTable =
                getPersoenlichesBudgetTable(
                    tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                    tranchenBerechnungsResultat.getBerechnungsStammdaten(),
                    translator
                );

            persoenlichesBudgetTable
                .addCell(
                    getDefaultParagraphTranslated("stip.berechnung.persoenlich.total", translator).setFont(FONT_BOLD)
                );
            persoenlichesBudgetTable.addCell(
                getDefaultParagraphNumber(
                    tranchenBerechnungsResultat.getBerechnung().toString()
                ).setFont(FONT_BOLD)
            );

            document.add(persoenlichesBudgetTable);

            for (var fammilienBudgetResultat : tranchenBerechnungsResultat.getFamilienBudgetresultate()) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

                document.add(
                    getHeaderParagraph(
                        tranchenBerechnungsResultat.getGueltigAb(),
                        tranchenBerechnungsResultat.getGueltigBis(),
                        translator
                    )
                );

                var budgetTypText = switch (fammilienBudgetResultat.getFamilienBudgetTyp()) {
                    case FAMILIE -> translator.translate("stip.berechnung.familien.typ.FAMILIE");
                    case VATER -> translator.translate("stip.berechnung.familien.typ.MUTTER");
                    case MUTTER -> translator.translate("stip.berechnung.familien.typ.VATER");
                };

                document.add(
                    new Paragraph(
                        String.format(
                            "%s %s",
                            translator.translate("stip.berechnung.familien.title"),
                            budgetTypText
                        )
                    ).setFont(FONT_BOLD)
                );
                Table familienBudgetTable = getFamilienBudgetTable(
                    fammilienBudgetResultat,
                    tranchenBerechnungsResultat.getBerechnungsStammdaten(),
                    translator
                );

                document.add(familienBudgetTable);
            }
        }

        document.close();
        pdfDocument.close();
        return out;
    }

    private static Paragraph getHeaderParagraph(LocalDate trancheVon, LocalDate trancheBis, TL tl) {
        Paragraph paragraph = new Paragraph(
            String.format(
                "%s. %s: %s, %s: %s %s: %d",
                tl.translate("stip.berechnung.berechnungsdetails"),
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
        );
        paragraph.setFont(FONT)
            .setFontSize(10)
            .setHorizontalAlignment(HorizontalAlignment.LEFT)
            .setFixedPosition(
                0,
                PAGE_SIZE.getHeight() - 25,
                PAGE_SIZE.getWidth()
            )
            .setTextAlignment(TextAlignment.CENTER);

        return paragraph;
    }

    private static Cell getDefaultParagraphCellTranslatedWithInfo(final String key, final TL tl) {
        var cell = new Cell();
        cell.add(getDefaultParagraphTranslated(key, tl));
        cell.add(getDefaultParagraphTranslatedSmall(key + ".info", tl));
        return cell;
    }

    private static Paragraph getDefaultParagraphTranslated(final String key, final TL tl) {
        return getDefaultParagraph(tl.translate(key));
    }

    private static Paragraph getDefaultParagraphTranslatedSmall(final String key, final TL tl) {
        return getDefaultParagraphSmall(tl.translate(key));
    }

    private static Paragraph getDefaultParagraphNumber(final String text) {
        return getDefaultParagraph(text).setTextAlignment(TextAlignment.RIGHT);

    }

    private static Paragraph getDefaultParagraphSmall(final String text) {
        return new Paragraph(text).setFont(FONT).setFontSize(FONT_SIZE_SMALL).setFontColor(ColorConstants.GRAY);
    }

    private static Paragraph getDefaultParagraph(final String text) {
        return new Paragraph(text).setFont(FONT).setFontSize(FONT_SIZE);
    }

    private static Table getPersoenlichesBudgetTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        final TL tl
    ) {
        Table persoenlichesBudgetTable = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addPersoenlichesBudgetEinnahmenToTable(
            persoenlichesBudgetresultat,
            berechnungsStammdaten,
            persoenlichesBudgetTable,
            tl
        );
        addPersoenlichesBudgetKostenToTable(persoenlichesBudgetresultat, persoenlichesBudgetTable, tl);
        return persoenlichesBudgetTable;
    }

    private static void addPersoenlichesBudgetEinnahmenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        Table persoenlichesBudgetTable,
        TL tl
    ) {
        // Einnahmen
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.title", tl).setFont(FONT_BOLD)
        );
        persoenlichesBudgetTable.addCell(new Paragraph(""));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen", tl)
        );

        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinkommen().toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.eoLeistungen", tl)
            );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getLeistungenEO().toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.alimente", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAlimente().toString()
            )
        );

        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.unterhaltsbeitraege", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getRente().toString()
            )
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.kinderUndAusbildungszulagen", tl)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat
                    .getKinderAusbildungszulagen()
                    .toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.ergaenzungsleistungen", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getErgaenzungsleistungen().toString()
            )
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.beitraegeGemeindeInstitution", tl)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getGemeindeInstitutionen().toString()
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
                    persoenlichesBudgetresultat.getSteuerbaresVermoegen()
                )
            )
        );
        persoenlichesBudgetTable.addCell(anrechenbaresVermoegenCell);
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAnrechenbaresVermoegen().toString()
            )
        );

        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.elterlicheLeistung", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAnteilFamilienbudget().toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.einkommenPartner", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinkommenPartner().toString()
            )
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.total", tl).setFont(FONT_BOLD)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmenPersoenlichesBudget().toString()
            ).setFont(FONT_BOLD)
        );
    }

    private static void addPersoenlichesBudgetKostenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        Table persoenlichesBudgetTable,
        TL tl
    ) {
        // Kosten
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.title", tl).setFont(FONT_BOLD)
        );
        persoenlichesBudgetTable.addCell(new Paragraph(""));

        var mehrkostenVerpflegungCell = new Cell();
        mehrkostenVerpflegungCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.mehrkostenVerpflegung", tl));
        mehrkostenVerpflegungCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTable.addCell(mehrkostenVerpflegungCell);
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getVerpflegung().toString()));

        var fahrkostenCell = new Cell();
        fahrkostenCell.add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.fahrkosten", tl));
        fahrkostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTable.addCell(fahrkostenCell);

        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getFahrkosten().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.ausbildungskosten", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getAusbildungskosten().toString()));

        var grundbedarfPersonenCell = new Cell();
        grundbedarfPersonenCell.add(
            getDefaultParagraph(
                tl.translate(
                    "stip.berechnung.persoenlich.kosten.grundbedarfPersonen",
                    "anzahl",
                    persoenlichesBudgetresultat.getAnzahlPersonenImHaushalt()
                )
            )
        );
        grundbedarfPersonenCell.add(
            getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurEigenerHaushalt.info", tl)
        );
        persoenlichesBudgetTable.addCell(grundbedarfPersonenCell);
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getGrundbedarf().toString()));

        var wohnkostenPersonenCell = new Cell();
        wohnkostenPersonenCell.add(
            getDefaultParagraph(
                tl.translate(
                    "stip.berechnung.persoenlich.kosten.wohnkostenPersonen",
                    "anzahl",
                    persoenlichesBudgetresultat.getAnzahlPersonenImHaushalt()
                )
            )
        );
        wohnkostenPersonenCell.add(
            getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurEigenerHaushalt.info", tl)
        );
        persoenlichesBudgetTable.addCell(wohnkostenPersonenCell);
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getWohnkosten().toString()));

        var medizinischeGrundversorgungPersonenCell = new Cell();
        medizinischeGrundversorgungPersonenCell.add(
            getDefaultParagraph(
                tl.translate(
                    "stip.berechnung.persoenlich.kosten.medizinischeGrundversorgungPersonen",
                    "anzahl",
                    persoenlichesBudgetresultat.getAnzahlPersonenImHaushalt()
                )
            )
        );
        medizinischeGrundversorgungPersonenCell.add(
            getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurEigenerHaushalt.info", tl)
        );
        persoenlichesBudgetTable.addCell(medizinischeGrundversorgungPersonenCell);
        persoenlichesBudgetTable
            .addCell(
                getDefaultParagraphNumber(persoenlichesBudgetresultat.getMedizinischeGrundversorgung().toString())
            );

        var fahrkostenPartnerCell = new Cell();
        fahrkostenPartnerCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.fahrkostenPartner", tl));
        fahrkostenPartnerCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurErwerbstaetig.info", tl));
        persoenlichesBudgetTable.addCell(fahrkostenPartnerCell);
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getFahrkostenPartner().toString()));

        var verpflegungPartnerCell = new Cell();
        verpflegungPartnerCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.verpflegungPartner", tl));
        verpflegungPartnerCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurErwerbstaetig.info", tl));
        persoenlichesBudgetTable.addCell(verpflegungPartnerCell);
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getVerpflegungPartner().toString()));

        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.betreuungskostenKinder", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getFremdbetreuung().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.kantonsGemeindesteuern", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getSteuernKantonGemeinde().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.bundessteuern", tl));
        persoenlichesBudgetTable.addCell(getDefaultParagraphNumber("0")); // TODO: Bundessteuern

        persoenlichesBudgetTable.addCell(
            getDefaultParagraphCellTranslatedWithInfo(
                "stip.berechnung.persoenlich.kosten.anteilLebenshaltungskosten",
                tl
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getAnteilLebenshaltungskosten().toString()));

        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.total", tl).setFont(FONT_BOLD)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAusgabenPersoenlichesBudget().toString()
            ).setFont(FONT_BOLD)
        );
    }

    private static Table getFamilienBudgetTable(
        final FamilienBudgetresultatDto familienBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        final TL tl
    ) {
        Table familienBudgetTable = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addFamilienBudgetEinnahmenToTable(familienBudgetresultat, berechnungsStammdaten, familienBudgetTable, tl);
        addFamilienBudgetKostenToTable(familienBudgetresultat, familienBudgetTable, tl);
        return familienBudgetTable;
    }

    private static void addFamilienBudgetEinnahmenToTable(
        final FamilienBudgetresultatDto familienBudgetResultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        Table familienBudgetTable,
        TL tl
    ) {
        // Einnahmen
        familienBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.title", tl).setFont(FONT_BOLD)
        );
        familienBudgetTable.addCell(new Paragraph(""));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.ergaenzungsleistungen", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getErgaenzungsleistungen().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.mietwert", tl));
        familienBudgetTable.addCell(getDefaultParagraphNumber(familienBudgetResultat.getEigenmietwert().toString()));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.kinderalimente", tl));
        familienBudgetTable.addCell(getDefaultParagraphNumber(familienBudgetResultat.getAlimente().toString()));

        var beitraegeSaule3aCell = new Cell();
        beitraegeSaule3aCell
            .add(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.beitraegeSaule3a", tl));
        beitraegeSaule3aCell.add(
            getDefaultParagraphSmall(
                tl.translate(
                    "stip.berechnung.familien.einnahmen.beitraegeSaule3a.info",
                    "maxSaeule3a",
                    berechnungsStammdaten.getMaxSaeule3a()
                )
            )
        );
        familienBudgetTable.addCell(beitraegeSaule3aCell);
        familienBudgetTable.addCell(getDefaultParagraphNumber(familienBudgetResultat.getSaeule3a().toString()));

        familienBudgetTable
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.familien.einnahmen.beitraegeSaule2", tl)
            );
        familienBudgetTable.addCell(getDefaultParagraphNumber(familienBudgetResultat.getSaeule2().toString()));

        familienBudgetTable
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo(
                    "stip.berechnung.familien.einnahmen.einkommensfreibeitrag",
                    tl
                )
            );
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(berechnungsStammdaten.getEinkommensfreibetrag().toString()));

        var anrechenbaresVermoegenCell = new Cell();
        anrechenbaresVermoegenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.anrechenbaresVermoegen", tl));
        anrechenbaresVermoegenCell.add(
            getDefaultParagraphSmall(
                tl.translate(
                    "stip.berechnung.familien.einnahmen.anrechenbaresVermoegen.info",
                    "vermoegensanteilInProzent",
                    berechnungsStammdaten.getVermoegensanteilInProzent(),
                    "steuerbaresVermoegen",
                    familienBudgetResultat.getSteuerbaresVermoegen(),
                    "freibetragVermoegen",
                    berechnungsStammdaten.getFreibetragVermoegen()
                )
            )
        );
        familienBudgetTable.addCell(anrechenbaresVermoegenCell);
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getAnrechenbaresVermoegen().toString()));

        familienBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.totalEinkuenfte", tl).setFont(FONT_BOLD)
        );
        familienBudgetTable.addCell(
            getDefaultParagraphNumber(
                familienBudgetResultat.getTotalEinkuenfte().toString()
            ).setFont(FONT_BOLD)
        );
    }

    private static void addFamilienBudgetKostenToTable(
        final FamilienBudgetresultatDto familienBudgetResultat,
        Table familienBudgetTable,
        TL tl
    ) {
        familienBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.familien.kosten.title", tl).setFont(FONT_BOLD)
        );
        familienBudgetTable.addCell(new Paragraph(""));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.grundbedarf", tl));
        familienBudgetTable.addCell(getDefaultParagraphNumber(familienBudgetResultat.getGrundbedarf().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.wohnkosten", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEffektiveWohnkosten().toString()));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.medizinischeGrundversorgung", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getMedizinischeGrundversorgung().toString()));

        familienBudgetTable
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.familien.kosten.integrationszulage", tl)
            );
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getIntegrationszulage().toString()));

        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.kantonsGemeindesteuern", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getSteuernKantonGemeinde().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.bundessteuern", tl));
        familienBudgetTable.addCell(getDefaultParagraphNumber(familienBudgetResultat.getSteuernBund().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.fahrkosten", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getFahrkostenPerson1().toString()));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.fahrkostenPartner", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getFahrkostenPerson2().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.verpflegung", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEssenskostenPerson1().toString()));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.verpflegungPartner", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEssenskostenPerson2().toString()));
        familienBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.familien.kosten.total", tl).setFont(FONT_BOLD)
        );
        familienBudgetTable.addCell(
            getDefaultParagraphNumber(
                familienBudgetResultat.getAusgabenFamilienbudget().toString()
            ).setFont(FONT_BOLD)
        );
    }
}
