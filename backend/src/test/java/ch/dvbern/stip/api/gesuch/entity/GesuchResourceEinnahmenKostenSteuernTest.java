package ch.dvbern.stip.api.gesuch.entity;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchResourceEinnahmenKostenSteuernTest {
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());

    private static UUID gesuchId;
    private static GesuchDtoSpec gesuch;

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testCreateEndpoint() {
        final var gesuch = TestUtil.createGesuchAusbildungFall(fallApiSpec, ausbildungApiSpec, gesuchApiSpec);
        gesuchId = gesuch.getId();
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void gesuchTrancheCreated() {
        gesuch = gesuchApiSpec.getCurrentGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);

        assertThat(gesuch.getGesuchTrancheToWorkWith(), notNullValue());
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigAb(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigAb()));
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGueltigBis(), is(GUELTIGKEIT_PERIODE_23_24.getGueltigBis()));
    }


    @Test
    @TestAsGesuchsteller
    @Order(3)
    void testUpdateGesuchEinnahmenKostenSteuern(){
        //total income is under 20 000
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setPersonInAusbildung(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung()
                .getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getPersonInAusbildung()
            );
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setPartner(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPartner()
                .getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getPartner()
            );

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setNettoerwerbseinkommen(10);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner().setJahreseinkommen(0);

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getCurrentGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getSteuernKantonGemeinde(), is(0));
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void deleteGesuch() {
        TestUtil.deleteGesuch(gesuchApiSpec, gesuchId);
    }
}
