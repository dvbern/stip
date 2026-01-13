import * as z from 'zod';

import { Sachbearbeiter } from '@dv/shared/model/gesuch';
import { SharedModelState } from '@dv/shared/model/state-colors';

export const GESUCHSTELLER_ROLES = ['V0_Gesuchsteller'] as const;

export const SPECIAL_PERMISSIONS = [
  'V0_Super-User',
  'V0_DEMO_DATA_APPLY',
] as const;

export const BENUTZER_ROLES = [
  'V0_Sachbearbeiter-Admin',
  'V0_Sachbearbeiter',
  'V0_Freigabestelle',
  'V0_Jurist',
] as const;

export const SOZIALDIENST_ADMIN_ROLE = 'V0_Sozialdienst-Admin';

export const SOZIALDIENST_BENUTZER_ROLES = [
  SOZIALDIENST_ADMIN_ROLE,
  'V0_Sozialdienst-Mitarbeiter',
] as const;

export type SpecialPermission = (typeof SPECIAL_PERMISSIONS)[number];
export type BenutzerRole = (typeof BENUTZER_ROLES)[number];
export type SozialdienstBenutzerRole =
  (typeof SOZIALDIENST_BENUTZER_ROLES)[number];
export type GesuchstellerRole = (typeof GESUCHSTELLER_ROLES)[number];
export type AvailableBenutzerRole =
  | SpecialPermission
  | BenutzerRole
  | SozialdienstBenutzerRole
  | GesuchstellerRole;
export type RolesMap = Partial<Record<AvailableBenutzerRole, true>>;

export const SharedModelBenutzerKeycloak = z.object({
  id: z.string(),
  username: z.string().optional(),
  firstName: z.string(),
  lastName: z.string(),
  email: z.string().optional(),
});
export type SharedModelBenutzerKeycloak = z.infer<
  typeof SharedModelBenutzerKeycloak
>;

export interface KeycloakUserCreate {
  vorname: string;
  name: string;
  email: string;
}

export const SharedModelBenutzerList = z.array(SharedModelBenutzerKeycloak);
export type SharedModelBenutzerList = z.infer<typeof SharedModelBenutzerList>;

export type SharedModelBenutzerRole = {
  name: BenutzerRole;
  color: SharedModelState;
};

export type SharedModelSachbearbeiter = Sachbearbeiter & {
  name: string;
  roles: {
    raw: BenutzerRole[];
    extraAmount?: number;
    compact: SharedModelBenutzerRole[];
    full: SharedModelBenutzerRole[];
  };
};

export const SharedModelUserAdminError = z.union([
  z
    .object({
      error: z.object({
        errorMessage: z.string().refine((v) => v.includes('same email')),
      }),
    })
    .transform((userError) => ({ type: 'emailExists' as const, userError })),
  z
    .object({
      error: z.object({
        errorMessage: z.string(),
      }),
    })
    .transform((userError) => ({ type: undefined, userError })),
  z.unknown().transform((error) => ({ type: undefined, error })),
]);
export type SharedModelUserAdminError = z.infer<
  typeof SharedModelUserAdminError
>;

export const toKnownUserErrorType = (error: unknown, fallbackType: string) => {
  const parsed = SharedModelUserAdminError.parse(error);
  return parsed.type ?? fallbackType;
};

export const roleToStateColor = (role: BenutzerRole): SharedModelState => {
  switch (role) {
    case 'V0_Sachbearbeiter':
      return 'info';
    case 'V0_Sachbearbeiter-Admin':
      return 'success';
    case 'V0_Jurist':
      return 'warning';
    default:
      return 'danger';
  }
};

const MAX_ROLES_SHOWN = 2;

export const mapToSachbearbeiterWithKnownRoles = (
  sachbearbeiter: Sachbearbeiter,
): SharedModelSachbearbeiter => {
  const roles = sachbearbeiter.sachbearbeiterRollen.filter(
    isKnownSachbearbeiterRole,
  );
  const colorMatchedRoles = roles.map((role) => ({
    name: role,
    color: roleToStateColor(role),
  }));
  return {
    ...sachbearbeiter,
    name: `${sachbearbeiter.vorname} ${sachbearbeiter.nachname}`,
    roles: {
      raw: roles,
      extraAmount:
        roles.length > MAX_ROLES_SHOWN ? roles.length - 2 : undefined,
      compact: colorMatchedRoles.slice(0, 2),
      full: colorMatchedRoles,
    },
  };
};

export const isKnownSachbearbeiterRole = (role: string): role is BenutzerRole =>
  BENUTZER_ROLES.includes(role as BenutzerRole);
