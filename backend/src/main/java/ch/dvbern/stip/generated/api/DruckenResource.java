package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DruckauftraegeColumnDto;
import ch.dvbern.stip.generated.dto.DruckauftragStatusDto;
import ch.dvbern.stip.generated.dto.DruckauftragTypDto;
import ch.dvbern.stip.generated.dto.GetDruckauftraegeQueryTypeDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.PaginatedDruckauftraegeDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/drucken/{getDruckauftraegeQueryType}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DruckenResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    PaginatedDruckauftraegeDto getAllDruckauftraege(@PathParam("getDruckauftraegeQueryType") GetDruckauftraegeQueryTypeDto getDruckauftraegeQueryType,@QueryParam("batchName")   String batchName,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("timestampErstellt")   LocalDate timestampErstellt,@QueryParam("druckauftragStatus")   DruckauftragStatusDto druckauftragStatus,@QueryParam("druckauftragTyp")   DruckauftragTypDto druckauftragTyp,@QueryParam("sortColumn")   DruckauftraegeColumnDto sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);
}
