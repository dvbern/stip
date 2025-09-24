package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.GetMassendruckJobQueryTypeDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.MassendruckDatenschutzbriefDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDetailDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDto;
import ch.dvbern.stip.generated.dto.MassendruckJobStatusDto;
import ch.dvbern.stip.generated.dto.MassendruckJobTypDto;
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

    @GET
    @Path("/{getMassendruckJobs}")
    @Produces({ "application/json", "text/plain" })
    PaginatedMassendruckJobDto getAllMassendruckJobs(@PathParam("getMassendruckJobs") GetMassendruckJobQueryTypeDto getMassendruckJobs,@QueryParam("massendruckJobNumber")   String massendruckJobNumber,@QueryParam("userErstellt")   String userErstellt,@QueryParam("timestampErstellt")   LocalDate timestampErstellt,@QueryParam("massendruckJobStatus")   MassendruckJobStatusDto massendruckJobStatus,@QueryParam("massendruckJobTyp")   MassendruckJobTypDto massendruckJobTyp,@QueryParam("sortColumn")   ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);

    @GET
    @Path("/detail/{massendruckJobId}")
    @Produces({ "application/json", "text/plain" })
    MassendruckJobDetailDto getMassendruckJobDetail(@PathParam("massendruckJobId") UUID massendruckJobId);

    @POST
    @Path("/datenschutzbrief/versendet/{massendruckDatenschutzbriefId}")
    @Produces({ "application/json", "text/plain" })
    MassendruckDatenschutzbriefDto massendruckDatenschutzbriefVersenden(@PathParam("massendruckDatenschutzbriefId") UUID massendruckDatenschutzbriefId);

    @POST
    @Path("/datenschutzbrief/versendet/{massendruckVerfuegungId}")
    @Produces({ "application/json", "text/plain" })
    MassendruckVerfuegungDto massendruckVerfuegungVersenden(@PathParam("massendruckVerfuegungId") UUID massendruckVerfuegungId);
}
