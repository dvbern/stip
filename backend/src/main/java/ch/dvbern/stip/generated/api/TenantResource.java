package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.TenantInfoDto;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/tenant/current")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")
public interface TenantResource {

    @GET
    @Produces({ "application/json", "text/plain" })
    TenantInfoDto getCurrentTenant();
}
