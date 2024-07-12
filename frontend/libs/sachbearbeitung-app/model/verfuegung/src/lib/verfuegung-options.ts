export type VerfuegungOption = {
  route: string;
  translationKey: string;
  translationOptions?: Record<string, string>;
  titleTranslationKey: string;
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
export const createBerechnungOption = (index: number): VerfuegungOption => ({
  route: 'berechnung/' + (index + 1),
  translationKey: 'sachbearbeitung-app.verfuegung.option.berechnung',
  translationOptions: { index: (index + 1).toString() },
  titleTranslationKey: 'sachbearbeitung-app.verfuegung.option.berechnung',
  iconSymbolName: 'description',
});

export const VERFUEGUNG_OPTIONS = [OPTION_ZUSAMMENFASSUNG];
