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

import ch.dvbern.stip.generated.dto.AusbildungDtoSpec;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDtoSpec;
import java.util.UUID;

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

public class AusbildungApiSpec {

    private Supplier<RequestSpecBuilder> reqSpecSupplier;
    private Consumer<RequestSpecBuilder> reqSpecCustomizer;

    private AusbildungApiSpec(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        this.reqSpecSupplier = reqSpecSupplier;
    }

    public static AusbildungApiSpec ausbildung(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        return new AusbildungApiSpec(reqSpecSupplier);
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
                createAusbildung(),
                getAusbildung(),
                updateAusbildung()
        );
    }

    public CreateAusbildungOper createAusbildung() {
        return new CreateAusbildungOper(createReqSpec());
    }

    public GetAusbildungOper getAusbildung() {
        return new GetAusbildungOper(createReqSpec());
    }

    public UpdateAusbildungOper updateAusbildung() {
        return new UpdateAusbildungOper(createReqSpec());
    }

    /**
     * Customize request specification
     * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
     * @return api
     */
    public AusbildungApiSpec reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
        this.reqSpecCustomizer = reqSpecCustomizer;
        return this;
    }

    /**
     * Returniert die erstellte Ausbildung
     * 
     *
     * @see #body  (required)
     * return AusbildungDtoSpec
     */
    public static class CreateAusbildungOper implements Oper {

        public static final Method REQ_METHOD = POST;
        public static final String REQ_URI = "/ausbildung";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public CreateAusbildungOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * POST /ausbildung
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * POST /ausbildung
         * @param handler handler
         * @return AusbildungDtoSpec
         */
        public AusbildungDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<AusbildungDtoSpec> type = new TypeRef<AusbildungDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param ausbildungUpdateDtoSpec (AusbildungUpdateDtoSpec)  (required)
         * @return operation
         */
        public CreateAusbildungOper body(AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec) {
            reqSpec.setBody(ausbildungUpdateDtoSpec);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public CreateAusbildungOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public CreateAusbildungOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Returniert die Ausbildung
     * 
     *
     * @see #ausbildungIdPath  (required)
     * return AusbildungDtoSpec
     */
    public static class GetAusbildungOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/ausbildung/{ausbildungId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetAusbildungOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /ausbildung/{ausbildungId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /ausbildung/{ausbildungId}
         * @param handler handler
         * @return AusbildungDtoSpec
         */
        public AusbildungDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<AusbildungDtoSpec> type = new TypeRef<AusbildungDtoSpec>(){};
            return execute(handler).as(type);
        }

        public static final String AUSBILDUNG_ID_PATH = "ausbildungId";

        /**
         * @param ausbildungId (UUID)  (required)
         * @return operation
         */
        public GetAusbildungOper ausbildungIdPath(Object ausbildungId) {
            reqSpec.addPathParam(AUSBILDUNG_ID_PATH, ausbildungId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetAusbildungOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetAusbildungOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Returniert die Ausbildung
     * 
     *
     * @see #ausbildungIdPath  (required)
     * @see #body  (required)
     * return AusbildungDtoSpec
     */
    public static class UpdateAusbildungOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/ausbildung/{ausbildungId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public UpdateAusbildungOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /ausbildung/{ausbildungId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /ausbildung/{ausbildungId}
         * @param handler handler
         * @return AusbildungDtoSpec
         */
        public AusbildungDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<AusbildungDtoSpec> type = new TypeRef<AusbildungDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param ausbildungUpdateDtoSpec (AusbildungUpdateDtoSpec)  (required)
         * @return operation
         */
        public UpdateAusbildungOper body(AusbildungUpdateDtoSpec ausbildungUpdateDtoSpec) {
            reqSpec.setBody(ausbildungUpdateDtoSpec);
            return this;
        }

        public static final String AUSBILDUNG_ID_PATH = "ausbildungId";

        /**
         * @param ausbildungId (UUID)  (required)
         * @return operation
         */
        public UpdateAusbildungOper ausbildungIdPath(Object ausbildungId) {
            reqSpec.addPathParam(AUSBILDUNG_ID_PATH, ausbildungId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public UpdateAusbildungOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public UpdateAusbildungOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
}