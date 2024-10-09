package ch.dvbern.stip.api.gesuch.entity;

import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNull;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchResourceEinnahmeKostenTest {
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final BenutzerApiSpec benutzerApiSpec = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final String geschwisterNameUpdateTest = "UPDATEDGeschwister";
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;

    private UUID adresseId;
    private UUID partnerAdresseId;

    void createGesuch() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(UUID.fromString(TestConstants.FALL_TEST_ID));
        gesuchDTO.setGesuchsperiodeId(TestConstants.TEST_GESUCHSPERIODE_ID);
        var response = gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
            .then();

        response.assertThat()
            .statusCode(Status.CREATED.getStatusCode());

        gesuchId = TestUtil.extractIdFromResponse(response);
    }


    void createTranche() {
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
    void testUpdateGesuchEmptyEinnahmenKosten() {
        createGesuch();
        createTranche();

        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung();
        gesuchUpdateDTO.getGesuchTrancheToWorkWith()
            .getGesuchFormular()
            .setPartner(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPartner()
                .getGesuchTrancheToWorkWith()
                .getGesuchFormular()
                .getPartner()
            );

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setEinnahmenKosten(null);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getCurrentGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        adresseId = gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse().getId();
        partnerAdresseId = gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner().getAdresse().getId();
        assertNull(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten());
    }
}
