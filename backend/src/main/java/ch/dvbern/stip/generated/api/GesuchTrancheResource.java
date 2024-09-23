package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuchtranche")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchTrancheResource {

    @PATCH
    @Path("/{aenderungId}/aenderung/einreichen")
    @Produces({ "text/plain" })
    Response aenderungEinreichen(@PathParam("aenderungId") UUID aenderungId);

    @POST
    @Path("/{gesuchId}/aenderungsantrag")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createAenderungsantrag(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull CreateAenderungsantragRequestDto createAenderungsantragRequestDto);

    @POST
    @Path("/{gesuchId}/tranche")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createGesuchTrancheCopy(@PathParam("gesuchId") UUID gesuchId,@Valid CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto);

    @GET
    @Path("/{gesuchTrancheId}/einreichen/validieren")
    @Produces({ "application/json", "text/plain" })
    Response gesuchTrancheEinreichenValidieren(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchId}/aenderungsantrag")
    @Produces({ "application/json", "text/plain" })
    Response getAenderungsantrag(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response getAllTranchenForGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchTrancheId}/dokumente/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchDokument(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp);

    @GET
    @Path("/{gesuchTrancheId}/dokumente")
    @Produces({ "application/json", "text/plain" })
    Response getGesuchDokumente(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchTrancheId}/requiredDokumente")
    @Produces({ "application/json", "text/plain" })
    Response getRequiredGesuchDokumentTyp(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/validatePages/{gesuchTrancheId}")
    @Produces({ "application/json" })
    Response validateGesuchTranchePages(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);
}
