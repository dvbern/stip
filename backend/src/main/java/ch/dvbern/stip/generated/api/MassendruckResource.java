package ch.dvbern.stip.generated.api;

import java.io.File;
import ch.dvbern.stip.generated.dto.FileDownloadTokenDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.MassendruckDatenschutzbriefDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDetailDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDto;
import ch.dvbern.stip.generated.dto.MassendruckVerfuegungDto;
import ch.dvbern.stip.generated.dto.PaginatedMassendruckJobDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/massendruck")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface MassendruckResource {

    @POST
    @Path("/{getGesucheSBQueryType}/create")
    @Produces({ "application/json", "text/plain" })
    MassendruckJobDto createMassendruckJobForQueryType(@PathParam("getGesucheSBQueryType") ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType getGesucheSBQueryType);

    @POST
    @Path("/{massendruckId}/delete")
    @Produces({ "application/json", "text/plain" })
    void deleteMassendruckJob(@PathParam("massendruckId") UUID massendruckId);

    @GET
    @Path("/download")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> downloadMassendruckDocument(@QueryParam("token") @NotNull   String token);

    @GET
    @Path("/{getMassendruckJobs}")
    @Produces({ "application/json", "text/plain" })
    PaginatedMassendruckJobDto getAllMassendruckJobs(@PathParam("getMassendruckJobs") ch.dvbern.stip.api.massendruck.type.GetMassendruckJobQueryType getMassendruckJobs,@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("massendruckJobNumber")   Integer massendruckJobNumber,@QueryParam("userErstellt")   String userErstellt,@QueryParam("timestampErstellt")   LocalDate timestampErstellt,@QueryParam("massendruckJobStatus")   ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus massendruckJobStatus,@QueryParam("massendruckJobTyp")   ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp massendruckJobTyp,@QueryParam("sortColumn")   ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);

    @GET
    @Path("/{massendruckId}/token")
    @Produces({ "application/json", "text/plain" })
    FileDownloadTokenDto getMassendruckDownloadToken(@PathParam("massendruckId") UUID massendruckId);

    @GET
    @Path("/detail/{massendruckJobId}")
    @Produces({ "application/json", "text/plain" })
    MassendruckJobDetailDto getMassendruckJobDetail(@PathParam("massendruckJobId") UUID massendruckJobId);

    @POST
    @Path("/datenschutzbrief/versendet/{massendruckDatenschutzbriefId}")
    @Produces({ "application/json", "text/plain" })
    MassendruckDatenschutzbriefDto massendruckDatenschutzbriefVersenden(@PathParam("massendruckDatenschutzbriefId") UUID massendruckDatenschutzbriefId);

    @POST
    @Path("/verfuegung/versendet/{massendruckVerfuegungId}")
    @Produces({ "application/json", "text/plain" })
    MassendruckVerfuegungDto massendruckVerfuegungVersenden(@PathParam("massendruckVerfuegungId") UUID massendruckVerfuegungId);

    @POST
    @Path("/{massendruckId}/retry")
    @Produces({ "application/json", "text/plain" })
    MassendruckJobDetailDto retryMassendruckJob(@PathParam("massendruckId") UUID massendruckId);
}
