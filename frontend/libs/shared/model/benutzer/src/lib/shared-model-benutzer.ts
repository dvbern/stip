import * as z from 'zod';

export const SharedModelRoleList = z.array(
  z.object({ id: z.string(), name: z.string() }),
);
export type SharedModelRoleList = z.infer<typeof SharedModelRoleList>;
export type SharedModelRole = SharedModelRoleList[number];

export const SharedModelBenutzer = z.object({
  id: z.string(),
});
export type SharedModelBenutzer = z.infer<typeof SharedModelBenutzer>;

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
