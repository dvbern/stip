package ch.dvbern.stip.api.tenancy.service;

import java.util.Objects;

import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@PersistenceUnitExtension
@RequestScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class DataTenantResolver implements TenantResolver {
    private final RoutingContext context;

    @Override
    public String getDefaultTenantId() {
        return OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
    }

    @Override
    public String resolveTenantId() {
        // tenant identifier already set by OIDC tenant resolver
        String tenantId = context.get(TENANT_IDENTIFIER_CONTEXT_NAME);
        return Objects.requireNonNullElse(tenantId, DEFAULT_TENANT_IDENTIFIER);
    }
}
