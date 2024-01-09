package ch.dvbern.stip.api.tenancy.service;

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Objects;

@ApplicationScoped
public class OidcTenantResolver implements TenantResolver {

    public static final String DEFAULT_TENANT_IDENTIFIER = MandantIdentifier.BERN.name().toLowerCase();
    public static final String TENANT_IDENTIFIER_CONTEXT_NAME = "tenantId";

    @Override
    public String resolve(RoutingContext context) {

        final var tenant = DEFAULT_TENANT_IDENTIFIER;

        // TODO: resolve tenant based on request
        context.put(TENANT_IDENTIFIER_CONTEXT_NAME, tenant);

        return tenant;
    }
}
