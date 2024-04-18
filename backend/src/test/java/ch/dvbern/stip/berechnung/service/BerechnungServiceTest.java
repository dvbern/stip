package ch.dvbern.stip.berechnung.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
@RequiredArgsConstructor
class BerechnungServiceTest {
    private final BerechnungService berechnungService;

    @Test
    void getV1Test() {
        final var gesuch = new Gesuch().setGesuchsperiode(new Gesuchsperiode()
            .setEinkommensfreibetrag(6000)
        );

        final var request = berechnungService.getBerechnungRequest(1, gesuch);
        assertThat(request, is(not(nullValue())));
    }

    @Test
    void getNonExistentTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            berechnungService.getBerechnungRequest(-1, null);
        });
    }
}
