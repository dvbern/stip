import {
  SharedModelError,
  SharedModelErrorTypes,
  ValidationError,
} from '@dv/shared/model/error';
import {
  AllFormSteps,
  GSFormStepProps,
  GSSteuererklaerungSteps,
  GesuchFormularType,
  SBFormStepProps,
  SBSteuerdatenSteps,
  ValidationMessage,
  ValidationReport,
} from '@dv/shared/model/gesuch';
import {
  FormPropsToStepsMap,
  FormRoutesToPropsMap,
  GesuchFormStep,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
import { isGesuchFormularProp } from '@dv/shared/util-fn/gesuch-util';

export function sharedUtilFnErrorTransformer(error: unknown): SharedModelError {
  const parsed = SharedModelError.safeParse(error);

  if (!parsed.success) {
    return {
      type: 'unknownError',
      error,
      message: 'Unknown Error',
      messageKey: 'shared.genericError.general',
    };
  }
  return parsed.data;
}

/**
 * Filter given {@link SharedModelError} by type
 * @example
 *
 *  errors
 *    .filter(byErrorType('validationError'))
 *    .map(e => e.validationErrors)
 *
 *  of(error)
 *    .pipe(
 *      filter(byErrorType('validationError')),
 *      map(e => e.validationErrors)
 *    )
 *
 *  switch (error.type) {
 *    case 'validationError':
 *      error.validationErrors;
 *  }
 *
 * @param type
 * @returns
 */
export const byErrorType = <K extends SharedModelErrorTypes>(type: K) => {
  return (
    error: SharedModelError,
  ): error is Extract<SharedModelError, { type: K }> => {
    return error.type === type;
  };
};

export const hasNoValidationErrors = (
  error: SharedModelError | undefined,
): boolean =>
  !error ||
  (error.type === 'validationError' && error.validationErrors.length === 0);

export const getValidationErrors = (error?: SharedModelError) => {
  if (error?.type === 'validationError') {
    return error.validationErrors;
  }
  return undefined;
};

export const transformValidationReportToFormSteps = (
  gesuchId: string | null,
  routeSuffix?: readonly string[],
  report?: ValidationReport,
  currentForm?: GesuchFormularType | null,
): (GesuchFormStep & { routeParts: string[] | null })[] => {
  if (!report || !gesuchId) {
    return [];
  }

  const messages: (ValidationMessage | ValidationError)[] =
    report.validationErrors.concat(report.validationWarnings);

  const steps = messages.map((m) => {
    const steuernValidtation = parseSteuernValidationError(m.propertyPath);
    if (steuernValidtation) {
      return currentForm?.steuerdatenTabs?.map(
        () => FormPropsToStepsMap[steuernValidtation],
      );
    }

    if (m.messageTemplate?.includes('documents.required')) {
      return FormPropsToStepsMap.dokuments;
    }

    if (m.propertyPath) {
      return FormPropsToStepsMap[m.propertyPath as AllFormSteps];
    }

    return undefined;
  });

  return steps
    .filter(isDefined)
    .flat()
    .reduce((acc, step) => {
      if (isDefined(step) && !acc.includes(step)) {
        acc.push(step);
      }
      return acc;
    }, [] as GesuchFormStep[])
    .sort((a, b) => {
      const stepsArr = Object.keys(FormRoutesToPropsMap);

      return stepsArr.indexOf(a.route) - stepsArr.indexOf(b.route);
    })
    .map((step) => {
      return {
        ...step,
        routeParts: routeSuffix
          ? ['/', 'gesuch', ...step.route.split('/'), gesuchId, ...routeSuffix]
          : null,
      };
    });
};

export const transformValidationMessagesToFormKeys = (
  messages?: ValidationMessage[],
  currentForm?: GesuchFormularType | null,
) => {
  const formKeys: AllFormSteps[] = [
    ...(Object.keys(currentForm ?? {}) as (
      | GSFormStepProps
      | SBFormStepProps
    )[]),
    'steuerdatenFamilie',
    'steuerdatenMutter',
    'steuerdatenVater',
    'steuererklaerungFamilie',
    'steuererklaerungMutter',
    'steuererklaerungVater',
    'dokuments',
  ];

  const ers = messages
    ?.filter(isDefined)
    .filter(
      (m) =>
        isGesuchFormularProp(formKeys)(m.propertyPath) ||
        isSpecialValidationError(m),
    )
    .map((m) => {
      if (m.messageTemplate?.includes('documents.required')) {
        return [{ ...m, propertyPath: 'dokuments' }, { ...m }];
      }
      return m;
    })
    .flat()
    .map((m) => m.propertyPath)
    .filter(isDefined)
    .filter(isGesuchFormularProp(formKeys));
  return ers;
};

const parseSteuernValidationError = (
  propertyPath?: string,
): GSSteuererklaerungSteps | SBSteuerdatenSteps | undefined => {
  if (
    !propertyPath ||
    (!propertyPath.startsWith('steuerdaten') &&
      !propertyPath.startsWith('steuererklaerung'))
  ) {
    return undefined;
  }

  return (propertyPath.split('.')[0] ?? propertyPath) as
    | GSSteuererklaerungSteps
    | SBSteuerdatenSteps;
};
