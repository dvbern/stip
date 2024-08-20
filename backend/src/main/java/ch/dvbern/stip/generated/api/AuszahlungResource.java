package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.ChangeAuszahlungKreditorDto;
import ch.dvbern.stip.generated.dto.CreateAuszahlungDto;
import ch.dvbern.stip.generated.dto.CreateAuszahlungKreditorDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/auszahlung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface AuszahlungResource {

    @PATCH
    @Path("/kreditor/change")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response changeKreditor(@Valid @NotNull ChangeAuszahlungKreditorDto changeAuszahlungKreditorDto);

    @POST
    @Path("/create")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createAuszahlung(@Valid @NotNull CreateAuszahlungDto createAuszahlungDto);

    @POST
    @Path("/kreditor/create")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    Response createKreditor(@Valid @NotNull CreateAuszahlungKreditorDto createAuszahlungKreditorDto);

    @GET
    @Path("/status/{deliveryId}")
    @Produces({ "application/json", "text/plain" })
    Response getImportStatus(@PathParam("deliveryId") Integer deliveryId);
}
