package ch.dvbern.stip.api.plz.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("")
public interface GeoCollectionDowloadService {

    @Path("")
    @GET
    byte[] getGeoCollectionDowload();
}
