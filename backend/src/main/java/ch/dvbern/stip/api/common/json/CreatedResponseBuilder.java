package ch.dvbern.stip.api.common.json;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;

import java.util.UUID;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@With
public class CreatedResponseBuilder {

    @SuppressWarnings({"java:S1170", "java:S1075"} /* this is a Builder! */)
    private static final String RESOURCE_PATH = "/{id}";

    UUID id;
    Class<?> resourceClass;


    public static CreatedResponseBuilder of(
        UUID id,
        Class<?> resourceClass
    ) {
        return new CreatedResponseBuilder(id, resourceClass);
    }

    public Response build() {
        var uri = UriBuilder.fromResource(resourceClass)
            .path(RESOURCE_PATH)
            .build(id);

        return Response.status(Status.CREATED)
            .location(uri)
            .build();
    }
}
