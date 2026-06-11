import {
  BerechnungsStammdaten,
  FamilienBudgetresultat,
  PersoenlichesBudgetresultat,
  PersonenHaushaltGruppe,
  TranchenBerechnungsresultat,
} from '@dv/shared/model/gesuch';

export type TranchenBerechnungsresultatView = {
  gesuchTrancheId: string;
  startDate: string;
  endDate: string;
  anzahlMonate: number;
  berechnungen: TranchenBerechnungsresultat[];
  total: number;
};

export interface PersoenlichesBudgetresultatView extends PersoenlichesBudgetresultat {
  typ: 'persoenlich';
  yearRange: string;
  name: string;
  gueltigAb: string;
  gueltigBis: string;
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
  personenHaushaltGroups: PersonenHaushaltGruppe[];
  persoenlich: PersoenlichesBudgetresultatView;
  familien: FamilienBudgetresultatView[];
  berechnungsStammdaten: BerechnungsStammdaten;
};
