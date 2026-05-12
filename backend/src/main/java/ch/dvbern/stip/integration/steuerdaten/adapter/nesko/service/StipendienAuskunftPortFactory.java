package ch.dvbern.stip.integration.steuerdaten.adapter.nesko.service;

import ch.dvbern.stip.api.config.type.AdapterConfig;
import ch.dvbern.stip.api.config.type.TenantConfig;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.StipendienAuskunftPort;
import ch.dvbern.stip.integration.steuerdaten.adapter.nesko.generated.stipendienauskunftservice.StipendienAuskunftService;
import ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.MessageContext;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class StipendienAuskunftPortFactory {

    @Inject
    @RestClient
    NeskoGetBearerTokenRequestService neskoGetBearerTokenRequestService;

    public StipendienAuskunftPort create(AdapterConfig.SteuerdatenAdapter config) {
        StipendienAuskunftService stipendienAuskunftService =
            new StipendienAuskunftService(toUrl(config.url().orElseThrow()));

        Map<String, List<String>> headers = new HashMap<>();
        headers.put("authorization", Collections.singletonList("Bearer " + getToken(config)));
        var port = stipendienAuskunftService.getStipendienAuskunft();
        ((BindingProvider) port).getRequestContext()
            .put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        return port;
    }

    public String getToken(AdapterConfig.SteuerdatenAdapter config) {
        return neskoGetBearerTokenRequestService.post(
            neskoGetBearerTokenRequestService
                .getAuthorization(config.username().orElseThrow(), config.password().orElseThrow()),
            neskoGetBearerTokenRequestService.getGrantType()
        ).getAccessToken();
    }

    private static URL toUrl(String url) {
        try {
            return URI.create(url).toURL();
        } catch (MalformedURLException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
