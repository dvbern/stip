package ch.dvbern.stip.stammdaten;

import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.shared.enums.Land;
import ch.dvbern.stip.stammdaten.dto.LandDTO;
import jakarta.annotation.security.DenyAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.stream.Stream;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("stammdaten")
@Tag(name = "Stammdaten")
@ApiResource
@DenyAll
public class StammdatenResource {

    @GET
    @Path("/lands")
    @Operation(
            summary = "Returniert eine List von LandDTO.")
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema( type = SchemaType.ARRAY, implementation = LandDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getAlleLaender(
    ) {
        return Response.ok(Stream.of(Land.values()).map(land -> LandDTO.from(land)).toList()).build();
    }
}
