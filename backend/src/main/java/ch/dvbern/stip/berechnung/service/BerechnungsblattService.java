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
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class BerechnungsblattService {
    private final BerechnungService berechnungService;

    private static final PdfFont FONT = PdfFontFactory.createFont(StandardFonts.HELVETICA);
    private static final PdfFont BOLD_FONT = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
    private static final PageSize PAGE_SIZE = PageSize.A4;

    private static final UnitValue[] TABLE_WIDTH_PERCENTAGES = UnitValue.createPercentArray(new float[] { 20, 80 });

    public void getBerechnungsblattFromGesuch(final Gesuch gesuch, final Locale locale) {
        TL tranlator = TLProducer.defaultBundle()
            .forAppLanguage(
                AppLanguages.fromLocale(locale)
            );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument, PAGE_SIZE);

        var berechnungsResultat = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        for (var tranchenBerechnungsResultat : berechnungsResultat.getTranchenBerechnungsresultate()) {
            document.add(
                getHeaderParagraph(
                    tranchenBerechnungsResultat.getGueltigAb(),
                    tranchenBerechnungsResultat.getGueltigBis(),
                    tranlator
                )
            );

            document.add(
                new Paragraph(
                    String.format(
                        "%s %s",
                        tranlator.translate("stip.berechnung.persoenlich.title"),
                        tranchenBerechnungsResultat.getNameGesuchsteller()
                    )
                )
            );

            Table persoenlichesBudgetTable =
                getPersoenlichesBudgetTable(tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(), tranlator);

            document.add(persoenlichesBudgetTable);

            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            for (var fammilienBudgetResultat : tranchenBerechnungsResultat.getFamilienBudgetresultate()) {

                var budgetTypText = switch (fammilienBudgetResultat.getFamilienBudgetTyp()) {
                    case FAMILIE -> tranlator.translate("stip.berechnung.familien.typ.FAMILIE");
                    case VATER -> tranlator.translate("stip.berechnung.familien.typ.MUTTER");
                    case MUTTER -> tranlator.translate("stip.berechnung.familien.typ.VATER");
                };

                document.add(
                    new Paragraph(
                        String.format(
                            "%s %s",
                            tranlator.translate("stip.berechnung.familien.title"),
                            budgetTypText
                        )
                    )
                );
                Table familienBudgetTable = getFamilienBudgetTable(
                    fammilienBudgetResultat,
                    tranchenBerechnungsResultat.getBerechnungsStammdaten(),
                    tranlator
                );

                document.add(familienBudgetTable);

                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }

    }

    private static Paragraph getHeaderParagraph(LocalDate trancheVon, LocalDate trancheBis, TL tl) {
        return new Paragraph(
            String.format(
                "%s %s: %s, %s: %s %s: %d.",
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
        ).setFont(FONT)
            .setFontSize(5)
            .setFixedPosition(PAGE_SIZE.getWidth() / 2, PAGE_SIZE.getHeight() - 10, PAGE_SIZE.getWidth() / 2)
            .setHorizontalAlignment(
                HorizontalAlignment.CENTER
            );
    }

    private static Paragraph getDefaultParagraphTranslated(final String key, final TL tl) {
        return getDefaultParagraph(tl.translate(key));
    }

    private static Paragraph getDefaultParagraph(final String text) {
        return new Paragraph(text).setFont(FONT);
    }

    private static Table getPersoenlichesBudgetTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final TL tl
    ) {
        Table persoenlichesBudgetTable = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addPersoenlichesBudgetEinnahmenToTable(persoenlichesBudgetresultat, persoenlichesBudgetTable, tl);
        addPersoenlichesBudgetKostenToTable(persoenlichesBudgetresultat, persoenlichesBudgetTable, tl);
        return persoenlichesBudgetTable;
    }

    private static void addPersoenlichesBudgetEinnahmenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        Table persoenlichesBudgetTable,
        TL tl
    ) {
        // Einnahmen
        persoenlichesBudgetTable.addCell(
            new Paragraph("Einnahmen").setFont(BOLD_FONT)
        );
        persoenlichesBudgetTable.startNewRow();
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.nettoerwerbseinkommen", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getEinkommen().toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.eoLeistungen", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getLeistungenEO().toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.alimente", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getAlimente().toString()
            )
        );

        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.unterhaltsbeitraege", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getRente().toString()
            )
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.kinderUndAusbildungszulagen", tl)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat
                    .getKinderAusbildungszulagen()
                    .toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.ergaenzungsleistungen", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getErgaenzungsleistungen().toString()
            )
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.beitraegeGemeindeInstitution", tl)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getGemeindeInstitutionen().toString()
            )
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.anrechenbaresVermoegen", tl)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getAnrechenbaresVermoegen().toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.elterlicheLeistung", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getAnteilFamilienbudget().toString()
            )
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einkommenPartner", tl));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getEinkommenPartner().toString()
            )
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.total", tl).setFont(BOLD_FONT)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getEinnahmenPersoenlichesBudget().toString()
            )
        );
    }

    private static void addPersoenlichesBudgetKostenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        Table persoenlichesBudgetTable,
        TL tl
    ) {
        // Kosten
        persoenlichesBudgetTable.addCell(
            new Paragraph("Kosten").setFont(BOLD_FONT)
        );
        persoenlichesBudgetTable.startNewRow();

        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.mehrkostenVerpflegung", tl));
        persoenlichesBudgetTable.addCell(getDefaultParagraph(persoenlichesBudgetresultat.getVerpflegung().toString()));
        persoenlichesBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.fahrkosten", tl));
        persoenlichesBudgetTable.addCell(getDefaultParagraph(persoenlichesBudgetresultat.getFahrkosten().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.ausbildungskosten", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraph(persoenlichesBudgetresultat.getAusbildungskosten().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.grundbedarfPersonen", tl));
        persoenlichesBudgetTable.addCell(getDefaultParagraph(persoenlichesBudgetresultat.getGrundbedarf().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.wohnkostenPersonen", tl));
        persoenlichesBudgetTable.addCell(getDefaultParagraph(persoenlichesBudgetresultat.getWohnkosten().toString()));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.medizinischeGrundversorgungPersonen", tl)
        );
        persoenlichesBudgetTable
            .addCell(getDefaultParagraph(persoenlichesBudgetresultat.getMedizinischeGrundversorgung().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.fahrkostenPartner", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraph(persoenlichesBudgetresultat.getFahrkostenPartner().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.verpflegungPartner", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraph(persoenlichesBudgetresultat.getVerpflegungPartner().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.betreuungskostenKinder", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraph(persoenlichesBudgetresultat.getFremdbetreuung().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kantonsGemeindesteuern", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraph(persoenlichesBudgetresultat.getSteuernKantonGemeinde().toString()));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.bundessteuern", tl));
        persoenlichesBudgetTable.addCell(getDefaultParagraph("0")); // TODO: Bundessteuern
        persoenlichesBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.anteilLebenshaltungskosten", tl));
        persoenlichesBudgetTable
            .addCell(getDefaultParagraph(persoenlichesBudgetresultat.getAnteilLebenshaltungskosten().toString()));
        persoenlichesBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.total", tl).setFont(BOLD_FONT)
        );
        persoenlichesBudgetTable.addCell(
            getDefaultParagraph(
                persoenlichesBudgetresultat.getAusgabenPersoenlichesBudget().toString()
            )
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
            new Paragraph("Einnahmen").setFont(BOLD_FONT)
        );
        familienBudgetTable.startNewRow();

        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.totalEinkuenfte", tl));
        familienBudgetTable.addCell(getDefaultParagraph(familienBudgetResultat.getTotalEinkuenfte().toString()));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.ergaenzungsleistungen", tl));
        familienBudgetTable.addCell(getDefaultParagraph(familienBudgetResultat.getErgaenzungsleistungen().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.mietwert", tl));
        familienBudgetTable.addCell(getDefaultParagraph(familienBudgetResultat.getEigenmietwert().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kinderalimente", tl));
        familienBudgetTable.addCell(getDefaultParagraph(familienBudgetResultat.getAlimente().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.beitraegeSaule3a", tl));
        familienBudgetTable.addCell(getDefaultParagraph(familienBudgetResultat.getSaeule3a().toString()));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.beitraegeSaule2", tl));
        familienBudgetTable.addCell(getDefaultParagraph(familienBudgetResultat.getSaeule2().toString()));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einkommensfreibeitrag", tl));
        familienBudgetTable.addCell(getDefaultParagraph(berechnungsStammdaten.getEinkommensfreibetrag().toString()));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.anrechenbaresVermoegen", tl));
        familienBudgetTable.addCell(getDefaultParagraph(familienBudgetResultat.getAnrechenbaresVermoegen().toString()));
        familienBudgetTable.addCell(
            getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.totalEinkuenfte", tl).setFont(BOLD_FONT)
        );
        familienBudgetTable.addCell(
            getDefaultParagraph(
                familienBudgetResultat.getTotalEinkuenfte().toString()
            )
        );
    }

    private static void addFamilienBudgetKostenToTable(
        final FamilienBudgetresultatDto familienBudgetResultat,
        Table familienBudgetTable,
        TL tl
    ) {
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.grundbedarf", tl));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.wohnkosten", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.medizinischeGrundversorgung", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.integrationszulage", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.kantonsGemeindesteuern", tl));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.bundessteuern", tl));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.fahrkosten", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.fahrkostenPartner", tl));
        familienBudgetTable.addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.verpflegung", tl));
        familienBudgetTable
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.verpflegungPartner", tl));
    }
}
