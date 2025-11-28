package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DarlehenDokumnetTypDto;
import ch.dvbern.stip.generated.dto.DarlehenDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateGsDto;
import ch.dvbern.stip.generated.dto.DarlehenUpdateSbDto;
import ch.dvbern.stip.generated.dto.DokumentDto;
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
    @Path("/{fallId}")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto createDarlehen(@PathParam("fallId") UUID fallId);

    @POST
    @Path("/{darlehenId}/{dokumentTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDarlehenDokument(@PathParam("darlehenId") DarlehenDto darlehenId,@PathParam("dokumentTyp") DarlehenDokumnetTypDto dokumentTyp,@FormParam(value = "id")  UUID id,@FormParam(value = "dokumentTyp")  DarlehenDokumnetTypDto dokumentTyp2,@FormParam(value = "dokumente")  List<DokumentDto> dokumente);

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

    @GET
    @Path("/{fallId}")
    @Produces({ "application/json", "text/plain" })
    DarlehenDto getActiveDarlehen(@PathParam("fallId") UUID fallId);

    @GET
    @Path("/{darlehenId}/{dokumentTyp}")
    @Produces({ "text/plain" })
    void getDarlehenDokument(@PathParam("darlehenId") DarlehenDto darlehenId,@PathParam("dokumentTyp") DarlehenDokumnetTypDto dokumentTyp);
}
