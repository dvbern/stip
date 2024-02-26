package ch.dvbern.stip.api.tenancy.service;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
@PersistenceUnitExtension
public class MockDataTenantResolver extends DataTenantResolver{
    public MockDataTenantResolver() {
        super(null);
    }

    @Override
    public String resolveTenantId() {
        return getDefaultTenantId();
    }
}
