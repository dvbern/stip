package ch.dvbern.stip.api.tenancy.service;

import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockTenantService extends TenantService {
    public MockTenantService() {
        super(null);
    }

    @Override
    public TenantInfoDto getCurrentTenant() {
        return new TenantInfoDto().identifier("bern");
    }
}
