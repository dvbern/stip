package ch.dvbern.stip.generated.api;

import java.util.UUID;

import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/gesuchtranche")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchTrancheResource {

    @PATCH
    @Path("/{aenderungId}/aenderung/ablehnen")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response aenderungAblehnen(@PathParam("aenderungId") UUID aenderungId,@Valid @NotNull KommentarDto kommentarDto);

    @POST
    @Path("/{aenderungId}/aenderung/akzeptieren")
    @Produces({ "application/json", "text/plain" })
    Response aenderungAkzeptieren(@PathParam("aenderungId") UUID aenderungId);

    @PATCH
    @Path("/{aenderungId}/aenderung/einreichen")
    @Produces({ "text/plain" })
    Response aenderungEinreichen(@PathParam("aenderungId") UUID aenderungId);

    @PATCH
    @Path("/{aenderungId}/aenderung/manuelleAenderung")
    @Produces({ "application/json", "text/plain" })
    Response aenderungManuellAnpassen(@PathParam("aenderungId") UUID aenderungId);

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

    @DELETE
    @Path("/{aenderungId}/aenderung/delete")
    @Produces({ "text/plain" })
    Response deleteAenderung(@PathParam("aenderungId") UUID aenderungId);

    @GET
    @Path("/{gesuchTrancheId}/einreichen/validieren")
    @Produces({ "application/json", "text/plain" })
    Response gesuchTrancheEinreichenValidieren(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

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
