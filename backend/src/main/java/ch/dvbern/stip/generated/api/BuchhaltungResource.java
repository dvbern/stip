package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.BuchhaltungEntryDto;
import ch.dvbern.stip.generated.dto.BuchhaltungSaldokorrekturDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/buchhaltung/{gesuchId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface BuchhaltungResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    BuchhaltungEntryDto createBuchhaltungSaldokorrektur(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull BuchhaltungSaldokorrekturDto buchhaltungSaldokorrekturDto);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<BuchhaltungEntryDto> getBuchhaltungEntrys(@PathParam("gesuchId") UUID gesuchId);
}
