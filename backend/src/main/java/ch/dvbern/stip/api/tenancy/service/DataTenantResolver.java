package ch.dvbern.stip.api.tenancy.service;

import java.util.Objects;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@PersistenceUnitExtension
@RequestScoped
@RequiredArgsConstructor
public class DataTenantResolver implements TenantResolver {

    private final RoutingContext context;

    @Override
    public String getDefaultTenantId() {
        return OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
    }

    @Override
    public String resolveTenantId() {
        String tenantId =
            context.get(TENANT_IDENTIFIER_CONTEXT_NAME); // tenant identifier already set by OIDC tenant resolver
        return Objects.requireNonNullElse(tenantId, DEFAULT_TENANT_IDENTIFIER);
    }
}
