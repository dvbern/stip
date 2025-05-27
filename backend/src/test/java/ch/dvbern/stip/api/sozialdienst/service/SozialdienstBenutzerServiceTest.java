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

package ch.dvbern.stip.api.sozialdienst.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsSozialdienstAdmin;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstAdminMapper;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerMapper;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.StepwiseExtension.AlwaysRun;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import io.quarkus.keycloak.admin.client.common.runtime.KeycloakAdminClientConfig;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class SozialdienstBenutzerServiceTest {
    SozialdienstService sozialdienstService;

    @Inject
    SozialdienstBenutzerRepository sozialdienstBenutzerRepository;

    @Inject
    SozialdienstRepository sozialdienstRepository;

    @Inject
    SozialdienstAdminMapper sozialdienstAdminMapper;

    @Inject
    SozialdienstBenutzerMapper sozialdienstBenutzerMapper;

    @Inject
    TenantService tenantService;

    @Inject
    MailService mailService;

    @Inject
    BenutzerService benutzerService;

    @Inject
    SozialdienstMapper sozialdienstMapper;

    @Inject
    KeycloakAdminClientConfig keycloakAdminClientConfigRuntimeValue;

    SozialdienstBenutzerService sozialdienstBenutzerService;
    SozialdienstBenutzerService sozialdienstBenutzerServiceMock;

    SozialdienstDto sozialdienstDto;

    private static final String VALID_IBAN_1 = "CH5089144653587876648";

    @Order(1)
    @TestAsSozialdienstAdmin
    @Test
    void init() throws NoSuchAlgorithmException {
        sozialdienstBenutzerService = new SozialdienstBenutzerService(
            null, sozialdienstBenutzerRepository, sozialdienstRepository, sozialdienstAdminMapper,
            sozialdienstBenutzerMapper,
            mailService, tenantService, keycloakAdminClientConfigRuntimeValue
        );

        sozialdienstBenutzerServiceMock = Mockito.spy(sozialdienstBenutzerService);
        final var mockKecloakAdminClient = Mockito.mock(Keycloak.class);
        final var mockKcRealmResource = Mockito.mock(RealmResource.class);
        final var mockKcUsersResource = Mockito.mock(UsersResource.class);
        final var mockKcUserResource = Mockito.mock(UserResource.class);
        final var mockKcRoleMappingResource = Mockito.mock(RoleMappingResource.class);
        final var mockKcRoleScopeResource = Mockito.mock(RoleScopeResource.class);

        Mockito.when(mockKecloakAdminClient.realm(ArgumentMatchers.any())).thenReturn(mockKcRealmResource);
        Mockito.when(mockKcRealmResource.users()).thenReturn(mockKcUsersResource);

        var mockCreatedResponse = Mockito.mock(Response.class);
        Mockito.when(mockCreatedResponse.getStatus()).thenReturn(Status.CREATED.getStatusCode());
        Mockito.when(mockCreatedResponse.getHeaderString("location"))
            .thenReturn("https://localhost:8080/sozialdienst/" + UUID.randomUUID().toString());

        Mockito.when(mockKcUsersResource.create(ArgumentMatchers.any())).thenReturn(mockCreatedResponse);
        Mockito.when(mockKcUsersResource.get(ArgumentMatchers.any())).thenReturn(mockKcUserResource);

        Mockito.when(mockKcUserResource.roles()).thenReturn(mockKcRoleMappingResource);
        Mockito.when(mockKcRoleMappingResource.realmLevel()).thenReturn(mockKcRoleScopeResource);
        Mockito.when(mockKcRoleScopeResource.listAvailable()).thenReturn(List.of());

        var mockDeletedResource = Mockito.mock(Response.class);
        Mockito.when(mockDeletedResource.getStatus()).thenReturn(Status.NO_CONTENT.getStatusCode());
        Mockito.when(mockKcUsersResource.delete(ArgumentMatchers.any())).thenReturn(mockDeletedResource);

        Mockito.when(sozialdienstBenutzerServiceMock.initKeycloak()).thenReturn(mockKecloakAdminClient);
        sozialdienstBenutzerServiceMock.setup();
        sozialdienstService = new SozialdienstService(
            benutzerService, sozialdienstRepository, sozialdienstMapper, sozialdienstBenutzerServiceMock
        );
    }

    @Order(2)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void createSozialdienst() {
        var sozialdienstCreateDto = new SozialdienstCreateDto();

        sozialdienstCreateDto.setName("a");
        sozialdienstCreateDto.setIban(VALID_IBAN_1);
        var sdAdresse = new AdresseDto();
        sdAdresse.setStrasse("Musterstrasse");
        sdAdresse.setPlz("12345");
        sdAdresse.setOrt("Musterort");
        sdAdresse.setHausnummer("1");
        sdAdresse.setLandId(TestConstants.TEST_LAND_SCHWEIZ_ID);
        sozialdienstCreateDto.setAdresse(sdAdresse);

        var sozialdienstAdminDto = new SozialdienstAdminDto();
        sozialdienstAdminDto.setKeycloakId(UUID.randomUUID().toString());
        sozialdienstAdminDto.setVorname("a");
        sozialdienstAdminDto.setNachname("b");
        sozialdienstAdminDto.setEmail("a@b.ch");
        sozialdienstCreateDto.setSozialdienstAdmin(sozialdienstAdminDto);

        sozialdienstDto = sozialdienstService.createSozialdienst(sozialdienstCreateDto);

        assertThat(sozialdienstDto.getId(), notNullValue());
    }

    @Order(3)
    @Transactional
    @TestAsAdmin
    @Test
    void replaceSozialdienstAdmin() {
        SozialdienstAdminDto sozialdienstAdminDto = new SozialdienstAdminDto();
        sozialdienstAdminDto.setKeycloakId(UUID.randomUUID().toString());
        sozialdienstAdminDto.setVorname("c");
        sozialdienstAdminDto.setNachname("d");
        sozialdienstAdminDto.setEmail("c@d.ch");
        sozialdienstService.replaceSozialdienstAdmin(sozialdienstDto.getId(), sozialdienstAdminDto);

        sozialdienstDto = sozialdienstService.getSozialdienstById(sozialdienstDto.getId());
        assertThat(sozialdienstDto.getSozialdienstAdmin().getVorname(), equalTo("c"));
        assertThat(sozialdienstDto.getSozialdienstAdmin().getNachname(), equalTo("d"));
        assertThat(sozialdienstDto.getSozialdienstAdmin().getEmail(), equalTo("c@d.ch"));
    }

    @Order(4)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void getSozialdienstBenutzerEmptyTest() {
        final var sozialdienstbenutzers = sozialdienstBenutzerServiceMock
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        assertThat(sozialdienstbenutzers.size(), equalTo(0));
    }

    @Order(5)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void createSozialdienstBenutzerTest() {
        SozialdienstBenutzerCreateDto createDto = new SozialdienstBenutzerCreateDto();
        var name = "replaced";
        var email = "fabrice.jakob@dvbern.ch";

        createDto.setVorname(name);
        createDto.setNachname(name);
        createDto.setEmail(email);
        createDto.setRedirectUri("");

        var sozialdienstbenutzer = sozialdienstBenutzerServiceMock
            .createSozialdienstBenutzer(sozialdienstRepository.requireById(sozialdienstDto.getId()), createDto);

        assertThat(sozialdienstbenutzer.getVorname(), equalTo(name));
        assertThat(sozialdienstbenutzer.getNachname(), equalTo(name));
        assertThat(sozialdienstbenutzer.getEmail(), equalTo(email));
    }

    @Order(6)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void getSozialdienstBenutzerTest() {
        final var sozialdienstbenutzers = sozialdienstBenutzerServiceMock
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        assertThat(sozialdienstbenutzers.size(), equalTo(1));
    }

    @Order(7)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void updateSozialdienstBenutzerTest() {
        final var sozialdienstbenutzers = sozialdienstBenutzerServiceMock
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        SozialdienstBenutzerUpdateDto updateDto = new SozialdienstBenutzerUpdateDto();
        var newname = "replaced2";

        updateDto.setId(sozialdienstbenutzers.get(0).getId());
        updateDto.setVorname(newname);
        updateDto.setNachname(newname);

        final var sozialdienstbenutzer = sozialdienstBenutzerServiceMock.updateSozialdienstBenutzer(updateDto);

        assertThat(sozialdienstbenutzer.getVorname(), equalTo(newname));
        assertThat(sozialdienstbenutzer.getNachname(), equalTo(newname));
    }

    @Order(8)
    @Transactional
    @TestAsSozialdienstAdmin
    @Test
    void deleteSozialdienstBenutzerTest() {
        var sozialdienstbenutzers = sozialdienstBenutzerServiceMock
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        sozialdienstBenutzerServiceMock.deleteSozialdienstBenutzer(sozialdienstbenutzers.get(0).getId());

        sozialdienstbenutzers = sozialdienstBenutzerServiceMock
            .getSozialdienstBenutzers(sozialdienstRepository.requireById(sozialdienstDto.getId()));

        assertThat(sozialdienstbenutzers.size(), equalTo(0));
    }

    @Order(99)
    @Transactional
    @TestAsAdmin
    @Test
    @AlwaysRun
    void deleteSozialdienst() {
        sozialdienstService.deleteSozialdienst(sozialdienstDto.getId());

        assertThrows(NotFoundException.class, () -> sozialdienstService.getSozialdienstById(sozialdienstDto.getId()));
    }

}
