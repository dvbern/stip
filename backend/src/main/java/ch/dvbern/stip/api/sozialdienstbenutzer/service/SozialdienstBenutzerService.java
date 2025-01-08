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

package ch.dvbern.stip.api.sozialdienstbenutzer.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.net.ssl.SSLContext;

import ch.dvbern.stip.api.benutzer.type.BenutzerStatus;
import ch.dvbern.stip.api.benutzereinstellungen.entity.Benutzereinstellungen;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.repo.SozialdienstRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerUpdateDto;
import io.quarkus.keycloak.admin.client.common.KeycloakAdminClientConfig;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.ClientBuilderWrapper;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;

@RequestScoped
@RequiredArgsConstructor
@Slf4j
public class SozialdienstBenutzerService {
    private final SozialdienstBenutzerRepository sozialdienstBenutzerRepository;
    private final SozialdienstRepository sozialdienstRepository;
    private final SozialdienstAdminMapper sozialdienstAdminMapper;
    private final SozialdienstBenutzerMapper sozialdienstBenutzerMapper;
    private final TenantService tenantService;

    private final KeycloakAdminClientConfig keycloakAdminClientConfigRuntimeValue;

    private Keycloak keycloak = null;

    @PostConstruct
    public void setup() throws NoSuchAlgorithmException {
        keycloak = initKeycloak();
    }

    public Keycloak initKeycloak() throws NoSuchAlgorithmException {
        final KeycloakAdminClientConfig config = keycloakAdminClientConfigRuntimeValue;
        if (config == null) {
            return null;
        }

        return KeycloakBuilder.builder()
            .clientId(config.clientId)
            .clientSecret(config.clientSecret.orElse(null))
            .grantType(config.grantType.asString())
            .username(config.username.orElse(null))
            .password(config.password.orElse(null))
            .realm(config.realm)
            .serverUrl(config.serverUrl.orElse(null))
            .scope(config.scope.orElse(null))
            .resteasyClient(ClientBuilderWrapper.create(SSLContext.getDefault(), true).build()) // TODO: When we upgrade
                                                                                                // to a quarkus version
                                                                                                // using hibernate 7 we
                                                                                                // will be able to use
                                                                                                // the newer keycloak
                                                                                                // admin client which
                                                                                                // will allow us to
                                                                                                // easily configure the
                                                                                                // truststore and secure
                                                                                                // this connection
            .build();
    }

    @Transactional
    public SozialdienstBenutzer getSozialdienstBenutzerById(UUID id) {
        return sozialdienstBenutzerRepository.requireById(id);
    }

    @Transactional
    public SozialdienstBenutzerDto getSozialdienstBenutzerDtoById(UUID id) {
        return sozialdienstBenutzerMapper.toDto(getSozialdienstBenutzerById(id));
    }

    @Transactional
    public SozialdienstBenutzerDto updateSozialdienstAdminBenutzer(
        final UUID sozialdienstAdminId,
        SozialdienstAdminUpdateDto dto
    ) {
        var sozialdienstAdmin = sozialdienstBenutzerRepository.requireById(sozialdienstAdminId);
        sozialdienstAdminMapper.partialUpdate(dto, sozialdienstAdmin);
        return sozialdienstBenutzerMapper.toDto(sozialdienstAdmin);
    }

    @Transactional
    public SozialdienstBenutzer createSozialdienstAdminBenutzer(SozialdienstAdminDto dto) {
        final var sozialdienstAdmin = sozialdienstAdminMapper.toEntity(dto);
        sozialdienstAdmin.setBenutzereinstellungen(new Benutzereinstellungen());
        sozialdienstAdmin.setBenutzerStatus(BenutzerStatus.AKTIV);
        sozialdienstBenutzerRepository.persistAndFlush(sozialdienstAdmin);
        return sozialdienstAdmin;
    }

    @Transactional
    public void deleteSozialdienstAdminBenutzer(final String benutzerId) {
        final var sozialdienstAdmin =
            sozialdienstBenutzerRepository.findByKeycloakId(benutzerId).orElseThrow(NotFoundException::new);
        sozialdienstBenutzerRepository.delete(sozialdienstAdmin);
    }

    public List<SozialdienstBenutzerDto> getSozialdienstBenutzers(Sozialdienst sozialdienst) {
        return sozialdienst.getSozialdienstBenutzers()
            .stream()
            .map(
                sozialdienstBenutzerMapper::toDto
            )
            .toList();
    }

    @Transactional
    public SozialdienstBenutzerDto createSozialdienstBenutzer(
        final Sozialdienst sozialdienst,
        final SozialdienstBenutzerCreateDto sozialdienstBenutzerCreateDto
    ) {
        var userRep = new UserRepresentation();
        userRep.setEnabled(true);
        userRep.setFirstName(sozialdienstBenutzerCreateDto.getVorname());
        userRep.setLastName(sozialdienstBenutzerCreateDto.getNachname());
        userRep.setUsername(sozialdienstBenutzerCreateDto.getEmail());
        userRep.setEmail(sozialdienstBenutzerCreateDto.getEmail());
        userRep.setEmailVerified(true);

        final var keycloakUsersResource = keycloak.realm(tenantService.getCurrentTenant().getIdentifier()).users();

        try (
            Response response = keycloakUsersResource.create(userRep)
        ) {
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                throw new WebApplicationException(response);
            }
            final var locationHeaderParts = response.getHeaderString("location").split("/");
            final var keycloakUserId = locationHeaderParts[locationHeaderParts.length - 1];
            final var keycloakUserResource = keycloakUsersResource.get(keycloakUserId);
            var rolesToAddList = new ArrayList<>(
                keycloakUserResource.roles()
                    .realmLevel()
                    .listAvailable()
                    .stream()
                    .filter(
                        roleRepresentation -> Objects.equals(
                            roleRepresentation.getName(),
                            OidcConstants.ROLE_SOZIALDIENST_MITARBEITER
                        )
                    )
                    .toList()
            );
            keycloakUserResource.roles().realmLevel().add(rolesToAddList);
            final var sozialdienstBenutzer = sozialdienstBenutzerMapper.toEntity(sozialdienstBenutzerCreateDto);;
            sozialdienstBenutzer.setBenutzerStatus(BenutzerStatus.AKTIV);
            sozialdienstBenutzer.setBenutzereinstellungen(new Benutzereinstellungen());
            sozialdienstBenutzer.setKeycloakId(keycloakUserId);
            sozialdienstBenutzerRepository.persistAndFlush(sozialdienstBenutzer);
            sozialdienstRepository.requireById(sozialdienst.getId())
                .getSozialdienstBenutzers()
                .add(sozialdienstBenutzer);
            keycloakUserResource.executeActionsEmail(List.of(OidcConstants.REQUIRED_ACTION_UPDATE_PASSWORD));
            return sozialdienstBenutzerMapper.toDto(sozialdienstBenutzer);
        }
    }

    @Transactional
    public SozialdienstBenutzerDto updateSozialdienstBenutzer(
        final SozialdienstBenutzerUpdateDto sozialdienstBenutzerUpdateDto
    ) {
        SozialdienstBenutzer sozialdienstBenutzer =
            sozialdienstBenutzerRepository.requireById(sozialdienstBenutzerUpdateDto.getId());
        final var keycloakUsersResource = keycloak.realm(tenantService.getCurrentTenant().getIdentifier()).users();

        var userRep = new UserRepresentation();
        userRep.setFirstName(sozialdienstBenutzerUpdateDto.getVorname());
        userRep.setLastName(sozialdienstBenutzerUpdateDto.getNachname());
        userRep.setUsername(userRep.getEmail());
        userRep.setEmail(userRep.getEmail());
        userRep.setEmailVerified(true);
        keycloakUsersResource.get(sozialdienstBenutzer.getKeycloakId()).update(userRep);

        return sozialdienstBenutzerMapper
            .toDto(sozialdienstBenutzerMapper.partialUpdate(sozialdienstBenutzerUpdateDto, sozialdienstBenutzer));
    }

    @Transactional
    public void deleteSozialdienstBenutzer(
        final UUID sozialdienstBenutzerId
    ) {
        SozialdienstBenutzer sozialdienstBenutzer =
            sozialdienstBenutzerRepository.requireById(sozialdienstBenutzerId);
        final var keycloakUsersResource = keycloak.realm(tenantService.getCurrentTenant().getIdentifier()).users();
        try (
            Response response = keycloakUsersResource.delete(sozialdienstBenutzer.getKeycloakId());
        ) {
            if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
                throw new WebApplicationException(response);
            }
            var sozialdienst =
                sozialdienstBenutzerRepository.findSozialdienstBySozialdienstBenutzer(sozialdienstBenutzer)
                    .orElseThrow(NotFoundException::new);
            sozialdienst.getSozialdienstBenutzers().remove(sozialdienstBenutzer);
            sozialdienstBenutzerRepository.delete(sozialdienstBenutzer);
        }
    }
}
