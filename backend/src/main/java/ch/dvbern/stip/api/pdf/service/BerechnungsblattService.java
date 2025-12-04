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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
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
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_MEDIUM;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.NUMBER_FORMAT;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.PAGE_SIZE;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class BerechnungsblattService {
    private static final UnitValue[] TABLE_WIDTH_PERCENTAGES = UnitValue.createPercentArray(new float[] { 85, 15 });

    PdfFont pdfFont = null;
    PdfFont pdfFontBold = null;

    private void addBerechnungsblattFamilie(
        Document document,
        final PersonInAusbildung pia,
        final SteuerdatenTyp typ,
        final TranchenBerechnungsresultatDto tranchenBerechnungsResultat,
        final TL translator
    ) {
        // Find the matching family budget for this type
        var matchingBudget = tranchenBerechnungsResultat.getFamilienBudgetresultate()
            .stream()
            .filter(fb -> fb.getFamilienBudgetTyp() == typ)
            .findFirst();

        if (matchingBudget.isEmpty()) {
            return;
        }

        final var familienBudgetResultat = matchingBudget.get();

        final var budgetTypText = switch (familienBudgetResultat.getFamilienBudgetTyp()) {
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

        final Table familienBudgetTableEinnahmen = getFamilienBudgetTableEinnahmen(
            familienBudgetResultat,
            tranchenBerechnungsResultat.getBerechnungsStammdaten(),
            translator
        );

        document.add(familienBudgetTableEinnahmen);
        document.add(getNewLineParagraph());

        final Table familienBudgetTableKosten = getFamilienBudgetTableKosten(
            familienBudgetResultat,
            translator
        );

        document.add(familienBudgetTableKosten);
        document.add(getNewLineParagraph());
        addFooterParagraph1(document, 15, translator);
    }

    private void addBerechnungsblattPIA(
        Document document,
        final PersonInAusbildung pia,
        final TranchenBerechnungsresultatDto tranchenBerechnungsResultat,
        final TL translator
    ) {
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

        final Table persoenlichesBudgetTableEinnahmen = getPersoenlichesBudgetTableEinnahmen(
            tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
            tranchenBerechnungsResultat.getBerechnungsStammdaten(),
            translator
        );

        document.add(persoenlichesBudgetTableEinnahmen);
        document.add(getNewLineParagraph());

        final Table persoenlichesBudgetTableKosten =
            getPersoenlichesBudgetTableKosten(
                tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                translator
            );

        document.add(persoenlichesBudgetTableKosten);
        document.add(getNewLineParagraph());

        final var persoenlichTotalCell = new Cell();
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

        final Table persoenlichesBudgetTableTotal = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        persoenlichesBudgetTableTotal.addCell(persoenlichTotalCell)
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);

        persoenlichesBudgetTableTotal.addCell(
            getDefaultParagraphNumber(
                tranchenBerechnungsResultat.getBerechnung()
            ).setFont(pdfFontBold).setBackgroundColor(ColorConstants.LIGHT_GRAY)
        );

        document.add(persoenlichesBudgetTableTotal);
        document.add(getNewLineParagraph());

        addFooterParagraph1(document, 40, translator);
        addFooterParagraph2(document, 15, translator);
    }

    public ByteArrayOutputStream getAllElternTypeBerechnungsblaetterOfGesuch(
        final Gesuch gesuch,
        final BerechnungsresultatDto berechnungsResultat,
        final SteuerdatenTyp steuerdatenTyp
    ) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdfFont = PdfUtils.createFont();
        pdfFontBold = PdfUtils.createFontBold();
        final Locale locale = LocaleUtil.getLocale(gesuch);

        TL translator = TLProducer.defaultBundle()
            .forAppLanguage(AppLanguages.fromLocale(locale));
        final PersonInAusbildung pia = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        boolean firstTranche = true;

        try (
            final PdfWriter writer = new PdfWriter(out);
            final PdfDocument pdfDocument = new PdfDocument(writer);
            final Document document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            for (var tranchenBerechnungsResultat : berechnungsResultat.getTranchenBerechnungsresultate()) {
                for (var familienBudget : tranchenBerechnungsResultat.getFamilienBudgetresultate()) {
                    final SteuerdatenTyp typ = familienBudget.getFamilienBudgetTyp();
                    if (steuerdatenTyp.equals(typ)) {
                        if (!firstTranche) {
                            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                        }
                        firstTranche = false;
                        addBerechnungsblattFamilie(document, pia, typ, tranchenBerechnungsResultat, translator);
                    }
                }
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
        if (firstTranche) {
            out = null;
        }
        return out;
    }

    public ByteArrayOutputStream getAllBerechnungsblaetterOfGesuch(
        final Gesuch gesuch,
        final BerechnungsresultatDto berechnungsResultat
    ) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdfFont = PdfUtils.createFont();
        pdfFontBold = PdfUtils.createFontBold();
        final Locale locale = LocaleUtil.getLocale(gesuch);

        TL translator = TLProducer.defaultBundle()
            .forAppLanguage(AppLanguages.fromLocale(locale));
        final PersonInAusbildung pia = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        boolean firstTranche = true;

        try (
            final PdfWriter writer = new PdfWriter(out);
            final PdfDocument pdfDocument = new PdfDocument(writer);
            final Document document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            // iterate through all tranchen
            for (var tranchenBerechnungsResultat : berechnungsResultat.getTranchenBerechnungsresultate()) {
                if (!firstTranche) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
                firstTranche = false;
                // add berechnungsblatt for PIA for current tranche
                addBerechnungsblattPIA(document, pia, tranchenBerechnungsResultat, translator);

                gesuch.getUnterschriftenblaetter().forEach(unterschriftenblatt -> {
                    final SteuerdatenTyp currentSteuerdatenTyp =
                        mapToSteuerdatenTyp(unterschriftenblatt.getDokumentTyp());

                    // add berechnungsblatt for Familie/Elterns for current tranche
                    for (var familienBudget : tranchenBerechnungsResultat.getFamilienBudgetresultate()) {
                        final SteuerdatenTyp typ = familienBudget.getFamilienBudgetTyp();
                        if (currentSteuerdatenTyp.equals(typ)) {
                            // if unterschriftenblatt is existing for the current type,
                            // add it to final document
                            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                            addBerechnungsblattFamilie(document, pia, typ, tranchenBerechnungsResultat, translator);
                        }
                    }
                });
                PdfUtils.makePageNumberEven(document);
            }

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        return out;
    }

    private SteuerdatenTyp mapToSteuerdatenTyp(final UnterschriftenblattDokumentTyp unterschriftenblattDokumentTyp) {
        return switch (unterschriftenblattDokumentTyp) {
            case MUTTER -> SteuerdatenTyp.MUTTER;
            case VATER -> SteuerdatenTyp.VATER;
            case GEMEINSAM -> SteuerdatenTyp.FAMILIE;
        };
    }

    public ByteArrayOutputStream getBerechnungsblattPersonInAusbildung(
        final Gesuch gesuch,
        final BerechnungsresultatDto berechnungsResultat
    ) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        pdfFont = PdfUtils.createFont();
        pdfFontBold = PdfUtils.createFontBold();
        final Locale locale = LocaleUtil.getLocale(gesuch);

        TL translator = TLProducer.defaultBundle()
            .forAppLanguage(AppLanguages.fromLocale(locale));
        final PersonInAusbildung pia = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();

        try (
            final PdfWriter writer = new PdfWriter(out);
            final PdfDocument pdfDocument = new PdfDocument(writer);
            final Document document = new Document(pdfDocument, PAGE_SIZE);
        ) {
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

                final Table persoenlichesBudgetTableEinnahmen = getPersoenlichesBudgetTableEinnahmen(
                    tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                    tranchenBerechnungsResultat.getBerechnungsStammdaten(),
                    translator
                );

                document.add(persoenlichesBudgetTableEinnahmen);
                document.add(getNewLineParagraph());

                final Table persoenlichesBudgetTableKosten =
                    getPersoenlichesBudgetTableKosten(
                        tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                        translator
                    );

                document.add(persoenlichesBudgetTableKosten);
                document.add(getNewLineParagraph());

                final var persoenlichTotalCell = new Cell();
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

                final Table persoenlichesBudgetTableTotal = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
                persoenlichesBudgetTableTotal.addCell(persoenlichTotalCell)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY);

                persoenlichesBudgetTableTotal.addCell(
                    getDefaultParagraphNumber(
                        tranchenBerechnungsResultat.getBerechnung()
                    ).setFont(pdfFontBold).setBackgroundColor(ColorConstants.LIGHT_GRAY)
                );

                document.add(persoenlichesBudgetTableTotal);
                document.add(getNewLineParagraph());

                addFooterParagraph1(document, 40, translator);
                addFooterParagraph2(document, 15, translator);
            }
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        return out;
    }

    private void addHeaderParagraph(
        final Document document,
        final PersonInAusbildung pia,
        final String budgetTypeText,
        final LocalDate trancheVon,
        final LocalDate trancheBis,
        final TL tl
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
        final Document document,
        final int verticalOffset,
        final TL tl
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
        final Document document,
        final int verticalOffset,
        final TL tl
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
        return new Paragraph(text).setFont(pdfFont).setFontSize(FONT_SIZE_MEDIUM).setFontColor(ColorConstants.GRAY);
    }

    private Paragraph getDefaultParagraph(final String text) {
        return new Paragraph(text).setFont(pdfFont).setFontSize(FONT_SIZE_BIG);
    }

    private Paragraph getNewLineParagraph() {
        return new Paragraph("");
    }

    private Table getPersoenlichesBudgetTableEinnahmen(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        final TL tl
    ) {
        final Table persoenlichesBudgetTableEinnahmen = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
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
        final Table persoenlichesBudgetTableKosten = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        addPersoenlichesBudgetKostenToTable(persoenlichesBudgetresultat, persoenlichesBudgetTableKosten, tl);
        return persoenlichesBudgetTableKosten;
    }

    private void addPersoenlichesBudgetEinnahmenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final BerechnungsStammdatenDto berechnungsStammdaten,
        final Table persoenlichesBudgetTableEinnahmen,
        TL tl
    ) {
        // Einnahmen
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.title", tl).setFont(pdfFontBold)
        );
        persoenlichesBudgetTableEinnahmen.addCell(new Paragraph(""));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen", tl)
        );

        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinkommen()
            )
        );
        persoenlichesBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.eoLeistungen", tl)
            );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getLeistungenEO()
            )
        );
        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.alimente", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAlimente()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.unterhaltsbeitraege", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getRente()
            )
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.kinderUndAusbildungszulagen", tl)
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat
                    .getKinderAusbildungszulagen()

            )
        );
        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.ergaenzungsleistungen", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getErgaenzungsleistungen()
            )
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.beitraegeGemeindeInstitution", tl)
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getGemeindeInstitutionen()
            )
        );

        final var anrechenbaresVermoegenCell = new Cell();
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
        persoenlichesBudgetTableEinnahmen.addCell(anrechenbaresVermoegenCell);
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAnrechenbaresVermoegen()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.elterlicheLeistung", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAnteilFamilienbudget()
            )
        );
        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.einkommenPartner", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinkommenPartner()
            )
        );

        final var persoenlichEinnahmenTitleTotalCell = new Cell();
        persoenlichEinnahmenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableEinnahmen.addCell(persoenlichEinnahmenTitleTotalCell);

        final var persoenlichEinnahmenTotalCell = new Cell();
        persoenlichEinnahmenTotalCell.add(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmenPersoenlichesBudget()
            ).setFont(pdfFontBold)
        ).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableEinnahmen.addCell(persoenlichEinnahmenTotalCell);
    }

    private void addPersoenlichesBudgetKostenToTable(
        final PersoenlichesBudgetresultatDto persoenlichesBudgetresultat,
        final Table persoenlichesBudgetTableKosten,
        final TL tl
    ) {
        // Kosten
        persoenlichesBudgetTableKosten.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.title", tl).setFont(pdfFontBold)
        );
        persoenlichesBudgetTableKosten.addCell(new Paragraph(""));

        final var mehrkostenVerpflegungCell = new Cell();
        mehrkostenVerpflegungCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.mehrkostenVerpflegung", tl));
        mehrkostenVerpflegungCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(mehrkostenVerpflegungCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getVerpflegung()));

        final var fahrkostenCell = new Cell();
        fahrkostenCell.add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.fahrkosten", tl));
        fahrkostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(fahrkostenCell);

        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getFahrkosten()));
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.ausbildungskosten", tl));
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getAusbildungskosten()));

        final String noPersonenTranslationReplaceKey = "anzahl";
        final var grundbedarfPersonenCell = new Cell();
        grundbedarfPersonenCell.add(
            getDefaultParagraph(
                tl.translate(
                    "stip.berechnung.persoenlich.kosten.grundbedarfPersonen",
                    noPersonenTranslationReplaceKey,
                    persoenlichesBudgetresultat.getAnzahlPersonenImHaushalt()
                )
            )
        );
        grundbedarfPersonenCell.add(
            getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurEigenerHaushalt.info", tl)
        );
        persoenlichesBudgetTableKosten.addCell(grundbedarfPersonenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getGrundbedarf()));

        final var wohnkostenPersonenCell = new Cell();
        wohnkostenPersonenCell.add(
            getDefaultParagraph(
                tl.translate(
                    "stip.berechnung.persoenlich.kosten.wohnkostenPersonen",
                    noPersonenTranslationReplaceKey,
                    persoenlichesBudgetresultat.getAnzahlPersonenImHaushalt()
                )
            )
        );
        wohnkostenPersonenCell.add(
            getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurEigenerHaushalt.info", tl)
        );
        persoenlichesBudgetTableKosten.addCell(wohnkostenPersonenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getWohnkosten()));

        final var medizinischeGrundversorgungPersonenCell = new Cell();
        medizinischeGrundversorgungPersonenCell.add(
            getDefaultParagraph(
                tl.translate(
                    "stip.berechnung.persoenlich.kosten.medizinischeGrundversorgungPersonen",
                    noPersonenTranslationReplaceKey,
                    persoenlichesBudgetresultat.getAnzahlPersonenImHaushalt()
                )
            )
        );
        medizinischeGrundversorgungPersonenCell.add(
            getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurEigenerHaushalt.info", tl)
        );
        persoenlichesBudgetTableKosten.addCell(medizinischeGrundversorgungPersonenCell);
        persoenlichesBudgetTableKosten
            .addCell(
                getDefaultParagraphNumber(persoenlichesBudgetresultat.getMedizinischeGrundversorgung())
            );

        final var fahrkostenPartnerCell = new Cell();
        fahrkostenPartnerCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.fahrkostenPartner", tl));
        fahrkostenPartnerCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurErwerbstaetig.info", tl));
        persoenlichesBudgetTableKosten.addCell(fahrkostenPartnerCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getFahrkostenPartner()));

        final var verpflegungPartnerCell = new Cell();
        verpflegungPartnerCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.verpflegungPartner", tl));
        verpflegungPartnerCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurErwerbstaetig.info", tl));
        persoenlichesBudgetTableKosten.addCell(verpflegungPartnerCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getVerpflegungPartner()));

        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.betreuungskostenKinder", tl));
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getFremdbetreuung()));
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.kantonsGemeindesteuern", tl));
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getSteuern()));
        persoenlichesBudgetTableKosten
            .addCell(
                getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.kantonsGemeindesteuernPartner", tl)
            );
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getSteuernPartner()));
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.bundessteuern", tl));
        persoenlichesBudgetTableKosten.addCell(getDefaultParagraphNumber(0)); // TODO: Bundessteuern
        persoenlichesBudgetTableKosten.addCell(
            getDefaultParagraphCellTranslatedWithInfo(
                "stip.berechnung.persoenlich.kosten.anteilLebenshaltungskosten",
                tl
            )
        );
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getAnteilLebenshaltungskosten()));

        final var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableKosten.addCell(persoenlichKostenTitleTotalCell);

        final var persoenlichKostenTotalCell = new Cell();
        persoenlichKostenTotalCell.add(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getAusgabenPersoenlichesBudget()
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
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.ergaenzungsleistungen", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getErgaenzungsleistungen()));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.mietwert", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEigenmietwert()));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.kinderalimente", tl));
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getAlimente()));

        final var beitraegeSaule3aCell = new Cell();
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
        familienBudgetTableEinnahmen.addCell(beitraegeSaule3aCell);
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getSaeule3a()));

        familienBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.familien.einnahmen.beitraegeSaule2", tl)
            );
        familienBudgetTableEinnahmen.addCell(getDefaultParagraphNumber(familienBudgetResultat.getSaeule2()));

        familienBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo(
                    "stip.berechnung.familien.einnahmen.einkommensfreibeitrag",
                    tl
                )
            );
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(berechnungsStammdaten.getEinkommensfreibetrag()));

        final var anrechenbaresVermoegenCell = new Cell();
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
        familienBudgetTableEinnahmen.addCell(anrechenbaresVermoegenCell);
        familienBudgetTableEinnahmen
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getAnrechenbaresVermoegen()));

        final var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(
                getDefaultParagraphTranslated("stip.berechnung.familien.einnahmen.totalEinkuenfte", tl)
                    .setFont(pdfFontBold)
            )
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableEinnahmen.addCell(persoenlichKostenTitleTotalCell);

        final var persoenlichKostenTotalCell = new Cell();
        persoenlichKostenTotalCell.add(
            getDefaultParagraphNumber(
                familienBudgetResultat.getTotalEinkuenfte()
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
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.grundbedarf", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getGrundbedarf()));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.wohnkosten", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEffektiveWohnkosten()));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.medizinischeGrundversorgung", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getMedizinischeGrundversorgung()));

        familienBudgetTableKosten
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.familien.kosten.integrationszulage", tl)
            );
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getIntegrationszulage()));

        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.kantonsGemeindesteuern", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getSteuernKantonGemeinde()));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.bundessteuern", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getSteuernBund()));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.fahrkosten", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getFahrkostenPerson1()));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.fahrkostenPartner", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getFahrkostenPerson2()));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.verpflegung", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEssenskostenPerson1()));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.verpflegungPartner", tl));
        familienBudgetTableKosten
            .addCell(getDefaultParagraphNumber(familienBudgetResultat.getEssenskostenPerson2()));

        final var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableKosten.addCell(persoenlichKostenTitleTotalCell);

        final var persoenlichKostenTotalCell = new Cell();
        persoenlichKostenTotalCell.add(
            getDefaultParagraphNumber(
                familienBudgetResultat.getTotalEinkuenfte()
            ).setFont(pdfFontBold)
        ).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableKosten.addCell(persoenlichKostenTotalCell);
    }
}
