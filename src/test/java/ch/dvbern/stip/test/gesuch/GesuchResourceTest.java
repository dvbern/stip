package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.generated.test.api.GesuchApiSpec;
import ch.dvbern.stip.generated.test.dto.*;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.utils.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GesuchResourceTest {

    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());

    private UUID gesuchId;

    @Test
    @Order(1)
    void testCreateEndpoint() {
        var gesuchDTO = new GesuchCreateDtoSpec();
        gesuchDTO.setFallId(UUID.fromString("4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b"));
        gesuchDTO.setGesuchsperiodeId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));

        gesuchApiSpec.createGesuch().body(gesuchDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    @Order(2)
    void testFindGesuchEndpoint() {
        var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(GesuchDtoSpec[].class);

        gesuchId = gesuche[0].getId();

        assertThat(gesuche.length, is(1));
        assertThat(gesuche[0].getFall().getId(), is(UUID.fromString("4b99f69f-ec53-4ef7-bd1f-0e76e04abe7b")));
        assertThat(gesuche[0].getGesuchsperiode().getId(), is(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")));
    }

    @Test
    @Order(3)
    void testUpdateGesuchPersonInAusbildungEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForPersonInAusbildung();


        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    @Order(4)
    void testUpdateGesuchAusbildungEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForAusbildung();


        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());
    }

    private GesuchUpdateDtoSpec prepareGesuchUpdateForAusbildung() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var ausbildung = new AusbildungUpdateDtoSpec();
        ausbildung.setAusbildungBegin("01.2022");
        ausbildung.setAusbildungEnd("02.2022");
        ausbildung.setAusbildungsland(AusbildungslandDtoSpec.SCHWEIZ);
        ausbildung.setAusbildungNichtGefunden(false);
        ausbildung.setPensum(AusbildungsPensumDtoSpec.VOLLZEIT);
        ausbildung.setAusbildungsgangId(UUID.fromString("3a8c2023-f29e-4466-a2d7-411a7d032f42"));
        ausbildung.setAusbildungstaetteId(UUID.fromString("9477487f-3ac4-4d02-b57c-e0cefb292ae5"));
        ausbildung.setFachrichtung("test");
        gesuchformularToWorkWith.setAusbildung(ausbildung);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    // Not working, cannot override mapping in collection with mapstruct
    //@Test
    //@Order(4)
    void testUpdateGesuchAddLebenslaufEndpoint() {
        var gesuchUpdatDTO = prepareGesuchUpdateForLebenslauf();
        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdatDTO).execute(ResponseBody::prettyPeek)
                .then()
                .assertThat()
                .statusCode(Response.Status.ACCEPTED.getStatusCode());

        var gesuche = gesuchApiSpec.getGesuche().execute(ResponseBody::prettyPeek)
                .then()
                .extract()
                .body()
                .as(GesuchDtoSpec[].class);

        gesuchId = gesuche[0].getId();

        assertThat(gesuche.length, is(1));
        assertThat(gesuche[0].getGesuchFormularToWorkWith().getPersonInAusbildung(), is(notNullValue()));
        assertThat(gesuche[0].getGesuchFormularToWorkWith().getLebenslaufItems().size(), is(1));
    }

    private GesuchUpdateDtoSpec prepareGesuchUpdateForLebenslauf() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var lebenslaufItem = new LebenslaufItemUpdateDtoSpec();
        lebenslaufItem.setBeschreibung("Test");
        lebenslaufItem.setBis("02.2022");
        lebenslaufItem.setVon("01.2022");
        lebenslaufItem.setTaetigskeitsart(TaetigskeitsartDtoSpec.ERWERBSTAETIGKEIT);
        lebenslaufItem.setBildungsart(BildungsartDtoSpec.FACHHOCHSCHULEN);
        lebenslaufItem.setWohnsitz(WohnsitzKantonDtoSpec.BE);

        gesuchformularToWorkWith.getLebenslaufItems().add(lebenslaufItem);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    private GesuchUpdateDtoSpec prepareGesuchUpdateForPersonInAusbildung() {
        var gesuchUpdatDTO = new GesuchUpdateDtoSpec();
        var gesuchformularToWorkWith = new GesuchFormularUpdateDtoSpec();
        var personInAusbildung = new PersonInAusbildungUpdateDtoSpec();
        var adresseDTO = prepareAdresseUpdate();
        personInAusbildung.setAdresse(adresseDTO);
        personInAusbildung.setAnrede(AnredeDtoSpec.HERR);
        personInAusbildung.setEmail("test@test.ch");
        personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 10, 10));
        personInAusbildung.setNachname("Tester");
        personInAusbildung.setVorname("Prosper");
        personInAusbildung.setNationalitaet(LandDtoSpec.CH);
        personInAusbildung.setTelefonnummer("078 888 88 88");
        personInAusbildung.setDigitaleKommunikation(true);
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(true);
        personInAusbildung.setKorrespondenzSprache(SpracheDtoSpec.DEUTSCH);
        personInAusbildung.setSozialhilfebeitraege(false);
        personInAusbildung.setZivilstand(ZivilstandDtoSpec.LEDIG);
        personInAusbildung.setSozialversicherungsnummer("756.0000.0000.05");
        personInAusbildung.setQuellenbesteuert(false);
        personInAusbildung.setWohnsitz(WohnsitzDtoSpec.ELTERN);

        gesuchformularToWorkWith.setPersonInAusbildung(personInAusbildung);
        gesuchUpdatDTO.setGesuchFormularToWorkWith(gesuchformularToWorkWith);
        return gesuchUpdatDTO;
    }

    private AdresseDtoSpec prepareAdresseUpdate() {
        var adresseDTO = new AdresseDtoSpec();
        adresseDTO.setLand(LandDtoSpec.CH);
        adresseDTO.setPlz("3000");
        adresseDTO.setOrt("Bern");
        adresseDTO.setStrasse("Strasse");
        return adresseDTO;
    }
}
