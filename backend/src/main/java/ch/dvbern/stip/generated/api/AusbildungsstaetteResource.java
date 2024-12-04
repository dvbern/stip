package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteUpdateDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/ausbildungsstaette")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AusbildungsstaetteResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungsstaetteDto createAusbildungsstaette(@Valid AusbildungsstaetteCreateDto ausbildungsstaetteCreateDto);

    @DELETE
    @Path("/{ausbildungsstaetteId}")
    @Produces({ "text/plain" })
    void deleteAusbildungsstaette(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId);

    @GET
    @Path("/{ausbildungsstaetteId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungsstaetteDto getAusbildungsstaette(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<AusbildungsstaetteDto> getAusbildungsstaetten();

    @PATCH
    @Path("/{ausbildungsstaetteId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungsstaetteDto updateAusbildungsstaette(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId,@Valid AusbildungsstaetteUpdateDto ausbildungsstaetteUpdateDto);
}
