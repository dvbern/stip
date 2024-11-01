export type InfosOptions = {
  route: string;
  translationKey: string;
  translationOptions?: Record<string, string>;
  titleTranslationKey: string;
  iconSymbolName: string;
};

export const INFOS_ROUTE = 'infos';
export const PROTOKOLL_ROUTE: InfosOptions = {
  route: 'protokoll',
  translationKey: 'sachbearbeitung-app.infos.protokoll',
  titleTranslationKey: 'sachbearbeitung-app.infos.protokoll',
  iconSymbolName: 'checklist',
};

export const NOTIZEN_ROUTE: InfosOptions = {
  route: 'notizen',
  translationKey: 'sachbearbeitung-app.infos.notizen',
  titleTranslationKey: 'sachbearbeitung-app.infos.notizen',
  iconSymbolName: 'description',
};

export const NOTIZEN_ROUTE_CREATE: InfosOptions = {
  route: 'notiz/create',
  translationKey: 'sachbearbeitung-app.infos.notizen.erstellen',
  titleTranslationKey: 'sachbearbeitung-app.infos.notizen',
  iconSymbolName: 'description',
};

export const NOTIZEN_ROUTE_DETAIL: InfosOptions = {
  route: 'notiz/:notizId',
  translationKey: 'sachbearbeitung-app.infos.notizen.detail',
  titleTranslationKey: 'sachbearbeitung-app.infos.notizen',
  iconSymbolName: 'description',
};

export const BUCHHALTUNG_ROUTE: InfosOptions = {
  route: 'buchhaltung',
  translationKey: 'sachbearbeitung-app.infos.buchhaltung',
  titleTranslationKey: 'sachbearbeitung-app.infos.buchhaltung',
  iconSymbolName: 'library_books',
};

export const DARLEHEN_ROUTE: InfosOptions = {
  route: 'darlehen',
  translationKey: 'sachbearbeitung-app.infos.darlehen',
  titleTranslationKey: 'sachbearbeitung-app.infos.darlehen',
  iconSymbolName: 'real_estate_agent',
};

export const BESCHWERDEN_ROUTE: InfosOptions = {
  route: 'beschwerden',
  translationKey: 'sachbearbeitung-app.infos.beschwerden',
  titleTranslationKey: 'sachbearbeitung-app.infos.beschwerden',
  iconSymbolName: 'exclamation',
};

export const INFOS_OPTIONS = [
  PROTOKOLL_ROUTE,
  NOTIZEN_ROUTE,
  BUCHHALTUNG_ROUTE,
  DARLEHEN_ROUTE,
  BESCHWERDEN_ROUTE,
];
