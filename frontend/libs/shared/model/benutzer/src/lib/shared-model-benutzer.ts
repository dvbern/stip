import * as z from 'zod';

import { SharedModelState } from '@dv/shared/model/state-colors';

export const BENUTZER_VERWALTUNG_ROLES = [
  'Admin',
  'Sachbearbeiter',
  'Jurist',
] as const;

export const SOZIALDIENST_ADMIN_ROLE = 'Sozialdienst-Admin';

export type BenutzerVerwaltungRole = (typeof BENUTZER_VERWALTUNG_ROLES)[number];

export const SharedModelRoleList = z.array(
  z.object({ id: z.string(), name: z.string() }),
);
export type SharedModelRoleList = z.infer<typeof SharedModelRoleList>;
export type SharedModelRole = SharedModelRoleList[number];

export const SharedModelBenutzerApi = z.object({
  id: z.string(),
  username: z.string().optional(),
  firstName: z.string(),
  lastName: z.string(),
  email: z.string().optional(),
});
export type SharedModelBenutzerApi = z.infer<typeof SharedModelBenutzerApi>;

export interface KeycloakUserCreate {
  vorname: string;
  name: string;
  email: string;
}

export const byBenutzertVerwaltungRoles = (role: SharedModelRole) => {
  return BENUTZER_VERWALTUNG_ROLES.some((r) => r === role.name);
};

export const bySozialdienstAdminRole = (role: SharedModelRole) => {
  return role.name === SOZIALDIENST_ADMIN_ROLE;
};

export const SharedModelBenutzerList = z.array(SharedModelBenutzerApi);
export type SharedModelBenutzerList = z.infer<typeof SharedModelBenutzerList>;

export type SharedModelBenutzerWithRoles = SharedModelBenutzerApi & {
  roles: SharedModelRoleList;
};

export type SharedModelBenutzerRole = {
  name: BenutzerVerwaltungRole;
  color: SharedModelState;
};

export type SharedModelBenutzer = SharedModelBenutzerApi & {
  name: string;
  roles: {
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

export const SharedModelClientMappingsRepresentation = z.object({
  id: z.string().optional(),
  client: z.string().optional(),
  mappings: SharedModelRoleList.optional(),
});
export type SharedModelClientMappingsRepresentation = z.infer<
  typeof SharedModelClientMappingsRepresentation
>;

export const SharedModelModelMappingsRepresentation = z.object({
  clientMappings: z
    .record(z.string(), SharedModelClientMappingsRepresentation)
    .optional(),
  realmMappings: SharedModelRoleList.optional(),
});
export type SharedModelModelMappingsRepresentation = z.infer<
  typeof SharedModelModelMappingsRepresentation
>;
