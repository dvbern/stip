package ch.dvbern.stip.api.ausbildung.resource;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.model.gesuch.AusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.FallDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AusbildungResourceTest {
    private final AusbildungApiSpec ausbildungApiSpec =
        AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private FallDto fall;

    private AusbildungDto ausbildung;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void prepare() {
        fall = fallApiSpec.getFallForGs()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(FallDto.class);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void createAusbildung() {
        final var ausbildungUpdateDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungUpdateDtoSpec.setFallId(fall.getId());

        ausbildung = ausbildungApiSpec.createAusbildung()
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungDto.class);

    }

    @Test
    @TestAsGesuchsteller
    @Order(3)
    void getAusbildung() {
        ausbildungApiSpec.getAusbildung()
            .ausbildungIdPath(ausbildung.getId())
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void updateAusbildung() {
        final var ausbildungUpdateDtoSpec = AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec();
        ausbildungUpdateDtoSpec.setId(ausbildung.getId());
        ausbildungUpdateDtoSpec.setFallId(fall.getId());
        final String ausbildungsOrtToSet = "Bielefeld";

        ausbildungUpdateDtoSpec.setAusbildungsort(ausbildungsOrtToSet);
        final var updatedAusbildung = ausbildungApiSpec.updateAusbildung()
            .ausbildungIdPath(ausbildung.getId())
            .body(ausbildungUpdateDtoSpec)
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .body()
            .as(AusbildungDto.class);

        assertThat(updatedAusbildung.getAusbildungsort(), is(ausbildungsOrtToSet));
    }
}
