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
import { SapStatus } from './sapStatus';

export interface BuchhaltungEntry { 
    timestampErstellt: string;
    stipendienBetrag?: number;
    auszahlung?: number;
    rueckforderung?: number;
    saldo: number;
    sapId?: number;
    sapStatus?: SapStatus;
    comment: string;
    verfuegungId?: string;
    gesuchId?: string;
}



