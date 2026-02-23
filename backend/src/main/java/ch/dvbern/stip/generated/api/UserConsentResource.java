package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.CreateUserConsentDto;
import ch.dvbern.stip.generated.dto.UserConsentDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/userconsent")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface UserConsentResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    UserConsentDto createUserConsent(@Valid @NotNull CreateUserConsentDto createUserConsentDto);

    @GET
    @Produces({ "application/json" })
    UserConsentDto getUserConsent();
}
