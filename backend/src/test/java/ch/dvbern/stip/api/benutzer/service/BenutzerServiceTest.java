package ch.dvbern.stip.api.benutzer.service;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsDeleteUser;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstAdmin;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
class BenutzerServiceTest {

    private final BenutzerService benutzerService;
    private final BenutzerRepository benutzerRepository;
    private UUID benutzerToDeleteId;
    private String benutzerToDeleteKeycloakId;

    @Test
    @TestAsDeleteUser
    @Order(1)
    void testCreateBenutzer() {
        benutzerToDeleteId = benutzerService.getOrCreateAndUpdateCurrentBenutzer().getId();
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByIdOptional(benutzerToDeleteId);
        assertThat(optionalBenutzer).isPresent();
        Benutzer benutzer = optionalBenutzer.get();
        benutzerToDeleteKeycloakId = benutzer.getKeycloakId();
        assertThat(benutzerToDeleteId).isEqualTo(benutzer.getId());
    }

    @Test
    @TestAsAdmin
    @Order(2)
    void testDeleteBenutzer() {
        benutzerService.deleteBenutzer(benutzerToDeleteKeycloakId);
        assertThat(benutzerRepository.findByIdOptional(benutzerToDeleteId)).isNotPresent();
    }

    @Order(3)
    @Test
    @TestAsAdmin
    void testCreateSozialdienstAdminBenutzer(){
        String keykloakId = UUID.randomUUID().toString();
        String email = "max@muster.com";
        String vorname = "Max";
        String nachname = "Muster";

        SozialdienstAdmin sozialdienstAdmin = new SozialdienstAdmin();
        sozialdienstAdmin.setEmail(email);
        sozialdienstAdmin.setVorname(vorname);
        sozialdienstAdmin.setNachname(nachname);
        sozialdienstAdmin.setKeykloakId(keykloakId);

        final var createdBenutzer = benutzerService.createSozialdienstAdminBenutzer(sozialdienstAdmin);
        final var benutzerId = createdBenutzer.getId();

        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByIdOptional(benutzerId);
        assertThat(optionalBenutzer).isPresent();
        Benutzer benutzer = optionalBenutzer.get();
        assertThat(benutzerId).isEqualTo(benutzer.getId());
    }
}
