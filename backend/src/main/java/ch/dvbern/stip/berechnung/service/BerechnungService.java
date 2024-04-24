package ch.dvbern.stip.berechnung.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.berechnung.dto.BerechnungModelVersion;
import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import ch.dvbern.stip.berechnung.dto.BerechnungRequestBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BerechnungService {
    private final Instance<BerechnungRequestBuilder> berechnungRequests;

    public BerechnungRequest getBerechnungRequest(final int version, final Gesuch gesuch) {
        final var builder = berechnungRequests.stream().filter(x -> {
            final var annotation = x.getClass().getAnnotation(BerechnungModelVersion.class);
            if (annotation != null && annotation.value() == version) {
                return true;
            }

            return false;
        }).findFirst();

        if (builder.isEmpty()) {
            throw new IllegalArgumentException("Cannot find a builder for version " + version);
        }

        return builder.get().buildRequest(gesuch);
    }
}
