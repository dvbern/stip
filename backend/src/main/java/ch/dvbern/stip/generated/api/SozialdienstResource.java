package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/sozialdienst")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface SozialdienstResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstDto createSozialdienst(@Valid SozialdienstCreateDto sozialdienstCreateDto);

    @DELETE
    @Path("/{sozialdienstId}")
    @Produces({ "application/json", "text/plain" })
    SozialdienstDto deleteSozialdienst(@PathParam("sozialdienstId") UUID sozialdienstId);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<SozialdienstDto> getAllSozialdienste();

    @GET
    @Path("/{sozialdienstId}")
    @Produces({ "application/json", "text/plain" })
    SozialdienstDto getSozialdienst(@PathParam("sozialdienstId") UUID sozialdienstId);

    @PATCH
    @Path("/{sozialdienstId}/replaceSozialdienstAdmin")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstAdminDto replaceSozialdienstAdmin(@PathParam("sozialdienstId") UUID sozialdienstId,@Valid SozialdienstAdminCreateDto sozialdienstAdminCreateDto);

    @PATCH
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstDto updateSozialdienst(@Valid SozialdienstUpdateDto sozialdienstUpdateDto);

    @PATCH
    @Path("/{sozialdienstId}/updateSozialdienstAdmin")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstAdminDto updateSozialdienstAdmin(@PathParam("sozialdienstId") UUID sozialdienstId,@Valid SozialdienstAdminUpdateDto sozialdienstAdminUpdateDto);
}
