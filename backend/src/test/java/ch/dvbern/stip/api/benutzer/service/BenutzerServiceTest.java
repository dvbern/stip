/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.benutzer.service;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.repo.BenutzerRepository;
import ch.dvbern.stip.api.benutzer.util.TestAsDeleteUser;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiterAdmin;
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
    @TestAsSachbearbeiterAdmin
    @Order(2)
    void testDeleteBenutzer() {
        benutzerService.deleteBenutzer(benutzerToDeleteKeycloakId);
        assertThat(benutzerRepository.findByIdOptional(benutzerToDeleteId)).isNotPresent();
    }
}
