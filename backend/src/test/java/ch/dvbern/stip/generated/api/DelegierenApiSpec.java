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

import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDtoSpec;
import ch.dvbern.stip.generated.dto.DelegierungCreateDtoSpec;
import ch.dvbern.stip.generated.dto.GetDelegierungSozQueryTypeDtoSpec;
import java.time.LocalDate;
import ch.dvbern.stip.generated.dto.PaginatedSozDashboardDtoSpec;
import ch.dvbern.stip.generated.dto.SortOrderDtoSpec;
import ch.dvbern.stip.generated.dto.SozDashboardColumnDtoSpec;
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

public class DelegierenApiSpec {

    private Supplier<RequestSpecBuilder> reqSpecSupplier;
    private Consumer<RequestSpecBuilder> reqSpecCustomizer;

    private DelegierenApiSpec(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        this.reqSpecSupplier = reqSpecSupplier;
    }

    public static DelegierenApiSpec delegieren(Supplier<RequestSpecBuilder> reqSpecSupplier) {
        return new DelegierenApiSpec(reqSpecSupplier);
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
                delegierterMitarbeiterAendern(),
                fallDelegieren(),
                getDelegierungsOfSozialdienst()
        );
    }

    public DelegierterMitarbeiterAendernOper delegierterMitarbeiterAendern() {
        return new DelegierterMitarbeiterAendernOper(createReqSpec());
    }

    public FallDelegierenOper fallDelegieren() {
        return new FallDelegierenOper(createReqSpec());
    }

    public GetDelegierungsOfSozialdienstOper getDelegierungsOfSozialdienst() {
        return new GetDelegierungsOfSozialdienstOper(createReqSpec());
    }

    /**
     * Customize request specification
     * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
     * @return api
     */
    public DelegierenApiSpec reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
        this.reqSpecCustomizer = reqSpecCustomizer;
        return this;
    }

    /**
     * 
     * 
     *
     * @see #delegierungIdPath Die ID der Delegierung (required)
     * @see #body  (required)
     */
    public static class DelegierterMitarbeiterAendernOper implements Oper {

        public static final Method REQ_METHOD = POST;
        public static final String REQ_URI = "/delegierung/{delegierungId}/mitarbeiterDelegieren";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public DelegierterMitarbeiterAendernOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("text/plain");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * POST /delegierung/{delegierungId}/mitarbeiterDelegieren
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

         /**
         * @param delegierterMitarbeiterAendernDtoSpec (DelegierterMitarbeiterAendernDtoSpec)  (required)
         * @return operation
         */
        public DelegierterMitarbeiterAendernOper body(DelegierterMitarbeiterAendernDtoSpec delegierterMitarbeiterAendernDtoSpec) {
            reqSpec.setBody(delegierterMitarbeiterAendernDtoSpec);
            return this;
        }

        public static final String DELEGIERUNG_ID_PATH = "delegierungId";

        /**
         * @param delegierungId (UUID) Die ID der Delegierung (required)
         * @return operation
         */
        public DelegierterMitarbeiterAendernOper delegierungIdPath(Object delegierungId) {
            reqSpec.addPathParam(DELEGIERUNG_ID_PATH, delegierungId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public DelegierterMitarbeiterAendernOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public DelegierterMitarbeiterAendernOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * 
     * 
     *
     * @see #fallIdPath Die ID vom Fall (required)
     * @see #sozialdienstIdPath  (required)
     * @see #body  (required)
     */
    public static class FallDelegierenOper implements Oper {

        public static final Method REQ_METHOD = POST;
        public static final String REQ_URI = "/delegieren/{fallId}/{sozialdienstId}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public FallDelegierenOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setContentType("application/json");
            reqSpec.setAccept("text/plain");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * POST /delegieren/{fallId}/{sozialdienstId}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

         /**
         * @param delegierungCreateDtoSpec (DelegierungCreateDtoSpec)  (required)
         * @return operation
         */
        public FallDelegierenOper body(DelegierungCreateDtoSpec delegierungCreateDtoSpec) {
            reqSpec.setBody(delegierungCreateDtoSpec);
            return this;
        }

        public static final String FALL_ID_PATH = "fallId";

        /**
         * @param fallId (UUID) Die ID vom Fall (required)
         * @return operation
         */
        public FallDelegierenOper fallIdPath(Object fallId) {
            reqSpec.addPathParam(FALL_ID_PATH, fallId);
            return this;
        }

        public static final String SOZIALDIENST_ID_PATH = "sozialdienstId";

        /**
         * @param sozialdienstId (UUID)  (required)
         * @return operation
         */
        public FallDelegierenOper sozialdienstIdPath(Object sozialdienstId) {
            reqSpec.addPathParam(SOZIALDIENST_ID_PATH, sozialdienstId);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public FallDelegierenOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public FallDelegierenOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
    /**
     * Returns a list of Faelle with Delegierung
     * 
     *
     * @see #getDelegierungSozQueryTypePath  (required)
     * @see #pageQuery  (required)
     * @see #pageSizeQuery  (required)
     * @see #fallNummerQuery  (optional)
     * @see #nachnameQuery  (optional)
     * @see #vornameQuery  (optional)
     * @see #geburtsdatumQuery  (optional)
     * @see #wohnortQuery  (optional)
     * @see #delegierungAngenommenQuery  (optional)
     * @see #sortColumnQuery  (optional)
     * @see #sortOrderQuery  (optional)
     * return PaginatedSozDashboardDtoSpec
     */
    public static class GetDelegierungsOfSozialdienstOper implements Oper {

        public static final Method REQ_METHOD = GET;
        public static final String REQ_URI = "/delegierung/{getDelegierungSozQueryType}";

        private RequestSpecBuilder reqSpec;
        private ResponseSpecBuilder respSpec;

        public GetDelegierungsOfSozialdienstOper(RequestSpecBuilder reqSpec) {
            this.reqSpec = reqSpec;
            reqSpec.setAccept("application/json");
            this.respSpec = new ResponseSpecBuilder();
        }

        /**
         * GET /delegierung/{getDelegierungSozQueryType}
         * @param handler handler
         * @param <T> type
         * @return type
         */
        @Override
        public <T> T execute(Function<Response, T> handler) {
            return handler.apply(RestAssured.given().spec(reqSpec.build()).expect().spec(respSpec.build()).when().request(REQ_METHOD, REQ_URI));
        }

        /**
         * GET /delegierung/{getDelegierungSozQueryType}
         * @param handler handler
         * @return PaginatedSozDashboardDtoSpec
         */
        public PaginatedSozDashboardDtoSpec executeAs(Function<Response, Response> handler) {
            TypeRef<PaginatedSozDashboardDtoSpec> type = new TypeRef<PaginatedSozDashboardDtoSpec>(){};
            return execute(handler).as(type);
        }

        public static final String GET_DELEGIERUNG_SOZ_QUERY_TYPE_PATH = "getDelegierungSozQueryType";

        /**
         * @param getDelegierungSozQueryType (GetDelegierungSozQueryTypeDtoSpec)  (required)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper getDelegierungSozQueryTypePath(Object getDelegierungSozQueryType) {
            reqSpec.addPathParam(GET_DELEGIERUNG_SOZ_QUERY_TYPE_PATH, getDelegierungSozQueryType);
            return this;
        }

        public static final String FALL_NUMMER_QUERY = "fallNummer";

        /**
         * @param fallNummer (String)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper fallNummerQuery(Object... fallNummer) {
            reqSpec.addQueryParam(FALL_NUMMER_QUERY, fallNummer);
            return this;
        }

        public static final String NACHNAME_QUERY = "nachname";

        /**
         * @param nachname (String)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper nachnameQuery(Object... nachname) {
            reqSpec.addQueryParam(NACHNAME_QUERY, nachname);
            return this;
        }

        public static final String VORNAME_QUERY = "vorname";

        /**
         * @param vorname (String)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper vornameQuery(Object... vorname) {
            reqSpec.addQueryParam(VORNAME_QUERY, vorname);
            return this;
        }

        public static final String GEBURTSDATUM_QUERY = "geburtsdatum";

        /**
         * @param geburtsdatum (LocalDate)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper geburtsdatumQuery(Object... geburtsdatum) {
            reqSpec.addQueryParam(GEBURTSDATUM_QUERY, geburtsdatum);
            return this;
        }

        public static final String WOHNORT_QUERY = "wohnort";

        /**
         * @param wohnort (String)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper wohnortQuery(Object... wohnort) {
            reqSpec.addQueryParam(WOHNORT_QUERY, wohnort);
            return this;
        }

        public static final String DELEGIERUNG_ANGENOMMEN_QUERY = "delegierungAngenommen";

        /**
         * @param delegierungAngenommen (Boolean)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper delegierungAngenommenQuery(Object... delegierungAngenommen) {
            reqSpec.addQueryParam(DELEGIERUNG_ANGENOMMEN_QUERY, delegierungAngenommen);
            return this;
        }

        public static final String PAGE_QUERY = "page";

        /**
         * @param page (Integer)  (required)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper pageQuery(Object... page) {
            reqSpec.addQueryParam(PAGE_QUERY, page);
            return this;
        }

        public static final String PAGE_SIZE_QUERY = "pageSize";

        /**
         * @param pageSize (Integer)  (required)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper pageSizeQuery(Object... pageSize) {
            reqSpec.addQueryParam(PAGE_SIZE_QUERY, pageSize);
            return this;
        }

        public static final String SORT_COLUMN_QUERY = "sortColumn";

        /**
         * @param sortColumn (SozDashboardColumnDtoSpec)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper sortColumnQuery(Object... sortColumn) {
            reqSpec.addQueryParam(SORT_COLUMN_QUERY, sortColumn);
            return this;
        }

        public static final String SORT_ORDER_QUERY = "sortOrder";

        /**
         * @param sortOrder (SortOrderDtoSpec)  (optional)
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper sortOrderQuery(Object... sortOrder) {
            reqSpec.addQueryParam(SORT_ORDER_QUERY, sortOrder);
            return this;
        }

        /**
         * Customize request specification
         * @param reqSpecCustomizer consumer to modify the RequestSpecBuilder
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper reqSpec(Consumer<RequestSpecBuilder> reqSpecCustomizer) {
            reqSpecCustomizer.accept(reqSpec);
            return this;
        }

        /**
         * Customize response specification
         * @param respSpecCustomizer consumer to modify the ResponseSpecBuilder
         * @return operation
         */
        public GetDelegierungsOfSozialdienstOper respSpec(Consumer<ResponseSpecBuilder> respSpecCustomizer) {
            respSpecCustomizer.accept(respSpec);
            return this;
        }
    }
}
