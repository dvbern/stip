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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.type.Anhangs;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.tenancy.service.TenantConfigService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.api.verfuegung.util.VerfuegungUtil;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.stipdecision.repo.StipDecisionTextRepository;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.pdf.util.PdfConstants.AUSBILDUNGSBEITRAEGE_LINK;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.FONT_SIZE_BIG;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.PAGE_SIZE;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_MEDIUM;
import static ch.dvbern.stip.api.pdf.util.PdfConstants.SPACING_SMALL;

@RequestScoped
@RequiredArgsConstructor
@UnlessBuildProfile("test")
@Slf4j
public class VerfuegungPdfService {
    private final StipDecisionTextRepository stipDecisionTextRepository;
    private final TenantConfigService tenantConfigService;
    private final BuchhaltungService buchhaltungService;
    private final VerfuegungService verfuegungService;
    private final BerechnungsblattService berechnungsblattService;

    private ByteArrayOutputStream createNegativeVerfuegungPdf(
        final Verfuegung verfuegung,
        final List<Anhangs> anhangs
    ) {
        final VerfuegungPdfSection verfuegungPdfSection =
            this::negativeVerfuegung;
        return this.createPdf(verfuegung, verfuegungPdfSection, anhangs);
    }

    public ByteArrayOutputStream createVerfuegungOhneAnspruchPdf(
        final Verfuegung verfuegung,
        final List<Anhangs> anhangs
    ) {
        final VerfuegungPdfSection verfuegungPdfSection =
            this::verfuegungOhneAnspruch;
        return this.createPdf(verfuegung, verfuegungPdfSection, anhangs);
    }

    private ByteArrayOutputStream createVerfuegungMitAnspruchPdf(
        final Verfuegung verfuegung,
        final List<Anhangs> anhangs
    ) {
        final VerfuegungPdfSection verfuegungPdfSection =
            this::verfuegungMitAnspruch;
        return this.createPdf(verfuegung, verfuegungPdfSection, anhangs);
    }

    private ByteArrayOutputStream createPdf(
        final Verfuegung verfuegung,
        final VerfuegungPdfSection section,
        final List<Anhangs> anhangs
    ) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final PdfFont pdfFont = PdfUtils.createFont();
        final PdfFont pdfFontBold = PdfUtils.createFontBold();

        final Gesuch gesuch = verfuegung.getGesuch();
        final Locale locale = LocaleUtil.getLocale(gesuch);
        final TL translator = TLProducer.defaultBundle().forAppLanguage(AppLanguages.fromLocale(locale));

        try (
            final PdfWriter writer = new PdfWriter(out);
            final PdfDocument pdfDocument = new PdfDocument(writer);
            final Document document = new Document(pdfDocument, PAGE_SIZE);
        ) {
            final float leftMargin = document.getLeftMargin();
            final Link ausbildungsbeitraegeUri =
                new Link(AUSBILDUNGSBEITRAEGE_LINK, PdfAction.createURI(AUSBILDUNGSBEITRAEGE_LINK));

            if (gesuch.getAusbildung().getFall().getDelegierung() != null) {
                addVerfuegung(
                    verfuegung,
                    document,
                    pdfDocument,
                    section,
                    leftMargin,
                    translator,
                    anhangs,
                    pdfFont,
                    pdfFontBold,
                    ausbildungsbeitraegeUri
                );

                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                PdfUtils.header(
                    gesuch,
                    document,
                    pdfDocument,
                    leftMargin,
                    translator,
                    true,
                    pdfFont,
                    ausbildungsbeitraegeUri
                );
            }

            addVerfuegung(
                verfuegung,
                document,
                pdfDocument,
                section,
                leftMargin,
                translator,
                anhangs,
                pdfFont,
                pdfFontBold,
                ausbildungsbeitraegeUri
            );
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

        return out;
    }

    private void addVerfuegung(
        final Verfuegung verfuegung,
        final Document document,
        final PdfDocument pdfDocument,
        final VerfuegungPdfSection section,
        final float leftMargin,
        final TL translator,
        final List<Anhangs> anhangs,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold,
        final Link ausbildungsbeitraegeUri
    ) throws IOException {
        final var gesuch = verfuegung.getGesuch();
        PdfUtils.header(gesuch, document, pdfDocument, leftMargin, translator, false, pdfFont, ausbildungsbeitraegeUri);

        // Add the main content and footer sections.
        section.render(verfuegung, document, leftMargin, translator, pdfFont, pdfFontBold, ausbildungsbeitraegeUri);
        anhangs.addFirst(Anhangs.RECHTSMITTELBELEHRUNG);
        PdfUtils.footer(gesuch, document, leftMargin, translator, pdfFont, anhangs, true);
        PdfUtils.rechtsmittelbelehrung(translator, document, leftMargin, pdfFont, pdfFontBold);
        PdfUtils.makePageNumberEven(document);
    }

    private void verfuegungOhneAnspruch(
        final Verfuegung verfuegung,
        final Document document,
        final float leftMargin,
        final TL translator,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold,
        final Link ausbildungsbeitraegeUri
    ) {

        final DateRange ausbildungsJahrDateRange = DateUtil.getGesuchDateRange(verfuegung.getGesuch());

        final String ausbildungsjahr = PdfUtils.formatAusbildungsjahr(
            ausbildungsJahrDateRange.getGueltigAb(),
            ausbildungsJahrDateRange.getGueltigBis()
        );
        document.add(
            PdfUtils.createParagraph(
                pdfFontBold,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegung.ausbildungsjahr"
                ),
                ausbildungsjahr,
                String.format(
                    " (%s - %s)",
                    DateUtil.formatDate(ausbildungsJahrDateRange.getGueltigAb()),
                    DateUtil.formatDate(ausbildungsJahrDateRange.getGueltigBis())
                )
            )
        );

        final PersonInAusbildung personInAusbildung = verfuegung.getGesuch()
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung();

        document.add(
            PdfUtils.getAnredeParagraph(
                personInAusbildung,
                pdfFont,
                translator,
                FONT_SIZE_BIG,
                leftMargin
            )
        );

        final String einreichedatum = DateUtil.formatDate(
            Objects.requireNonNull(verfuegung.getGesuch().getEinreichedatum())
        );
        document.add(
            PdfUtils.createParagraph(
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
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.eins"
                )
            )
        );

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.zwei"
                )
            )
        );

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.drei"
                )
            )
        );

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.vier"
                )
            )
        );

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegungOhneAnspruch.textBlock.fuenf"
                )
            ).add(ausbildungsbeitraegeUri)
        );
    }

    private void verfuegungMitAnspruch(
        final Verfuegung verfuegung,
        final Document document,
        final float leftMargin,
        final TL translator,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold,
        final Link ausbildungsbeitraegeUri
    ) {
        final var relevantBuchhaltung =
            buchhaltungService.getLatestBuchhaltungEntry(verfuegung.getGesuch().getAusbildung().getFall().getId());

        final boolean isAenderung = VerfuegungUtil.isAenderung(verfuegung);
        final boolean isRueckforderung = VerfuegungUtil.isRueckforderung(verfuegung, buchhaltungService);

        final DateRange ausbildungsJahrDateRange = DateUtil.getGesuchDateRange(verfuegung.getGesuch());

        final String ausbildungsjahr = PdfUtils.formatAusbildungsjahr(
            ausbildungsJahrDateRange.getGueltigAb(),
            ausbildungsJahrDateRange.getGueltigBis()
        );

        final String fullAusbildungsjahr = String.format(
            " (%s - %s)",
            DateUtil.formatDate(ausbildungsJahrDateRange.getGueltigAb()),
            DateUtil.formatDate(ausbildungsJahrDateRange.getGueltigBis())
        );

        String fullTitle = ausbildungsjahr + fullAusbildungsjahr;

        if (isRueckforderung) {
            fullTitle = String.format(
                "%s - %s",
                fullTitle,
                translator.translate("stip.pdf.verfuegungMitAnspruch.rueckforderung")
            );
        } else if (isAenderung) {
            fullTitle = String.format(
                "%s - %s",
                fullTitle,
                translator.translate("stip.pdf.verfuegungMitAnspruch.aenderung")
            );
        }

        document.add(
            PdfUtils.createParagraph(
                pdfFontBold,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate(
                    "stip.pdf.verfuegung.ausbildungsjahr"
                ),
                fullTitle
            )
        );

        if (isAenderung) {
            final LocalDate datumLetzteVerfuegung = verfuegung.getGesuch()
                .getVerfuegungs()
                .stream()
                .sorted(Comparator.comparing(Verfuegung::getTimestampErstellt).reversed())
                .skip(1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No previous Verfuegung available"))
                .getTimestampErstellt()
                .toLocalDate();

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate(
                        "stip.pdf.verfuegungMitAnspruch.textBlock.aenderung.eins",
                        "DATUM_LETZTE_VERFUEGUNG",
                        DateUtil.formatDate(datumLetzteVerfuegung),
                        "LINK_TO_DASHBOARD",
                        tenantConfigService.getFrontendURI()
                    )
                )
            );
        } else {
            final String einreichedatum =
                DateUtil.formatDate(Objects.requireNonNull(verfuegung.getGesuch().getEinreichedatum()));

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate(
                        "stip.pdf.verfuegungMitAnspruch.textBlock.standard.eins",
                        "DATUM",
                        einreichedatum
                    )
                )
            );
        }

        final float[] columnWidths = { 50, 50 };
        final Table calculationTable = PdfUtils.createTable(columnWidths, leftMargin);
        calculationTable.setMarginTop(SPACING_MEDIUM);
        calculationTable.setMarginBottom(SPACING_MEDIUM);
        calculationTable.setPaddingRight(SPACING_MEDIUM);

        final var actualDuration = DateUtil.wasEingereichtAfterDueDate(verfuegung.getGesuch())
            ? DateUtil.getStipendiumDurationRoundDown(verfuegung.getGesuch())
            : 12;

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate(
                    "stip.pdf.verfuegungMitAnspruch.berechnung.anspruch",
                    "X_MONATE",
                    actualDuration
                )
            ).setPadding(1)
        );

        final int anspruch = Objects.requireNonNullElse(relevantBuchhaltung.getStipendium(), 0);

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                PdfUtils.formatNumber(anspruch)
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.verfuegungMitAnspruch.berechnung.ausbezahlt")
            ).setPadding(1)
        );

        final int ausbezahlt = isRueckforderung ? 0 : anspruch - relevantBuchhaltung.getSaldo();

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                PdfUtils.formatNumber(ausbezahlt)
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                translator.translate("stip.pdf.verfuegungMitAnspruch.berechnung.rueckforderungen")
            ).setPadding(1)
        );

        final int rueckforderungen =
            isRueckforderung ? relevantBuchhaltung.getStipendium() - relevantBuchhaltung.getSaldo() : 0;

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                PdfUtils.formatNumber(rueckforderungen)
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        int total = relevantBuchhaltung.getSaldo();
        var totalLabel = translator.translate("stip.pdf.verfuegungMitAnspruch.berechnung.standard.total");

        if (isRueckforderung || total < 0) {
            totalLabel = String.format(
                "%s %s",
                totalLabel,
                translator.translate("stip.pdf.verfuegungMitAnspruch.berechnung.standard.rueckforderung")
            );
        } else {
            totalLabel = String.format(
                "%s %s",
                totalLabel,
                translator.translate("stip.pdf.verfuegungMitAnspruch.berechnung.standard.auszahlung")
            );
        }

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                totalLabel
            ).setPadding(1)
        );

        calculationTable.addCell(
            PdfUtils.createCell(
                pdfFont,
                FONT_SIZE_BIG,
                1,
                1,
                PdfUtils.formatNumber(Math.abs(total))
            ).setPadding(1).setTextAlignment(TextAlignment.RIGHT)
        );

        document.add(calculationTable);

        if (total < 0) {
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.pdf.verfuegungMitAnspruch.textBlock.rueckforderung.eins")
                )
            );
        } else {

            final var zahlungsverbindung =
                verfuegung.getGesuch().getAusbildung().getFall().getRelevantZahlungsverbindung();
            final var sprache = verfuegung.getGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung()
                .getKorrespondenzSprache();
            final var land = sprache.equals(Sprache.DEUTSCH) ? zahlungsverbindung.getAdresse().getLand().getDeKurzform()
                : zahlungsverbindung.getAdresse().getLand().getFrKurzform();
            final String auszahlung = String.format(
                "%s %s, %s, %s %s, %s %s, %s",
                zahlungsverbindung.getVorname(),
                zahlungsverbindung.getNachname(),
                zahlungsverbindung.getIban(),
                zahlungsverbindung.getAdresse().getStrasse(),
                zahlungsverbindung.getAdresse().getHausnummer(),
                zahlungsverbindung.getAdresse().getPlz(),
                zahlungsverbindung.getAdresse().getOrt(),
                land
            );

            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate(
                        "stip.pdf.verfuegungMitAnspruch.textBlock.standard.zwei",
                        "AUSZAHLUNG",
                        auszahlung
                    )
                )
            );
            document.add(
                PdfUtils.createParagraph(
                    pdfFont,
                    FONT_SIZE_BIG,
                    leftMargin,
                    translator.translate("stip.pdf.verfuegungMitAnspruch.textBlock.standard.drei")
                )
            );
        }

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.verfuegungMitAnspruch.textBlock.standard.vier")
            )
        );

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.verfuegungMitAnspruch.textBlock.standard.fuenf")
            )
        );

    }

    private void negativeVerfuegung(
        final Verfuegung verfuegung,
        final Document document,
        final float leftMargin,
        final TL translator,
        final PdfFont pdfFont,
        final PdfFont pdfFontBold,
        final Link ausbildungsbeitraegeUri
    ) {
        final var gesuch = verfuegung.getGesuch();

        final Locale locale = LocaleUtil.getLocale(gesuch);

        final DateRange ausbildungsJahrDateRange = DateUtil.getGesuchDateRange(gesuch);

        final String ausbildungsjahr = PdfUtils.formatAusbildungsjahr(
            ausbildungsJahrDateRange.getGueltigAb(),
            ausbildungsJahrDateRange.getGueltigBis()
        );
        document.add(
            PdfUtils.createParagraph(
                pdfFontBold,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.verfuegung.ausbildungsjahr"),
                ausbildungsjahr,
                String.format(
                    " (%s - %s)",
                    DateUtil.formatDate(ausbildungsJahrDateRange.getGueltigAb()),
                    DateUtil.formatDate(ausbildungsJahrDateRange.getGueltigBis())
                )
            )
        );

        final PersonInAusbildung personInAusbildung = gesuch
            .getLatestGesuchTranche()
            .getGesuchFormular()
            .getPersonInAusbildung();

        document.add(
            PdfUtils.getAnredeParagraph(
                personInAusbildung,
                pdfFont,
                translator,
                FONT_SIZE_BIG,
                leftMargin
            )
        );

        final String einreichedatum = DateUtil.formatDate(Objects.requireNonNull(gesuch.getEinreichedatum()));
        document.add(
            PdfUtils.createParagraph(
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

        document.add(PdfUtils.createParagraph(pdfFont, FONT_SIZE_BIG, leftMargin, decision));

        document.add(
            PdfUtils.createParagraph(pdfFont, FONT_SIZE_BIG, leftMargin)
                .add(translator.translate("stip.pdf.verfuegung.entschuldigung") + " ")
                .add(ausbildungsbeitraegeUri)
                .add(".")
        );

        document.add(
            PdfUtils.createParagraph(
                pdfFont,
                FONT_SIZE_BIG,
                leftMargin,
                translator.translate("stip.pdf.verfuegung.glueckWunsch")
            ).setPaddingTop(SPACING_SMALL)
        );
    }

    public void createVerfuegungsDocuments(final Gesuch gesuch, final BerechnungsresultatDto stipendien) {
        final int berechnungsresultat = stipendien.getBerechnungReduziert() != null
            ? stipendien.getBerechnungReduziert()
            : stipendien.getBerechnungTotal();

        final var verfuegung = verfuegungService.getLatestVerfuegung(gesuch.getId());

        ByteArrayOutputStream verfuegungsBrief;
        if (verfuegung.isNegativeVerfuegung()) {
            verfuegungsBrief = createNegativeVerfuegungPdf(verfuegung, new ArrayList<>());
        } else if (berechnungsresultat == 0 && gesuch.isFirstVerfuegung()) {
            verfuegungsBrief = createVerfuegungOhneAnspruchPdf(
                verfuegung,
                new ArrayList<>()
            );
        } else {
            verfuegungsBrief = createVerfuegungMitAnspruchPdf(
                verfuegung,
                new ArrayList<>(List.of(Anhangs.BERECHNUNGSBLAETTER))
            );
        }

        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERFUEGUNGSBRIEF,
            verfuegungsBrief
        );

        if (!verfuegung.isNegativeVerfuegung()) {
            ByteArrayOutputStream berechnungsBlaetter;

            createAndStoreBerechnungsblattPdf(gesuch, verfuegung, stipendien, SteuerdatenTyp.MUTTER);
            createAndStoreBerechnungsblattPdf(gesuch, verfuegung, stipendien, SteuerdatenTyp.VATER);
            createAndStoreBerechnungsblattPdf(gesuch, verfuegung, stipendien, SteuerdatenTyp.FAMILIE);

            var berechnungsBlaetterPia =
                berechnungsblattService.getBerechnungsblattPersonInAusbildung(gesuch, stipendien);

            verfuegungService.createAndStoreVerfuegungDokument(
                verfuegung,
                VerfuegungDokumentTyp.BERECHNUNGSBLATT_PIA,
                berechnungsBlaetterPia
            );

            berechnungsBlaetter = berechnungsblattService.getAllBerechnungsblaetterOfGesuch(gesuch, stipendien);

            final var versendeteVerfuegungOutput = createVersendeteVerfuegung(
                verfuegungsBrief,
                berechnungsBlaetter
            );
            verfuegungService.createAndStoreVerfuegungDokument(
                verfuegung,
                VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG,
                versendeteVerfuegungOutput
            );

            return;
        }

        verfuegungService.createAndStoreVerfuegungDokument(
            verfuegung,
            VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG,
            verfuegungsBrief
        );
    }

    private void createAndStoreBerechnungsblattPdf(
        final Gesuch gesuch,
        final Verfuegung verfuegung,
        final BerechnungsresultatDto berechnungsResultat,
        final SteuerdatenTyp steuerdatenTyp
    ) {
        ByteArrayOutputStream berechnungsBlaetter = berechnungsblattService.getAllElternTypeBerechnungsblaetterOfGesuch(
            gesuch,
            berechnungsResultat,
            steuerdatenTyp
        );
        if (berechnungsBlaetter != null) {
            verfuegungService.createAndStoreVerfuegungDokument(
                verfuegung,
                mapToVerfuegungDokumentTyp(steuerdatenTyp),
                berechnungsBlaetter
            );
        }
    }

    private VerfuegungDokumentTyp mapToVerfuegungDokumentTyp(final SteuerdatenTyp steuerdatenTyp) {
        return switch (steuerdatenTyp) {
            case MUTTER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_MUTTER;
            case VATER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_VATER;
            case FAMILIE -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_FAMILIE;
        };
    }

    private ByteArrayOutputStream createVersendeteVerfuegung(
        final ByteArrayOutputStream verfuegungsbrief,
        final ByteArrayOutputStream mergedBerechnungsblaetter
    ) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (final PdfDocument targetPdf = new PdfDocument(new PdfWriter(out))) {
            final PdfMerger merger = new PdfMerger(targetPdf);

            try (
                final PdfDocument verfuegungPdf = new PdfDocument(
                    new PdfReader(new ByteArrayInputStream(verfuegungsbrief.toByteArray()))
                )
            ) {
                merger.merge(verfuegungPdf, 1, verfuegungPdf.getNumberOfPages());
            }

            try (
                final PdfDocument blattPdf = new PdfDocument(
                    new PdfReader(new ByteArrayInputStream(mergedBerechnungsblaetter.toByteArray()))
                )
            ) {
                merger.merge(blattPdf, 1, blattPdf.getNumberOfPages());
            }
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to merge PDFs for Versendete Verfügung", e);
        }

        return out;
    }

    @FunctionalInterface
    private interface VerfuegungPdfSection {
        void render(
            Verfuegung verfuegung,
            Document document,
            float leftMargin,
            TL translator,
            PdfFont pdfFont,
            PdfFont pdfFontBold,
            Link ausbildungsbeitraegeUri
        )
        throws IOException;
    }

}
