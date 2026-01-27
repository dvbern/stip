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
            .filter(fb -> fb.getSteuerdatenTyp() == typ)
            .findFirst();

        if (matchingBudget.isEmpty()) {
            return;
        }

        final var familienBudgetResultat = matchingBudget.get();

        final var budgetTypText = switch (familienBudgetResultat.getSteuerdatenTyp()) {
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

        // final Table familienBudgetTableEinnahmen = getFamilienBudgetTableEinnahmen(
        // familienBudgetResultat,
        // tranchenBerechnungsResultat.getBerechnungsStammdaten(),
        // translator
        // );
        //
        // document.add(familienBudgetTableEinnahmen);
        // document.add(getNewLineParagraph());
        //
        // final Table familienBudgetTableKosten = getFamilienBudgetTableKosten(
        // familienBudgetResultat,
        // translator
        // );
        //
        // document.add(familienBudgetTableKosten);
        // document.add(getNewLineParagraph());
        // addFooterParagraph1(document, 15, translator);
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

        // final Table persoenlichesBudgetTableEinnahmen = getPersoenlichesBudgetTableEinnahmen(
        // tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
        // tranchenBerechnungsResultat.getBerechnungsStammdaten(),
        // translator
        // );
        //
        // document.add(persoenlichesBudgetTableEinnahmen);
        // document.add(getNewLineParagraph());
        //
        // final Table persoenlichesBudgetTableKosten =
        // getPersoenlichesBudgetTableKosten(
        // tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
        // translator
        // );
        //
        // document.add(persoenlichesBudgetTableKosten);
        // document.add(getNewLineParagraph());
        //
        // final var persoenlichTotalCell = new Cell();
        // persoenlichTotalCell.add(
        // getDefaultParagraphTranslated("stip.berechnung.persoenlich.total", translator).setFont(pdfFontBold)
        // );
        // if (
        // !(tranchenBerechnungsResultat.getBerechnungsanteilKinder() != null
        // && tranchenBerechnungsResultat.getBerechnungsanteilKinder().compareTo(BigDecimal.ONE) == 0)
        // ) {
        // persoenlichTotalCell.add(
        // getDefaultParagraphSmall(
        // translator.translate(
        // "stip.berechnung.persoenlich.geteilteBerechnung",
        // "berechnungsanteilKinder",
        // tranchenBerechnungsResultat.getBerechnungsanteilKinder()
        // )
        // ).setFontColor(ColorConstants.WHITE)
        // );
        // }
        //
        // final Table persoenlichesBudgetTableTotal = new Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
        // persoenlichesBudgetTableTotal.addCell(persoenlichTotalCell)
        // .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        //
        // persoenlichesBudgetTableTotal.addCell(
        // getDefaultParagraphNumber(
        // tranchenBerechnungsResultat.getBerechnungAnteilTotal()
        // ).setFont(pdfFontBold).setBackgroundColor(ColorConstants.LIGHT_GRAY)
        // );
        //
        // document.add(persoenlichesBudgetTableTotal);
        // document.add(getNewLineParagraph());
        //
        // addFooterParagraph1(document, 40, translator);
        // addFooterParagraph2(document, 15, translator);
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
                    final SteuerdatenTyp typ = familienBudget.getSteuerdatenTyp();
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
                        final SteuerdatenTyp typ = familienBudget.getSteuerdatenTyp();
                        if (currentSteuerdatenTyp.equals(SteuerdatenTyp.FAMILIE) || currentSteuerdatenTyp.equals(typ)) {
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

                // TODO KSTIP-2590: Why is this duplicated with addBerechnungsblattPIA?
                // final Table persoenlichesBudgetTableEinnahmen = getPersoenlichesBudgetTableEinnahmen(
                // tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                // tranchenBerechnungsResultat.getBerechnungsStammdaten(),
                // translator
                // );
                //
                // document.add(persoenlichesBudgetTableEinnahmen);
                // document.add(getNewLineParagraph());
                //
                // final Table persoenlichesBudgetTableKosten =
                // getPersoenlichesBudgetTableKosten(
                // tranchenBerechnungsResultat.getPersoenlichesBudgetresultat(),
                // translator
                // );
                //
                // document.add(persoenlichesBudgetTableKosten);
                // document.add(getNewLineParagraph());
                //
                // final var persoenlichTotalCell = new Cell();
                // persoenlichTotalCell.add(
                // getDefaultParagraphTranslated("stip.berechnung.persoenlich.total", translator).setFont(pdfFontBold)
                // );
                // if (
                // !(tranchenBerechnungsResultat.getBerechnungsanteilKinder() != null
                // && tranchenBerechnungsResultat.getBerechnungsanteilKinder().compareTo(BigDecimal.ONE) == 0)
                // ) {
                // persoenlichTotalCell.add(
                // getDefaultParagraphSmall(
                // translator.translate(
                // "stip.berechnung.persoenlich.geteilteBerechnung",
                // "berechnungsanteilKinder",
                // tranchenBerechnungsResultat.getBerechnungsanteilKinder()
                // )
                // ).setFontColor(ColorConstants.WHITE)
                // );
                // }
                //
                // final Table persoenlichesBudgetTableTotal = new
                // Table(TABLE_WIDTH_PERCENTAGES).useAllAvailableWidth();
                // persoenlichesBudgetTableTotal.addCell(persoenlichTotalCell)
                // .setBackgroundColor(ColorConstants.LIGHT_GRAY);
                //
                // persoenlichesBudgetTableTotal.addCell(
                // getDefaultParagraphNumber(
                // tranchenBerechnungsResultat.getBerechnungAnteilTotal()
                // ).setFont(pdfFontBold).setBackgroundColor(ColorConstants.LIGHT_GRAY)
                // );
                //
                // document.add(persoenlichesBudgetTableTotal);
                // document.add(getNewLineParagraph());
                //
                // addFooterParagraph1(document, 40, translator);
                // addFooterParagraph2(document, 15, translator);
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

        // TODO: Update Help texts
        // TODO: Update FR Translation texts
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.nettoerwerbseinkommen", tl)
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getNettoerwerbseinkommenTotal()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.einnahmenBGSA", tl)
            );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getEinnahmenBGSATotal()
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
                persoenlichesBudgetresultat.getEinnahmen().getKinderAusbildungszulagenTotal()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.unterhaltsbeitraege", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getUnterhaltsbeitraegeTotal()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(
                getDefaultParagraphCellTranslatedWithInfo("stip.berechnung.persoenlich.einnahmen.eoLeistungen", tl)
            );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getEoLeistungenTotal()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.taggelderAHVIV", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getTaggelderAHVIVTotal()
            )
        );

        persoenlichesBudgetTableEinnahmen
            .addCell(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.renten", tl));
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getRentenTotal()
            )
        );

        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.ergaenzungsleistungen", tl)
        );
        persoenlichesBudgetTableEinnahmen.addCell(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat
                    .getEinnahmen()
                    .getErgaenzungsleistungenTotal()

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

        final var persoenlichEinnahmenTitleTotalCell = new Cell();
        persoenlichEinnahmenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.einnahmen.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableEinnahmen.addCell(persoenlichEinnahmenTitleTotalCell);

        final var persoenlichEinnahmenTotalCell = new Cell();
        persoenlichEinnahmenTotalCell.add(
            getDefaultParagraphNumber(
                persoenlichesBudgetresultat.getEinnahmen().getTotal()
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

        // TODO: Update Help texts
        final var ausbildungskostenCell = new Cell();
        ausbildungskostenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.ausbildungskosten", tl));
        ausbildungskostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(ausbildungskostenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getAusbildungskosten()));

        final var fahrkostenCell = new Cell();
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

        final var grundbedarfCell = new Cell();
        grundbedarfCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.grundbedarf", tl));
        grundbedarfCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(grundbedarfCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getGrundbedarf()));

        final var wohnkostenCell = new Cell();
        wohnkostenCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.wohnkosten", tl));
        wohnkostenCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(wohnkostenCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getWohnkosten()));

        final var medizinischeGrundversorgungCell = new Cell();
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

        final var fahrkostenPartnerCellCell = new Cell();
        fahrkostenPartnerCellCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.fahrkostenPartnerCell", tl));
        fahrkostenPartnerCellCell
            .add(getDefaultParagraphTranslatedSmall("stip.berechnung.persoenlich.kosten.nurElternWohnend.info", tl));
        persoenlichesBudgetTableKosten.addCell(fahrkostenPartnerCellCell);
        persoenlichesBudgetTableKosten
            .addCell(getDefaultParagraphNumber(persoenlichesBudgetresultat.getKosten().getFahrkostenPartner()));

        final var verpflegungPartnerCell = new Cell();
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

        final var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.persoenlich.kosten.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        persoenlichesBudgetTableKosten.addCell(persoenlichKostenTitleTotalCell);

        final var persoenlichKostenTotalCell = new Cell();
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

        final var persoenlichKostenTitleTotalCell = new Cell();
        persoenlichKostenTitleTotalCell
            .add(getDefaultParagraphTranslated("stip.berechnung.familien.kosten.total", tl).setFont(pdfFontBold))
            .setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableKosten.addCell(persoenlichKostenTitleTotalCell);

        final var persoenlichKostenTotalCell = new Cell();
        persoenlichKostenTotalCell.add(
            getDefaultParagraphNumber(
                familienBudgetResultat.getKosten().getTotal()
            ).setFont(pdfFontBold)
        ).setBackgroundColor(ColorConstants.LIGHT_GRAY);
        familienBudgetTableKosten.addCell(persoenlichKostenTotalCell);
    }
}
