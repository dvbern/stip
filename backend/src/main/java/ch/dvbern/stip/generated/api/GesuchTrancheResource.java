package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuch/{gesuchId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchTrancheResource {

    @POST
    @Path("/aenderungsantrag")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createAenderungsantrag(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull CreateAenderungsantragRequestDto createAenderungsantragRequestDto);

    @POST
    @Path("/tranche/{trancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createGesuchTrancheCopy(@PathParam("gesuchId") UUID gesuchId,@PathParam("trancheId") UUID trancheId,@Valid CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto);

    @GET
    @Path("/aenderungsantrag")
    @Produces({ "application/json", "text/plain" })
    Response getAenderungsantrag(@PathParam("gesuchId") UUID gesuchId);
}
