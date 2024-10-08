package ch.dvbern.stip.api.tenancy.resource;

import ch.dvbern.stip.api.common.authorization.AllowAll;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.api.TenantResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class TenantResourceImpl implements TenantResource {

    private final TenantService tenantService;

    @Override
    @AllowAll
    public Response getCurrentTenant() {
        var currentTenant = tenantService.getCurrentTenant();
        return Response.ok(currentTenant).build();
    }
}
