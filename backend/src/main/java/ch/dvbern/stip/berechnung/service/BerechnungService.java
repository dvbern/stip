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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.dto.BerechnungsStammdatenMapper;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.FamilienBudgetresultatMapper;
import ch.dvbern.stip.berechnung.dto.PersoenlichesBudgetResultatMapper;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import ch.dvbern.stip.berechnung.util.DmnRequestContextUtil;
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNDecisionResult.DecisionEvaluationStatus;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionEvent;
import org.kie.dmn.core.api.event.DefaultDMNRuntimeEventListener;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BerechnungService {
    private static final List<String> BERECHNUNG_MODEL_NAMES =
        List.of("Stipendium", "Familienbudget", "PersoenlichesBudget");
    private static final String STIPENDIUM_DECISION_NAME = "Stipendium";

    private final PersonenImHaushaltService personenImHaushaltService;
    private final Instance<BerechnungRequestBuilder> berechnungRequests;
    private final Instance<PersoenlichesBudgetResultatMapper> persoenlichesBudgetResultatMappers;
    private final Instance<FamilienBudgetresultatMapper> familienBudgetresultatMappers;
    private final Instance<BerechnungsStammdatenMapper> berechnungsStammdatenMappers;
    private final DMNService dmnService;
    private final TenantService tenantService;
    private final GesuchHistoryRepository gesuchHistoryRepository;

    private PersoenlichesBudgetresultatDto persoenlichesBudgetresultatFromRequest(
        final DmnRequest berechnungRequest,
        final BerechnungResult berechnungResult,
        final List<FamilienBudgetresultatDto> familienBudgetresultatList,
        final int majorVersion,
        final int minorVersion
    ) {
        final var mapper = persoenlichesBudgetResultatMappers.stream().filter(persoenlichesBudgetResultatMapper -> {
            final var versionAnnotation =
                persoenlichesBudgetResultatMapper.getClass().getAnnotation(DmnModelVersion.class);
            return (versionAnnotation != null) &&
            (versionAnnotation.major() == majorVersion) &&
            (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (mapper.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a PersoenlichesBudgetResultatMapper for version " + majorVersion + '.' + minorVersion
            );
        }
        final var decisionResults = berechnungResult.getDecisionEventList()
            .stream()
            .filter(
                afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision()
                    .getName()
                    .equals("PersoenlichesbudgetBerechnet")
            )
            .toList()
            .get(0)
            .getResult()
            .getDecisionResults();

        final int einnahmenPersoenlichesBudget = ((BigDecimal) decisionResults.stream()
            .filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("EinnahmenPersoenlichesBudget")
            )
            .toList()
            .get(0)
            .getResult()).intValue();

        final int ausgabenPersoenlichesBudget = ((BigDecimal) decisionResults.stream()
            .filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("AusgabenPersoenlichesBudget")
            )
            .toList()
            .get(0)
            .getResult()).intValue();

        final int persoenlichesbudgetBerechnet = ((BigDecimal) decisionResults.stream()
            .filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("PersoenlichesbudgetBerechnet")
            )
            .toList()
            .get(0)
            .getResult()).intValue();

        return mapper.get()
            .mapFromRequest(
                berechnungRequest,
                einnahmenPersoenlichesBudget,
                ausgabenPersoenlichesBudget,
                persoenlichesbudgetBerechnet,
                familienBudgetresultatList
            );
    }

    private FamilienBudgetresultatDto familienBudgetresultatFromRequest(
        final DmnRequest berechnungRequest,
        final BerechnungResult berechnungResult,
        final SteuerdatenTyp steuerdatenTyp,
        final int budgetToUse,
        final int majorVersion,
        final int minorVersion
    ) {
        final var mapper = familienBudgetresultatMappers.stream().filter(familienBudgetresultatMapper -> {
            final var versionAnnotation = familienBudgetresultatMapper.getClass().getAnnotation(DmnModelVersion.class);
            return (versionAnnotation != null) &&
            (versionAnnotation.major() == majorVersion) &&
            (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (mapper.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a FamilienBudgetresultatMapper for version " + majorVersion + '.' + minorVersion
            );
        }
        final var decisionResults = berechnungResult.getDecisionEventList()
            .stream()
            .filter(
                afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision()
                    .getName()
                    .equals("Familienbudget_" + (budgetToUse + 1))
            )
            .toList()
            .get(0)
            .getResult()
            .getDecisionResults();

        @SuppressWarnings("unchecked") // It's fine (and necessary)
        final var familienbudgetMap = ((HashMap<String, BigDecimal>) decisionResults.stream()
            .filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("Familienbudget_" + (budgetToUse + 1))
            )
            .toList()
            .get(0)
            .getResult());
        final int familienbudgetBerechnet = familienbudgetMap.get("familienbudgetBerechnet").intValue();

        final String fambudgetKey = "familienbudgetBerechnet";
        final var einnahmenFamilienbudget = ((BigDecimal) berechnungResult.getDecisionEventList()
            .stream()
            .filter(
                afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals(fambudgetKey)
            )
            .toList()
            .get(budgetToUse)
            .getResult()
            .getDecisionResults()
            .stream()
            .filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("EinnahmenFamilienbudget")
            )
            .toList()
            .get(0)
            .getResult()).intValue();

        final var ausgabenFamilienbudget = ((BigDecimal) berechnungResult.getDecisionEventList()
            .stream()
            .filter(
                afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals(fambudgetKey)
            )
            .toList()
            .get(budgetToUse)
            .getResult()
            .getDecisionResults()
            .stream()
            .filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("AusgabenFamilienbudget")
            )
            .toList()
            .get(0)
            .getResult()).intValue();

        return mapper.get()
            .mapFromRequest(
                berechnungRequest,
                steuerdatenTyp,
                budgetToUse,
                einnahmenFamilienbudget,
                ausgabenFamilienbudget,
                familienbudgetBerechnet
            );
    }

    private BerechnungsStammdatenDto berechnungsStammdatenFromRequest(
        final DmnRequest berechnungRequest,
        final int majorVersion,
        final int minorVersion
    ) {
        final var mapper = berechnungsStammdatenMappers.stream().filter(berechnungsStammdatenMapper -> {
            final var versionAnnotation = berechnungsStammdatenMapper.getClass().getAnnotation(DmnModelVersion.class);
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

        final var eingereicht = gesuchHistoryRepository.requireLatestEingereicht(gesuch.getId());
        final var actualDuration = wasEingereichtAfterDueDate(gesuch, eingereicht.getTimestampMutiert())
            ? getActualDuration(gesuch, eingereicht.getTimestampMutiert())
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
                int berechnung;
                if (actualDuration != null) {
                    berechnung = berechnungsresultat.getBerechnung() * actualDuration / 12;
                } else {
                    berechnung = berechnungsresultat.getBerechnung();
                }

                berechnungsresultat.setBerechnung(
                    berechnung * monthsValid / 12
                );
            }
            berechnungsresultate.addAll(trancheBerechnungsresultate);
        }

        int berechnungsresultat =
            -berechnungsresultate.stream().mapToInt(TranchenBerechnungsresultatDto::getBerechnung).sum();
        if (berechnungsresultat < -gesuch.getGesuchsperiode().getStipLimiteMinimalstipendium()) {
            berechnungsresultat = 0;
        }

        return new BerechnungsresultatDto(
            gesuch.getGesuchsperiode().getGesuchsjahr().getTechnischesJahr(),
            berechnungsresultat,
            berechnungsresultate,
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
            final var berechnungsRequest = (BerechnungRequestV1) getBerechnungRequest(
                majorVersion,
                minorVersion,
                gesuchTranche.getGesuch(),
                gesuchTranche,
                elternTypToSolveFor
            );

            final var stipendienCalculated = calculateStipendien(berechnungsRequest);

            final List<FamilienBudgetresultatDto> familienBudgetresultatList = new ArrayList<>();

            final ListIterator<Steuerdaten> steuerdatenListIterator = steuerdaten.stream()
                .sorted(
                    Comparator.comparing(Steuerdaten::getSteuerdatenTyp)
                )
                .toList()
                .listIterator();

            while (steuerdatenListIterator.hasNext()) {
                final var budgetIndex = steuerdatenListIterator.nextIndex();
                final Steuerdaten steuerdatum = steuerdatenListIterator.next();
                familienBudgetresultatList.add(
                    familienBudgetresultatFromRequest(
                        berechnungsRequest,
                        stipendienCalculated,
                        steuerdatum.getSteuerdatenTyp(),
                        budgetIndex,
                        majorVersion,
                        minorVersion
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
                        persoenlichesBudgetresultatFromRequest(
                            berechnungsRequest,
                            stipendienCalculated,
                            familienBudgetresultatList,
                            majorVersion,
                            minorVersion
                        ),
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
                        persoenlichesBudgetresultatFromRequest(
                            berechnungsRequest,
                            stipendienCalculated,
                            familienBudgetresultatList,
                            majorVersion,
                            minorVersion
                        ),
                        familienBudgetresultatList
                    )
                );
            }
        }
        return berechnungsresultatDtoList;
    }

    public DmnRequest getBerechnungRequest(
        final int majorVersion,
        final int minorVersion,
        final Gesuch gesuch,
        final GesuchTranche gesuchTranche,
        final ElternTyp elternTyp
    ) {
        final var builder = berechnungRequests.stream().filter(berechnungRequestBuilder -> {
            final var versionAnnotation = berechnungRequestBuilder.getClass().getAnnotation(DmnModelVersion.class);
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

    public BerechnungResult calculateStipendien(final DmnRequest request) {
        List<Resource> models = new ArrayList<>(BERECHNUNG_MODEL_NAMES.size());
        for (var modelName : BERECHNUNG_MODEL_NAMES) {
            models.add(
                dmnService.loadModelsForTenantAndVersionByName(
                    tenantService.getCurrentTenant().getIdentifier(),
                    request.getVersion(),
                    modelName
                ).get(0)
            );
        }

        final var listener = new DefaultDMNRuntimeEventListener() {
            final List<AfterEvaluateDecisionEvent> decisionNodeList = new ArrayList<>();
            final List<DMNDecisionResult> unsuccessfulResults = new ArrayList<>();

            @Override
            public void afterEvaluateDecision(AfterEvaluateDecisionEvent event) {
                decisionNodeList.add(event);
                event.getResult()
                    .getDecisionResults()
                    .stream()
                    .filter(
                        decisionResult -> decisionResult.getEvaluationStatus() != DecisionEvaluationStatus.SUCCEEDED
                    )
                    .forEach(unsuccessfulResults::add);
            }
        };

        final var result = dmnService.evaluateModel(models, DmnRequestContextUtil.toContext(request), listener);
        if (
            listener.unsuccessfulResults.stream()
                .noneMatch(
                    dmnDecisionResult -> dmnDecisionResult
                        .getEvaluationStatus() != DecisionEvaluationStatus.SUCCEEDED
                )
        ) {
            LOG.warn(
                "DMN evaluation had decision results that did not succeed: {}",
                Arrays.toString(
                    listener.unsuccessfulResults.stream()
                        .filter(
                            dmnDecisionResult -> dmnDecisionResult
                                .getEvaluationStatus() != DecisionEvaluationStatus.SUCCEEDED
                        )
                        .toArray()
                )
            );
        }

        final var stipendien = (BigDecimal) result.getDecisionResultByName(STIPENDIUM_DECISION_NAME).getResult();
        if (stipendien == null) {
            throw new AppErrorException("Result of Stipendienberechnung was null!");
        }

        return new BerechnungResult(stipendien.intValue(), result.getDecisionResults(), listener.decisionNodeList);
    }

    boolean wasEingereichtAfterDueDate(final Gesuch gesuch, final LocalDateTime eingereicht) {
        // TODO KSTIP-1852: unit test this
        // TODO KSTIP-998: Use new einreichedatum instead of envers query here
        final var einreichefrist = gesuch.getGesuchsperiode().getEinreichefristNormal();

        return eingereicht.isAfter(einreichefrist.atTime(LocalTime.MAX));
    }

    // TODO KSTIP-1852: unit test this
    int getActualDuration(final Gesuch gesuch, final LocalDateTime eingereicht) {
        final var lastTranche = gesuch.getTranchenTranchen()
            .max(Comparator.comparing(tranche -> tranche.getGueltigkeit().getGueltigBis()))
            .orElseThrow(NotFoundException::new);

        final var roundedEingereicht = DateUtil.roundToStartOrEnd(
            eingereicht.toLocalDate(),
            14,
            true,
            false
        );

        return DateUtil.getMonthsBetween(roundedEingereicht, lastTranche.getGueltigkeit().getGueltigBis());
    }
}
