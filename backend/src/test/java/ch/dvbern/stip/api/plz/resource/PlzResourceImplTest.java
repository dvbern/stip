package ch.dvbern.stip.api.plz.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.generated.api.PlzResource;
import io.quarkus.security.ForbiddenException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class PlzResourceImplTest {
    @Inject
    PlzResource plzResource;

    @TestAsGesuchsteller
    @Test
    void getPlzAsGS() {
        assertEquals(HttpStatus.SC_OK,plzResource.getPlz().getStatus());
    }

    @TestAsSachbearbeiter
    @Test
    void getPlzAsSB() {
        assertThrows(ForbiddenException.class, () ->plzResource.getPlz());

    }
}
