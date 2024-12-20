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

import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstAdminDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstAdminUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstDtoSpec;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDtoSpec;
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

public class SozialdienstApiSpec {

    private Supplier<RequestSpecBuilder> reqSpecSupplier;
    private Consumer<RequestSpecBuilder> reqSpecCustomizer;

    private SozialdienstApiSpec(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        this.reqSpecSupplier = reqSpecSupplier;
    }

    public static SozialdienstApiSpec sozialdienst(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        return new SozialdienstApiSpec(reqSpecSupplier);
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
                createSozialdienst(),
                deleteSozialdienst(),
                getAllSozialdienste(),
                getSozialdienst(),
                replaceSozialdienstAdmin(),
                updateSozialdienst(),
                updateSozialdienstAdmin()
        );
    }

    public CreateSozialdienstOper createSozialdienst() {
        return new CreateSozialdienstOper(createReqSpec());
    }

    public DeleteSozialdienstOper deleteSozialdienst() {
        return new DeleteSozialdienstOper(createReqSpec());
    }

    public GetAllSozialdiensteOper getAllSozialdienste() {
        return new GetAllSozialdiensteOper(createReqSpec());
    }

    public GetSozialdienstOper getSozialdienst() {
        return new GetSozialdienstOper(createReqSpec());
    }

    public ReplaceSozialdienstAdminOper replaceSozialdienstAdmin() {
        return new ReplaceSozialdienstAdminOper(createReqSpec());
    }

    public UpdateSozialdienstOper updateSozialdienst() {
        return new UpdateSozialdienstOper(createReqSpec());
    }

    public UpdateSozialdienstAdminOper updateSozialdienstAdmin() {
        return new UpdateSozialdienstAdminOper(createReqSpec());
    }

    /**
     * Customize request specification
     * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
     * @return api
     */
    public SozialdienstApiSpec reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
        this.reqSpecCustomizer = reqSpecCustomizer;
        return this;
    }

    /**
     * Create a new Sozialdienst
     * 
     *
     * @see #body  (optional)
     * return SozialdienstDtoSpec
     */
    public static class CreateSozialdienstOper implements Oper {

        public static final Method REQ_METHOD = POST;
        public static final String REQ_URI = "/sozialdienst";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public CreateSozialdienstOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * POST /sozialdienst
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * POST /sozialdienst
         * @param handler handler
         * @return SozialdienstDtoSpec
         */
        public SozialdienstDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<SozialdienstDtoSpec> type = new TypeRef<SozialdienstDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param sozialdienstCreateDtoSpec (SozialdienstCreateDtoSpec)  (optional)
         * @return operation
         */
        public CreateSozialdienstOper body(SozialdienstCreateDtoSpec sozialdienstCreateDtoSpec) {
            reqSpec.setBody(sozialdienstCreateDtoSpec);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public CreateSozialdienstOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public CreateSozialdienstOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Delete a Sozialdienst by Id
     * 
     *
     * @see #sozialdienstIdPath  (required)
     * return SozialdienstDtoSpec
     */
    public static class DeleteSozialdienstOper implements Oper {

        public static final Method REQ_METHOD = DELETE;
        public static final String REQ_URI = "/sozialdienst/{sozialdienstId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public DeleteSozialdienstOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * DELETE /sozialdienst/{sozialdienstId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * DELETE /sozialdienst/{sozialdienstId}
         * @param handler handler
         * @return SozialdienstDtoSpec
         */
        public SozialdienstDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<SozialdienstDtoSpec> type = new TypeRef<SozialdienstDtoSpec>(){};
            return execute(handler).as(type);
        }

        public static final String SOZIALDIENST_ID_PATH = "sozialdienstId";

        /**
         * @param sozialdienstId (UUID)  (required)
         * @return operation
         */
        public DeleteSozialdienstOper sozialdienstIdPath(Object sozialdienstId) {
            reqSpec.addPathParam(SOZIALDIENST_ID_PATH, sozialdienstId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public DeleteSozialdienstOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public DeleteSozialdienstOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Returns all Sozialdienste
     * 
     *
     * return List&lt;SozialdienstDtoSpec&gt;
     */
    public static class GetAllSozialdiensteOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/sozialdienst";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetAllSozialdiensteOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /sozialdienst
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /sozialdienst
         * @param handler handler
         * @return List&lt;SozialdienstDtoSpec&gt;
         */
        public List<SozialdienstDtoSpec> executeAs(Function<Response, Response> handler) {
            TypeRef<List<SozialdienstDtoSpec>> type = new TypeRef<List<SozialdienstDtoSpec>>(){};
            return execute(handler).as(type);
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetAllSozialdiensteOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetAllSozialdiensteOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Get a Sozialdienst by Id
     * 
     *
     * @see #sozialdienstIdPath  (required)
     * return SozialdienstDtoSpec
     */
    public static class GetSozialdienstOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/sozialdienst/{sozialdienstId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetSozialdienstOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /sozialdienst/{sozialdienstId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /sozialdienst/{sozialdienstId}
         * @param handler handler
         * @return SozialdienstDtoSpec
         */
        public SozialdienstDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<SozialdienstDtoSpec> type = new TypeRef<SozialdienstDtoSpec>(){};
            return execute(handler).as(type);
        }

        public static final String SOZIALDIENST_ID_PATH = "sozialdienstId";

        /**
         * @param sozialdienstId (UUID)  (required)
         * @return operation
         */
        public GetSozialdienstOper sozialdienstIdPath(Object sozialdienstId) {
            reqSpec.addPathParam(SOZIALDIENST_ID_PATH, sozialdienstId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetSozialdienstOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetSozialdienstOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * 
     * 
     *
     * @see #sozialdienstIdPath  (required)
     * @see #body  (optional)
     * return SozialdienstAdminDtoSpec
     */
    public static class ReplaceSozialdienstAdminOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/sozialdienst/{sozialdienstId}/replaceSozialdienstAdmin";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public ReplaceSozialdienstAdminOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /sozialdienst/{sozialdienstId}/replaceSozialdienstAdmin
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /sozialdienst/{sozialdienstId}/replaceSozialdienstAdmin
         * @param handler handler
         * @return SozialdienstAdminDtoSpec
         */
        public SozialdienstAdminDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<SozialdienstAdminDtoSpec> type = new TypeRef<SozialdienstAdminDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param sozialdienstAdminCreateDtoSpec (SozialdienstAdminCreateDtoSpec)  (optional)
         * @return operation
         */
        public ReplaceSozialdienstAdminOper body(SozialdienstAdminCreateDtoSpec sozialdienstAdminCreateDtoSpec) {
            reqSpec.setBody(sozialdienstAdminCreateDtoSpec);
            return this;
        }

        public static final String SOZIALDIENST_ID_PATH = "sozialdienstId";

        /**
         * @param sozialdienstId (UUID)  (required)
         * @return operation
         */
        public ReplaceSozialdienstAdminOper sozialdienstIdPath(Object sozialdienstId) {
            reqSpec.addPathParam(SOZIALDIENST_ID_PATH, sozialdienstId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public ReplaceSozialdienstAdminOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public ReplaceSozialdienstAdminOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Update a Sozialdienst
     * 
     *
     * @see #body  (optional)
     * return SozialdienstDtoSpec
     */
    public static class UpdateSozialdienstOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/sozialdienst";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public UpdateSozialdienstOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /sozialdienst
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /sozialdienst
         * @param handler handler
         * @return SozialdienstDtoSpec
         */
        public SozialdienstDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<SozialdienstDtoSpec> type = new TypeRef<SozialdienstDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param sozialdienstUpdateDtoSpec (SozialdienstUpdateDtoSpec)  (optional)
         * @return operation
         */
        public UpdateSozialdienstOper body(SozialdienstUpdateDtoSpec sozialdienstUpdateDtoSpec) {
            reqSpec.setBody(sozialdienstUpdateDtoSpec);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public UpdateSozialdienstOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public UpdateSozialdienstOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * 
     * 
     *
     * @see #sozialdienstIdPath  (required)
     * @see #body  (optional)
     * return SozialdienstAdminDtoSpec
     */
    public static class UpdateSozialdienstAdminOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/sozialdienst/{sozialdienstId}/updateSozialdienstAdmin";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public UpdateSozialdienstAdminOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /sozialdienst/{sozialdienstId}/updateSozialdienstAdmin
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /sozialdienst/{sozialdienstId}/updateSozialdienstAdmin
         * @param handler handler
         * @return SozialdienstAdminDtoSpec
         */
        public SozialdienstAdminDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<SozialdienstAdminDtoSpec> type = new TypeRef<SozialdienstAdminDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param sozialdienstAdminUpdateDtoSpec (SozialdienstAdminUpdateDtoSpec)  (optional)
         * @return operation
         */
        public UpdateSozialdienstAdminOper body(SozialdienstAdminUpdateDtoSpec sozialdienstAdminUpdateDtoSpec) {
            reqSpec.setBody(sozialdienstAdminUpdateDtoSpec);
            return this;
        }

        public static final String SOZIALDIENST_ID_PATH = "sozialdienstId";

        /**
         * @param sozialdienstId (UUID)  (required)
         * @return operation
         */
        public UpdateSozialdienstAdminOper sozialdienstIdPath(Object sozialdienstId) {
            reqSpec.addPathParam(SOZIALDIENST_ID_PATH, sozialdienstId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public UpdateSozialdienstAdminOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public UpdateSozialdienstAdminOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
}
