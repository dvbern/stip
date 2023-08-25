package ch.dvbern.stip.test.tenancy.resource;

import ch.dvbern.oss.stip.contract.test.api.TenantApiSpec;
import ch.dvbern.oss.stip.contract.test.dto.TenantInfoDtoSpec;
import ch.dvbern.stip.test.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.test.util.RequestSpecUtil;
import ch.dvbern.stip.test.util.TestDatabaseEnvironment;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ResponseBody;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.MalformedURLException;
import java.net.URL;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantResourceTest {

    @ConfigProperty(name = "keycloak.url")
    String keycloakUrlString;

    private final TenantApiSpec api = TenantApiSpec.tenant(RequestSpecUtil.quarkusSpec());

    @Test
    @TestAsGesuchsteller
    void test_get_current() throws MalformedURLException {
        final var tenant = DEFAULT_TENANT_IDENTIFIER;
        final var tenantInfo = api.getCurrentTenant()
                .execute(ResponseBody::prettyPeek)
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
