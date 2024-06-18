package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.generator.api.GesuchTestSpecGenerator;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.api.BenutzerApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuchTranche;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchEinnahmenKostenVeranlagungscodeTest {
    public final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    public final BenutzerApiSpec benutzerApiSpec = BenutzerApiSpec.benutzer(RequestSpecUtil.quarkusSpec());
    public final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final String geschwisterNameUpdateTest = "UPDATEDGeschwister";
    private UUID gesuchId;
    private GesuchDtoSpec gesuch;

    private UUID piaAdresseId;
    private UUID partnerAdresseId;

    @Inject
    GesuchService gesuchService;

    @Inject
    GesuchTrancheMapper gesuchTrancheMapper;

    @Inject
    ElternMapper elternMapper;

    @Inject
    LebenslaufItemMapper lebenslaufItemMapper;

    @InjectMock
    GesuchRepository gesuchRepository;

    static final String TENANT_ID = "bern";

    @Test
    //@TestAsGesuchsteller
    @TestAsSachbearbeiter
    @Order(3)
    void gesuchEinreichenTest() {
        GesuchTranche tranche = initTrancheFromGesuchUpdate(GesuchGenerator.createFullGesuch());
        tranche.getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang().setBildungsart(new Bildungsart()));

        when(gesuchRepository.requireById(any())).thenReturn(tranche.getGesuch());
        when(gesuchRepository.findGesucheBySvNummer(any())).thenReturn(Stream.of(tranche.getGesuch()));

        tranche.getGesuchFormular().setTranche(tranche);
        tranche.getGesuch().setGesuchDokuments(
            Arrays.stream(DokumentTyp.values())
                .map(x -> new GesuchDokument().setDokumentTyp(x).setGesuch(tranche.getGesuch()))
                .toList()
        );

        gesuchService.gesuchEinreichen(tranche.getGesuch().getId());

        assertThat(
            tranche.getGesuch().getGesuchStatus(),
            Matchers.is(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
        );
    }

    @Test
    @TestAsSachbearbeiter
    @Order(3)
    void testUpdateGesuchEinnahmenKostenSteuerjahrNullValue(){

        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setSteuerjahr(null);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());

        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId);

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()

            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        Integer vorjahrGesuchsjahr = gesuch.getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() - 1;
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getSteuerjahr(), is(vorjahrGesuchsjahr));


    }

    @Test
    @TestAsSachbearbeiter
    @Order(4)
    void testUpdateGesuchEinnahmenKostenVeranlagungscodeSB(){
        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(99);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId);

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()

            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getSteuerjahr(), is(99));
    }

    @Test
    @TestAsGesuchsteller
    @Order(5)
    void testUpdateGesuchEinnahmenKostenVeranlagungscodeGS(){

        var gesuchUpdateDTO = GesuchTestSpecGenerator.gesuchUpdateDtoSpecEinnahmenKosten;
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().setVeranlagungsCode(99);
        gesuchUpdateDTO.getGesuchTrancheToWorkWith().setId(gesuch.getGesuchTrancheToWorkWith().getId());
        gesuchApiSpec.gesuchEinreichen().gesuchIdPath(gesuchId);

        gesuchApiSpec.updateGesuch().gesuchIdPath(gesuchId).body(gesuchUpdateDTO).execute(ResponseBody::prettyPeek)
            .then()
            .assertThat()
            .statusCode(Status.ACCEPTED.getStatusCode());
        gesuch = gesuchApiSpec.getGesuch().gesuchIdPath(gesuchId).execute(ResponseBody::prettyPeek)
            .then()

            .extract()
            .body()
            .as(GesuchDtoSpec.class);
        assertThat(gesuch.getGesuchTrancheToWorkWith().getGesuchFormular().getEinnahmenKosten().getSteuerjahr(), is(0));
    }



    private GesuchTranche initTrancheFromGesuchUpdate(GesuchUpdateDto gesuchUpdateDto) {
        GesuchTranche tranche = prepareGesuchTrancheWithIds(gesuchUpdateDto.getGesuchTrancheToWorkWith());
        return gesuchTrancheMapper.partialUpdate(gesuchUpdateDto.getGesuchTrancheToWorkWith(), tranche);
    }


    private GesuchTranche prepareGesuchTrancheWithIds(GesuchTrancheUpdateDto trancheUpdate) {
        GesuchTranche tranche = initGesuchTranche();
        GesuchFormular gesuchFormular = new GesuchFormular();

        trancheUpdate.getGesuchFormular().getElterns().forEach(elternUpdateDto -> {
            elternUpdateDto.setId(UUID.randomUUID());
            gesuchFormular.getElterns().add(elternMapper.partialUpdate(elternUpdateDto, new Eltern()));
        });

        trancheUpdate.getGesuchFormular().getLebenslaufItems().forEach(item -> {
            item.setId(UUID.randomUUID());
            gesuchFormular.getLebenslaufItems().add(lebenslaufItemMapper.partialUpdate(item, new LebenslaufItem()));
        });

        return tranche.setGesuchFormular(gesuchFormular);
    }


}
