package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.LandEuEftaDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/stammdaten/land")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface StammdatenResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    List<ch.dvbern.stip.api.stammdaten.type.Land> getLaender();

    @GET
    @Path("/euefta")
    @Produces({ "application/json", "text/plain" })
    List<LandEuEftaDto> getLaenderEuEfta();

    @PATCH
    @Path("/euefta")
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    List<LandEuEftaDto> setLaenderEuEfta(@Valid @NotNull List<LandEuEftaDto> landEuEftaDto);
}
