package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDto;
import ch.dvbern.stip.generated.dto.DelegierungCreateDto;
import ch.dvbern.stip.generated.dto.GetDelegierungSozQueryTypeDto;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.PaginatedSozDashboardDto;
import ch.dvbern.stip.generated.dto.SozDashboardColumnDto;
import java.util.UUID;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface DelegierenResource {

    @POST
    @Path("/delegierung/{delegierungId}/mitarbeiterDelegieren")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    void delegierterMitarbeiterAendern(@PathParam("delegierungId") UUID delegierungId,@Valid @NotNull DelegierterMitarbeiterAendernDto delegierterMitarbeiterAendernDto);

    @POST
    @Path("/delegieren/{fallId}/{sozialdienstId}")
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    void fallDelegieren(@PathParam("fallId") UUID fallId,@PathParam("sozialdienstId") UUID sozialdienstId,@Valid @NotNull DelegierungCreateDto delegierungCreateDto);

    @GET
    @Path("/delegierung/{getDelegierungSozQueryType}")
    @Produces({ "application/json", "text/plain" })
    PaginatedSozDashboardDto getDelegierungSoz(@PathParam("getDelegierungSozQueryType") GetDelegierungSozQueryTypeDto getDelegierungSozQueryType,@PathParam("sozialdienstId") UUID sozialdienstId,@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("fallNummer")   String fallNummer,@QueryParam("nachname")   String nachname,@QueryParam("vorname")   String vorname,@QueryParam("geburtsdatum")   LocalDate geburtsdatum,@QueryParam("wohnort")   String wohnort,@QueryParam("delegierungAngenommen")   Boolean delegierungAngenommen,@QueryParam("letzteAktivitaetFrom")   LocalDate letzteAktivitaetFrom,@QueryParam("letzteAktivitaetTo")   LocalDate letzteAktivitaetTo,@QueryParam("sortColumn")   SozDashboardColumnDto sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder);
}
