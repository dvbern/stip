package ch.dvbern.stip.api.tenancy.service;


import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantInfoDto;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@ApplicationScoped
@RequiredArgsConstructor
public class TenantService {

    private final RoutingContext context;

    @ConfigProperty(name = "keycloak.frontend-url")
    String keycloakFrontendUrl;

    public TenantInfoDto getCurrentTenant() {
        final String tenantId = context.get(TENANT_IDENTIFIER_CONTEXT_NAME);

        final TenantAuthConfigDto tenantAuthConfig = new TenantAuthConfigDto();
        tenantAuthConfig.setAuthServerUrl(keycloakFrontendUrl);
        tenantAuthConfig.setRealm(tenantId);

        return new TenantInfoDto()
            .identifier(tenantId)
            .clientAuth(tenantAuthConfig);
    }
}
