package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

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
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.FamilienBudgetresultatMapper;
import ch.dvbern.stip.berechnung.dto.PersoenlichesBudgetResultatMapper;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import ch.dvbern.stip.berechnung.util.DmnRequestContextUtil;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.dmn.api.core.DMNDecisionResult;
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
    private final DMNService dmnService;
    private final TenantService tenantService;

    private static JsonObject decisionEventListToJSON(final List<AfterEvaluateDecisionEvent> decisionEventList) {
        final var factory = Json.createBuilderFactory(null);
        final JsonObjectBuilder baseObjectBuilder = factory.createObjectBuilder();
        for (final AfterEvaluateDecisionEvent event : decisionEventList) {
            final JsonObjectBuilder eventObjectBuilder = factory.createObjectBuilder();
            for (final DMNDecisionResult result : event.getResult().getDecisionResults()) {
                eventObjectBuilder.add(result.getDecisionName(), result.getResult().toString());
            }
            baseObjectBuilder.add(event.getDecision().getName(), eventObjectBuilder);
        }
        return baseObjectBuilder.build();
    }

    private PersoenlichesBudgetresultatDto persoenlichesBudgetresultatFromRequest(
        final DmnRequest berechnungRequest,
        final BerechnungResult berechnungResult,
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
            throw new IllegalArgumentException("Cannot find a PersoenlichesBudgetResultatMapper for version " + majorVersion + '.' + minorVersion);
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
            persoenlichesbudgetBerechnet
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
            throw new IllegalArgumentException("Cannot find a FamilienBudgetresultatMapper for version " + majorVersion + '.' + minorVersion);
        }
        final var decisionResults = berechnungResult.getDecisionEventList().stream().filter(
            afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals("Familienbudget_" + budgetToUse)
        ).toList().get(0).getResult().getDecisionResults();

        @SuppressWarnings("unchecked") // It's fine (and necessary)
        final var familienbudgetMap = ((HashMap<String, BigDecimal>) decisionResults.stream().filter(
                dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("Familienbudget_" + budgetToUse)
        ).toList().get(0).getResult());
        final int familienbudgetBerechnet = familienbudgetMap.get("familienbudgetBerechnet").intValue();

        final String fambudgetKey = "familienbudgetBerechnet";
        final var einnahmenFamilienbudget = ((BigDecimal) berechnungResult.getDecisionEventList().stream().filter(
            afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals(fambudgetKey)
        ).toList().get(budgetToUse-1).getResult().getDecisionResults().stream().filter(
            dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("EinnahmenFamilienbudget")
        ).toList().get(0).getResult()).intValue();

        final var ausgabenFamilienbudget = ((BigDecimal) berechnungResult.getDecisionEventList().stream().filter(
            afterEvaluateDecisionEvent -> afterEvaluateDecisionEvent.getDecision().getName().equals(fambudgetKey)
        ).toList().get(budgetToUse-1).getResult().getDecisionResults().stream().filter(
            dmnDecisionResult -> dmnDecisionResult.getDecisionName().equals("AusgabenFamilienbudget")
        ).toList().get(0).getResult()).intValue();

        return mapper.get().mapFromRequest(
            berechnungRequest,
            steuerdatenTyp,
            budgetToUse,
            einnahmenFamilienbudget,
            ausgabenFamilienbudget,
            familienbudgetBerechnet,
            0
        );
    }

    private int calcMonthsBetween(final LocalDate from, final LocalDate to) {
        final var monthsValid = ChronoUnit.MONTHS.between(
            from,
            to.plusDays(1)
        );
        LOG.info("Months Between " + from + " and " + to + " is: " + monthsValid);
        return (int) monthsValid;
    }

    public List<BerechnungsresultatDto> getBerechnungsresultateFromGesuch(
        final Gesuch gesuch,
        final int majorVersion,
        final int minorVersion
    ) {
        final var gesuchStatusToFilterFor = List.of(GesuchTrancheStatus.AKZETPIERT, GesuchTrancheStatus.IN_BEARBEITUNG_GS, GesuchTrancheStatus.UEBERPRUEFEN);
        final var gesuchTranchen = gesuch.getGesuchTranchen().stream().filter(gesuchTranche -> gesuchStatusToFilterFor.contains(gesuchTranche.getStatus())).toList();

        List<BerechnungsresultatDto> berechnungsresultate = new ArrayList<>(gesuchTranchen.size());
        for (final var gesuchTranche : gesuchTranchen) {
            final var berechnungsresultat = getBerechnungsresultatFromGesuchTranche(
                gesuchTranche,
                majorVersion,
                minorVersion
            );

            final var monthsValid = calcMonthsBetween(
                gesuchTranche.getGueltigkeit().getGueltigAb(),
                gesuchTranche.getGueltigkeit().getGueltigBis()
            );
            berechnungsresultat.setBerechnung(
                (berechnungsresultat.getBerechnung() * 12 / monthsValid)
            );
            berechnungsresultate.add(berechnungsresultat);
        }
        return berechnungsresultate;
    }

    public BerechnungsresultatDto getBerechnungsresultatFromGesuchTranche(
        final GesuchTranche gesuchTranche,
        final int majorVersion,
        final int minorVersion
    ) {
        final var gesuch = gesuchTranche.getGesuch();
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        // Use DMN model simply to get the number of budgets required. Bit overkill to do it for both parents but may serve as sanity check.
        final var personenImHaushaltRequestForVater = personenImHaushaltService.getPersonenImHaushaltRequest(
            majorVersion,
            minorVersion,
            gesuchFormular,
            ElternTyp.VATER
        );
        final var personenImHaushaltRequestForMutter = personenImHaushaltService.getPersonenImHaushaltRequest(
            majorVersion,
            minorVersion,
            gesuchFormular,
            ElternTyp.MUTTER
        );
        final var personenImHaushaltForVater = personenImHaushaltService.calculatePersonenImHaushalt(personenImHaushaltRequestForVater);
        final var personenImHaushaltForMutter = personenImHaushaltService.calculatePersonenImHaushalt(personenImHaushaltRequestForMutter);
        if (personenImHaushaltForVater.getNoBudgetsRequired() != personenImHaushaltForMutter.getNoBudgetsRequired()) {
            throw new IllegalStateException("noBudgetsRequired do not match between parents");
        }
        final var noBudgetsRequired = personenImHaushaltForVater.getNoBudgetsRequired();

        // Run the DMN for the stipendien berechnung
        final var stipendienBerechnungsRequestForVater = (BerechnungRequestV1) getBerechnungRequest(
            majorVersion,
            minorVersion,
            gesuch,
            gesuchTranche,
            ElternTyp.VATER
        );

        final var stipendienCalculatedForVater = calculateStipendien(stipendienBerechnungsRequestForVater);

        final var factory = Json.createBuilderFactory(null);
        JsonObjectBuilder baseObjectBuilder = factory.createObjectBuilder();
        // If only one budget is required then there is no need to calculate the proportional stipendium based on the kids in the houshold. So we just take the one of the father
        var berechnung = stipendienCalculatedForVater.getStipendien();
        BerechnungRequestV1 stipendienBerechnungsRequestForMutter = null;
        if (noBudgetsRequired > 1) {
            stipendienBerechnungsRequestForMutter = (BerechnungRequestV1) getBerechnungRequest(
                majorVersion,
                minorVersion,
                gesuch,
                gesuchTranche,
                ElternTyp.MUTTER
            );
            final var stipendienCalculatedForMutter = calculateStipendien(stipendienBerechnungsRequestForMutter);

            baseObjectBuilder.add("vaterBerechnungsDaten", decisionEventListToJSON(stipendienCalculatedForVater.getDecisionEventList()));
            baseObjectBuilder.add("mutterBerechnungsDaten", decisionEventListToJSON(stipendienCalculatedForMutter.getDecisionEventList()));

            // To address differences in the stipendienberechnung based on how many kids are in the households and how their care is divided between father and mother,
            // we calculate how many "kidpercentages" each household has and divide this by the total number of kids in all households.
            // This value can then be multiplied with the respective stipendienberechnung to get a proportianal stipendienamount.
            BigDecimal kinderProzenteVater = BigDecimal.ZERO;
            BigDecimal kinderProzenteMutter = BigDecimal.ZERO;
            int noKinderOhneEigenenHaushalt = 0;
            // If the PIA is living split between both we take the noted percentage. Otherwise we assume 50/50
            if (gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitz() == Wohnsitz.MUTTER_VATER) {
                kinderProzenteVater = gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitzAnteilVater();
                kinderProzenteMutter = gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitzAnteilMutter();
                noKinderOhneEigenenHaushalt += 1;
            } else if (gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitz() == Wohnsitz.FAMILIE) {
                if (gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitzAnteilVater() == null &&
                    gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitzAnteilMutter() == null) {
                    kinderProzenteVater = BigDecimal.valueOf(50);
                    kinderProzenteMutter = BigDecimal.valueOf(50);
                } else {
                    kinderProzenteVater = gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitzAnteilVater();
                    kinderProzenteMutter = gesuchTranche.getGesuchFormular().getPersonInAusbildung().getWohnsitzAnteilMutter();
                }
                noKinderOhneEigenenHaushalt += 1;
            }

            // Sum up the percentages for all siblings
            final var geschwisters = gesuchTranche.getGesuchFormular().getGeschwisters();
            for (final var geschwister : geschwisters) {
                if (geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                    kinderProzenteVater = kinderProzenteVater.add(geschwister.getWohnsitzAnteilVater());
                    kinderProzenteMutter = kinderProzenteMutter.add(geschwister.getWohnsitzAnteilMutter());
                    noKinderOhneEigenenHaushalt += 1;
                }
            }
            // If all kids have their own living arrangement the calcualtion is moot and would lead to a /0 error. So we just the previously assigned value
            if (noKinderOhneEigenenHaushalt > 0) {
                // Calculate the relative percentage (i.e. how much of all kids live with each parent)
                kinderProzenteVater = kinderProzenteVater.divide(BigDecimal.valueOf(noKinderOhneEigenenHaushalt)).round(new MathContext(2, RoundingMode.HALF_UP));
                kinderProzenteMutter = kinderProzenteMutter.divide(BigDecimal.valueOf(noKinderOhneEigenenHaushalt)).round(new MathContext(2, RoundingMode.HALF_UP));

                // Calculate the total stipendien amount based on the respective amounts and their relative kid percentages.
                berechnung =
                    kinderProzenteVater.multiply(BigDecimal.valueOf(stipendienCalculatedForVater.getStipendien())
                        .divide(BigDecimal.valueOf(100))).round(new MathContext(2, RoundingMode.HALF_UP)).intValue()
                        + kinderProzenteMutter.multiply(BigDecimal.valueOf(stipendienCalculatedForMutter.getStipendien())
                        .divide(BigDecimal.valueOf(100))).round(new MathContext(2, RoundingMode.HALF_UP)).intValue();
            }
        } else {
            // If there is only one budget.
            baseObjectBuilder.add("berechnungsDaten", decisionEventListToJSON(stipendienCalculatedForVater.getDecisionEventList()));
        }

        final ListIterator<Steuerdaten> steuerdatenListIterator = gesuchFormular.getSteuerdaten().stream().sorted(
            Comparator.comparing(Steuerdaten::getSteuerdatenTyp)
        ).toList().listIterator();

        List<FamilienBudgetresultatDto> familienBudgetresultatList = new ArrayList<>();
        while (steuerdatenListIterator.hasNext()) {
            final Steuerdaten steuerdaten = steuerdatenListIterator.next();
            familienBudgetresultatList.add(
                familienBudgetresultatFromRequest(
                    stipendienBerechnungsRequestForVater,
                    stipendienCalculatedForVater,
                    steuerdaten.getSteuerdatenTyp(),
                    steuerdatenListIterator.nextIndex(),
                    majorVersion,
                    minorVersion
                )
            );
        }

        return new BerechnungsresultatDto(
            berechnung,
            gesuchTranche.getGueltigkeit().getGueltigAb(),
            gesuchTranche.getGueltigkeit().getGueltigBis(),
            persoenlichesBudgetresultatFromRequest(
                stipendienBerechnungsRequestForVater,
                stipendienCalculatedForVater,
                majorVersion,
                minorVersion
            ),
            familienBudgetresultatList
        );
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
