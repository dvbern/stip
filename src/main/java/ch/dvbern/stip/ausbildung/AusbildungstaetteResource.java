package ch.dvbern.stip.ausbildung;


import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.ausbildung.service.AusbildungService;
import jakarta.annotation.security.DenyAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("ausbildungstaette")
@Tag(name ="Ausbildungstaette")
@ApiResource
@DenyAll
public class AusbildungstaetteResource {

    @Inject
    private AusbildungService ausbildungService;

    @GET
    @Path("/all")
    @Operation(
            summary = "Returniert alle Ausbildungstaette zur Verfuegung.")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getAlleAusbildungstaette(
    ) {
        return Response.ok(ausbildungService.findAlleAusbildungstaetteDTO()).build();
    }
}
