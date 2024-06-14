package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.api.model.gesuch.AdresseSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.*;

import static ch.dvbern.stip.api.util.TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG_2;
import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchResourceTest2 {
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final BenutzerApiSpec benutzerApiSpec = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final String geschwisterNameUpdateTest = "UPDATEDGeschwister";
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;

    private UUID adresseId;
    private UUID partnerAdresseId;
    @Test
    @TestAsGesuchsteller
    @Order(1)
    void testCreateEndpoint() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(UUID.fromString(TestConstants.FALL_TEST_ID));
        gesuchDTO.setGesuchsperiodeId(TestConstants.GESUCHSPERIODE_TEST_ID);
        var response = gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
            .then();

        response.assertThat()
            .statusCode(Status.CREATED.getStatusCode());

        gesuchId = TestUtil.extractIdFromResponse(response);
    }

    @Test
    @TestAsGesuchsteller
    @Order(2)
    void gesuchTrancheCreated() {
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
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
    void testUpdateGesuchEmptyEinnahmenKosten() {
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setPartner(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPartner.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner());

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setEinnahmenKosten(null);
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        adresseId = gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse().getId();
        partnerAdresseId = gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPartner().getAdresse().getId();
        assertNull(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten());
    }

    @Test
    @TestAsGesuchsteller
    @Order(4)
    void testUpdateGesuchEinnahmenKostenWithDefaultValues1() {//steuerjahr, veranlagungscode überprüfen
        //6da21e1f-91b3-4fb9-b438-3db41c0280bd
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(null);

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse().setId(adresseId);


        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertNotNull(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten());
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getVeranlagungsCode(), is(0));
        //todo: check steuerjahr default wert too
    }
}
