import {
  BerechnungsStammdaten,
  FamilienBudgetresultat,
  PersoenlichesBudgetresultat,
} from '@dv/shared/model/gesuch';

export type TeilberechnungsArt = 'a' | 'b' | '';

export type VerminderteBerechnung = {
  monate: number;
  reduktionsBetrag: number;
  berechnungReduziert: number;
};

export interface PersoenlichesBudgetresultatView
  extends PersoenlichesBudgetresultat {
  typ: 'persoenlich';
  yearRange: string;
  name: string;
  gueltigAb: string;
  gueltigBis: string;
  geteilteBerechnung: {
    berechnungsanteilKinder: number;
    anteil: number;
  } | null;
}

export interface FamilienBudgetresultatView extends FamilienBudgetresultat {
  typ: 'familien';
  yearRange: string;
  name: string;
  gueltigAb: string;
  gueltigBis: string;
  anzahlMonate: number;
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
