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

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus;
import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.dto.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.dto.CalculatorRequest;
import ch.dvbern.stip.berechnung.dto.CalculatorVersion;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BerechnungService {
    private final Instance<BerechnungRequestBuilder> berechnungRequests;
    private final Instance<BerechnungsStammdatenMapper> berechnungsStammdatenMappers;
    private final Instance<StipendienCalculator> stipendienCalculators;
    private final TenantService tenantService;

    public static String serializeBerechnungresultatDto(final BerechnungsresultatDto berechnungsresultatDto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, berechnungsresultatDto);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return writer.toString();
    }

    private BerechnungsStammdatenDto berechnungsStammdatenFromRequest(
        final CalculatorRequest berechnungRequest,
        final int majorVersion,
        final int minorVersion
    ) {
        final var mapper = berechnungsStammdatenMappers.stream().filter(berechnungsStammdatenMapper -> {
            final var versionAnnotation = berechnungsStammdatenMapper.getClass().getAnnotation(CalculatorVersion.class);
            return (versionAnnotation != null) &&
            (versionAnnotation.major() == majorVersion) &&
            (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (mapper.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a BerechnungsStammdatenMapper for version " + majorVersion + '.' + minorVersion
            );
        }

        return mapper.get().mapFromRequest(berechnungRequest);
    }

    public BerechnungsresultatDto getBerechnungsresultatFromGesuch(
        final Gesuch gesuch,
        final int majorVersion,
        final int minorVersion
    ) {
        final var gesuchTrancheStatusToFilterFor = List.of(
            GesuchTrancheStatus.AKZEPTIERT,
            GesuchTrancheStatus.IN_BEARBEITUNG_GS,
            GesuchTrancheStatus.UEBERPRUEFEN
        );
        final var gesuchTranchen = gesuch.getGesuchTranchen()
            .stream()
            .filter(
                gesuchTranche -> gesuchTranche.getTyp() == GesuchTrancheTyp.TRANCHE
            )
            .filter(
                gesuchTranche -> gesuchTrancheStatusToFilterFor.contains(gesuchTranche.getStatus())
            )
            .sorted(
                Comparator.comparing(gesuchTranche -> gesuchTranche.getGueltigkeit().getGueltigAb())
            )
            .toList();

        if (Objects.isNull(gesuch.getEinreichedatum())) {
            throw new IllegalStateException("Berechnen of a Gesuch which has no Einreichedatum is not allowed");
        }

        final List<TranchenBerechnungsresultatDto> berechnungsresultate = gesuchTranchen.stream()
            .flatMap(
                gesuchTranche -> getBerechnungsresultatFromGesuchTranche(gesuchTranche, majorVersion, minorVersion)
            )
            .toList();

        int berechnungVorKuerzungUndTeilung =
            -berechnungsresultate.stream().mapToInt(TranchenBerechnungsresultatDto::getTotal).sum();
        if (berechnungVorKuerzungUndTeilung < gesuch.getGesuchsperiode().getStipLimiteMinimalstipendium()) {
            berechnungVorKuerzungUndTeilung = 0;
        }
        final var monateMitDarlehen = getMonateMitDarlehen(gesuch);
        final Integer ungekuerztDarlehen = getDarlehen(berechnungVorKuerzungUndTeilung, monateMitDarlehen);
        final int ungekuerztStipendien =
            BerechnungUtil.subtractGesezlichesDarlehen(berechnungVorKuerzungUndTeilung, monateMitDarlehen);

        final var monateUebrigNachEinreichefrist = DateUtil.wasEingereichtAfterDueDate(gesuch)
            ? DateUtil.getStipendiumDurationRoundDown(gesuch)
            : 12;

        final var totalNachKuerzungNachEinreichefrist =
            monateUebrigNachEinreichefrist < 12
                ? BigDecimal.valueOf(berechnungVorKuerzungUndTeilung)
                    .multiply(BigDecimal.valueOf(monateUebrigNachEinreichefrist))
                    .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
                    .intValue()
                : null;

        final var totalVorKuerzungUnterbruch =
            Objects.requireNonNullElse(totalNachKuerzungNachEinreichefrist, berechnungVorKuerzungUndTeilung);

        final var anzahlMonateUnterbruch = gesuch.getAusbildung()
            .getAusbildungUnterbruchAntrags()
            .stream()
            .sorted(Comparator.comparing(AusbildungUnterbruchAntrag::getTimestampErstellt))
            .filter(ausbildungUnterbruchAntrag -> ausbildungUnterbruchAntrag.getGesuch().getId().equals(gesuch.getId()))
            .filter(
                ausbildungUnterbruchAntrag -> ausbildungUnterbruchAntrag
                    .getStatus() == AusbildungUnterbruchAntragStatus.AKZEPTIERT
            )
            .map(
                ausbildungUnterbruchAntrag -> Objects
                    .requireNonNullElse(ausbildungUnterbruchAntrag.getMonateOhneAnspruch(), 0)
            )
            .findFirst()
            .orElse(0);

        final var totalNachKuerzungUnterbruch =
            anzahlMonateUnterbruch > 0
                ? BigDecimal.valueOf(totalVorKuerzungUnterbruch)
                    .multiply(BigDecimal.valueOf(12 - anzahlMonateUnterbruch))
                    .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
                    .intValue()
                : null;

        final int totalVorTeilungDarlehen =
            Objects.requireNonNullElse(totalNachKuerzungUnterbruch, totalVorKuerzungUnterbruch);

        final var berechnungDarlehen = getDarlehen(totalVorTeilungDarlehen, monateMitDarlehen);
        final var berechnungStipendium =
            BerechnungUtil.subtractGesezlichesDarlehen(totalVorTeilungDarlehen, monateMitDarlehen);

        return new BerechnungsresultatDto(
            gesuch.getGesuchsperiode().getGesuchsjahr().getTechnischesJahr(),
            berechnungVorKuerzungUndTeilung,
            totalVorTeilungDarlehen,
            berechnungStipendium,
            berechnungsresultate,
            monateMitDarlehen,
            ungekuerztStipendien,
            ungekuerztDarlehen,
            totalNachKuerzungNachEinreichefrist,
            12 - monateUebrigNachEinreichefrist,
            totalNachKuerzungUnterbruch,
            anzahlMonateUnterbruch,
            berechnungDarlehen
        );
    }

    private static Integer getDarlehen(final int stipendium, final int monateMitDarlehen) {
        if (monateMitDarlehen == 0) {
            return null;
        }

        final var darlehenFuer12MonateMitDarlehen = BerechnungUtil.calculateGesetzlichesDarlehen(stipendium);

        if (monateMitDarlehen == 12) {
            return darlehenFuer12MonateMitDarlehen;
        }
        return BerechnungUtil.roundGesetzlichesDarlehen(darlehenFuer12MonateMitDarlehen * monateMitDarlehen / 12);
    }

    public static int getMonateMitDarlehen(Gesuch gesuch) {
        final var ausbildung = gesuch.getAusbildung();

        if (!ausbildung.getAusbildungsgang().getAbschluss().getBildungskategorie().isTertiaerstufe()) {
            return 0;
        }

        int monateTertiaerstufeLebenslauf = 0;

        for (var item : gesuch.getLatestGesuchTranche().getGesuchFormular().getLebenslaufItems()) {
            if (
                item.isAusbildung()
                && item.getAbschluss().getBildungskategorie().isTertiaerstufe()
            ) {
                monateTertiaerstufeLebenslauf += DateUtil.getMonthsBetween(item.getVon(), item.getBis());
            }
        }

        final var gesuchStartDate = gesuch.getEarliestGesuchTranche().getGueltigkeit().getGueltigAb();

        final var monateTertiaerstufeBisGesuchStart = monateTertiaerstufeLebenslauf + DateUtil.getMonthsBetween(
            ausbildung.getAusbildungBegin(),
            gesuchStartDate.atStartOfDay().toLocalDate()
        );

        final var monateTertiaerStufeBisGesuchEnde = monateTertiaerstufeBisGesuchStart + 12;

        if (monateTertiaerStufeBisGesuchEnde < BerechnungUtil.monthLimitAusbildungTertiaerstufe) {
            return 0;
        }

        if (monateTertiaerstufeBisGesuchStart < BerechnungUtil.monthLimitAusbildungTertiaerstufe) {
            final var monateMitDarlehen =
                monateTertiaerStufeBisGesuchEnde - BerechnungUtil.monthLimitAusbildungTertiaerstufe;
            return monateMitDarlehen;
        }
        return 12;
    }

    public Stream<TranchenBerechnungsresultatDto> getBerechnungsresultatFromGesuchTranche(
        final GesuchTranche gesuchTranche,
        final int majorVersion,
        final int minorVersion
    ) {
        final var gesuchsperiode = gesuchTranche.getGesuch().getGesuchsperiode();
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        final var steuerdaten = gesuchFormular.getSteuerdaten();
        final var yearRange = "%s/%s".formatted(
            gesuchsperiode.getGesuchsperiodeStart().getYear(),
            gesuchsperiode.getGesuchsperiodeStopp().getYear()
        );

        List<AbstractFamilieEntity> kinderDerElternInHaushalten = new ArrayList<>(
            gesuchFormular.getGeschwisters()
                .stream()
                .filter(
                    geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT
                )
                .map(AbstractFamilieEntity.class::cast)
                .toList()
        );

        if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
            kinderDerElternInHaushalten.add(gesuchFormular.getPersonInAusbildung());
        }

        final var teilzeitKinderDerElternInHaushalten = kinderDerElternInHaushalten.stream()
            .filter(
                geschwister -> Objects.requireNonNullElse(geschwister.getWohnsitzAnteilVater(), BigDecimal.ZERO)
                    .intValue() > 0
                &&
                Objects.requireNonNullElse(geschwister.getWohnsitzAnteilMutter(), BigDecimal.ZERO).intValue() > 0
            )
            .toList();

        int noKinderDerElternOhneEigenenHaushalt = teilzeitKinderDerElternInHaushalten.size();

        final var teilzeitKinderDerPia = gesuchFormular.getKinds()
            .stream()
            .filter(kind -> kind.getWohnsitzAnteilPia() > 0 && kind.getWohnsitzAnteilPia() < 100)
            .toList();

        List<TranchenBerechnungsresultatDto> berechnungsresultatDtoList = new ArrayList<>();

        List<ElternTyp> toSolveFor;
        if (gesuchFormular.getFamiliensituation().getElternVerheiratetZusammen()) {
            toSolveFor = List.of(ElternTyp.VATER);
        } else if (gesuchFormular.getElterns().isEmpty()) {
            toSolveFor = List.of(ElternTyp.MUTTER);
        } else {
            toSolveFor = gesuchFormular.getElterns().stream().map(Eltern::getElternTyp).toList();
        }

        var teilzeitKinderBeiPiaAnrechnenLoopVals = Set.of(Boolean.TRUE);
        if (!teilzeitKinderDerPia.isEmpty()) {
            teilzeitKinderBeiPiaAnrechnenLoopVals = Set.of(Boolean.TRUE, Boolean.FALSE);
        }

        for (final var teilzeitKinderBeiPiaAnrechnenLoopVal : teilzeitKinderBeiPiaAnrechnenLoopVals) {
            for (final var elternTypToSolveFor : toSolveFor) {
                final var berechnungsRequest = getBerechnungRequest(
                    majorVersion,
                    minorVersion,
                    gesuchTranche.getGesuch(),
                    gesuchTranche,
                    elternTypToSolveFor,
                    teilzeitKinderBeiPiaAnrechnenLoopVal
                );

                final var stipendienCalculated = calculateStipendien(berechnungsRequest);

                final List<FamilienBudgetresultatDto> familienBudgetresultatList = new ArrayList<>();

                final var steuerdatenList = steuerdaten.stream()
                    .sorted(
                        Comparator.comparing(Steuerdaten::getSteuerdatenTyp)
                    )
                    .toList();

                for (int i = 0; i < steuerdatenList.size(); i++) {
                    familienBudgetresultatList.add(
                        stipendienCalculated.getFamilienBudgetresultate().get(i)
                    );
                }

                if (
                    steuerdaten.size() <= 1
                    || noKinderDerElternOhneEigenenHaushalt == 0
                ) {
                    if (
                        berechnungsresultatDtoList.size() == teilzeitKinderBeiPiaAnrechnenLoopVals.size()
                    ) {
                        continue;
                    }

                    // KSTIP-2548: positive Zwischenbeiträge/Teilrechnungen auf 0 setzen
                    final var total = Math.min(0, stipendienCalculated.getStipendien());

                    berechnungsresultatDtoList.add(
                        new TranchenBerechnungsresultatDto(
                            total,
                            stipendienCalculated.getStipendien(),
                            gesuchTranche.getGueltigkeit().getGueltigAb(),
                            gesuchTranche.getGueltigkeit().getGueltigBis(),
                            DateUtil.formatDate(gesuchTranche.getGesuch().getAusbildung().getAusbildungBegin()),
                            DateUtil.formatDate(gesuchTranche.getGesuch().getAusbildung().getAusbildungEnd()),
                            yearRange,
                            gesuchTranche.getId(),
                            teilzeitKinderBeiPiaAnrechnenLoopVal,
                            berechnungsStammdatenFromRequest(
                                berechnungsRequest,
                                majorVersion,
                                minorVersion
                            ),
                            stipendienCalculated.getPersoenlichesBudgetresultat(),
                            familienBudgetresultatList,
                            BerechnungUtil.getPersonenHaushaltGroups(
                                stipendienCalculated.getPersoenlichesBudgetresultat(),
                                familienBudgetresultatList
                            ),
                            null,
                            null
                        )
                    );
                } else {
                    // To address differences in the stipendienberechnung based on how many kids are in the households
                    // and how their care is divided between father and mother,
                    // we calculate how many "kidpercentages" each household has and divide this by the total number of
                    // kids in all households.
                    // This value can then be multiplied with the respective stipendienberechnung to get a proportianal
                    // stipendienamount.
                    BigDecimal kinderDerElternProzente = BigDecimal.ZERO;

                    for (final var kindDerElternInHaushalten : teilzeitKinderDerElternInHaushalten) {
                        kinderDerElternProzente =
                            kinderDerElternProzente
                                .add(kindDerElternInHaushalten.getWohnsitzAnteil(elternTypToSolveFor));
                    }

                    final BigDecimal kinderDerElternProzenteNormalized = kinderDerElternProzente.divide(
                        BigDecimal.valueOf(teilzeitKinderDerElternInHaushalten.size()),
                        2,
                        RoundingMode.HALF_UP
                    );

                    // Calculate the total stipendien amount based on the respective amounts and their relative kid
                    // percentages.
                    var berechnetStipendien = kinderDerElternProzenteNormalized.multiply(
                        BigDecimal.valueOf(stipendienCalculated.getStipendien())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    ).intValue();

                    // KSTIP-2548: positive Zwischenbeiträge/Teilrechnungen auf 0 setzen
                    berechnetStipendien = Math.min(0, berechnetStipendien);

                    berechnungsresultatDtoList.add(
                        new TranchenBerechnungsresultatDto(
                            berechnetStipendien,
                            stipendienCalculated.getStipendien(),
                            gesuchTranche.getGueltigkeit().getGueltigAb(),
                            gesuchTranche.getGueltigkeit().getGueltigBis(),
                            DateUtil.formatDate(gesuchTranche.getGesuch().getAusbildung().getAusbildungBegin()),
                            DateUtil.formatDate(gesuchTranche.getGesuch().getAusbildung().getAusbildungEnd()),
                            yearRange,
                            gesuchTranche.getId(),
                            teilzeitKinderBeiPiaAnrechnenLoopVal,
                            berechnungsStammdatenFromRequest(
                                berechnungsRequest,
                                majorVersion,
                                minorVersion
                            ),
                            stipendienCalculated.getPersoenlichesBudgetresultat(),
                            familienBudgetresultatList,
                            BerechnungUtil.getPersonenHaushaltGroups(
                                stipendienCalculated.getPersoenlichesBudgetresultat(),
                                familienBudgetresultatList
                            ),
                            kinderDerElternProzenteNormalized,
                            null
                        )
                    );
                }
            }
        }
        if (teilzeitKinderBeiPiaAnrechnenLoopVals.size() > 1) {
            BigDecimal kinderDerPiaProzente = BigDecimal.ZERO;

            for (final var kindDerPia : teilzeitKinderDerPia) {
                kinderDerPiaProzente =
                    kinderDerPiaProzente.add(BigDecimal.valueOf(kindDerPia.getWohnsitzAnteilPia()));
            }

            final BigDecimal kinderDerPiAProzenteNormalized = kinderDerPiaProzente.divide(
                BigDecimal.valueOf(teilzeitKinderDerPia.size()),
                2,
                RoundingMode.HALF_UP
            );

            final BigDecimal kinderDerPiAProzenteNormalizedInverted =
                BigDecimal.valueOf(100).subtract(kinderDerPiAProzenteNormalized);

            for (final var berechnungsresultatDto : berechnungsresultatDtoList) {
                var kinderDerPiAProzenteToUse = kinderDerPiAProzenteNormalized;
                if (!berechnungsresultatDto.getTeilzeitKinderBeiPiaAnrechnen()) {
                    kinderDerPiAProzenteToUse = kinderDerPiAProzenteNormalizedInverted;
                }

                // Calculate the total stipendien amount based on the respective amounts and their relative kid
                // percentages.
                var berechnetStipendien = kinderDerPiAProzenteToUse.multiply(
                    BigDecimal.valueOf(berechnungsresultatDto.getTotal())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                ).intValue();
                berechnungsresultatDto.setTotal(berechnetStipendien);
                berechnungsresultatDto.setBerechnungsanteilKinderPia(kinderDerPiAProzenteToUse);
            }
        }
        return berechnungsresultatDtoList.stream();
    }

    public CalculatorRequest getBerechnungRequest(
        final int majorVersion,
        final int minorVersion,
        final Gesuch gesuch,
        final GesuchTranche gesuchTranche,
        final ElternTyp elternTyp,
        final boolean teilzeitKinderBeiPiaAnrechnen
    ) {
        final var builder = berechnungRequests.stream().filter(berechnungRequestBuilder -> {
            final var versionAnnotation = berechnungRequestBuilder.getClass().getAnnotation(CalculatorVersion.class);
            return (versionAnnotation != null) &&
            (versionAnnotation.major() == majorVersion) &&
            (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (builder.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a builder for version " + majorVersion + '.' + minorVersion
            );
        }

        return builder.get()
            .buildRequest(
                gesuch,
                gesuchTranche,
                elternTyp,
                teilzeitKinderBeiPiaAnrechnen
            );
    }

    public BerechnungResult calculateStipendien(final CalculatorRequest request) {
        final var calculator = stipendienCalculators.stream().filter(stipendienCalculator -> {
            final var clazz = stipendienCalculator.getClass();
            final var forTenant = clazz.getAnnotation(CalculatorTenant.class);
            if (forTenant != null && forTenant.value() != tenantService.getCurrentTenantIdentifier()) {
                return false;
            }

            final var versionAnnotation = clazz.getAnnotation(CalculatorVersion.class);
            return (versionAnnotation != null) &&
            (versionAnnotation.major() == request.majorVersion()) &&
            (versionAnnotation.minor() == request.minorVersion());
        }).findFirst();

        if (calculator.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a Stipendien Calculator for version " + request.getVersion()
            );
        }

        return calculator.get().calculateStipendien(request);
    }
}
