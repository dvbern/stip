package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.BenutzerDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterUpdateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenListDto;
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
    void createOrUpdateSachbearbeiterStammdaten(@PathParam("benutzerId") UUID benutzerId,@Valid @NotNull SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdatenDto);

    @PATCH
    @Path("/stammdaten/sachbearbeiter")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    void createOrUpdateSachbearbeiterStammdatenList(@Valid List<SachbearbeiterZuordnungStammdatenListDto> sachbearbeiterZuordnungStammdatenListDto);

    @POST
    @Path("/sachbearbeiter")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SachbearbeiterDto createSachbearbeiter(@Valid @NotNull SachbearbeiterUpdateDto sachbearbeiterUpdateDto);

    @DELETE
    @Path("/{benutzerId}")
    @Produces({ "application/json", "text/plain" })
    void deleteBenutzer(@PathParam("benutzerId") String benutzerId);

    @DELETE
    @Path("/sachbearbeiter/{sachbearbeiterId}")
    @Produces({ "text/plain" })
    void deleteSachbearbeiter(@PathParam("sachbearbeiterId") UUID sachbearbeiterId);

    @GET
    @Path("/stammdaten/sachbearbeiter")
    @Produces({ "application/json", "text/plain" })
    List<BenutzerDto> getSachbearbeitende();

    @GET
    @Path("/sachbearbeiter/{sachbearbeiterId}")
    @Produces({ "application/json", "text/plain" })
    SachbearbeiterDto getSachbearbeiterForManagement(@PathParam("sachbearbeiterId") UUID sachbearbeiterId);

    @GET
    @Path("/stammdaten/sachbearbeiter/{benutzerId}")
    @Produces({ "application/json", "text/plain" })
    SachbearbeiterZuordnungStammdatenDto getSachbearbeiterStammdaten(@PathParam("benutzerId") UUID benutzerId);

    @GET
    @Path("/sachbearbeiter")
    @Produces({ "application/json", "text/plain" })
    List<SachbearbeiterDto> getSachbearbeitersForManagement();

    @GET
    @Path("/prepare/me")
    @Produces({ "application/json", "text/plain" })
    BenutzerDto prepareCurrentBenutzer();

    @PATCH
    @Path("/sachbearbeiter/{sachbearbeiterId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    SachbearbeiterDto updateSachbearbeiter(@PathParam("sachbearbeiterId") UUID sachbearbeiterId,@Valid @NotNull SachbearbeiterUpdateDto sachbearbeiterUpdateDto);
}
