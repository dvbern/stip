package ch.dvbern.stip.api.stammdaten.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
@QuarkusTest
class StammdatenResourceImplTest {
    @Inject
    StammdatenResourceImpl stammdatenResource;

    @TestAsGesuchsteller
    @Test
    void getLaenderasGS() {
        assertEquals(HttpStatus.SC_OK,stammdatenResource.getLaender().getStatus());

    }

    @TestAsSachbearbeiter
    @Test
    void getLaenderasBS() {
        assertThrows(ForbiddenException.class, () ->stammdatenResource.getLaender());
    }
}
