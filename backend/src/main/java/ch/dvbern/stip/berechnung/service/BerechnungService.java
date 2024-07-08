package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.util.DmnRequestContextUtil;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.NotFoundException;
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

    public BerechnungsresultatDto getBerechnungsResultatFromGesuch(final Gesuch gesuch) throws JsonProcessingException {
        final int majorVersionToUse = 1;
        final int minorVersionToUse = 0;
        final var gesuchTranche = gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new);
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        final var personenImHaushaltRequestForVater = personenImHaushaltService.getPersonenImHaushaltRequest(
            majorVersionToUse,
            minorVersionToUse,
            gesuchFormular,
            ElternTyp.VATER
        );
        final var personenImHaushaltRequestForMutter = personenImHaushaltService.getPersonenImHaushaltRequest(
            majorVersionToUse,
            minorVersionToUse,
            gesuchFormular,
            ElternTyp.MUTTER
        );
        final var personenImHaushaltForVater = personenImHaushaltService.calculatePersonenImHaushalt(personenImHaushaltRequestForVater);
        final var personenImHaushaltForMutter = personenImHaushaltService.calculatePersonenImHaushalt(personenImHaushaltRequestForMutter);
        if (personenImHaushaltForVater.getNoBudgetsRequired() != personenImHaushaltForMutter.getNoBudgetsRequired()) {
            throw new IllegalStateException("noBudgetsRequired do not match between parents");
        }
        final var noBudgetsRequired = personenImHaushaltForVater.getNoBudgetsRequired();

        final var stipendienCalculatedForVater = calculateStipendien(
            getBerechnungRequest(
                majorVersionToUse,
                minorVersionToUse,
                gesuch,
                gesuchTranche,
                ElternTyp.VATER
            )
        );
        final var stipendienCalculatedForMutter = calculateStipendien(
            getBerechnungRequest(
                majorVersionToUse,
                minorVersionToUse,
                gesuch,
                gesuchTranche,
                ElternTyp.VATER
            )
        );
        var berechnung = stipendienCalculatedForVater.getStipendien();
        if (noBudgetsRequired > 1) {
            BigDecimal kinderProzenteVater = BigDecimal.ZERO;
            BigDecimal kinderProzenteMutter = BigDecimal.ZERO;
            int noKinderOhneEigenenHaushalt = 0;
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

            final var geschwisters = gesuchTranche.getGesuchFormular().getGeschwisters();
            for (final var geschwister : geschwisters) {
                if (geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                    kinderProzenteVater = kinderProzenteVater.add(geschwister.getWohnsitzAnteilVater());
                    kinderProzenteMutter = kinderProzenteMutter.add(geschwister.getWohnsitzAnteilMutter());
                    noKinderOhneEigenenHaushalt += 1;
                }
            }
            if (noKinderOhneEigenenHaushalt > 0) {
                kinderProzenteVater = kinderProzenteVater.divide(BigDecimal.valueOf(noKinderOhneEigenenHaushalt));
                kinderProzenteMutter = kinderProzenteMutter.divide(BigDecimal.valueOf(noKinderOhneEigenenHaushalt));

                berechnung =
                    kinderProzenteVater.multiply(BigDecimal.valueOf(stipendienCalculatedForVater.getStipendien())
                        .divide(BigDecimal.valueOf(100))).intValue()
                        + kinderProzenteMutter.multiply(BigDecimal.valueOf(stipendienCalculatedForMutter.getStipendien())
                        .divide(BigDecimal.valueOf(100))).intValue();
            }
        }

        final var factory = Json.createBuilderFactory(null);
        JsonObjectBuilder baseObjectBuilder = factory.createObjectBuilder();
        baseObjectBuilder.add("vaterBerechnungsDaten", decisionEventListToJSON(stipendienCalculatedForVater.getDecisionEventList()));
        baseObjectBuilder.add("mutterBerechnungsDaten", decisionEventListToJSON(stipendienCalculatedForMutter.getDecisionEventList()));

        return new BerechnungsresultatDto(berechnung, baseObjectBuilder.build());
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
