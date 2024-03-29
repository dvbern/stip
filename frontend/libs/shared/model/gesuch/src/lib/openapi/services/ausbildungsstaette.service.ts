/**
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
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent, HttpParameterCodec, HttpContext 
        }       from '@angular/common/http';
import { CustomHttpParameterCodec }                          from '../encoder';
import { Observable }                                        from 'rxjs';

import { Ausbildungsstaette } from '../model/ausbildungsstaette';
import { AusbildungsstaetteCreate } from '../model/ausbildungsstaetteCreate';
import { AusbildungsstaetteUpdate } from '../model/ausbildungsstaetteUpdate';
import { ValidationReport } from '../model/validationReport';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


export interface AusbildungsstaetteServiceCreateAusbildungsstaetteRequestParams {
    ausbildungsstaetteCreate?: AusbildungsstaetteCreate;
}

export interface AusbildungsstaetteServiceDeleteAusbildungsstaetteRequestParams {
    ausbildungsstaetteId: string;
}

export interface AusbildungsstaetteServiceGetAusbildungsstaetteRequestParams {
    ausbildungsstaetteId: string;
}

export interface AusbildungsstaetteServiceUpdateAusbildungsstaetteRequestParams {
    ausbildungsstaetteId: string;
    ausbildungsstaetteUpdate?: AusbildungsstaetteUpdate;
}


@Injectable({
  providedIn: 'root'
})
export class AusbildungsstaetteService {

    protected basePath = '/api/v1';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();
    public encoder: HttpParameterCodec;

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string|string[], @Optional() configuration: Configuration) {
        if (configuration) {
            this.configuration = configuration;
        }
        if (typeof this.configuration.basePath !== 'string') {
            if (Array.isArray(basePath) && basePath.length > 0) {
                basePath = basePath[0];
            }

            if (typeof basePath !== 'string') {
                basePath = this.basePath;
            }
            this.configuration.basePath = basePath;
        }
        this.encoder = this.configuration.encoder || new CustomHttpParameterCodec();
    }


    private addToHttpParams(httpParams: HttpParams, value: any, key?: string): HttpParams {
        if (typeof value === "object" && value instanceof Date === false) {
            httpParams = this.addToHttpParamsRecursive(httpParams, value);
        } else {
            httpParams = this.addToHttpParamsRecursive(httpParams, value, key);
        }
        return httpParams;
    }

    private addToHttpParamsRecursive(httpParams: HttpParams, value?: any, key?: string): HttpParams {
        if (value == null) {
            return httpParams;
        }

        if (typeof value === "object") {
            if (Array.isArray(value)) {
                (value as any[]).forEach( elem => httpParams = this.addToHttpParamsRecursive(httpParams, elem, key));
            } else if (value instanceof Date) {
                if (key != null) {
                    httpParams = httpParams.append(key, (value as Date).toISOString().substr(0, 10));
                } else {
                   throw Error("key may not be null if value is Date");
                }
            } else {
                Object.keys(value).forEach( k => httpParams = this.addToHttpParamsRecursive(
                    httpParams, value[k], key != null ? `${key}.${k}` : k));
            }
        } else if (key != null) {
            httpParams = httpParams.append(key, value);
        } else {
            throw Error("key may not be null if value is not object or array");
        }
        return httpParams;
    }

    /**
     * @param requestParameters
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
     public createAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceCreateAusbildungsstaetteRequestParams, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<Ausbildungsstaette>;
     public createAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceCreateAusbildungsstaetteRequestParams, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpResponse<Ausbildungsstaette>>;
     public createAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceCreateAusbildungsstaetteRequestParams, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpEvent<Ausbildungsstaette>>;
     public createAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceCreateAusbildungsstaetteRequestParams, observe: 'body' | 'response' | 'events' = 'body', reportProgress = false, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<any> {
        const ausbildungsstaetteCreate = requestParameters.ausbildungsstaetteCreate;

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (auth-uat-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-uat-bern');
        if (localVarCredential) {
            // using credentials
        }

        // authentication (auth-dev-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-dev-bern');
        if (localVarCredential) {
            // using credentials
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/json',
                'text/plain'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Content-Type', httpContentTypeSelected);
        }

        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        const localVarPath = `/ausbildungsstaette`;
        return this.httpClient.request<Ausbildungsstaette>('post', `${this.configuration.basePath}${localVarPath}`,
            {
                context: localVarHttpContext,
                body: ausbildungsstaetteCreate,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: <any>observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Delete Ausbildungsstaette
     * @param requestParameters
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
     public deleteAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceDeleteAusbildungsstaetteRequestParams, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'text/plain', context?: HttpContext}): Observable<any>;
     public deleteAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceDeleteAusbildungsstaetteRequestParams, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'text/plain', context?: HttpContext}): Observable<HttpResponse<any>>;
     public deleteAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceDeleteAusbildungsstaetteRequestParams, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'text/plain', context?: HttpContext}): Observable<HttpEvent<any>>;
     public deleteAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceDeleteAusbildungsstaetteRequestParams, observe: 'body' | 'response' | 'events' = 'body', reportProgress = false, options?: {httpHeaderAccept?: 'text/plain', context?: HttpContext}): Observable<any> {
        const ausbildungsstaetteId = requestParameters.ausbildungsstaetteId;
        if (ausbildungsstaetteId === null || ausbildungsstaetteId === undefined) {
            throw new Error('Required parameter ausbildungsstaetteId was null or undefined when calling deleteAusbildungsstaette$.');
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (auth-uat-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-uat-bern');
        if (localVarCredential) {
            // using credentials
        }

        // authentication (auth-dev-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-dev-bern');
        if (localVarCredential) {
            // using credentials
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'text/plain'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        const localVarPath = `/ausbildungsstaette/${this.configuration.encodeParam({name: "ausbildungsstaetteId", value: ausbildungsstaetteId, in: "path", style: "simple", explode: false, dataType: "string", dataFormat: "uuid"})}`;
        return this.httpClient.request<any>('delete', `${this.configuration.basePath}${localVarPath}`,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: <any>observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * returns a Ausbildungsstaette with the given id
     * @param requestParameters
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
     public getAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceGetAusbildungsstaetteRequestParams, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<Ausbildungsstaette>;
     public getAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceGetAusbildungsstaetteRequestParams, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpResponse<Ausbildungsstaette>>;
     public getAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceGetAusbildungsstaetteRequestParams, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpEvent<Ausbildungsstaette>>;
     public getAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceGetAusbildungsstaetteRequestParams, observe: 'body' | 'response' | 'events' = 'body', reportProgress = false, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<any> {
        const ausbildungsstaetteId = requestParameters.ausbildungsstaetteId;
        if (ausbildungsstaetteId === null || ausbildungsstaetteId === undefined) {
            throw new Error('Required parameter ausbildungsstaetteId was null or undefined when calling getAusbildungsstaette$.');
        }

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (auth-uat-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-uat-bern');
        if (localVarCredential) {
            // using credentials
        }

        // authentication (auth-dev-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-dev-bern');
        if (localVarCredential) {
            // using credentials
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/json',
                'text/plain'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        const localVarPath = `/ausbildungsstaette/${this.configuration.encodeParam({name: "ausbildungsstaetteId", value: ausbildungsstaetteId, in: "path", style: "simple", explode: false, dataType: "string", dataFormat: "uuid"})}`;
        return this.httpClient.request<Ausbildungsstaette>('get', `${this.configuration.basePath}${localVarPath}`,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: <any>observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Returniert alle Ausbildungsstaette zur Verfuegung.
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
     public getAusbildungsstaetten$(observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<Array<Ausbildungsstaette>>;
     public getAusbildungsstaetten$(observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpResponse<Array<Ausbildungsstaette>>>;
     public getAusbildungsstaetten$(observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpEvent<Array<Ausbildungsstaette>>>;
     public getAusbildungsstaetten$(observe: 'body' | 'response' | 'events' = 'body', reportProgress = false, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<any> {

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (auth-uat-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-uat-bern');
        if (localVarCredential) {
            // using credentials
        }

        // authentication (auth-dev-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-dev-bern');
        if (localVarCredential) {
            // using credentials
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/json',
                'text/plain'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        const localVarPath = `/ausbildungsstaette`;
        return this.httpClient.request<Array<Ausbildungsstaette>>('get', `${this.configuration.basePath}${localVarPath}`,
            {
                context: localVarHttpContext,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: <any>observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * @param requestParameters
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
     public updateAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceUpdateAusbildungsstaetteRequestParams, observe?: 'body', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<Ausbildungsstaette>;
     public updateAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceUpdateAusbildungsstaetteRequestParams, observe?: 'response', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpResponse<Ausbildungsstaette>>;
     public updateAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceUpdateAusbildungsstaetteRequestParams, observe?: 'events', reportProgress?: boolean, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<HttpEvent<Ausbildungsstaette>>;
     public updateAusbildungsstaette$(requestParameters: AusbildungsstaetteServiceUpdateAusbildungsstaetteRequestParams, observe: 'body' | 'response' | 'events' = 'body', reportProgress = false, options?: {httpHeaderAccept?: 'application/json' | 'text/plain', context?: HttpContext}): Observable<any> {
        const ausbildungsstaetteId = requestParameters.ausbildungsstaetteId;
        if (ausbildungsstaetteId === null || ausbildungsstaetteId === undefined) {
            throw new Error('Required parameter ausbildungsstaetteId was null or undefined when calling updateAusbildungsstaette$.');
        }
        const ausbildungsstaetteUpdate = requestParameters.ausbildungsstaetteUpdate;

        let localVarHeaders = this.defaultHeaders;

        let localVarCredential: string | undefined;
        // authentication (auth-uat-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-uat-bern');
        if (localVarCredential) {
            // using credentials
        }

        // authentication (auth-dev-bern) required
        localVarCredential = this.configuration.lookupCredential('auth-dev-bern');
        if (localVarCredential) {
            // using credentials
        }

        let localVarHttpHeaderAcceptSelected: string | undefined = options && options.httpHeaderAccept;
        if (localVarHttpHeaderAcceptSelected === undefined) {
            // to determine the Accept header
            const httpHeaderAccepts: string[] = [
                'application/json',
                'text/plain'
            ];
            localVarHttpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        }
        if (localVarHttpHeaderAcceptSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Accept', localVarHttpHeaderAcceptSelected);
        }

        let localVarHttpContext: HttpContext | undefined = options && options.context;
        if (localVarHttpContext === undefined) {
            localVarHttpContext = new HttpContext();
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            localVarHeaders = localVarHeaders.set('Content-Type', httpContentTypeSelected);
        }

        let responseType_: 'text' | 'json' | 'blob' = 'json';
        if (localVarHttpHeaderAcceptSelected) {
            if (localVarHttpHeaderAcceptSelected.startsWith('text')) {
                responseType_ = 'text';
            } else if (this.configuration.isJsonMime(localVarHttpHeaderAcceptSelected)) {
                responseType_ = 'json';
            } else {
                responseType_ = 'blob';
            }
        }

        const localVarPath = `/ausbildungsstaette/${this.configuration.encodeParam({name: "ausbildungsstaetteId", value: ausbildungsstaetteId, in: "path", style: "simple", explode: false, dataType: "string", dataFormat: "uuid"})}`;
        return this.httpClient.request<Ausbildungsstaette>('patch', `${this.configuration.basePath}${localVarPath}`,
            {
                context: localVarHttpContext,
                body: ausbildungsstaetteUpdate,
                responseType: <any>responseType_,
                withCredentials: this.configuration.withCredentials,
                headers: localVarHeaders,
                observe: <any>observe,
                reportProgress: reportProgress
            }
        );
    }

}
