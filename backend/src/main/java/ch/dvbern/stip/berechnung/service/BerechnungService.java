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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateUtil;
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
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.ws.rs.NotFoundException;
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

    private FamilienBudgetresultatDto familienBudgetresultatFromRequest(
        final BerechnungResult berechnungResult,
        final int budgetToUse
    ) {
        // TODO DVSTIP-50: Replace this with something better?
        return berechnungResult.getFamilienBudgetresultate().get(budgetToUse);
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

    private int calcMonthsBetween(final LocalDate from, final LocalDate to) {
        return (int) ChronoUnit.MONTHS.between(
            from,
            to.plusDays(1)
        );
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

        final var actualDuration = wasEingereichtAfterDueDate(gesuch, gesuch.getEinreichedatum())
            ? getActualDuration(gesuch, gesuch.getEinreichedatum())
            : null;

        List<TranchenBerechnungsresultatDto> berechnungsresultate = new ArrayList<>(gesuchTranchen.size());
        for (final var gesuchTranche : gesuchTranchen) {
            final var trancheBerechnungsresultate = getBerechnungsresultatFromGesuchTranche(
                gesuchTranche,
                majorVersion,
                minorVersion
            );

            final var monthsValid = calcMonthsBetween(
                gesuchTranche.getGueltigkeit().getGueltigAb(),
                gesuchTranche.getGueltigkeit().getGueltigBis()
            );
            for (final var berechnungsresultat : trancheBerechnungsresultate) {
                berechnungsresultat.setBerechnung(
                    berechnungsresultat.getBerechnung() * monthsValid / 12
                );
            }
            berechnungsresultate.addAll(trancheBerechnungsresultate);
        }

        int berechnungsresultat =
            -berechnungsresultate.stream().mapToInt(TranchenBerechnungsresultatDto::getBerechnung).sum();
        if (berechnungsresultat < gesuch.getGesuchsperiode().getStipLimiteMinimalstipendium()) {
            berechnungsresultat = 0;
        }

        Integer berechnungsresultatReduziert =
            actualDuration != null ? berechnungsresultat * actualDuration / 12 : null;

        return new BerechnungsresultatDto(
            gesuch.getGesuchsperiode().getGesuchsjahr().getTechnischesJahr(),
            berechnungsresultat,
            berechnungsresultate,
            berechnungsresultatReduziert,
            actualDuration
        );
    }

    public List<TranchenBerechnungsresultatDto> getBerechnungsresultatFromGesuchTranche(
        final GesuchTranche gesuchTranche,
        final int majorVersion,
        final int minorVersion
    ) {
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        final var steuerdaten = gesuchFormular.getSteuerdaten();

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

        final var teilZeitKinderInHaushalten = kinderDerElternInHaushalten.stream()
            .filter(
                geschwister -> Objects.requireNonNullElse(geschwister.getWohnsitzAnteilVater(), BigDecimal.ZERO)
                    .intValue() > 0
                &&
                Objects.requireNonNullElse(geschwister.getWohnsitzAnteilMutter(), BigDecimal.ZERO).intValue() > 0
            )
            .toList();

        int noKinderOhneEigenenHaushalt = teilZeitKinderInHaushalten.size();

        List<TranchenBerechnungsresultatDto> berechnungsresultatDtoList = new ArrayList<>();

        List<ElternTyp> toSolveFor;
        if (gesuchFormular.getFamiliensituation().getElternVerheiratetZusammen()) {
            toSolveFor = List.of(ElternTyp.VATER);
        } else {
            toSolveFor = List.of(ElternTyp.MUTTER, ElternTyp.VATER);
        }

        for (final var elternTypToSolveFor : toSolveFor) {
            final var berechnungsRequest = getBerechnungRequest(
                majorVersion,
                minorVersion,
                gesuchTranche.getGesuch(),
                gesuchTranche,
                elternTypToSolveFor
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
                    familienBudgetresultatFromRequest(
                        stipendienCalculated,
                        i
                    )
                );
            }

            if (
                steuerdaten.size() <= 1
                || noKinderOhneEigenenHaushalt == 0
            ) {
                if (!berechnungsresultatDtoList.isEmpty()) {
                    continue;
                }
                berechnungsresultatDtoList.add(
                    new TranchenBerechnungsresultatDto(
                        gesuchTranche.getGesuchFormular().getPersonInAusbildung().getFullName(),
                        stipendienCalculated.getStipendien(),
                        gesuchTranche.getGueltigkeit().getGueltigAb(),
                        gesuchTranche.getGueltigkeit().getGueltigBis(),
                        gesuchTranche.getId(),
                        BigDecimal.ONE,
                        berechnungsStammdatenFromRequest(
                            berechnungsRequest,
                            majorVersion,
                            minorVersion
                        ),
                        stipendienCalculated.getPersoenlichesBudgetresultat(),
                        familienBudgetresultatList
                    )
                );
            } else {
                // To address differences in the stipendienberechnung based on how many kids are in the households and
                // how their care is divided between father and mother,
                // we calculate how many "kidpercentages" each household has and divide this by the total number of kids
                // in all households.
                // This value can then be multiplied with the respective stipendienberechnung to get a proportianal
                // stipendienamount.
                BigDecimal kinderProzente = BigDecimal.ZERO;

                for (final var kindDerElternInHaushalten : teilZeitKinderInHaushalten) {
                    kinderProzente =
                        kinderProzente.add(kindDerElternInHaushalten.getWohnsitzAnteil(elternTypToSolveFor));
                }

                final BigDecimal kinderProzenteNormalized = kinderProzente.divide(
                    BigDecimal.valueOf(teilZeitKinderInHaushalten.size()),
                    2,
                    RoundingMode.HALF_UP
                );

                // Calculate the total stipendien amount based on the respective amounts and their relative kid
                // percentages.
                final var berechnung = kinderProzenteNormalized.multiply(
                    BigDecimal.valueOf(stipendienCalculated.getStipendien())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                ).intValue();

                berechnungsresultatDtoList.add(
                    new TranchenBerechnungsresultatDto(
                        gesuchTranche.getGesuchFormular().getPersonInAusbildung().getFullName(),
                        berechnung,
                        gesuchTranche.getGueltigkeit().getGueltigAb(),
                        gesuchTranche.getGueltigkeit().getGueltigBis(),
                        gesuchTranche.getId(),
                        kinderProzenteNormalized,
                        berechnungsStammdatenFromRequest(
                            berechnungsRequest,
                            majorVersion,
                            minorVersion
                        ),
                        stipendienCalculated.getPersoenlichesBudgetresultat(),
                        familienBudgetresultatList
                    )
                );
            }
        }
        return berechnungsresultatDtoList;
    }

    public CalculatorRequest getBerechnungRequest(
        final int majorVersion,
        final int minorVersion,
        final Gesuch gesuch,
        final GesuchTranche gesuchTranche,
        final ElternTyp elternTyp
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

        return builder.get().buildRequest(gesuch, gesuchTranche, elternTyp);
    }

    public BerechnungResult calculateStipendien(final CalculatorRequest request) {
        final var calculator = stipendienCalculators.stream().filter(stipendienCalculator -> {
            final var clazz = stipendienCalculator.getClass();
            final var forMandant = clazz.getAnnotation(CalculatorMandant.class);
            if (forMandant != null && forMandant.value() != tenantService.getCurrentMandantIdentifier()) {
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

    boolean wasEingereichtAfterDueDate(final Gesuch gesuch, final LocalDate eingereicht) {
        final var einreichefrist = gesuch.getGesuchsperiode().getEinreichefristNormal();
        return eingereicht.isAfter(einreichefrist);
    }

    int getActualDuration(final Gesuch gesuch, final LocalDate eingereicht) {
        final var lastTranche = gesuch.getTranchenTranchen()
            .max(Comparator.comparing(tranche -> tranche.getGueltigkeit().getGueltigBis()))
            .orElseThrow(NotFoundException::new);

        final var roundedEingereicht = DateUtil.roundToStartOrEnd(
            eingereicht,
            14,
            true,
            false
        );

        return DateUtil.getMonthsBetween(roundedEingereicht, lastTranche.getGueltigkeit().getGueltigBis());
    }
}
