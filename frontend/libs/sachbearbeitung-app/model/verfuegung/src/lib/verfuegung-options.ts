import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';

export type VerfuegungOption = {
  route: string;
  translationKey: SachbearbeitungAppTranslationKey;
  translationOptions?: Record<string, string>;
  titleTranslationKey: SachbearbeitungAppTranslationKey;
  iconSymbolName: string;
};

export const VERFUEGUNG_ROUTE = 'verfuegung';
export const OPTION_ZUSAMMENFASSUNG: VerfuegungOption = {
  route: 'zusammenfassung',
  translationKey: 'sachbearbeitung-app.verfuegung.option.zusammenfassung',
  titleTranslationKey: 'sachbearbeitung-app.verfuegung.option.zusammenfassung',
  iconSymbolName: 'equal',
};

export const BERECHNUNG_ROUTE = 'berechnung';
export const createBerechnungOption = (
  index: number,
  totalBerechnungen: number,
): VerfuegungOption => {
  const key =
    totalBerechnungen > 1
      ? 'sachbearbeitung-app.verfuegung.option.berechnung.withCounter'
      : 'sachbearbeitung-app.verfuegung.option.berechnung';

  return {
    route: `berechnung/${index + 1}`,
    translationKey: key,
    translationOptions: {
      counter: (index + 1).toString(),
    },
    titleTranslationKey:
      'sachbearbeitung-app.verfuegung.option.berechnung.title',
    iconSymbolName: 'description',
  };
};

export const VERFUEGUNG_OPTIONS = [OPTION_ZUSAMMENFASSUNG];
