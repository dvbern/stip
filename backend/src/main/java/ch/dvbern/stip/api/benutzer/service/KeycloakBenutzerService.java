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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLContext;

import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import com.github.javaparser.utils.Log;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.keycloak.admin.client.common.runtime.KeycloakAdminClientConfig;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.BadRequestException;
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
@UnlessBuildProfile("test")
@RequiredArgsConstructor
@Slf4j
public class KeycloakBenutzerService {
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
            throw new IllegalStateException("Could not inject KC Admin client config");
        }

        // TODO: When we upgrade to a quarkus version using hibernate 7 we will be able to use the newer keycloak
        // admin client which will allow us to easily configure the truststore and secure the connection
        return KeycloakBuilder.builder()
            .clientId(config.clientId())
            .clientSecret(config.clientSecret().orElse(null))
            .grantType(config.grantType().asString())
            .username(config.username().orElse(null))
            .password(config.password().orElse(null))
            .realm(config.realm())
            .serverUrl(config.serverUrl().orElse(null))
            .scope(config.scope().orElse(null))
            .resteasyClient(ClientBuilderWrapper.create(SSLContext.getDefault(), true).build())
            .build();
    }

    public String createKeycloakBenutzer(
        final String vorname,
        final String nachname,
        final String eMail,
        final List<String> roles
    ) {
        if (!OidcConstants.POSSIBLE_USER_ROLES.containsAll(roles)) {
            throw new BadRequestException(String.format("Cannot create keycloak benutzer with role %s", roles));
        }

        var userRep = new UserRepresentation();
        userRep.setEnabled(true);
        userRep.setFirstName(vorname);
        userRep.setLastName(nachname);
        userRep.setUsername(eMail);
        userRep.setEmail(eMail);
        userRep.setEmailVerified(true);

        final var keycloakUsersResource = keycloak.realm(tenantService.getCurrentTenant().getIdentifier()).users();

        try (
            Response response = keycloakUsersResource.create(userRep)
        ) {
            response.bufferEntity();
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                Log.error("Failed to create Keycloak user");
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
                        roleRepresentation -> roles.contains(roleRepresentation.getName())
                    )
                    .toList()
            );
            try {
                keycloakUserResource.roles().realmLevel().add(rolesToAddList);
            } catch (Exception e) {
                deleteKeycloakBenutzer(keycloakUserId);
                throw new WebApplicationException(
                    "Failed to assign roles to newly created Keylcoak user, Deleted the user", e
                );
            }

            return keycloakUserId;
        }
    }

    public void updateKeycloakBenutzer(
        final String keycloakId,
        final String vorname,
        final String nachname,
        final List<String> roles
    ) {
        final var keycloakUsersResource = keycloak.realm(tenantService.getCurrentTenant().getIdentifier()).users();

        var userRep = new UserRepresentation();
        userRep.setFirstName(vorname);
        userRep.setLastName(nachname);

        final var keycloakUserResource = keycloakUsersResource.get(keycloakId);

        if (Objects.nonNull(roles)) {
            var rolesToRemoveList = new ArrayList<>(
                keycloakUserResource.roles()
                    .realmLevel()
                    .listAll()
                    .stream()
                    .filter(roleRepresentation -> !roles.contains(roleRepresentation.getName()))
                    .toList()
            );

            var rolesToAddList = new ArrayList<>(
                keycloakUserResource.roles()
                    .realmLevel()
                    .listAvailable()
                    .stream()
                    .filter(
                        roleRepresentation -> roles.contains(roleRepresentation.getName())
                    )
                    .toList()
            );
            keycloakUserResource.roles().realmLevel().remove(rolesToRemoveList);
            keycloakUserResource.roles().realmLevel().add(rolesToAddList);
        }

        keycloakUserResource.update(userRep);

    }

    public void deleteKeycloakBenutzer(
        final String keycloakId
    ) {
        final var keycloakUsersResource = keycloak.realm(tenantService.getCurrentTenant().getIdentifier()).users();
        try (
            Response response = keycloakUsersResource.delete(keycloakId);
        ) {
            response.bufferEntity();
            if (response.getStatus() != Status.NO_CONTENT.getStatusCode()) {
                Log.error(String.format("Failed to delete Keycloak user with id: %s", keycloakId));
                throw new WebApplicationException(response);
            }
        }
    }
}
