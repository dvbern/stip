package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.dmn.api.core.event.AfterEvaluateDecisionEvent;
import org.kie.dmn.core.api.event.DefaultDMNRuntimeEventListener;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BerechnungService {
    private static final List<String>
        BERECHNUNG_MODEL_NAMES = List.of("Stipendium", "Familienbudget", "PersoenlichesBudget");
    private static final String STIPENDIUM_DECISION_NAME = "Stipendium";

    private final PersonenImHaushaltService personenImHaushaltService;
    private final Instance<BerechnungRequestBuilder> berechnungRequests;
    private final Instance<PersoenlichesBudgetResultatMapper> persoenlichesBudgetResultatMappers;
    private final Instance<FamilienBudgetresultatMapper> familienBudgetresultatMappers;
    private final Instance<BerechnungsStammdatenMapper> berechnungsStammdatenMappers;
    private final DMNService dmnService;
    private final TenantService tenantService;

    private PersoenlichesBudgetresultatDto persoenlichesBudgetresultatFromRequest(
        final DmnRequest berechnungRequest,
        final BerechnungResult berechnungResult,
        final List<FamilienBudgetresultatDto> familienBudgetresultatList,
        final int majorVersion,
        final int minorVersion
    ) {
        final var mapper = persoenlichesBudgetResultatMappers.stream().filter(persoenlichesBudgetResultatMapper -> {
            final var versionAnnotation = persoenlichesBudgetResultatMapper.getClass().getAnnotation(DmnModelVersion.class);
            return (versionAnnotation != null) &&
                (versionAnnotation.major() == majorVersion) &&
                (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (mapper.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot find a PersoenlichesBudgetResultatMapper for version " + majorVersion + '.' + minorVersion
            );
        }
        final var decisionResults = berechnungResult.getDecisionEventList().stream().filter(
            afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals("PersoenlichesbudgetBerechnet")
        ).toList().get(0).getResult().getDecisionResults();

        final int einnahmenPersoenlichesBudget = ((BigDecimal) decisionResults.stream().filter(
            dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("EinnahmenPersoenlichesBudget")
        ).toList().get(0).getResult()).intValue();

        final int ausgabenPersoenlichesBudget = ((BigDecimal) decisionResults.stream().filter(
            dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("AusgabenPersoenlichesBudget")
        ).toList().get(0).getResult()).intValue();

        final int persoenlichesbudgetBerechnet = ((BigDecimal) decisionResults.stream().filter(
            dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("PersoenlichesbudgetBerechnet")
        ).toList().get(0).getResult()).intValue();

        return mapper.get().mapFromRequest(
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
        final var decisionResults = berechnungResult.getDecisionEventList().stream().filter(
            afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals("Familienbudget_" + (budgetToUse + 1))
        ).toList().get(0).getResult().getDecisionResults();

        @SuppressWarnings("unchecked") // It's fine (and necessary)
        final var familienbudgetMap = ((HashMap<String, BigDecimal>) decisionResults.stream().filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("Familienbudget_" + (budgetToUse + 1))
        ).toList().get(0).getResult());
        final int familienbudgetBerechnet = familienbudgetMap.get("familienbudgetBerechnet").intValue();

        final String fambudgetKey = "familienbudgetBerechnet";
        final var einnahmenFamilienbudget = ((BigDecimal) berechnungResult.getDecisionEventList().stream().filter(
            afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals(fambudgetKey)
        ).toList().get(budgetToUse).getResult().getDecisionResults().stream().filter(
            dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("EinnahmenFamilienbudget")
        ).toList().get(0).getResult()).intValue();

        final var ausgabenFamilienbudget = ((BigDecimal) berechnungResult.getDecisionEventList().stream().filter(
            afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals(fambudgetKey)
        ).toList().get(budgetToUse).getResult().getDecisionResults().stream().filter(
            dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("AusgabenFamilienbudget")
        ).toList().get(0).getResult()).intValue();

        return mapper.get().mapFromRequest(
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
        final var gesuchStatusToFilterFor = List.of(GesuchTrancheStatus.AKZEPTIERT, GesuchTrancheStatus.IN_BEARBEITUNG_GS, GesuchTrancheStatus.UEBERPRUEFEN);
        final var gesuchTranchen = gesuch.getGesuchTranchen().stream().filter(
            gesuchTranche -> gesuchStatusToFilterFor.contains(gesuchTranche.getStatus())
        ).sorted(
            Comparator.comparing(gesuchTranche -> gesuchTranche.getGueltigkeit().getGueltigAb())
        ).toList();

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
                    (berechnungsresultat.getBerechnung() * monthsValid / 12)
                );
            }
            berechnungsresultate.addAll(trancheBerechnungsresultate);
        }

        int berechnungsresultat = -berechnungsresultate.stream().mapToInt(TranchenBerechnungsresultatDto::getBerechnung).sum();
        if (berechnungsresultat < -gesuch.getGesuchsperiode().getStipLimiteMinimalstipendium()) {
            berechnungsresultat = 0;
        }

        return new BerechnungsresultatDto(
            berechnungsresultat,
            berechnungsresultate
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
            gesuchFormular.getGeschwisters().stream()
                .filter(
                    geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT
                ).map(AbstractFamilieEntity.class::cast)
                .toList()
        );

        if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
            kinderDerElternInHaushalten.add(gesuchFormular.getPersonInAusbildung());
        }

        final var teilZeitKinderInHaushalten = kinderDerElternInHaushalten.stream().filter(
            geschwister -> Objects.requireNonNullElse(geschwister.getWohnsitzAnteilVater(),  BigDecimal.ZERO).intValue() > 0 &&
                           Objects.requireNonNullElse(geschwister.getWohnsitzAnteilMutter(), BigDecimal.ZERO).intValue() > 0
        ).toList();

        int noKinderOhneEigenenHaushalt = teilZeitKinderInHaushalten.size();

        List<TranchenBerechnungsresultatDto> berechnungsresultatDtoList = new ArrayList<>();

        for (final var stuerdatum : steuerdaten) {
            final var steuerdatenTyp = stuerdatum.getSteuerdatenTyp();
            final var elternTypToSolveFor =
                steuerdatenTyp == SteuerdatenTyp.MUTTER
                    ? ElternTyp.MUTTER
                    : ElternTyp.VATER;

            final var berechnungsRequest = (BerechnungRequestV1) getBerechnungRequest(
                majorVersion,
                minorVersion,
                gesuchTranche.getGesuch(),
                gesuchTranche,
                elternTypToSolveFor
            );

            final var stipendienCalculated = calculateStipendien(berechnungsRequest);

            final List<FamilienBudgetresultatDto> familienBudgetresultatList = new ArrayList<>();

            final ListIterator<Steuerdaten> steuerdatenListIterator = steuerdaten.stream().sorted(
                Comparator.comparing(Steuerdaten::getSteuerdatenTyp)
            ).toList().listIterator();

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
                // To address differences in the stipendienberechnung based on how many kids are in the households and how their care is divided between father and mother,
                // we calculate how many "kidpercentages" each household has and divide this by the total number of kids in all households.
                // This value can then be multiplied with the respective stipendienberechnung to get a proportianal stipendienamount.
                BigDecimal kinderProzente = BigDecimal.ZERO;



                for (final var kindDerElternInHaushalten : teilZeitKinderInHaushalten) {
                    kinderProzente = kinderProzente.add(kindDerElternInHaushalten.getWohnsitzAnteil(elternTypToSolveFor));
                }

                final BigDecimal kinderProzenteNormalized = kinderProzente.divide(
                    BigDecimal.valueOf(teilZeitKinderInHaushalten.size()), 2, RoundingMode.HALF_UP
                );

                // Calculate the total stipendien amount based on the respective amounts and their relative kid percentages.
                final var berechnung = kinderProzenteNormalized.multiply(
                    BigDecimal.valueOf(stipendienCalculated.getStipendien())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    ).intValue();

                berechnungsresultatDtoList.add(
                    new TranchenBerechnungsresultatDto(
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
            throw new IllegalArgumentException("Cannot find a builder for version " + majorVersion + '.' + minorVersion);
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

            @Override
            public void afterEvaluateDecision(AfterEvaluateDecisionEvent event) {
                decisionNodeList.add(event);
            }
        };

        final var result = dmnService.evaluateModel(models, DmnRequestContextUtil.toContext(request), listener);
        final var stipendien = (BigDecimal) result.getDecisionResultByName(STIPENDIUM_DECISION_NAME).getResult();
        if (stipendien == null) {
            throw new AppErrorException("Result of Stipendienberechnung was null!");
        }

        return new BerechnungResult(stipendien.intValue(), result.getDecisionResults(), listener.decisionNodeList);
    }
}
