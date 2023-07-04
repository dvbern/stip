package ch.dvbern.stip.test.ausbildung;

import ch.dvbern.stip.api.ausbildung.type.Ausbildungsland;
import ch.dvbern.stip.api.ausbildung.repo.AusbildungstaetteRepository;
import ch.dvbern.stip.generated.test.api.AusbildungstaetteApiSpec;
import ch.dvbern.stip.test.utils.TestDatabaseEnvironment;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungstaette;
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


    private final AusbildungstaetteRepository ausbildungstaetteRepository;

    public final AusbildungstaetteApiSpec api = AusbildungstaetteApiSpec.ausbildungstaette(RequestSpecUtil.quarkusSpec());

    @BeforeAll
    @Transactional
    public void setup() {

        Ausbildungstaette entity = new Ausbildungstaette();
        entity.setName("Test");
        entity.setAusbildungsland(Ausbildungsland.SCHWEIZ);

        ausbildungstaetteRepository.persist(entity);
    }

    @Test
    public void test_get_ausbildungsstaetten() {
        var res = api.getAusbildungstaetten().execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract()
                .as(Ausbildungstaette[].class);

        assertThat(res.length, greaterThanOrEqualTo(1));
    }

}
