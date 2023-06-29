/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.gesuch;

import ch.dvbern.stip.annotations.ApiResource;
import ch.dvbern.stip.dokument.dto.DownloadFileData;
import ch.dvbern.stip.dokument.dto.GesuchDokumentDTO;
import ch.dvbern.stip.dokument.dto.MultipartBody;
import ch.dvbern.stip.dokument.model.Dokument;
import ch.dvbern.stip.dokument.model.DokumentTyp;
import ch.dvbern.stip.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.gesuch.model.Gesuch;
import ch.dvbern.stip.gesuch.dto.GesuchDTO;
import ch.dvbern.stip.gesuch.service.GesuchService;
import ch.dvbern.stip.shared.dto.ResponseId;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.annotation.security.DenyAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("gesuch")
@Tag(name = "Gesuch")
@ApiResource
@DenyAll
public class GesuchResource {

    @Inject
    GesuchService gesuchService;

    @Inject
    GesuchDokumentService gesuchDokumentService;

    @POST
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = String.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            GesuchDTO gesuch
    ) {
        Gesuch changed = gesuchService.saveGesuch(gesuch);
        return Response.ok(new ResponseId(changed.getId())).build();
    }

    @PUT
    @Path("/{gesuchId}")
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = GesuchDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveGesuch(
            @PathParam("gesuchId") UUID gesuchId,
            GesuchDTO gesuch
    ) {
        if (!gesuchId.equals(gesuch.getId())) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        gesuchService.saveGesuch(gesuch);
        return Response.ok().build();
    }

    @GET
    @Path("/{gesuchId}")
    @Operation(
            summary = "Returniert das Gesuch mit der gegebene Id.")
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = GesuchDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getGesuchDTO(
            @PathParam("gesuchId") UUID gesuchId
    ) {
        return gesuchService.findGesuchDTO(gesuchId).map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    @Operation(
            summary = "Returniert alle Gesuch.")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getGesuchs() {
        return gesuchService.findAll().map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @POST
    @Path("/{gesuchId}/dokument/{dokumentTyp}")
    @APIResponse(responseCode = "200")
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadDokument(
            @PathParam("gesuchId") UUID gesuchId,
            @PathParam("dokumentTyp") DokumentTyp dokumentTyp,
            MultipartBody body
    ) throws IOException {
        return Response.ok().entity(gesuchDokumentService.uploadDokument(gesuchId, dokumentTyp, body.files)).build();
    }
    @GET
    @Path("/{gesuchId}/dokument/{dokumentTyp}/{dokumentID}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public DownloadFileData downloadDokument(@PathParam("gesuchId") UUID gesuchId,
                                             @PathParam("dokumentTyp") DokumentTyp dokumentTyp,
                                             @PathParam("dokumentID") UUID dokumentId) {
        Optional<Dokument> optionalDokument = gesuchDokumentService.findDokument(dokumentId);
        DownloadFileData downloadFileData = null;
        if (optionalDokument.isPresent()) {
            //TODO add validation gesuchId dokumentTyp stimmen ???
            Dokument dokument = optionalDokument.get();
            File nf = new File(dokument.getFilepfad() + dokument.getFilename());
            downloadFileData = new DownloadFileData(dokument.getFilename(), nf);
        }
        return downloadFileData;
    }


    @GET
    @Path("/{gesuchId}/dokument/{dokumentTyp}")
    @Operation(
            summary = "Returniert der GesuchDokument mit der gegebene Id und alle Dokument die dazu gehoeren.")
    @APIResponse(responseCode = "200",
            content = @Content(schema = @Schema(implementation = GesuchDokumentDTO.class)))
    @APIResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    @APIResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    @APIResponse(responseCode = "500", ref = "#/components/responses/ServerError")
    @APIResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response getAllDokumenteForTyp(@PathParam("gesuchId") UUID gesuchId,
                                          @PathParam("dokumentTyp") DokumentTyp dokumentTyp) {
        return gesuchDokumentService.findGesuchDokumentForTyp(gesuchId, dokumentTyp).map(Response::ok)
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @DELETE
    @Path("/{gesuchId}/dokument/{dokumentTyp}/{dokumentID}")
    public Response deleteDokument(@PathParam("gesuchId") UUID gesuchId,
                                   @PathParam("dokumentTyp") DokumentTyp dokumentTyp,
                                   @PathParam("dokumentID") UUID dokumentId) {
        return Response.noContent().build();
    }
}
