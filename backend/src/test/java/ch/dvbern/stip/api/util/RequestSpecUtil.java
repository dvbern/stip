package ch.dvbern.stip.api.util;

import java.util.function.Supplier;

import io.restassured.builder.RequestSpecBuilder;

public class RequestSpecUtil {

    public static Supplier<RequestSpecBuilder> quarkusSpec() {
        return () -> new RequestSpecBuilder()
            .setBaseUri("http://localhost:8081")
            .setBasePath("/api/v1");
    }

}
