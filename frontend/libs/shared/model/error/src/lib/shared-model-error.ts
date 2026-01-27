import { HttpErrorResponse } from '@angular/common/http';
import { z } from 'zod';

import {
  DemoDataError as DvDemoDataError,
  NeskoError as DvNeskoError,
  ValidationMessage as DvValidationMessage,
  ValidationReport,
} from '@dv/shared/model/gesuch';
import { Extends } from '@dv/shared/model/type-util';

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
  DvValidationMessage
>;

export const ValidationWarning = ValidationMessage;

export type ValidationWarning = Extends<
  z.infer<typeof ValidationWarning>,
  DvValidationMessage
>;

export const GenericValidationError = z.object({
  type: z.literal('VALIDATION'),
  violations: z.array(
    z.object({
      path: z.string(),
      key: z.string(),
      message: z.string(),
    }),
  ),
});
export type GenericValidationError = z.infer<typeof GenericValidationError>;

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

export const NeskoError = z.object({
  type: z.string(),
  neskoError: z.string(),
  userMessage: z.string(),
});
export type NeskoError = Extends<z.infer<typeof NeskoError>, DvNeskoError>;

export const DemoDataError = z.object({
  internalMessage: z.string(),
  errorClass: z.string(),
  validationErrors: z.array(ValidationError).optional().nullable(),
});
export type DemoDataError = Extends<
  z.infer<typeof DemoDataError>,
  DvDemoDataError
>;

export const ParseError = z.instanceof(z.ZodError);
export type ParseError = z.infer<typeof ParseError>;

const ErrorTypes = {
  neskoError: z.object({
    error: NeskoError,
  }),
  demoDataError: z.object({
    error: DemoDataError,
  }),
  genericValidationError: z.object({
    error: GenericValidationError,
  }),
  validationError: z.object({
    error: z.object({
      validationErrors: z.array(ValidationError),
      validationWarnings: z.array(ValidationWarning),
      hasDocuments: z.boolean().nullable(),
    }),
  }),
  unknownHttpError: UnknownHttpError,
  zodError: ParseError,
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
    ErrorTypes.neskoError.transform(({ error: { neskoError, userMessage } }) =>
      createError('neskoError', {
        message: userMessage,
        messageKey: 'shared.genericError.nesko',
        errorCode: neskoError,
      }),
    ),
    ErrorTypes.genericValidationError.transform(({ error }) =>
      createError('genericValidationError', {
        messageKey: 'shared.genericError.validation',
        messageKeys: error.violations.map((e) => e.key),
        validationErrors: error.violations,
      }),
    ),
    ErrorTypes.validationError.transform(
      ({ error: { validationErrors, validationWarnings, hasDocuments } }) =>
        createError('validationError', {
          message: validationErrors[0]?.message,
          messageKey:
            validationErrors[0]?.messageTemplate ??
            'shared.genericError.validation',
          messageKeys: validationErrors.map((e) => e.messageTemplate),
          validationErrors,
          validationWarnings,
          hasDocuments,
        }),
    ),
    ErrorTypes.demoDataError.transform(({ error }) =>
      createError('demoDataError', {
        messageKey: 'demo-data-app.genericError.demoData',
        internalMessage: error.internalMessage,
        errorClass: error.errorClass,
        validationErrors: error.validationErrors,
      }),
    ),
    ErrorTypes.unknownHttpError.transform(({ error, ...http }) => {
      return createError('unknownHttpError', {
        ...http,
        messageKey: 'shared.genericError.http',
        error,
      });
    }),
    ErrorTypes.zodError.transform((error) =>
      createError('zodError', {
        message: `Zod Parse Error:\n${JSON.stringify(error.format(), null, 2)}`,
        messageKey: 'shared.genericError.validation',
        errors: error.issues,
      }),
    ),
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

export const isSharedModelError = (
  error: unknown,
): error is SharedModelError => {
  return SharedModelError.safeParse(error).success;
};

const createError = <K extends SharedModelErrorTypes, T>(
  type: K,
  data: T & { messageKey: string; messageKeys?: string[]; message?: string },
) => ({
  type,
  ...data,
});
