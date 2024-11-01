package ch.dvbern.stip.api.benutzer.service;

import ch.dvbern.stip.api.benutzer.entity.SozialdienstAdmin;
import ch.dvbern.stip.api.benutzer.repo.SozialdienstAdminRepository;
import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RequiredArgsConstructor
class SozialdienstAdminServiceTest {
    private final SozialdienstAdminRepository sozialdienstAdminRepository;
    private final SozialdienstAdminService sozialdienstAdminService;

    @Test
    @TestAsAdmin
    void testCreateSozialdienstAdminBenutzer(){
        String keykloakId = UUID.randomUUID().toString();
        String vorname = "Max";
        String nachname = "Muster";

        SozialdienstAdmin sozialdienstAdmin = new SozialdienstAdmin();
        sozialdienstAdmin.setVorname(vorname);
        sozialdienstAdmin.setNachname(nachname);
        sozialdienstAdmin.setKeycloakId(keykloakId);

        final var createdAdmin = sozialdienstAdminService.createSozialdienstAdminBenutzer(sozialdienstAdmin);
        final var adminId = createdAdmin.getId();

        Optional<SozialdienstAdmin> optionalBenutzer = sozialdienstAdminRepository.findByIdOptional(adminId);
        assertThat(optionalBenutzer).isPresent();
        SozialdienstAdmin admin = optionalBenutzer.get();
        assertThat(adminId).isEqualTo(admin.getId());
    }
}
