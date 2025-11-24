package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/darlehen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DarlehenResource {

    @POST
    @Produces({ "application/json", "text/plain" })
    DarlehenDto createDarlehen();

    @POST
    @Path("/{darlehenId}/ablehnen")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenAblehen(@PathParam("darlehenId") UUID darlehenId);

    @POST
    @Path("/{darlehenId}/akzeptieren")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenAkzeptieren(@PathParam("darlehenId") UUID darlehenId);

    @POST
    @Path("/{darlehenId}/eingeben")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenEingeben(@PathParam("darlehenId") UUID darlehenId);

    @POST
    @Path("/{darlehenId}/freigeben")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenFreigeben(@PathParam("darlehenId") UUID darlehenId);

    @PATCH
    @Path("/{darlehenId}/gs")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenUpdateGs(@PathParam("darlehenId") UUID darlehenId,@Valid @NotNull DarlehenUpdateGsDto darlehenUpdateGsDto);

    @PATCH
    @Path("/{darlehenId}/sb")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenUpdateSb(@PathParam("darlehenId") UUID darlehenId,@Valid @NotNull DarlehenUpdateSbDto darlehenUpdateSbDto);

    @POST
    @Path("/{darlehenId}/zurueckweisen")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto darlehenZurueckweisen(@PathParam("darlehenId") UUID darlehenId);
}
