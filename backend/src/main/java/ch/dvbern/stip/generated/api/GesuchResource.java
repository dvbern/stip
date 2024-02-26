package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.DokumentDto;
import java.io.File;
import ch.dvbern.stip.generated.dto.GesuchCreateDto;
import ch.dvbern.stip.generated.dto.GesuchDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface GesuchResource {

    @POST
    @Path("/{gesuchId}/dokument/{dokumentTyp}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "text/plain" })
    io.smallrye.mutiny.Uni<Response> createDokument(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchId") UUID gesuchId,@FormParam(value = "fileUpload")  org.jboss.resteasy.reactive.multipart.FileUpload fileUpload);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response createGesuch(@Valid @NotNull GesuchCreateDto gesuchCreateDto);

    @DELETE
    @Path("/{gesuchId}/dokument/{dokumentTyp}/{dokumentId}")
    @Produces({ "text/plain" })
    Response deleteDokument(@PathParam("dokumentId") UUID dokumentId,@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchId") UUID gesuchId);

    @DELETE
    @Path("/{gesuchId}")
    @Produces({ "text/plain" })
    Response deleteGesuch(@PathParam("gesuchId") UUID gesuchId);

    @PATCH
    @Path("/{gesuchId}/einreichen")
    @Produces({ "application/json", "text/plain" })
    Response gesuchEinreichen(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/einreichen/validieren")
    @Produces({ "application/json", "text/plain" })
    Response gesuchEinreichenValidieren(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}/dokument/{dokumentTyp}/{dokumentId}")
    @Produces({ "application/octet-stream" })
    org.jboss.resteasy.reactive.RestMulti<io.vertx.mutiny.core.buffer.Buffer> getDokument(@PathParam("gesuchId") UUID gesuchId,@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("dokumentId") UUID dokumentId);

    @GET
    @Path("/{gesuchId}/dokument/{dokumentTyp}")
    @Produces({ "application/json", "text/plain" })
    Response getDokumenteForTyp(@PathParam("dokumentTyp") ch.dvbern.stip.api.dokument.type.DokumentTyp dokumentTyp,@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Path("/{gesuchId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesuch(@PathParam("gesuchId") UUID gesuchId);

    @GET
    @Produces({ "application/json", "text/plain" })
    Response getGesuche();

    @GET
    @Path("/benutzer/{benutzerId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheForBenutzer(@PathParam("benutzerId") UUID benutzerId);

    @GET
    @Path("/fall/{fallId}")
    @Produces({ "application/json", "text/plain" })
    Response getGesucheForFall(@PathParam("fallId") UUID fallId);

    @PATCH
    @Path("/{gesuchId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response updateGesuch(@PathParam("gesuchId") UUID gesuchId,@Valid @NotNull GesuchUpdateDto gesuchUpdateDto);
}
