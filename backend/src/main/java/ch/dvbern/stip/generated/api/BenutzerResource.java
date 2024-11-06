package ch.dvbern.stip.generated.api;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/benutzer")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface BenutzerResource {

    @POST
    @Path("/stammdaten/sachbearbeiter/{benutzerId}")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response createOrUpdateSachbearbeiterStammdaten(@PathParam("benutzerId") UUID benutzerId,@Valid @NotNull SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto);

    @PATCH
    @Path("/stammdaten/sachbearbeiter")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createOrUpdateSachbearbeiterStammdatenList(@Valid List<SachbearbeiterZuordnungStammdatenListDto> sachbearbeiterZuordnungStammdatenListDto);

    @DELETE
    @Path("/{benutzerId}")
    @Produces({ "application/json", "text/plain" })
    Response deleteBenutzer(@PathParam("benutzerId") String benutzerId);

    @GET
    @Path("/stammdaten/sachbearbeiter")
    @Produces({ "application/json", "text/plain" })
    Response getSachbearbeitende();

    @GET
    @Path("/stammdaten/sachbearbeiter/{benutzerId}")
    @Produces({ "application/json", "text/plain" })
    Response getSachbearbeiterStammdaten(@PathParam("benutzerId") UUID benutzerId);

    @GET
    @Path("/prepare/me")
    @Produces({ "application/json", "text/plain" })
    Response prepareCurrentBenutzer();
}
