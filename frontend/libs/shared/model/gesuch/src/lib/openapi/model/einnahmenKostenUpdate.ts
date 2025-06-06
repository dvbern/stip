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

export interface EinnahmenKostenUpdate { 
    nettoerwerbseinkommen: number;
    /**
     * Required nur wenn mind. ein Elternteil Alimente zahlt
     */
    alimente?: number;
    /**
     * Required nur wenn mind. ein Kind gibt
     */
    zulagen?: number;
    /**
     * Required nur wenn mind. ein Elternteil gestorben ist
     */
    renten?: number;
    eoLeistungen?: number;
    ergaenzungsleistungen?: number;
    beitraege?: number;
    /**
     * The cost for the education calculated over the whole year
     */
    ausbildungskosten?: number;
    fahrkosten: number;
    wohnkosten?: number;
    /**
     * Required nur wenn Person eigener Haushalt hat
     */
    wgWohnend?: boolean;
    verdienstRealisiert: boolean;
    /**
     * Required nur wenn die Person keinen eigenen Haushalt führt
     */
    auswaertigeMittagessenProWoche?: number;
    /**
     * Betreuungskosten eigener Kinder
     */
    betreuungskostenKinder?: number;
    /**
     * 2-Stelliger Veranlagungscode (0-99)
     */
    veranlagungsCode?: number;
    /**
     * Aktuelles oder ein vergangenes Steuerjahr als 4-stellige Zahl. Default ist Vorjahr des Gesuchsjahrs
     */
    steuerjahr?: number;
    /**
     * Ganze Zahl, ohne Kommastellen
     */
    vermoegen?: number;
    /**
     * transient and calculated readonly field
     */
    steuernKantonGemeinde?: number;
}

