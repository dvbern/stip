package ch.dvbern.stip.berechnung.service;

import java.util.UUID;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.util.TestUtil;
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
        final var gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());

        final var request = berechnungService.getBerechnungRequest(
            1,
            0,
            gesuch,
            gesuch.getNewestGesuchTranche().orElseThrow(),
            ElternTyp.VATER
        );
        assertThat(request, is(not(nullValue())));
    }

    @Test
    void getNonExistentTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            berechnungService.getBerechnungRequest(
                -1,
                0,
                null,
                null,
                ElternTyp.MUTTER
            );
        });
    }
}
