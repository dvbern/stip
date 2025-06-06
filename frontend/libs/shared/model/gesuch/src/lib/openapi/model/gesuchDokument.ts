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
import { Dokumentstatus } from './dokumentstatus';
import { CustomDokumentTyp } from './customDokumentTyp';
import { DokumentTyp } from './dokumentTyp';
import { Dokument } from './dokument';

export interface GesuchDokument { 
    id: string;
    dokumentTyp?: DokumentTyp;
    customDokumentTyp?: CustomDokumentTyp;
    dokumente: Array<Dokument>;
    status: Dokumentstatus;
}



