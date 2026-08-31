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

import java.util.Objects;

import ch.dvbern.stip.api.config.type.TenantConfig.KeycloakAdminClientConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

@RequestScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminClient {
    private final TenantService tenantService;

    private Keycloak keycloak = null;

    Keycloak getKeycloak() {
        final KeycloakAdminClientConfig keycloakAdminClientConfig =
            tenantService.getConfigForCurrentTenant().keycloakAdminClientConfig();
        if (Objects.isNull(keycloak)) {
            keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakAdminClientConfig.serverUrl())
                .realm(tenantService.getCurrentStringIdentifier())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakAdminClientConfig.clientId())
                .clientSecret(keycloakAdminClientConfig.clientSecret())
                .build();
        }
        return keycloak;
    }
}
