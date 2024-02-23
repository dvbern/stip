package ch.dvbern.stip.api.tenancy.service;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockDataTenantResolver extends DataTenantResolver{
    public MockDataTenantResolver() {
        super(null);
    }

    @Override
    public String resolveTenantId() {
        return getDefaultTenantId();
    }
}
