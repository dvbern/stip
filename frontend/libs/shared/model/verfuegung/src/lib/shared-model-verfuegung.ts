import {
  BerechnungsStammdaten,
  FamilienBudgetresultat,
  FamilienBudgetresultatKosten,
  PersoenlichesBudgetresultat,
  PersoenlichesBudgetresultatEinnahmen,
  PersoenlichesBudgetresultatKosten,
} from '@dv/shared/model/gesuch';

export type TeilberechnungsArt = 'a' | 'b' | '';

export type VerminderteBerechnung = {
  monate: number;
  reduktionsBetrag: number;
  berechnungReduziert: number;
};

// todo: necessary?
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

// export interface FamilienBudgetresultatEinnahmenView
//   extends FamilienBudgetresultatEinnahmen {
//   name: string;
//   sozialversicherungsnummer: string;
//   geburtsdatum: string;
// }

export interface FamilienBudgetresultatKostenView
  extends FamilienBudgetresultatKosten {
  // name: string;
  // sozialversicherungsnummer: string;
  // geburtsdatum: string;
  anzahlPersonenImHaushalt: number;
  anzahlKinderInAusbildung: number;
}

// Todo: Ask Fabirce, why FamilienBudgetresultatKosten verpflegugn und partner are not PersonValueItem any more?
export interface FamilienBudgetresultatView extends FamilienBudgetresultat {
  typ: 'familien';
  kosten: FamilienBudgetresultatKostenView;
}

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

// name: string; // TODO: what if Familie?
//   sozialversicherungsnummer: string; // TODO: what if Familie?
//   geburtsdatum: string; // TODO: what if Familie?
