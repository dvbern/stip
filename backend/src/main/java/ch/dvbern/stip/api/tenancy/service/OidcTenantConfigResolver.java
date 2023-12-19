package ch.dvbern.stip.api.tenancy.service;

import io.quarkus.oidc.OidcRequestContext;
import io.quarkus.oidc.OidcTenantConfig;
import io.quarkus.oidc.TenantConfigResolver;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.function.Supplier;

import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.DEFAULT_TENANT_IDENTIFIER;
import static ch.dvbern.stip.api.tenancy.service.OidcTenantResolver.TENANT_IDENTIFIER_CONTEXT_NAME;

@ApplicationScoped
@RequiredArgsConstructor
public class OidcTenantConfigResolver implements TenantConfigResolver {

    @ConfigProperty(name = "keycloak.url")
    String keycloakUrl;

    @ConfigProperty(name = "keycloak.frontend-url")
    String keycloakFrontendUrl;

    @ConfigProperty(name = "keycloak.client-id")
    String keycloakClientId;

    @Override
    public Uni<OidcTenantConfig> resolve(RoutingContext context, OidcRequestContext<OidcTenantConfig> requestContext) {
        String tenantId = DEFAULT_TENANT_IDENTIFIER; // TODO: add routing context based resolver

        context.put(TENANT_IDENTIFIER_CONTEXT_NAME, tenantId);

        return Uni.createFrom().item(getTenantConfig(tenantId));
    }

    public Supplier<OidcTenantConfig> getTenantConfig(String identifier) {
        final var config = new OidcTenantConfig();

        config.setTenantId(identifier);
        config.setAuthServerUrl(getTenantAuthServer(identifier));
        config.setClientId(keycloakClientId);

        return () -> config;

    }

    public String getAuthServerFrontendUrl() {
        return keycloakFrontendUrl;
    }

    private String getTenantAuthServer(String tenantId) {
        return keycloakUrl + "/realms/" + tenantId;
    }
}
