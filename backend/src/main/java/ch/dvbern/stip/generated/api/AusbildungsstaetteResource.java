package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.AbschlussDto;
import ch.dvbern.stip.generated.dto.AbschlussSlimDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
import ch.dvbern.stip.generated.dto.AusbildungsstaetteSlimDto;
import ch.dvbern.stip.generated.dto.BrueckenangebotCreateDto;
import ch.dvbern.stip.generated.dto.PaginatedAbschlussDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsgangDto;
import ch.dvbern.stip.generated.dto.PaginatedAusbildungsstaetteDto;
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
public interface AusbildungsstaetteResource {

    @POST
    @Path("/abschluss")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AbschlussDto createAbschlussBrueckenangebot(@Valid @NotNull BrueckenangebotCreateDto brueckenangebotCreateDto);

    @POST
    @Path("/ausbildungsgang")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungsgangDto createAusbildungsgang(@Valid @NotNull AusbildungsgangCreateDto ausbildungsgangCreateDto);

    @POST
    @Path("/ausbildungsstaette")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    AusbildungsstaetteDto createAusbildungsstaette(@Valid @NotNull AusbildungsstaetteCreateDto ausbildungsstaetteCreateDto);

    @GET
    @Path("/abschluss/slim")
    @Produces({ "application/json", "text/plain" })
    List<AbschlussSlimDto> getAllAbschluessForAuswahl();

    @GET
    @Path("/abschluss")
    @Produces({ "application/json", "text/plain" })
    PaginatedAbschlussDto getAllAbschlussForUebersicht(@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("sortColumn")   ch.dvbern.stip.api.ausbildung.type.AbschlussSortColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder,@QueryParam("ausbildungskategorie")   ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie,@QueryParam("bildungsrichtung")   ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung,@QueryParam("bezeichnungDe")   String bezeichnungDe,@QueryParam("bezeichnungFr")   String bezeichnungFr,@QueryParam("aktiv")   Boolean aktiv);

    @GET
    @Path("/ausbildungsgang")
    @Produces({ "application/json", "text/plain" })
    PaginatedAusbildungsgangDto getAllAusbildungsgangForUebersicht(@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("sortColumn")   ch.dvbern.stip.api.ausbildung.type.AusbildungsgangSortColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder,@QueryParam("abschlussBezeichnungDe")   String abschlussBezeichnungDe,@QueryParam("abschlussBezeichnungFr")   String abschlussBezeichnungFr,@QueryParam("ausbildungskategorie")   ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie,@QueryParam("ausbildungsstaetteNameDe")   String ausbildungsstaetteNameDe,@QueryParam("ausbildungsstaetteNameFr")   String ausbildungsstaetteNameFr,@QueryParam("aktiv")   Boolean aktiv);

    @GET
    @Path("/ausbildungsstaette/slim")
    @Produces({ "application/json", "text/plain" })
    List<AusbildungsstaetteSlimDto> getAllAusbildungsstaetteForAuswahl();

    @GET
    @Path("/ausbildungsstaette")
    @Produces({ "application/json", "text/plain" })
    PaginatedAusbildungsstaetteDto getAllAusbildungsstaetteForUebersicht(@QueryParam("page") @NotNull   Integer page,@QueryParam("pageSize") @NotNull   Integer pageSize,@QueryParam("sortColumn")   ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteSortColumn sortColumn,@QueryParam("sortOrder")   ch.dvbern.stip.api.gesuch.type.SortOrder sortOrder,@QueryParam("nameDe")   String nameDe,@QueryParam("nameFr")   String nameFr,@QueryParam("chShis")   String chShis,@QueryParam("burNo")   String burNo,@QueryParam("ctNo")   String ctNo,@QueryParam("aktiv")   Boolean aktiv);

    @PATCH
    @Path("/abschluss/inaktiv/{abschlussId}")
    @Produces({ "application/json", "text/plain" })
    AbschlussDto setAbschlussInaktiv(@PathParam("abschlussId") UUID abschlussId);

    @PATCH
    @Path("/ausbildungsgang/inaktiv/{ausbildungsgangId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungsgangDto setAusbildungsgangInaktiv(@PathParam("ausbildungsgangId") UUID ausbildungsgangId);

    @PATCH
    @Path("/ausbildungsstaette/inaktiv/{ausbildungsstaetteId}")
    @Produces({ "application/json", "text/plain" })
    AusbildungsstaetteDto setAusbildungsstaetteInaktiv(@PathParam("ausbildungsstaetteId") UUID ausbildungsstaetteId);
}
