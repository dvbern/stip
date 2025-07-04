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

package ch.dvbern.stip.api;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.KeycloakBenutzerService;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import io.quarkus.keycloak.admin.client.common.runtime.KeycloakAdminClientConfig;
import io.quarkus.test.Mock;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Mock
@RequestScoped
public class KeycloakBenutzerServiceMock extends KeycloakBenutzerService {
    private static final HashSet<String> USED_KC_IDS = new HashSet<>();

    public KeycloakBenutzerServiceMock() {
        super(null, null);
    }

    @Inject
    public KeycloakBenutzerServiceMock(
    TenantService tenantService,
    KeycloakAdminClientConfig keycloakAdminClientConfigRuntimeValue
    ) {
        super(tenantService, keycloakAdminClientConfigRuntimeValue);
    }

    @Override
    @PostConstruct
    public void setup() throws NoSuchAlgorithmException {}

    @Override
    public String createKeycloakBenutzer(
        final String vorname,
        final String nachname,
        final String eMail,
        final List<String> roles
    ) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void updateKeycloakBenutzer(
        final String keycloakId,
        final String vorname,
        final String nachname,
        final List<String> roles
    ) {}

    @Override
    public void deleteKeycloakBenutzer(
        final String keycloakId
    ) {}
}
