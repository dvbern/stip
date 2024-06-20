package ch.dvbern.stip.api.benutzer.service;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsDeleteUser;
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
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public class BenutzerServiceTest {

    private final BenutzerService benutzerService;
    private final BenutzerRepository benutzerRepository;
    private UUID benutzerToDeleteId;

    @Test
    @TestAsDeleteUser
    @Order(1)
    public void testCreateBenutzer() {
        benutzerToDeleteId = benutzerService.getOrCreateAndUpdateCurrentBenutzer().getId();
        Optional<Benutzer> optionalBenutzer = benutzerRepository.findByIdOptional(benutzerToDeleteId);
        assertThat(optionalBenutzer).isPresent();
        Benutzer benutzer = optionalBenutzer.get();
        assertThat(benutzerToDeleteId).isEqualTo(benutzer.getId());
    }

    @Test
    @TestAsAdmin
    @Order(2)
    public void testDeleteBenutzer() {
        benutzerService.deleteBenutzer(benutzerToDeleteId);
        assertThat(benutzerRepository.findByIdOptional(benutzerToDeleteId).isPresent()).isFalse();
    }
}
