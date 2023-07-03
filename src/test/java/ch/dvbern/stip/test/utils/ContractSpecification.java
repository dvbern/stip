package ch.dvbern.stip.test.utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testcontainers.shaded.org.hamcrest.CoreMatchers;


import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static org.hamcrest.Matchers.containsString;

public class ContractSpecification {

   /* public static <T> RequestSpecification withRequestBody(T expected) {
        return new RequestSpecBuilder().setBody(expected).setContentType(ContentType.JSON).build();
    }

    public static <T> ResponseSpecification hasResponseBody(T expected) {
        return new ResponseSpecBuilder().expectBody()
    }

    public static ResponseSpecification created(UUID id) {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectHeader("Location", containsString(id.toString())).build();
    }

    */
}
