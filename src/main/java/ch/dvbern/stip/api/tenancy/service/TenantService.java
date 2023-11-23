package ch.dvbern.stip.api.tenancy.service;


import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantService {

    private final RoutingContext context;
    private final OidcTenantConfigResolver oidcConfigResolver;

    public TenantInfoDto getCurrentTenant() {
        final String tenantId = context.get(TENANT_IDENTIFIER_CONTEXT_NAME);

        final TenantInfoDto tenantInfoDto = new TenantInfoDto();
        tenantInfoDto.identifier(tenantId);

        final TenantAuthConfigDto tenantAuthConfig = new TenantAuthConfigDto();
        tenantAuthConfig.setAuthServerUrl(oidcConfigResolver.getAuthServerFrontendUrl());
        tenantAuthConfig.setRealm(tenantId);
        tenantInfoDto.setClientAuth(tenantAuthConfig);

        return tenantInfoDto;
    }
}
