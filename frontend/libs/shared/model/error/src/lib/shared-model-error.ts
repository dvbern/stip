import { HttpErrorResponse } from '@angular/common/http';
import { z } from 'zod';

import {
  ValidationError as DvValidationError,
  ValidationWarning as DvValidationWarning,
  ValidationReport,
} from '@dv/shared/model/gesuch';

type Extends<T, U extends T> = T extends U ? T : never;

export const GENERIC_REQUIRED_ERROR =
  '{jakarta.validation.constraints.NotNull.message}';

const ValidationMessage = z.object({
  messageTemplate: z.string(),
  message: z.string(),
  propertyPath: z
    .string()
    .nullable()
    .transform((value) => value ?? undefined),
});

export const ValidationError = ValidationMessage;

export type ValidationError = Extends<
  z.infer<typeof ValidationError>,
  DvValidationError
>;

export const ValidationWarning = ValidationMessage;

export type ValidationWarning = Extends<
  z.infer<typeof ValidationWarning>,
  DvValidationWarning
>;

export const UnknownHttpError = z.object({
  error: z.string().or(z.record(z.unknown())).or(z.null()),
  headers: z.unknown(),
  message: z.string(),
  name: z.string(),
  ok: z.boolean(),
  status: z.number(),
  statusText: z.string(),
  url: z.string().nullable(),
});

export type UnknownHttpError = Extends<
  z.infer<typeof UnknownHttpError>,
  HttpErrorResponse
>;

const ErrorTypes = {
  validationError: z.object({
    error: z.object({
      validationErrors: z.array(ValidationError),
      validationWarnings: z.array(ValidationWarning),
    }),
  }),
  unknownHttpError: UnknownHttpError,
  unknownError: z.unknown(),
};
type ErrorTypes = {
  [K in keyof typeof ErrorTypes]: Extends<
    z.infer<(typeof ErrorTypes)[K]>,
    ValidationReport
  >;
};

export type SharedModelValidationError = z.infer<typeof ValidationError>;

export type SharedModelErrorTypes = keyof typeof ErrorTypes;

export const SharedModelError = z.intersection(
  z.union([
    ErrorTypes.validationError.transform(
      ({ error: { validationErrors, validationWarnings } }) =>
        createError('validationError', {
          message: validationErrors[0]?.message,
          messageKey: 'shared.genericError.validation',
          validationErrors,
          validationWarnings,
        }),
    ),
    ErrorTypes.unknownHttpError.transform(({ error, ...http }) => {
      return createError('unknownHttpError', {
        ...http,
        messageKey: 'shared.genericError.http',
        error,
      });
    }),
    ErrorTypes.unknownError.transform((error) =>
      createError('unknownError', {
        messageKey: 'shared.genericError.general',
        error,
      }),
    ),
  ]),
  z.object({
    status: z
      .number()
      .or(z.string().transform((x) => +x))
      .optional(),
  }),
);

export type SharedModelError = z.infer<typeof SharedModelError>;

const createError = <K extends SharedModelErrorTypes, T>(
  type: K,
  data: T & { messageKey: string; message?: string },
) => ({
  type,
  ...data,
});
