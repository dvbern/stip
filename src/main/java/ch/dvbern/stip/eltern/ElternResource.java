package ch.dvbern.stip.eltern;

import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.eltern.dto.ElternContainerDTO;
import ch.dvbern.stip.eltern.service.ElternService;
import jakarta.annotation.security.DenyAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("eltern")
@Tag(name ="eltern")
@ApiResource
@DenyAll
public class ElternResource {

    @Inject
    private ElternService elternService;

    @PUT
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = ElternContainerDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveElternContainer(
            ElternContainerDTO elternContainerDTO
    ){
        // TODO
        return Response.ok().build();
    }
}

