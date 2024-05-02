package ch.dvbern.stip.api.tenancy.service;

import java.util.Objects;

import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@PersistenceUnitExtension
@ApplicationScoped
@UnlessBuildProfile("test")
@RequiredArgsConstructor
public class DataTenantResolver implements TenantResolver {
    private static final ThreadLocal<String> EXPLICIT_TENANT_ID = new ThreadLocal<>();

    private final Instance<RoutingContext> context;

    public static void setTenantId(String tenantId) {
        if (tenantId == null) {
            EXPLICIT_TENANT_ID.remove();
        }
        else {
            EXPLICIT_TENANT_ID.set(tenantId);
        }
    }

    @Override
    public String getDefaultTenantId() {
        return OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
    }

    @Override
    public String resolveTenantId() {
        // tenant identifier already set by OIDC tenant resolver
        if (context.isResolvable() && EXPLICIT_TENANT_ID.get() == null) {
            String tenantId = context.get().get(TENANT_IDENTIFIER_CONTEXT_NAME);
            return Objects.requireNonNullElse(tenantId, DEFAULT_TENANT_IDENTIFIER);
        } else {
            return EXPLICIT_TENANT_ID.get();
        }
    }
}
