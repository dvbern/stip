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
    GesuchsperiodeWithDatenDto createGesuchsperiode(@Valid @NotNull GesuchsperiodeCreateDto gesuchsperiodeCreateDto);

    @DELETE
    @Path("/{gesuchsperiodeId}")
    @Produces({ "text/plain" })
    void deleteGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @GET
    @Path("/{gesuchsperiodeId}")
    @Produces({ "application/json", "text/plain" })
    GesuchsperiodeWithDatenDto getGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<GesuchsperiodeDto> getGesuchsperioden();

    @GET
    @Path("/latest")
    @Produces({ "application/json", "text/plain" })
    NullableGesuchsperiodeWithDatenDto getLatest();

    @PATCH
    @Path("/publish/{gesuchsperiodeId}")
    @Produces({ "application/json" })
    GesuchsperiodeWithDatenDto publishGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId);

    @PATCH
    @Path("/{gesuchsperiodeId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchsperiodeWithDatenDto updateGesuchsperiode(@PathParam("gesuchsperiodeId") UUID gesuchsperiodeId,@Valid GesuchsperiodeUpdateDto gesuchsperiodeUpdateDto);
}
