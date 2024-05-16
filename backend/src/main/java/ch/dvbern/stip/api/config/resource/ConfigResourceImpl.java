package ch.dvbern.stip.api.config.resource;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.generated.api.ConfigurationResource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class ConfigResourceImpl implements ConfigurationResource {

    private final ConfigService configService;

    @Override
    public Response getDeploymentConfig() {
        return Response.ok(configService.getDeploymentConfiguration()).build();
    }
}
