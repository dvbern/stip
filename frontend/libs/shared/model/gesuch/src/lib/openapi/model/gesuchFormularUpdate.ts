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
import { ElternUpdate } from './elternUpdate';
import { Darlehen } from './darlehen';
import { EinnahmenKostenUpdate } from './einnahmenKostenUpdate';
import { KindUpdate } from './kindUpdate';
import { FamiliensituationUpdate } from './familiensituationUpdate';
import { SteuererklaerungUpdate } from './steuererklaerungUpdate';
import { PartnerUpdate } from './partnerUpdate';
import { GeschwisterUpdate } from './geschwisterUpdate';
import { LebenslaufItemUpdate } from './lebenslaufItemUpdate';
import { PersonInAusbildungUpdate } from './personInAusbildungUpdate';

export interface GesuchFormularUpdate { 
    personInAusbildung?: PersonInAusbildungUpdate;
    familiensituation?: FamiliensituationUpdate;
    partner?: PartnerUpdate;
    elterns?: Array<ElternUpdate>;
    geschwisters?: Array<GeschwisterUpdate>;
    lebenslaufItems?: Array<LebenslaufItemUpdate>;
    kinds?: Array<KindUpdate>;
    einnahmenKosten?: EinnahmenKostenUpdate;
    darlehen?: Darlehen;
    steuererklaerung?: Array<SteuererklaerungUpdate>;
}

