package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import ch.dvbern.stip.generated.dto.CreateGesuchTrancheRequestDto;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDto;
import ch.dvbern.stip.generated.dto.GesuchWithChangesDto;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.PatchAenderungsInfoRequestDto;
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
    @Path("/{aenderungId}/aenderung/ablehnen")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchTrancheDto aenderungAblehnen(@PathParam("aenderungId") UUID aenderungId,@Valid @NotNull KommentarDto kommentarDto);

    @POST
    @Path("/{aenderungId}/aenderung/akzeptieren")
    @Produces({ "application/json", "text/plain" })
    GesuchTrancheDto aenderungAkzeptieren(@PathParam("aenderungId") UUID aenderungId);

    @PATCH
    @Path("/{aenderungId}/aenderung/einreichen")
    @Produces({ "text/plain" })
    void aenderungEinreichen(@PathParam("aenderungId") UUID aenderungId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumenteEinreichen")
    @Produces({ "application/json", "text/plain" })
    GesuchDto aenderungFehlendeDokumenteEinreichen(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{gesuchTrancheId}/fehlendeDokumente")
    @Produces({ "application/json", "text/plain" })
    GesuchWithChangesDto aenderungFehlendeDokumenteUebermitteln(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{aenderungId}/aenderung/manuelleAenderung")
    @Produces({ "application/json", "text/plain" })
    GesuchTrancheDto aenderungManuellAnpassen(@PathParam("aenderungId") UUID aenderungId);

    @POST
    @Path("/{gesuchId}/aenderungsantrag")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchTrancheDto createAenderungsantrag(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull CreateAenderungsantragRequestDto createAenderungsantragRequestDto);

    @POST
    @Path("/{gesuchId}/tranche")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchTrancheDto createGesuchTrancheCopy(@PathParam("gesuchId") UUID gesuchId,@Valid CreateGesuchTrancheRequestDto createGesuchTrancheRequestDto);

    @DELETE
    @Path("/{aenderungId}/aenderung/delete")
    @Produces({ "text/plain" })
    void deleteAenderung(@PathParam("aenderungId") UUID aenderungId);

    @GET
    @Path("/{gesuchTrancheId}/einreichen/validieren/gs")
    @Produces({ "application/json", "text/plain" })
    ValidationReportDto gesuchTrancheEinreichenValidierenGS(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchTrancheId}/einreichen/validieren/sb")
    @Produces({ "application/json", "text/plain" })
    ValidationReportDto gesuchTrancheEinreichenValidierenSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/gs/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchTrancheListDto getAllTranchenForGesuchGS(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/sb/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    GesuchTrancheListDto getAllTranchenForGesuchSB(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchTrancheId}/dokumenteToUpload/gs")
    @Produces({ "application/json", "text/plain" })
    DokumenteToUploadDto getDocumentsToUploadGS(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchTrancheId}/dokumenteToUpload/sb")
    @Produces({ "application/json", "text/plain" })
    DokumenteToUploadDto getDocumentsToUploadSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchTrancheId}/dokumente/gs")
    @Produces({ "application/json", "text/plain" })
    List<GesuchDokumentDto> getGesuchDokumenteGS(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/{gesuchTrancheId}/dokumente/sb")
    @Produces({ "application/json", "text/plain" })
    List<GesuchDokumentDto> getGesuchDokumenteSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @PATCH
    @Path("/{aenderungId}/patchAenderungInfo")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    GesuchDto patchAenderungInfo(@PathParam("aenderungId") UUID aenderungId,@Valid @NotNull PatchAenderungsInfoRequestDto patchAenderungsInfoRequestDto);

    @GET
    @Path("/validatePages/{gesuchTrancheId}/gs")
    @Produces({ "application/json" })
    ValidationReportDto validateGesuchTranchePagesGS(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);

    @GET
    @Path("/validatePages/{gesuchTrancheId}/sb")
    @Produces({ "application/json" })
    ValidationReportDto validateGesuchTranchePagesSB(@PathParam("gesuchTrancheId") UUID gesuchTrancheId);
}
