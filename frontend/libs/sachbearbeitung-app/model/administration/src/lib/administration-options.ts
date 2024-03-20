export type AdminOption = {
  route: string;
  translationKey: string;
  titleTranslationKey: string;
  iconSymbolName: string;
};

export const OPTION_AUSBILDUNGSSTAETTE = {
  route: 'ausbildungsstaette',
  translationKey: 'sachbearbeitung-app.admin.option.ausbildungsstaette',
  titleTranslationKey:
    'sachbearbeitung-app.admin.option.ausbildungsstaetteTitle',
  iconSymbolName: 'school',
} satisfies AdminOption;

export const OPTION_BUCHSTABEN_ZUTEILUNG = {
  route: 'buchstaben-zuteilung',
  translationKey: 'sachbearbeitung-app.admin.option.buchstabenZuteilung',
  titleTranslationKey:
    'sachbearbeitung-app.admin.option.buchstabenZuteilungTitle',
  iconSymbolName: 'sort_by_alpha',
};

export const OPTION_GESUCHSPERIODEN = {
  route: 'gesuchsperioden',
  translationKey: 'sachbearbeitung-app.admin.option.gesuchsperioden',
  titleTranslationKey: 'sachbearbeitung-app.admin.option.gesuchsperiodenTitle',
  iconSymbolName: 'format_indent_increase',
};

export const AdminOptions = [
  OPTION_AUSBILDUNGSSTAETTE,
  OPTION_BUCHSTABEN_ZUTEILUNG,
  OPTION_GESUCHSPERIODEN,
];
