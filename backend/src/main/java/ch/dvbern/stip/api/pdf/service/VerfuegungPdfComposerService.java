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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ch.dvbern.stip.api.common.util.LocaleUtil;
import ch.dvbern.stip.api.darlehen.service.DarlehenService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.pdf.type.Anhangs;
import ch.dvbern.stip.api.pdf.util.PdfUtils;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.service.VerfuegungService;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import ch.dvbern.stip.integration.pdf.domain.model.BerechnungBlattPdfData;
import ch.dvbern.stip.integration.pdf.domain.model.PdfPayload;
import ch.dvbern.stip.integration.pdf.domain.model.PdfTemplateType;
import ch.dvbern.stip.integration.pdf.domain.port.PdfPortFactory;
import ch.dvbern.stip.integration.pdf.domain.service.BerechnungCopyMapper;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class VerfuegungPdfComposerService {

    private final VerfuegungService verfuegungService;
    private final DarlehenService darlehenService;
    private final PdfPortFactory pdfPortFactory;
    private final BerechnungCopyMapper berechnungCopyMapper;
    private final VerfuegungBriefPdfService verfuegungBriefPdfService;

    public void createVerfuegungsDocuments(
        final Gesuch gesuch,
        final Optional<BerechnungsresultatDto> stipendienBerechnungOpt
    ) {
        final var verfuegung = verfuegungService.getLatestVerfuegung(gesuch.getId());
        final var verfuegungsBrief = getVerfuegungsBrief(verfuegung, stipendienBerechnungOpt, gesuch);
        storeVerfuegungsDokument(verfuegung, VerfuegungDokumentTyp.VERFUEGUNGSBRIEF, verfuegungsBrief);

        if (verfuegung.getVerfuegungStatus().isNegativ() || stipendienBerechnungOpt.isEmpty()) {
            storeVerfuegungsDokument(verfuegung, VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG, verfuegungsBrief);
            return;
        }

        final var versendeteVerfuegung = buildVersendeteVerfuegung(
            gesuch,
            verfuegung,
            verfuegungsBrief,
            stipendienBerechnungOpt.get()
        );
        storeVerfuegungsDokument(verfuegung, VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG, versendeteVerfuegung);
    }

    private ByteArrayOutputStream buildVersendeteVerfuegung(
        final Gesuch gesuch,
        final Verfuegung verfuegung,
        final ByteArrayOutputStream verfuegungsBrief,
        final BerechnungsresultatDto stipendienBerechnung
    ) {
        final var berechnungsBlaetter = renderAndStoreBerechnungsblaetter(verfuegung, stipendienBerechnung, gesuch);
        final var darlehensVerfuegung = createDarlehensVerfuegung(gesuch, stipendienBerechnung);

        final var versendetePdfs = new ArrayList<ByteArrayOutputStream>();
        versendetePdfs.add(verfuegungsBrief);
        versendetePdfs.add(berechnungsBlaetter);
        darlehensVerfuegung.ifPresent(versendetePdfs::add);

        final var versendeteVerfuegungWithPageNumbers = PdfUtils.makePageNumberEven(PdfUtils.addPageNumbers(PdfUtils.mergePdfs(versendetePdfs)));

        if (!gesuch.getAusbildung().getFall().isDelegiert()) {
            return versendeteVerfuegungWithPageNumbers;
        }

        final var sozialdienstDeckblatt = verfuegungBriefPdfService.createSozialdienstDeckblatt(gesuch);

        final var versendetePdfsWithSozialdienst = new ArrayList<ByteArrayOutputStream>();
        versendetePdfsWithSozialdienst.add(versendeteVerfuegungWithPageNumbers);
        versendetePdfsWithSozialdienst.add(sozialdienstDeckblatt);
        versendetePdfsWithSozialdienst.add(versendeteVerfuegungWithPageNumbers);

        return PdfUtils.makePageNumberEven(PdfUtils.mergePdfs(versendetePdfsWithSozialdienst));
    }

    private ByteArrayOutputStream renderAndStoreBerechnungsblaetter(
        final Verfuegung verfuegung,
        final BerechnungsresultatDto stipendienBerechnung,
        final Gesuch gesuch
    ) {
        final var lang = LocaleUtil.getKorrespondenzSprache(gesuch);

        final var allBerechnungsBlaetter = new ArrayList<ByteArrayOutputStream>();
        final var piaBlaetter = new ArrayList<ByteArrayOutputStream>();
        final var familienBlaetterByTyp =
            new EnumMap<SteuerdatenTyp, List<ByteArrayOutputStream>>(SteuerdatenTyp.class);

        final var strippedStipendienBerechnung = berechnungCopyMapper.copy(stipendienBerechnung);
        allBerechnungsBlaetter.add(
            renderBerechnungsblatt(
                strippedStipendienBerechnung,
                PdfTemplateType.BERECHNUNGSBLATT_UEBERSICHT,
                lang
            )
        );

        for (
            final TranchenBerechnungsresultatDto tranche : stipendienBerechnung.getTranchenBerechnungsresultate()
        ) {
            for (final FamilienBudgetresultatDto familien : tranche.getFamilienBudgetresultate()) {
                final var familienBlatt = renderBerechnungsblatt(
                    BerechnungBlattPdfData.of(familien, tranche),
                    PdfTemplateType.BERECHNUNGSBLATT_FAMILIE,
                    lang
                );
                familienBlaetterByTyp
                    .computeIfAbsent(familien.getSteuerdatenTyp(), k -> new ArrayList<>())
                    .add(familienBlatt);
                allBerechnungsBlaetter.add(familienBlatt);
            }

            final PersoenlichesBudgetresultatDto persoenlich = tranche.getPersoenlichesBudgetresultat();
            if (persoenlich != null) {
                final var piaBlatt = renderBerechnungsblatt(
                    BerechnungBlattPdfData.of(persoenlich, tranche),
                    PdfTemplateType.BERECHNUNGSBLATT_PIA,
                    lang
                );
                piaBlaetter.add(piaBlatt);
                allBerechnungsBlaetter.add(piaBlatt);
            }
        }

        storePiaBlatt(verfuegung, piaBlaetter);
        storeFamilienBlaetter(verfuegung, familienBlaetterByTyp);

        final var mergedBerechnungsBlaetter = PdfUtils.mergePdfs(allBerechnungsBlaetter);

        return PdfUtils.makePageNumberEven(mergedBerechnungsBlaetter);
    }

    private <T> ByteArrayOutputStream renderBerechnungsblatt(
        final T payload,
        final PdfTemplateType template,
        final Sprache lang
    ) {
        final var pdfPayload = PdfPayload.builder(payload)
            .lang(lang)
            .template(template)
            .build();

        return pdfPortFactory.getPdfAdapter().renderPdf(pdfPayload);
    }

    private void storePiaBlatt(final Verfuegung verfuegung, final List<ByteArrayOutputStream> piaBlaetter) {
        final ByteArrayOutputStream piaMerged = PdfUtils.mergePdfs(piaBlaetter);

        if (piaMerged != null) {
            storeVerfuegungsDokument(verfuegung, VerfuegungDokumentTyp.BERECHNUNGSBLATT_PIA, piaMerged);
        }
    }

    private void storeFamilienBlaetter(
        final Verfuegung verfuegung,
        final EnumMap<SteuerdatenTyp, List<ByteArrayOutputStream>> familienBlaetterByTyp
    ) {
        for (final SteuerdatenTyp typ : SteuerdatenTyp.values()) {
            final var blaetter = familienBlaetterByTyp.get(typ);
            if (blaetter == null || blaetter.isEmpty()) {
                continue;
            }

            final var merged = PdfUtils.mergePdfs(blaetter);
            if (merged != null) {
                storeVerfuegungsDokument(verfuegung, mapSteuerdatenTypToVerfuegungDokumentTyp(typ), merged);
            }
        }
    }

    private Optional<ByteArrayOutputStream> createDarlehensVerfuegung(
        final Gesuch gesuch,
        final BerechnungsresultatDto stipendienBerechnung
    ) {
        if (Objects.requireNonNullElse(stipendienBerechnung.getBerechnungDarlehen(), 0) <= 0) {
            return Optional.empty();
        }
        return darlehenService.createGesetzlichDarlehen(gesuch, stipendienBerechnung.getBerechnungDarlehen());
    }

    private void storeVerfuegungsDokument(
        final Verfuegung verfuegung,
        final VerfuegungDokumentTyp dokumentTyp,
        final ByteArrayOutputStream pdfContent
    ) {
        verfuegungService.createAndStoreVerfuegungDokument(verfuegung, dokumentTyp, pdfContent);
    }

    private ByteArrayOutputStream getVerfuegungsBrief(
        final Verfuegung verfuegung,
        final Optional<BerechnungsresultatDto> stipendienBerechnungOpt,
        final Gesuch gesuch
    ) {
        if (verfuegung.getVerfuegungStatus().isNegativ()) {
            return verfuegungBriefPdfService.createNegativeVerfuegungPdf(verfuegung, new ArrayList<>());
        }

        if (
            stipendienBerechnungOpt.map(BerechnungsresultatDto::getBerechnungStipendium).orElse(0) == 0
            && gesuch.isFirstVerfuegung()
        ) {
            return verfuegungBriefPdfService.createVerfuegungOhneAnspruchPdf(verfuegung, new ArrayList<>());
        }

        final List<Anhangs> anhangs = new ArrayList<>(List.of(Anhangs.BERECHNUNGSBLAETTER));

        if (stipendienBerechnungOpt.map(BerechnungsresultatDto::getBerechnungDarlehen).orElse(0) > 0) {
            anhangs.add(Anhangs.DARLEHENS_VERFUEGUNG);
        }

        return verfuegungBriefPdfService.createVerfuegungMitAnspruchPdf(verfuegung, anhangs);
    }

    private VerfuegungDokumentTyp mapSteuerdatenTypToVerfuegungDokumentTyp(final SteuerdatenTyp steuerdatenTyp) {
        return switch (steuerdatenTyp) {
            case MUTTER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_MUTTER;
            case VATER -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_VATER;
            case FAMILIE -> VerfuegungDokumentTyp.BERECHNUNGSBLATT_FAMILIE;
        };
    }

}
