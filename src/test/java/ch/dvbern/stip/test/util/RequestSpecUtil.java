package ch.dvbern.stip.test.util;

import io.restassured.builder.RequestSpecBuilder;

import java.util.function.Supplier;

public class RequestSpecUtil {

    public static Supplier<RequestSpecBuilder> quarkusSpec() {
        return () -> new RequestSpecBuilder()
                .setBaseUri("http://localhost:8081")
                .setBasePath("/api/v1");
    }

}
