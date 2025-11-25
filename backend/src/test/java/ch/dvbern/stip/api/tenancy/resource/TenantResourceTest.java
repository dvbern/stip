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

package ch.dvbern.stip.api.tenancy.resource;

import java.net.MalformedURLException;
import java.net.URL;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.TenantApiSpec;
import ch.dvbern.stip.generated.dto.TenantInfoDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantResourceTest {

    private final TenantApiSpec api = TenantApiSpec.tenant(RequestSpecUtil.quarkusSpec());
    @ConfigProperty(name = "keycloak.url")
    String keycloakUrlString;

    @Test
    @TestAsGesuchsteller
    void test_get_current() throws MalformedURLException {
        final var tenant = DEFAULT_TENANT_IDENTIFIER;
        final var tenantInfo = api.getCurrentTenant()
            .execute(TestUtil.PEEK_IF_ENV_SET)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(TenantInfoDtoSpec.class);

        assertThat(tenantInfo.getIdentifier()).isEqualTo(tenant);

        final var keycloakUrl = new URL(keycloakUrlString);

        assertThat(new URL(tenantInfo.getClientAuth().getAuthServerUrl()))
            .isEqualToWithSortedQueryParameters(keycloakUrl);
    }
}
