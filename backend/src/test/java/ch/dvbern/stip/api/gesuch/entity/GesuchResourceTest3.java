package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.AdresseDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.UUID;

import static ch.dvbern.stip.api.util.TestConstants.GUELTIGKEIT_PERIODE_23_24;
import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNull;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchResourceTest3 {
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final BenutzerApiSpec benutzerApiSpec = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final String geschwisterNameUpdateTest = "UPDATEDGeschwister";
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;

    private UUID adresseId;
    private AdresseDtoSpec piaAdresse;
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
    @Order(5)
    void testUpdateGesuchEinnahmenKostenWithDefaultValues2() {//steuerjahr, veranlagungscode überprüfen
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(2028);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(99);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse().setId(adresseId);

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
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
        adresseId = gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse().getId();
        piaAdresse = gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse();
        //todo: check steuerjahr default wert too
    }


    @Test
    @TestAsGesuchsteller
    @Order(6)
    void testUpdateGesuchEinnahmeKostenVermoegen(){
        LocalDate ageUnder18 = LocalDate.now().minusYears(5);
        LocalDate ageAbove18 = LocalDate.now().minusYears(20);
        //under age 18
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setAdresse(piaAdresse);
        //gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getAdresse().setId(adresseId);

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(ageUnder18);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().setPersonInAusbildung(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung());

        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
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
        assertNull(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getVermoegen());

        //above age 18
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(GesuchTestSpecGenerator.gesuchUpdateDtoSpecPersonInAusbildung.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().getGeburtsdatum().minusYears(18));
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(ageAbove18);

        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()
            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertNotNull(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getVermoegen());
    }
    //todo: test steuerjahr

}
