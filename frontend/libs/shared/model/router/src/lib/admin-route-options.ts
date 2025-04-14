import { AvailableBenutzerRole } from '@dv/shared/model/benutzer';

export type AdminOption = {
  type: 'PARENT';
  route: string;
  translationKey: string;
  titleTranslationKey: string;
  iconSymbolName: string;
  allowedRoles: AvailableBenutzerRole[];
};

export type ChildAdminOption = {
  type: 'CHILD';
  route: string;
  titleTranslationKey: string;
  parentRoute: string;
};
