import * as z from 'zod';

import { SharedModelState } from '@dv/shared/model/state-colors';

export const SACHBEARBEITER_APP_ROLES = [
  'Sachbearbeiter',
  'Admin',
  'Jurist',
] as const;
export type UsableRole = (typeof SACHBEARBEITER_APP_ROLES)[number];
export const SharedModelRoleList = z.array(
  z.object({ id: z.string(), name: z.string() }),
);
export type SharedModelRoleList = z.infer<typeof SharedModelRoleList>;
export type SharedModelRole = SharedModelRoleList[number];

export const SharedModelBenutzerApi = z.object({
  id: z.string(),
  username: z.string(),
  firstName: z.string(),
  lastName: z.string(),
  email: z.string().optional(),
});
export type SharedModelBenutzerApi = z.infer<typeof SharedModelBenutzerApi>;

export const SharedModelBenutzerList = z.array(SharedModelBenutzerApi);
export type SharedModelBenutzerList = z.infer<typeof SharedModelBenutzerList>;

export type SharedModelBenutzerRole = {
  name: UsableRole;
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
