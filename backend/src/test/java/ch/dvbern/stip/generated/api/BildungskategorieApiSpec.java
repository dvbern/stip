/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.api;

import ch.dvbern.stip.generated.dto.BildungskategorieDtoSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import static io.restassured.http.Method.*;

public class BildungskategorieApiSpec {

    private Supplier<RequestSpecBuilder> reqSpecSupplier;
    private Consumer<RequestSpecBuilder> reqSpecCustomizer;

    private BildungskategorieApiSpec(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        this.reqSpecSupplier = reqSpecSupplier;
    }

    public static BildungskategorieApiSpec bildungskategorie(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        return new BildungskategorieApiSpec(reqSpecSupplier);
    }

    private RequestSpecBuilder createReqSpec() {
        RequestSpecBuilder reqSpec = reqSpecSupplier.get();
        if(reqSpecCustomizer != null) {
            reqSpecCustomizer.accept(reqSpec);
        }
        return reqSpec;
    }

    public List<Oper> getAllOperations() {
        return Arrays.asList(
                getBildungskategorien()
        );
    }

    public GetBildungskategorienOper getBildungskategorien() {
        return new GetBildungskategorienOper(createReqSpec());
    }

    /**
     * Customize request specification
     * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
     * @return api
     */
    public BildungskategorieApiSpec reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
        this.reqSpecCustomizer = reqSpecCustomizer;
        return this;
    }

    /**
     * Gets all Bildungskategorien
     * 
     *
     * return List&lt;BildungskategorieDtoSpec&gt;
     */
    public static class GetBildungskategorienOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/bildungskategorie";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetBildungskategorienOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /bildungskategorie
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /bildungskategorie
         * @param handler handler
         * @return List&lt;BildungskategorieDtoSpec&gt;
         */
        public List<BildungskategorieDtoSpec> executeAs(Function<Response, Response> handler) {
            TypeRef<List<BildungskategorieDtoSpec>> type = new TypeRef<List<BildungskategorieDtoSpec>>(){};
            return execute(handler).as(type);
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetBildungskategorienOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetBildungskategorienOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
}