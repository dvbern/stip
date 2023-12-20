package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.BenutzerDto;
import ch.dvbern.stip.generated.dto.BenutzerUpdateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/benutzer")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface BenutzerResource {

    @POST
    @Path("/stammdaten/sachbearbeiter/{benutzerId}")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response createOrUpdateSachbearbeiterStammdaten(@PathParam("benutzerId") UUID benutzerId,@Valid @NotNull SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto);

    @GET
    @Path("/me")
    @Produces({ "application/json", "text/plain" })
    Response getCurrentBenutzer();

    @GET
    @Path("/stammdaten/sachbearbeiter")
    @Produces({ "application/json", "text/plain" })
    Response getSachbearbeitende();

    @GET
    @Path("/stammdaten/sachbearbeiter/{benutzerId}")
    @Produces({ "application/json", "text/plain" })
    Response getSachbearbeiterStammdaten(@PathParam("benutzerId") UUID benutzerId);

    @PUT
    @Path("/me")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateCurrentBenutzer(@Valid @NotNull BenutzerUpdateDto benutzerUpdateDto);
}
