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

import ch.dvbern.stip.generated.dto.GesuchsjahrCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsjahrDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsjahrUpdateDtoSpec;
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

public class GesuchsjahrApiSpec {

    private Supplier<RequestSpecBuilder> reqSpecSupplier;
    private Consumer<RequestSpecBuilder> reqSpecCustomizer;

    private GesuchsjahrApiSpec(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        this.reqSpecSupplier = reqSpecSupplier;
    }

    public static GesuchsjahrApiSpec gesuchsjahr(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        return new GesuchsjahrApiSpec(reqSpecSupplier);
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
                createGesuchsjahr(),
                deleteGesuchsjahr(),
                getGesuchsjahr(),
                getGesuchsjahre(),
                publishGesuchsjahr(),
                updateGesuchsjahr()
        );
    }

    public CreateGesuchsjahrOper createGesuchsjahr() {
        return new CreateGesuchsjahrOper(createReqSpec());
    }

    public DeleteGesuchsjahrOper deleteGesuchsjahr() {
        return new DeleteGesuchsjahrOper(createReqSpec());
    }

    public GetGesuchsjahrOper getGesuchsjahr() {
        return new GetGesuchsjahrOper(createReqSpec());
    }

    public GetGesuchsjahreOper getGesuchsjahre() {
        return new GetGesuchsjahreOper(createReqSpec());
    }

    public PublishGesuchsjahrOper publishGesuchsjahr() {
        return new PublishGesuchsjahrOper(createReqSpec());
    }

    public UpdateGesuchsjahrOper updateGesuchsjahr() {
        return new UpdateGesuchsjahrOper(createReqSpec());
    }

    /**
     * Customize request specification
     * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
     * @return api
     */
    public GesuchsjahrApiSpec reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
        this.reqSpecCustomizer = reqSpecCustomizer;
        return this;
    }

    /**
     * Creates a new Gesuchsjahr
     * 
     *
     * @see #body  (required)
     * return GesuchsjahrDtoSpec
     */
    public static class CreateGesuchsjahrOper implements Oper {

        public static final Method REQ_METHOD = POST;
        public static final String REQ_URI = "/gesuchsjahr";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public CreateGesuchsjahrOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * POST /gesuchsjahr
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * POST /gesuchsjahr
         * @param handler handler
         * @return GesuchsjahrDtoSpec
         */
        public GesuchsjahrDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchsjahrDtoSpec> type = new TypeRef<GesuchsjahrDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param gesuchsjahrCreateDtoSpec (GesuchsjahrCreateDtoSpec)  (required)
         * @return operation
         */
        public CreateGesuchsjahrOper body(GesuchsjahrCreateDtoSpec gesuchsjahrCreateDtoSpec) {
            reqSpec.setBody(gesuchsjahrCreateDtoSpec);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public CreateGesuchsjahrOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public CreateGesuchsjahrOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Deletes a Gesuchsjahr with the given id
     * 
     *
     * @see #gesuchsjahrIdPath  (required)
     */
    public static class DeleteGesuchsjahrOper implements Oper {

        public static final Method REQ_METHOD = DELETE;
        public static final String REQ_URI = "/gesuchsjahr/{gesuchsjahrId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public DeleteGesuchsjahrOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("text/plain");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * DELETE /gesuchsjahr/{gesuchsjahrId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        public static final String GESUCHSJAHR_ID_PATH = "gesuchsjahrId";

        /**
         * @param gesuchsjahrId (UUID)  (required)
         * @return operation
         */
        public DeleteGesuchsjahrOper gesuchsjahrIdPath(Object gesuchsjahrId) {
            reqSpec.addPathParam(GESUCHSJAHR_ID_PATH, gesuchsjahrId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public DeleteGesuchsjahrOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public DeleteGesuchsjahrOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Returns a Gesuchsjahr with the given id
     * 
     *
     * @see #gesuchsjahrIdPath  (required)
     * return GesuchsjahrDtoSpec
     */
    public static class GetGesuchsjahrOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/gesuchsjahr/{gesuchsjahrId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetGesuchsjahrOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /gesuchsjahr/{gesuchsjahrId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /gesuchsjahr/{gesuchsjahrId}
         * @param handler handler
         * @return GesuchsjahrDtoSpec
         */
        public GesuchsjahrDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchsjahrDtoSpec> type = new TypeRef<GesuchsjahrDtoSpec>(){};
            return execute(handler).as(type);
        }

        public static final String GESUCHSJAHR_ID_PATH = "gesuchsjahrId";

        /**
         * @param gesuchsjahrId (UUID)  (required)
         * @return operation
         */
        public GetGesuchsjahrOper gesuchsjahrIdPath(Object gesuchsjahrId) {
            reqSpec.addPathParam(GESUCHSJAHR_ID_PATH, gesuchsjahrId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetGesuchsjahrOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetGesuchsjahrOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Gets all Gesuchsjahre
     * 
     *
     * return List&lt;GesuchsjahrDtoSpec&gt;
     */
    public static class GetGesuchsjahreOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/gesuchsjahr";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetGesuchsjahreOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /gesuchsjahr
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /gesuchsjahr
         * @param handler handler
         * @return List&lt;GesuchsjahrDtoSpec&gt;
         */
        public List<GesuchsjahrDtoSpec> executeAs(Function<Response, Response> handler) {
            TypeRef<List<GesuchsjahrDtoSpec>> type = new TypeRef<List<GesuchsjahrDtoSpec>>(){};
            return execute(handler).as(type);
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetGesuchsjahreOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetGesuchsjahreOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Publishes a Gesuchsjahr with the given id
     * 
     *
     * @see #gesuchsjahrIdPath  (required)
     * return GesuchsjahrDtoSpec
     */
    public static class PublishGesuchsjahrOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/gesuchsjahr/publish/{gesuchsjahrId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public PublishGesuchsjahrOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /gesuchsjahr/publish/{gesuchsjahrId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /gesuchsjahr/publish/{gesuchsjahrId}
         * @param handler handler
         * @return GesuchsjahrDtoSpec
         */
        public GesuchsjahrDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchsjahrDtoSpec> type = new TypeRef<GesuchsjahrDtoSpec>(){};
            return execute(handler).as(type);
        }

        public static final String GESUCHSJAHR_ID_PATH = "gesuchsjahrId";

        /**
         * @param gesuchsjahrId (UUID)  (required)
         * @return operation
         */
        public PublishGesuchsjahrOper gesuchsjahrIdPath(Object gesuchsjahrId) {
            reqSpec.addPathParam(GESUCHSJAHR_ID_PATH, gesuchsjahrId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public PublishGesuchsjahrOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public PublishGesuchsjahrOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Updates a Gesuchsjahr with the given id
     * 
     *
     * @see #gesuchsjahrIdPath  (required)
     * @see #body  (optional)
     * return GesuchsjahrDtoSpec
     */
    public static class UpdateGesuchsjahrOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/gesuchsjahr/{gesuchsjahrId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public UpdateGesuchsjahrOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /gesuchsjahr/{gesuchsjahrId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /gesuchsjahr/{gesuchsjahrId}
         * @param handler handler
         * @return GesuchsjahrDtoSpec
         */
        public GesuchsjahrDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchsjahrDtoSpec> type = new TypeRef<GesuchsjahrDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param gesuchsjahrUpdateDtoSpec (GesuchsjahrUpdateDtoSpec)  (optional)
         * @return operation
         */
        public UpdateGesuchsjahrOper body(GesuchsjahrUpdateDtoSpec gesuchsjahrUpdateDtoSpec) {
            reqSpec.setBody(gesuchsjahrUpdateDtoSpec);
            return this;
        }

        public static final String GESUCHSJAHR_ID_PATH = "gesuchsjahrId";

        /**
         * @param gesuchsjahrId (UUID)  (required)
         * @return operation
         */
        public UpdateGesuchsjahrOper gesuchsjahrIdPath(Object gesuchsjahrId) {
            reqSpec.addPathParam(GESUCHSJAHR_ID_PATH, gesuchsjahrId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public UpdateGesuchsjahrOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public UpdateGesuchsjahrOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
}