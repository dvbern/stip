package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AuszahlungDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/auszahlung/{fallId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AuszahlungResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    UUID createAuszahlungForGesuch(@PathParam("fallId") UUID fallId,@Valid @NotNull AuszahlungDto auszahlungDto);

    @GET
    @Produces({ "application/json", "text/plain" })
    AuszahlungDto getAuszahlungForGesuch(@PathParam("fallId") UUID fallId);

    @PATCH
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AuszahlungDto updateAuszahlungForGesuch(@PathParam("fallId") UUID fallId,@Valid @NotNull AuszahlungUpdateDto auszahlungUpdateDto);
}
