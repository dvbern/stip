package ch.dvbern.stip.api.tenancy.service;

import ch.dvbern.stip.api.common.type.MandantIdentifier;
import io.quarkus.oidc.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OidcTenantResolver implements TenantResolver {

    public static final String DEFAULT_TENANT_IDENTIFIER = MandantIdentifier.BERN.name().toLowerCase();
    public static final String TENANT_IDENTIFIER_CONTEXT_NAME = "tenantId";

    @Override
    @SuppressWarnings("java:S1135")
    public String resolve(RoutingContext context) {

        final var tenant = DEFAULT_TENANT_IDENTIFIER;

        // TODO KSTIP-781: resolve tenant based on request and implement a test for it and remove SupressWarning
        context.put(TENANT_IDENTIFIER_CONTEXT_NAME, tenant);

        return tenant;
    }
}
