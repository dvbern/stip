package ch.dvbern.stip.api.tenancy.service;

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StipTenantResolver implements TenantResolver {

    public static final String DEFAULT_TENANT_IDENTIFIER = MandantIdentifier.BERN.name();
    public static final String TENANT_IDENTIFIER_CONTEXT_NAME = "tenantId";

    @Override
    public String resolve(RoutingContext context) {
        String tenantId = DEFAULT_TENANT_IDENTIFIER;
        context.put(TENANT_IDENTIFIER_CONTEXT_NAME, tenantId);

        return tenantId;
    }
}
