package ch.dvbern.stip.test.tenancy.service;

import ch.dvbern.stip.api.tenancy.service.OidcTenantResolver;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class TenantResolverTest {

    @Inject
    OidcTenantResolver oidcTenantResolver;

    @Test
    void test_get_default_tenant() {
        var tenantId = oidcTenantResolver.resolve(null);
        assertThat(tenantId, Matchers.is(OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER));
    }
}
