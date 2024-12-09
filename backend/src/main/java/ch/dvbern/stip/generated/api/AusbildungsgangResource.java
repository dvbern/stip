package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/ausbildungsgang")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungsgangResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungsgangDto createAusbildungsgang(@Valid @NotNull AusbildungsgangCreateDto ausbildungsgangCreateDto);

    @DELETE
    @Path("/{ausbildungsgangId}")
    @Produces({ "text/plain" })
    void deleteAusbildungsgang(@PathParam("ausbildungsgangId") UUID ausbildungsgangId);

    @GET
    @Path("/{ausbildungsgangId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungsgangDto getAusbildungsgang(@PathParam("ausbildungsgangId") UUID ausbildungsgangId);

    @PUT
    @Path("/{ausbildungsgangId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungsgangDto updateAusbildungsgang(@PathParam("ausbildungsgangId") UUID ausbildungsgangId,@Valid @NotNull AusbildungsgangUpdateDto ausbildungsgangUpdateDto);
}
