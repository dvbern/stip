package ch.dvbern.stip.api.plz.service;

import ch.dvbern.stip.api.plz.entity.GeoCollectionItem;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/items")
@RegisterRestClient(configKey = "GeoCollection-api")
public interface GeoCollectionService {

    @GET
    GeoCollectionItem get();
}
