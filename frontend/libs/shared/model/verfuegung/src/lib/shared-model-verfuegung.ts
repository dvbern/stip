import { SharedTranslationKey } from '@dv/shared/assets/i18n';
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
  berechnungen: (TranchenBerechnungsresultat & {
    berechnungsanteilTotal: number;
  })[];
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

export type VerfuegungOption = {
  route: string;
  translationKey: SharedTranslationKey;
  translationOptions?: Record<string, string>;
  titleTranslationKey: SharedTranslationKey;
  iconSymbolName: string;
};

export const VERFUEGUNG_ROUTE = 'verfuegung';
export const OPTION_ZUSAMMENFASSUNG: VerfuegungOption = {
  route: 'zusammenfassung',
  translationKey: 'shared.verfuegung.option.zusammenfassung',
  titleTranslationKey: 'shared.verfuegung.option.zusammenfassung',
  iconSymbolName: 'equal',
};

export const BERECHNUNG_ROUTE = 'berechnung';
export const createBerechnungOption = (
  index: number,
  totalBerechnungen: number,
): VerfuegungOption => {
  const key =
    totalBerechnungen > 1
      ? 'shared.verfuegung.option.berechnung.withCounter'
      : 'shared.verfuegung.option.berechnung';

  return {
    route: `berechnung/${index + 1}`,
    translationKey: key,
    translationOptions: {
      counter: (index + 1).toString(),
    },
    titleTranslationKey: 'shared.verfuegung.option.berechnung.title',
    iconSymbolName: 'description',
  };
};

export const VERFUEGUNG_OPTIONS = [OPTION_ZUSAMMENFASSUNG];
