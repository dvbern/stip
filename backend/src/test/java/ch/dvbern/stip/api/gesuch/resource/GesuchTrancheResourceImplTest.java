package ch.dvbern.stip.api.gesuch.resource;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wiremock.org.eclipse.jetty.http.HttpStatus;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GesuchTrancheResourceImplTest {
    @InjectMock
    GesuchRepository gesuchRepository;
    @InjectMock
    GesuchTrancheRepository gesuchTrancheRepository;
    @Inject
    GesuchTrancheResourceImpl gesuchTrancheResource;

    private Gesuch gesuch;
    private CreateAenderungsantragRequestDto dto;

    @BeforeEach
    void setup(){
        gesuch = GesuchGenerator.initGesuch();
        Adresse adresse = new Adresse().setPlz("3011").setLand(Land.CH).setOrt("Bern").setStrasse("Musterstrasse").setHausnummer("1");
        Ausbildungsgang ausbildungsgang = new Ausbildungsgang();
        ausbildungsgang.setBildungskategorie(new Bildungskategorie().setBfs(5));
        gesuch.getCurrentGesuchTranche().setGesuchFormular(new GesuchFormular());
        gesuch.getCurrentGesuchTranche().getGesuchFormular().setFamiliensituation(new Familiensituation()
            .setElternVerheiratetZusammen(true));
        gesuch.getCurrentGesuchTranche().getGesuchFormular().setAuszahlung(new Auszahlung()
            .setKontoinhaber(Kontoinhaber.GESUCHSTELLER));
        gesuch.getCurrentGesuchTranche().getGesuchFormular().setPersonInAusbildung((PersonInAusbildung) new PersonInAusbildung()
            .setAdresse(adresse)
            .setGeburtsdatum(LocalDate.now().minusYears(10)));
        gesuch.getCurrentGesuchTranche().getGesuchFormular().setAusbildung(new Ausbildung()
            .setAusbildungsgang(ausbildungsgang)
            .setAusbildungBegin(gesuch.getNewestGesuchTranche().get().getGueltigkeit().getGueltigAb())
            .setAusbildungEnd(gesuch.getNewestGesuchTranche().get().getGueltigkeit().getGueltigBis()));
        gesuch.getCurrentGesuchTranche().getGesuchFormular().setEinnahmenKosten(new EinnahmenKosten().setNettoerwerbseinkommen(0));

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.findForGesuch(any())).thenReturn(gesuch.getGesuchTranchen().stream());

        dto = new CreateAenderungsantragRequestDto();
        dto.setComment("");
        dto.setStart(gesuch.getNewestGesuchTranche().get().getGueltigkeit().getGueltigAb());
        dto.setEnd(gesuch.getNewestGesuchTranche().get().getGueltigkeit().getGueltigBis());
    }

    @TestAsGesuchsteller
    @Test
    void onlyOneAenderungShouldBeAllowed(){
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        // act
        Response response = gesuchTrancheResource.createAenderungsantrag(gesuch.getId(),dto);
        // assert
        assertEquals(HttpStatus.FORBIDDEN_403,response.getStatus());
    }
    @TestAsGesuchsteller
    @Test
    void onlyOneAenderungShouldBeAllowed_Tranche(){
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        // act
        Response response = gesuchTrancheResource.createAenderungsantrag(gesuch.getId(),dto);
        // assert
        assertNotEquals(HttpStatus.FORBIDDEN_403,response.getStatus());
    }
    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAbgelehnt(){
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.ABGELEHNT);
        // act
        Response response = gesuchTrancheResource.createAenderungsantrag(gesuch.getId(),dto);
        // assert
        assertNotEquals(HttpStatus.FORBIDDEN_403,response.getStatus());
    }
    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAbgelehnt_Tranche(){
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.ABGELEHNT);
        // act
        Response response = gesuchTrancheResource.createAenderungsantrag(gesuch.getId(),dto);
        // assert
        assertNotEquals(HttpStatus.FORBIDDEN_403,response.getStatus());
    }
    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAngenommen(){
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // act
        Response response = gesuchTrancheResource.createAenderungsantrag(gesuch.getId(),dto);
        // assert
        assertNotEquals(HttpStatus.FORBIDDEN_403,response.getStatus());
    }
    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAngenommen_Tranche(){
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // act
        Response response = gesuchTrancheResource.createAenderungsantrag(gesuch.getId(),dto);
        // assert
        assertNotEquals(HttpStatus.FORBIDDEN_403,response.getStatus());
    }
}
