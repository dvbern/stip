package ch.dvbern.stip.test.ausbildung;

import ch.dvbern.stip.api.ausbildung.type.Ausbildungsland;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungsstaetteRepository;
import ch.dvbern.stip.generated.test.api.AusbildungsstaetteApiSpec;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AusbildungsstaeteResourceTest {


    private final AusbildungsstaetteRepository ausbildungsstaetteRepository;

    public final AusbildungsstaetteApiSpec api = AusbildungsstaetteApiSpec.ausbildungsstaette(RequestSpecUtil.quarkusSpec());

    @BeforeAll
    @Transactional
    public void setup() {

        Ausbildungsstaette entity = new Ausbildungsstaette();
        entity.setName("Test");
        entity.setAusbildungsland(Ausbildungsland.SCHWEIZ);

        ausbildungsstaetteRepository.persist(entity);
    }

    @Test
    public void test_get_ausbildungsstaetten() {
        var res = api.getAusbildungsstaetten().execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(Ausbildungsstaette[].class);

        assertThat(res.length, greaterThanOrEqualTo(1));
    }

}
