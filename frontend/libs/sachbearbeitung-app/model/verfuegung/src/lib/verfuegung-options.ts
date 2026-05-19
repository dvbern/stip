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
export const createBerechnungOption = (
  index: number,
  totalBerechnungen: number,
  startDate: string,
  endDate: string,
): VerfuegungOption => {
  const key =
    totalBerechnungen > 1
      ? 'sachbearbeitung-app.verfuegung.option.berechnung.withIndex'
      : 'sachbearbeitung-app.verfuegung.option.berechnung';

  return {
    route: `berechnung/${index + 1}`,
    translationKey: key,
    translationOptions: {
      index: (index + 1).toString(),
      startDate: startDate,
      endDate: endDate,
    },
    titleTranslationKey:
      'sachbearbeitung-app.verfuegung.option.berechnung.title',
    iconSymbolName: 'description',
  };
};

export const VERFUEGUNG_OPTIONS = [OPTION_ZUSAMMENFASSUNG];
