package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDto;
import ch.dvbern.stip.generated.dto.NullableGesuchsperiodeWithDatenDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuchsperiode")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchsperiodeResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createGesuchsperiode(@Valid @NotNull GesuchsperiodeCreateDto gesuchsperiodeCreateDto);

    @DELETE
    @Path("/{gesuchsperiodeId}")
    @Produces({ "text/plain" })
    Response deleteGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @GET
    @Path("/aktive")
    @Produces({ "application/json", "text/plain" })
    Response getAktiveGesuchsperioden();

    @GET
    @Path("/{gesuchsperiodeId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getGesuchsperioden();

    @GET
    @Path("/latest")
    @Produces({ "application/json", "text/plain" })
    Response getLatest();

    @PATCH
    @Path("/publish/{gesuchsperiodeId}")
    @Produces({ "application/json" })
    Response publishGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @PATCH
    @Path("/{gesuchsperiodeId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId,@Valid GesuchsperiodeUpdateDto gesuchsperiodeUpdateDto);
}
