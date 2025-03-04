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

import ch.dvbern.stip.generated.dto.GesuchNotizCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchNotizUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.JuristischeAbklaerungNotizAntwortDtoSpec;
import java.util.UUID;
import ch.dvbern.stip.generated.dto.ValidationReportDtoSpec;

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

public class GesuchNotizApiSpec {

    private Supplier<RequestSpecBuilder> reqSpecSupplier;
    private Consumer<RequestSpecBuilder> reqSpecCustomizer;

    private GesuchNotizApiSpec(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        this.reqSpecSupplier = reqSpecSupplier;
    }

    public static GesuchNotizApiSpec gesuchNotiz(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        return new GesuchNotizApiSpec(reqSpecSupplier);
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
                answerJuristischeAbklaerungNotiz(),
                createNotiz(),
                deleteNotiz(),
                getNotiz(),
                getNotizen(),
                updateNotiz()
        );
    }

    public AnswerJuristischeAbklaerungNotizOper answerJuristischeAbklaerungNotiz() {
        return new AnswerJuristischeAbklaerungNotizOper(createReqSpec());
    }

    public CreateNotizOper createNotiz() {
        return new CreateNotizOper(createReqSpec());
    }

    public DeleteNotizOper deleteNotiz() {
        return new DeleteNotizOper(createReqSpec());
    }

    public GetNotizOper getNotiz() {
        return new GetNotizOper(createReqSpec());
    }

    public GetNotizenOper getNotizen() {
        return new GetNotizenOper(createReqSpec());
    }

    public UpdateNotizOper updateNotiz() {
        return new UpdateNotizOper(createReqSpec());
    }

    /**
     * Customize request specification
     * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
     * @return api
     */
    public GesuchNotizApiSpec reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
        this.reqSpecCustomizer = reqSpecCustomizer;
        return this;
    }

    /**
     * Sets the Answer of a Juristische Abklaerung Notiz Antwort
     * 
     *
     * @see #notizIdPath  (required)
     * @see #body  (required)
     * return GesuchNotizDtoSpec
     */
    public static class AnswerJuristischeAbklaerungNotizOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/gesuch/notiz/juristischeAbklaerung/{notizId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public AnswerJuristischeAbklaerungNotizOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /gesuch/notiz/juristischeAbklaerung/{notizId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /gesuch/notiz/juristischeAbklaerung/{notizId}
         * @param handler handler
         * @return GesuchNotizDtoSpec
         */
        public GesuchNotizDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchNotizDtoSpec> type = new TypeRef<GesuchNotizDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param juristischeAbklaerungNotizAntwortDtoSpec (JuristischeAbklaerungNotizAntwortDtoSpec)  (required)
         * @return operation
         */
        public AnswerJuristischeAbklaerungNotizOper body(JuristischeAbklaerungNotizAntwortDtoSpec juristischeAbklaerungNotizAntwortDtoSpec) {
            reqSpec.setBody(juristischeAbklaerungNotizAntwortDtoSpec);
            return this;
        }

        public static final String NOTIZ_ID_PATH = "notizId";

        /**
         * @param notizId (UUID)  (required)
         * @return operation
         */
        public AnswerJuristischeAbklaerungNotizOper notizIdPath(Object notizId) {
            reqSpec.addPathParam(NOTIZ_ID_PATH, notizId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public AnswerJuristischeAbklaerungNotizOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public AnswerJuristischeAbklaerungNotizOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Creates a new Notiz for an existing gesuch
     * 
     *
     * @see #body  (required)
     * return GesuchNotizDtoSpec
     */
    public static class CreateNotizOper implements Oper {

        public static final Method REQ_METHOD = POST;
        public static final String REQ_URI = "/gesuch/notiz/create";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public CreateNotizOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * POST /gesuch/notiz/create
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * POST /gesuch/notiz/create
         * @param handler handler
         * @return GesuchNotizDtoSpec
         */
        public GesuchNotizDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchNotizDtoSpec> type = new TypeRef<GesuchNotizDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param gesuchNotizCreateDtoSpec (GesuchNotizCreateDtoSpec)  (required)
         * @return operation
         */
        public CreateNotizOper body(GesuchNotizCreateDtoSpec gesuchNotizCreateDtoSpec) {
            reqSpec.setBody(gesuchNotizCreateDtoSpec);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public CreateNotizOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public CreateNotizOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * delete a Notiz of a Gesuch with the given Notiz-Id
     * 
     *
     * @see #notizIdPath  (required)
     */
    public static class DeleteNotizOper implements Oper {

        public static final Method REQ_METHOD = DELETE;
        public static final String REQ_URI = "/gesuch/notiz/{notizId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public DeleteNotizOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("text/plain");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * DELETE /gesuch/notiz/{notizId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        public static final String NOTIZ_ID_PATH = "notizId";

        /**
         * @param notizId (UUID)  (required)
         * @return operation
         */
        public DeleteNotizOper notizIdPath(Object notizId) {
            reqSpec.addPathParam(NOTIZ_ID_PATH, notizId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public DeleteNotizOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public DeleteNotizOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * get a single Notiz of a Gesuch with the given Notiz-Id
     * 
     *
     * @see #notizIdPath  (required)
     * return GesuchNotizDtoSpec
     */
    public static class GetNotizOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/gesuch/notiz/{notizId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetNotizOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /gesuch/notiz/{notizId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /gesuch/notiz/{notizId}
         * @param handler handler
         * @return GesuchNotizDtoSpec
         */
        public GesuchNotizDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchNotizDtoSpec> type = new TypeRef<GesuchNotizDtoSpec>(){};
            return execute(handler).as(type);
        }

        public static final String NOTIZ_ID_PATH = "notizId";

        /**
         * @param notizId (UUID)  (required)
         * @return operation
         */
        public GetNotizOper notizIdPath(Object notizId) {
            reqSpec.addPathParam(NOTIZ_ID_PATH, notizId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetNotizOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetNotizOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Get all notizen of a fall from given gesuch
     * 
     *
     * @see #gesuchIdPath  (required)
     * return List&lt;GesuchNotizDtoSpec&gt;
     */
    public static class GetNotizenOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/gesuch/{gesuchId}/notiz/all";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetNotizenOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /gesuch/{gesuchId}/notiz/all
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /gesuch/{gesuchId}/notiz/all
         * @param handler handler
         * @return List&lt;GesuchNotizDtoSpec&gt;
         */
        public List<GesuchNotizDtoSpec> executeAs(Function<Response, Response> handler) {
            TypeRef<List<GesuchNotizDtoSpec>> type = new TypeRef<List<GesuchNotizDtoSpec>>(){};
            return execute(handler).as(type);
        }

        public static final String GESUCH_ID_PATH = "gesuchId";

        /**
         * @param gesuchId (UUID)  (required)
         * @return operation
         */
        public GetNotizenOper gesuchIdPath(Object gesuchId) {
            reqSpec.addPathParam(GESUCH_ID_PATH, gesuchId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetNotizenOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetNotizenOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Updates the Notiz of a Gesuch with the given Notiz-Id
     * 
     *
     * @see #body  (required)
     * return GesuchNotizDtoSpec
     */
    public static class UpdateNotizOper implements Oper {

        public static final Method REQ_METHOD = PATCH;
        public static final String REQ_URI = "/gesuch/notiz";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public UpdateNotizOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * PATCH /gesuch/notiz
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * PATCH /gesuch/notiz
         * @param handler handler
         * @return GesuchNotizDtoSpec
         */
        public GesuchNotizDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<GesuchNotizDtoSpec> type = new TypeRef<GesuchNotizDtoSpec>(){};
            return execute(handler).as(type);
        }

         /**
         * @param gesuchNotizUpdateDtoSpec (GesuchNotizUpdateDtoSpec)  (required)
         * @return operation
         */
        public UpdateNotizOper body(GesuchNotizUpdateDtoSpec gesuchNotizUpdateDtoSpec) {
            reqSpec.setBody(gesuchNotizUpdateDtoSpec);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public UpdateNotizOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public UpdateNotizOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
}
