import {
  BerechnungsStammdaten,
  FamilienBudgetresultat,
  PersoenlichesBudgetresultat,
  PersoenlichesBudgetresultatEinnahmen,
  PersoenlichesBudgetresultatKosten,
  PersonValueItem,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';

export type TeilberechnungsArt = 'a' | 'b' | '';

export type VerminderteBerechnung = {
  monate: number;
  reduktionsBetrag: number;
  berechnungReduziert: number;
};

export type PersoenlichesBudgetresultatEinnahmenView =
  PersoenlichesBudgetresultatEinnahmen;

export interface PersoenlichesBudgetresultatKostenView
  extends PersoenlichesBudgetresultatKosten {
  // vornamePia: string;
  // vornamePartner: string;
  anzahlPersonenImHaushalt: number;
}

// TODO: Is proKopfTeilung new for totalVorTeilung?
export interface PersoenlichesBudgetresultatView
  extends PersoenlichesBudgetresultat {
  typ: 'persoenlich';
  yearRange: string;
  name: string;
  geburtsdatum: string;
  gueltigAb: string;
  gueltigBis: string;
  sozialversicherungsnummer: string;
  // TODO: Abklaeren, ob wir das so brauchen
  geteilteBerechnung: {
    berechnungsanteilKinder: number;
    anteil: number;
  } | null;
  einnahmen: PersoenlichesBudgetresultatEinnahmenView;
  kosten: PersoenlichesBudgetresultatKostenView;
}

// old
export type PersoenlicheBerechnung = {
  typ: 'persoenlich';
  name: string;
  sozialversicherungsnummer: string;
  geburtsdatum: string;
  total: number;
  yearRange: string;
  gueltigAb: string;
  gueltigBis: string;
  monate: number;
  berechnung: number;
  totalEinnahmen: number;
  totalKosten: number;
  totalVorTeilung?: number;
  geteilteBerechnung: {
    berechnungsanteilKinder: number;
    anteil: number;
  } | null;
  einnahmen: {
    vornamePia: string;
    vornamePartner: string;
    total: number;
    nettoerwerbseinkommen: number;
    nettoerwerbseinkommenPartner: number;
    nettoerwerbseinkommenTotal: number;
    einnahmenBGSA: number;
    kinderAusbildungszulagen: number;
    kinderAusbildungszulagenPartner: number;
    kinderAusbildungszulagenKinder: PersonValueItem[];
    kinderAusbildungszulagenTotal: number;
    unterhaltsbeitraege: number;
    unterhaltsbeitraegePartner: number;
    unterhaltsbeitraegeKinder: PersonValueItem[];
    unterhaltsbeitraegeTotal: number;
    eoLeistungen: number;
    eoLeistungenPartner: number;
    eoLeistungenTotal: number;
    taggelderAHVIV: number;
    taggelderAHVIVPartner: number;
    taggelderAHVIVTotal: number;
    renten: number;
    rentenPartner: number;
    rentenKinder: PersonValueItem[];
    rentenTotal: number;
    ergaenzungsleistungen: number;
    ergaenzungsleistungenPartner: number;
    ergaenzungsleistungenKinder: PersonValueItem[];
    ergaenzungsleistungenTotal: number;
    beitraegeGemeindeInstitutionen: number;
    andereEinnahmen: number;
    andereEinnahmenPartner: number;
    andereEinnahmenKinder: PersonValueItem[];
    andereEinnahmenTotal: number;
    anrechenbaresVermoegen: number;
    steuerbaresVermoegen: number;
    limiteAlterAntragsstellerHalbierungElternbeitrag: number;
  };
  kosten: {
    vornamePia: string;
    vornamePartner: string;
    anzahlPersonenImHaushalt: number;
    total: number;
    ausbildungskosten: number;
    fahrkosten: number;
    verpflegungskosten: number;
    grundbedarf: number;
    wohnkosten: number;
    medizinischeGrundversorgung: PersonValueItem[];
    fahrkostenPartner: number;
    verpflegungPartner: number;
    betreuungskostenKinder: number;
    kantonsGemeindesteuern: number;
    bundessteuern: number;
    anteilLebenshaltungskosten: number;
  };
};

// export interface FamilienBudgetresultatEinnahmenView
//   extends FamilienBudgetresultatEinnahmen {
//   name: string;
//   sozialversicherungsnummer: string;
//   geburtsdatum: string;
// }

// export interface FamilienBudgetresultatKostenView
//   extends FamilienBudgetresultatKosten {
//   name: string;
//   sozialversicherungsnummer: string;
//   geburtsdatum: string;
// }

// Todo: Ask Fabirce, why FamilienBudgetresultatKosten verpflegugn und partner are not PersonValueItem any more?
export interface FamilienBudgetresultatView extends FamilienBudgetresultat {
  typ: 'familien';
}

// old
export type FamilienBerechnung = {
  typ: 'familien';
  familienBudgetTyp: SteuerdatenTyp;
  name: string; // TODO: what if Familie?
  sozialversicherungsnummer: string; // TODO: what if Familie?
  geburtsdatum: string; // TODO: what if Familie?
  steuerjahr: number;
  veranlagungsStatus: string;
  gueltigAb: string;
  gueltigBis: string;
  monate: number;
  total: number;
  totalEinnahmen: number;
  totalKosten: number;
  einnahmen: {
    total: number;
    totalEinkuenfte: number;
    einnahmenBGSA: number;
    ergaenzungsleistungen: number;
    andereEinnahmen: number;
    eigenmietwert: number;
    unterhaltsbeitraege: number;
    sauele3: number;
    sauele2: number;
    renten: number;
    einkommensfreibetrag: number;
    einkommensfreibetragLimite: number;
    zwischentotal: number;
    anrechenbaresVermoegen: number;
    freibetragVermoegen: number;
    steuerbaresVermoegen: number;
  };
  kosten: {
    anzahlPersonenImHaushalt: number;
    total: number;
    grundbedarf: number;
    wohnkosten: number;
    medizinischeGrundversorgung: number;
    integrationszulage: number;
    integrationszulageAnzahl: number;
    integrationszulageLimite: number;
    integrationszulageTotal: number;
    kantonsGemeindesteuern: number;
    bundessteuern: number;
    fahrkosten: PersonValueItem[];
    verpflegung: PersonValueItem[];
  };
};

export type BerechnungPersonalOrFam =
  | PersoenlichesBudgetresultatView
  | FamilienBudgetresultatView;
export type BerechnungsValue = keyof Pick<
  BerechnungPersonalOrFam,
  'einnahmen' | 'kosten'
>;

export type BerechnungView = {
  persoenlich: PersoenlichesBudgetresultatView;
  familien: FamilienBudgetresultatView[];
  berechnung: number;
  berechnungsStammdaten: BerechnungsStammdaten;
};
