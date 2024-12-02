package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.WelcomeMailDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/mail/welcome")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface MailResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    void sendWelcomeEmail(@Valid WelcomeMailDto welcomeMailDto);
}
