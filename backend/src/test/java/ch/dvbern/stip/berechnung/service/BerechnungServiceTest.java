package ch.dvbern.stip.berechnung.service;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @TestAsGesuchsteller
    @ParameterizedTest
    @CsvSource({
        "1, 6427",
        "2, 14192",
        "3, 0",
        "4, 17986",
        "5, 39751", // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "6, 27179",
        "7, 6669",
        "8, 266",   // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "9, 23527"
    })
    void testBerechnungFaelle(final int fall, final int expectedStipendien) {
        // Load Fall resources/berechnung/fall_{fall}.json, deserialize to a BerechnungRequestV1
        // and calculate Stipendien for it
        final var result = berechnungService.calculateStipendien(BerechnungUtil.getRequest(fall));
        assertThat(result.getStipendien(), is(expectedStipendien));
    }
}
