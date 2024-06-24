package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.berechnung.dto.BerechnungModelVersion;
import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import ch.dvbern.stip.berechnung.dto.BerechnungResult;
import ch.dvbern.stip.berechnung.util.BerechnungRequestContextUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BerechnungService {
    private static final String STIPENDIUM_DECISION_NAME = "Stipendium";

    private final Instance<BerechnungRequestBuilder> berechnungRequests;
    private final DMNService dmnService;
    private final TenantService tenantService;

    public BerechnungRequest getBerechnungRequest(
        final int majorVersion,
        final int minorVersion,
        final Gesuch gesuch,
        final GesuchTranche gesuchTranche,
        final ElternTyp elternTyp
    ) {
        final var builder = berechnungRequests.stream().filter(berechnungRequestBuilder -> {
            final var versionAnnotation = berechnungRequestBuilder.getClass().getAnnotation(BerechnungModelVersion.class);
            return (versionAnnotation != null) &&
                (versionAnnotation.major() == majorVersion) &&
                (versionAnnotation.minor() == minorVersion);
        }).findFirst();

        if (builder.isEmpty()) {
            throw new IllegalArgumentException("Cannot find a builder for version " + majorVersion + '.' + minorVersion);
        }

        return builder.get().buildRequest(gesuch, gesuchTranche, elternTyp);
    }

    public BerechnungResult calculateStipendien(final BerechnungRequest request) {
        final var models = dmnService.loadModelsForTenantAndVersion(
            tenantService.getCurrentTenant().getIdentifier(),
            request.getVersion()
        );

        final var result = dmnService.evaluateModel(models, BerechnungRequestContextUtil.toContext(request));
        final var stipendien = (BigDecimal) result.getDecisionResultByName(STIPENDIUM_DECISION_NAME).getResult();
        if (stipendien == null) {
            throw new AppErrorException("Result of Stipendienberechnung was null!");
        }

        return new BerechnungResult(stipendien.intValue(), result.getDecisionResults());
    }
}
