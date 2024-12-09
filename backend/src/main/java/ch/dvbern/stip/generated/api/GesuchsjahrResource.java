package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrDto;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuchsjahr")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchsjahrResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchsjahrDto createGesuchsjahr(@Valid @NotNull GesuchsjahrCreateDto gesuchsjahrCreateDto);

    @DELETE
    @Path("/{gesuchsjahrId}")
    @Produces({ "text/plain" })
    void deleteGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId);

    @GET
    @Path("/{gesuchsjahrId}")
    @Produces({ "application/json", "text/plain" })
    GesuchsjahrDto getGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<GesuchsjahrDto> getGesuchsjahre();

    @PATCH
    @Path("/publish/{gesuchsjahrId}")
    @Produces({ "application/json" })
    GesuchsjahrDto publishGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId);

    @PATCH
    @Path("/{gesuchsjahrId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchsjahrDto updateGesuchsjahr(@PathParam("gesuchsjahrId") UUID gesuchsjahrId,@Valid GesuchsjahrUpdateDto gesuchsjahrUpdateDto);
}
