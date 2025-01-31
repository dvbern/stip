package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.SozialdienstAdminDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerUpdateDto;
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

    @POST
    @Path("/benutzer")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstBenutzerDto createSozialdienstBenutzer(@Valid SozialdienstBenutzerCreateDto sozialdienstBenutzerCreateDto);

    @DELETE
    @Path("/{sozialdienstId}")
    @Produces({ "application/json", "text/plain" })
    SozialdienstDto deleteSozialdienst(@PathParam("sozialdienstId") UUID sozialdienstId);

    @DELETE
    @Path("/benutzer/{sozialdienstBenutzerId}")
    @Produces({ "text/plain" })
    void deleteSozialdienstBenutzer(@PathParam("sozialdienstBenutzerId") UUID sozialdienstBenutzerId);

    @GET
    @Produces({ "application/json", "text/plain" })
    List<SozialdienstDto> getAllSozialdienste();

    @GET
    @Path("/{sozialdienstId}")
    @Produces({ "application/json", "text/plain" })
    SozialdienstDto getSozialdienst(@PathParam("sozialdienstId") UUID sozialdienstId);

    @GET
    @Path("/benutzer/{sozialdienstBenutzerId}")
    @Produces({ "application/json", "text/plain" })
    SozialdienstBenutzerDto getSozialdienstBenutzer(@PathParam("sozialdienstBenutzerId") UUID sozialdienstBenutzerId);

    @GET
    @Path("/benutzer")
    @Produces({ "application/json", "text/plain" })
    List<SozialdienstBenutzerDto> getSozialdienstBenutzerList();

    @PATCH
    @Path("/{sozialdienstId}/replaceSozialdienstAdmin")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstBenutzerDto replaceSozialdienstAdmin(@PathParam("sozialdienstId") UUID sozialdienstId,@Valid SozialdienstAdminDto sozialdienstAdminDto);

    @PATCH
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstDto updateSozialdienst(@Valid SozialdienstUpdateDto sozialdienstUpdateDto);

    @PATCH
    @Path("/{sozialdienstId}/updateSozialdienstAdmin")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstBenutzerDto updateSozialdienstAdmin(@PathParam("sozialdienstId") UUID sozialdienstId,@Valid SozialdienstAdminUpdateDto sozialdienstAdminUpdateDto);

    @PATCH
    @Path("/benutzer")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SozialdienstBenutzerDto updateSozialdienstBenutzer(@Valid SozialdienstBenutzerUpdateDto sozialdienstBenutzerUpdateDto);
}
