package ch.dvbern.stip.test.tenancy.service;

import ch.dvbern.stip.api.tenancy.service.StipTenantResolver;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
public class TenantResolverTest {

    @Inject
    StipTenantResolver stipTenantResolver;

    @Test
    void test_get_default_tenant() {
        var tenantId = stipTenantResolver.resolve(null);
        assertThat(tenantId, Matchers.is(StipTenantResolver.DEFAULT_TENANT_IDENTIFIER));
    }
}
