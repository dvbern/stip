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


@Path("/auszahlung/{gesuchId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AuszahlungResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    UUID createAuszahlungForGesuch(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull AuszahlungDto auszahlungDto);

    @GET
    @Produces({ "application/json", "text/plain" })
    AuszahlungDto getAuszahlungForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    UUID updateAuszahlungForGesuch(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull AuszahlungUpdateDto auszahlungUpdateDto);
}
