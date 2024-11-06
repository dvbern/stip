package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.WelcomeMailDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;


@Path("/mail/welcome")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface MailResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "text/plain" })
    Response sendWelcomeEmail(@Valid WelcomeMailDto welcomeMailDto);
}
