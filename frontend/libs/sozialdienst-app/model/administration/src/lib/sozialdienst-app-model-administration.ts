import { AdminOption, ChildAdminOption } from '@dv/shared/model/router';

export const OPTION_SOZIALDIENST_BENUTZER: AdminOption = {
  type: 'PARENT',
  route: 'sozialdienst-benutzer',
  translationKey: 'sozialdienst-app.admin.option.sozialdienstBenutzer',
  titleTranslationKey:
    'sozialdienst-app.admin.sozialdienstBenutzer.route.overview',
  iconSymbolName: 'people',
  allowedRoles: ['V0_Sozialdienst-Admin'],
};

export const OPTION_SOZIALDIENST_BENUTZER_DETAIL: ChildAdminOption = {
  type: 'CHILD',
  parentRoute: OPTION_SOZIALDIENST_BENUTZER.route,
  route: 'sozialdienst-benutzer',
  titleTranslationKey:
    'sozialdienst-app.admin.sozialdienstBenutzer.route.overview',
};

export const AdminOptions = [OPTION_SOZIALDIENST_BENUTZER];
