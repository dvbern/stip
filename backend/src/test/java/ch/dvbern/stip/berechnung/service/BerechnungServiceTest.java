package ch.dvbern.stip.berechnung.service;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class BerechnungServiceTest {
    @Inject
    BerechnungService berechnungService;

    @BeforeAll
    static void mocks() {
        final var mock = Mockito.mock(TenantService.class);
        Mockito.when(mock.getCurrentTenant()).thenReturn(new TenantInfoDto().identifier("bern"));
        QuarkusMock.installMockForType(mock, TenantService.class);
    }

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
        "5, 39751",   // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "6, 27179",
        "7, 6669",
        "8, 266"  ,    // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "9, 23527"
    })
    void testBerechnungFaelle(final int fall, final int expectedStipendien) {
        final var result = berechnungService.calculateStipendien(BerechnungUtil.getRequest(fall));
        assertThat(result.getStipendien(), is(expectedStipendien));
    }
}
