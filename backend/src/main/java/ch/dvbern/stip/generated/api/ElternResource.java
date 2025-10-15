package ch.dvbern.stip.generated.api;

import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;




import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;


@Path("/setVersteckteEltern/{gesuchTrancheId}")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")
public interface ElternResource {

    @PATCH
    @Consumes({ "application/json" })
    @Produces({ "application/json", "text/plain" })
    List<ch.dvbern.stip.api.eltern.type.ElternTyp> setVersteckteEltern(@PathParam("gesuchTrancheId") UUID gesuchTrancheId,@Valid List<ch.dvbern.stip.api.eltern.type.ElternTyp> chDvbernStipApiElternTypeElternTyp);
}
