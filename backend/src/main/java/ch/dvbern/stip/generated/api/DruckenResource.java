package ch.dvbern.stip.generated.api;

import java.time.LocalDate;

import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.generated.dto.DruckauftraegeColumnDto;
import ch.dvbern.stip.generated.dto.GetDruckauftraegeQueryTypeDto;
import ch.dvbern.stip.generated.dto.PaginatedDruckauftraegeDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("/drucken/{getDruckauftraegeQueryType}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DruckenResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    PaginatedDruckauftraegeDto getAllDruckauftraege(@PathParam("getDruckauftraegeQueryType") GetDruckauftraegeQueryTypeDto getDruckauftraegeQueryType,@QueryParam("batchName")   String batchName,@QueryParam("bearbeiter")   String bearbeiter,@QueryParam("timestampErstellt")   LocalDate timestampErstellt,@QueryParam("druckauftragStatus")
    MassendruckJobStatus massendruckJobStatus,@QueryParam("druckauftragTyp")   ch.dvbern.stip.api.massendruck.type.DruckauftragTyp druckauftragTyp,@QueryParam("sortColumn")   DruckauftraegeColumnDto sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);
}
