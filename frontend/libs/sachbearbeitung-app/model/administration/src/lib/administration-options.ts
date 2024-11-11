export type AdminOption = {
  type: 'PARENT';
  route: string;
  translationKey: string;
  titleTranslationKey: string;
  iconSymbolName: string;
};

export const OPTION_AUSBILDUNGSSTAETTE: AdminOption = {
  type: 'PARENT',
  route: 'ausbildungsstaette',
  translationKey: 'sachbearbeitung-app.admin.option.ausbildungsstaette',
  titleTranslationKey:
    'sachbearbeitung-app.admin.ausbildungsstaette.route.overview',
  iconSymbolName: 'school',
};

export const OPTION_BUCHSTABEN_ZUTEILUNG: AdminOption = {
  type: 'PARENT',
  route: 'buchstaben-zuteilung',
  translationKey: 'sachbearbeitung-app.admin.option.buchstabenZuteilung',
  titleTranslationKey:
    'sachbearbeitung-app.admin.buchstabenZuteilung.route.overview',
  iconSymbolName: 'sort_by_alpha',
};

export const OPTION_GESUCHSPERIODEN: AdminOption = {
  type: 'PARENT',
  route: 'gesuchsperioden',
  translationKey: 'sachbearbeitung-app.admin.option.gesuchsperioden',
  titleTranslationKey:
    'sachbearbeitung-app.admin.gesuchsperiode.route.overview',
  iconSymbolName: 'format_indent_increase',
};

export const OPTION_EU_EFTA_LAENDER: AdminOption = {
  type: 'PARENT',
  route: 'eu-efta-laender',
  translationKey: 'sachbearbeitung-app.admin.option.euEftaLaender',
  titleTranslationKey: 'sachbearbeitung-app.admin.euEftaLaender.route.overview',
  iconSymbolName: 'public',
};

export const OPTION_BENUTZERVERWALTUNG: AdminOption = {
  type: 'PARENT',
  route: 'benutzerverwaltung',
  translationKey: 'sachbearbeitung-app.admin.option.benutzerverwaltung',
  titleTranslationKey:
    'sachbearbeitung-app.admin.benutzerverwaltung.route.overview',
  iconSymbolName: 'people',
};

export const AdminOptions = [
  OPTION_AUSBILDUNGSSTAETTE,
  OPTION_BUCHSTABEN_ZUTEILUNG,
  OPTION_GESUCHSPERIODEN,
  OPTION_EU_EFTA_LAENDER,
  OPTION_BENUTZERVERWALTUNG,
];

export type ChildAdminOption = {
  type: 'CHILD';
  route: string;
  titleTranslationKey: string;
  parentRoute: string;
};

export const CHILD_OPTION_GESUCHSJAHRE = (
  titleTranslationKey: string,
): ChildAdminOption => ({
  type: 'CHILD',
  route: 'jahr',
  titleTranslationKey,
  parentRoute: OPTION_GESUCHSPERIODEN.route,
});

export const CHILD_OPTION_GESUCHSPERIODE = (
  titleTranslationKey: string,
): ChildAdminOption => ({
  type: 'CHILD',
  route: 'gesuchsperiode',
  titleTranslationKey,
  parentRoute: OPTION_GESUCHSPERIODEN.route,
});

export const CHILD_OPTION_BENUTZER_ERSTELLEN = (
  titleTranslationKey: string,
): ChildAdminOption => ({
  type: 'CHILD',
  route: 'erstellen',
  titleTranslationKey,
  parentRoute: OPTION_BENUTZERVERWALTUNG.route,
});
