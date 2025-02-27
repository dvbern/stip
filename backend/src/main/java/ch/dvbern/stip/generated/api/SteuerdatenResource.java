package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.NeskoTokenDto;
import ch.dvbern.stip.generated.dto.SteuerdatenDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/steuerdaten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface SteuerdatenResource {

    @GET
    @Path("/{gesuchTrancheId}")
    @Produces({ "application/json", "text/plain" })
    List<SteuerdatenDto> getSteuerdaten(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    List<SteuerdatenDto> updateSteuerdaten(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid @NotNull List<SteuerdatenDto> steuerdatenDto);

    @POST
    @Path("/nesko/{steuerdatenId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SteuerdatenDto updateSteuerdatenFromNesko(@PathParam("steuerdatenId") UUID steuerdatenId,@Valid NeskoTokenDto neskoTokenDto);
}
