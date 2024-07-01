package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.util.DmnRequestContextUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;
import org.kie.api.io.Resource;

@ApplicationScoped
@RequiredArgsConstructor
public class BerechnungService {
    private static final List<String>
        BERECHNUNG_MODEL_NAMES = List.of("Stipendium", "Familienbudget", "PersoenlichesBudget");
    private static final String STIPENDIUM_DECISION_NAME = "Stipendium";

    private final Instance<BerechnungRequestBuilder> berechnungRequests;
    private final DMNService dmnService;
    private final TenantService tenantService;

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
        List<List<Resource>> modelsList = new ArrayList<>(3);
        for (var modelName : BERECHNUNG_MODEL_NAMES) {
            modelsList.add(
                dmnService.loadModelsForTenantAndVersionByName(
                    tenantService.getCurrentTenant().getIdentifier(),
                    request.getVersion(),
                    modelName
                )
            );
        }
        final var models = modelsList.stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        final var result = dmnService.evaluateModel(models, DmnRequestContextUtil.toContext(request));
        final var stipendien = (BigDecimal) result.getDecisionResultByName(STIPENDIUM_DECISION_NAME).getResult();
        if (stipendien == null) {
            throw new AppErrorException("Result of Stipendienberechnung was null!");
        }

        return new BerechnungResult(stipendien.intValue(), result.getDecisionResults());
    }
}
