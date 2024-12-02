import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { map, pipe, switchMap, tap, withLatestFrom } from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import {
  SharedDataAccessGesuchEvents,
  isGesuchFormularProp,
  selectSharedDataAccessGesuchCache,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { toAbschlussPhase } from '@dv/shared/model/einreichen';
import { SharedModelError, ValidationError } from '@dv/shared/model/error';
import {
  GesuchService,
  GesuchTrancheService,
  SharedModelGesuchFormular,
  SharedModelGesuchFormularPropsSteuerdatenSteps,
  ValidationMessage,
  ValidationReport,
} from '@dv/shared/model/gesuch';
import {
  SPECIAL_VALIDATION_ERRORS,
  SharedModelGesuchFormStep,
  gesuchFormStepsFieldMap,
  gesuchPropFormStepsMap,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
import { shouldIgnoreErrorsIf } from '@dv/shared/util/http';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isPending,
  pending,
} from '@dv/shared/util/remote-data';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

type EinreichenState = {
  validationResult: CachedRemoteData<ValidationReport>;
  einreichenValidationResult: CachedRemoteData<ValidationReport>;
  einreichungsResult: RemoteData<unknown>;
  trancheEinreichenResult: RemoteData<unknown>;
};

const initialState: EinreichenState = {
  validationResult: initial(),
  einreichenValidationResult: initial(),
  einreichungsResult: initial(),
  trancheEinreichenResult: initial(),
};

@Injectable({ providedIn: 'root' })
export class EinreichenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('EinreichenStore'),
) {
  private store = inject(Store);
  private gesuchService = inject(GesuchService);
  private gesuchTrancheService = inject(GesuchTrancheService);
  private gesuchViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchsView,
  );
  private cachedGesuchViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchCache,
  );
  private sharedDataAccessConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

  validationViewSig = computed(() => {
    const { gesuch, lastUpdate } = this.gesuchViewSig();
    const { gesuch: cachedGesuch } = this.cachedGesuchViewSig();
    const validationReport = this.validationResult.data();
    const currentForm = (gesuch ?? cachedGesuch)?.gesuchTrancheToWorkWith
      .gesuchFormular;

    return {
      invalidFormularProps: {
        lastUpdate,
        validations: {
          errors: transformValidationMessagesToFormKeys(
            validationReport?.validationErrors,
            currentForm,
          ),
          warnings: transformValidationMessagesToFormKeys(
            validationReport?.validationWarnings,
            currentForm,
          ),
          hasDocuments: validationReport?.hasDocuments ?? null,
        },
        specialValidationErrors: validationReport?.validationErrors
          .filter(isSpecialValidationError)
          .map((error) =>
            SPECIAL_VALIDATION_ERRORS[error.messageTemplate](error),
          ),
      },
    };
  });

  einreichenViewSig = computed(() => {
    const validationReport = this.einreichenValidationResult.data();
    const { trancheSetting } = this.gesuchViewSig();
    const { gesuch, isEditingTranche, trancheTyp, gesuchId } =
      this.cachedGesuchViewSig();
    const { compileTimeConfig } = this.sharedDataAccessConfigSig();

    const error = validationReport
      ? sharedUtilFnErrorTransformer({ error: validationReport })
      : undefined;
    const validationErrors = getValidationErrors(error) ?? [];

    return {
      loading:
        isPending(this.validationResult()) ||
        isPending(this.einreichenValidationResult()),
      specialValidationErrors: validationErrors
        .filter(isSpecialValidationError)
        .map((error) =>
          SPECIAL_VALIDATION_ERRORS[error.messageTemplate](error),
        ),
      invalidFormularSteps: transformValidationReportToFormSteps(
        gesuchId,
        trancheSetting?.routesSuffix,
        validationReport,
        gesuch?.gesuchTrancheToWorkWith.gesuchFormular,
      ),
      gesuchStatus: gesuch?.gesuchStatus,
      abschlussPhase: toAbschlussPhase(gesuch, {
        appType: compileTimeConfig?.appType,
        isComplete: hasNoValidationErrors(error),
        checkTranche: !!isEditingTranche && trancheTyp === 'AENDERUNG',
      }),
    };
  });

  einreicheOperationIsInProgressSig = computed(() => {
    return (
      isPending(this.einreichungsResult()) ||
      isPending(this.trancheEinreichenResult())
    );
  });

  validateSteps$ = rxMethod<{
    gesuchTrancheId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          validationResult: cachedPending(state.validationResult),
        }));
      }),
      withLatestFrom(
        this.store.select(selectSharedDataAccessGesuchCache).pipe(
          map(({ gesuch }) => ({
            typ: gesuch?.gesuchTrancheToWorkWith.typ,
            status: gesuch?.gesuchStatus,
          })),
        ),
      ),
      switchMap(([{ gesuchTrancheId }, { typ, status }]) =>
        this.validate$(
          gesuchTrancheId,
          typ !== 'AENDERUNG' || status === 'IN_BEARBEITUNG_GS',
        ),
      ),
      handleApiResponse((validationResult) => {
        patchState(this, {
          validationResult,
        });
      }),
    ),
  );

  validateEinreichen$ = rxMethod<{
    gesuchTrancheId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          einreichenValidationResult: cachedPending(state.validationResult),
        }));
      }),
      switchMap(({ gesuchTrancheId }) => this.validate$(gesuchTrancheId)),
      handleApiResponse((einreichenValidationResult) => {
        patchState(this, {
          einreichenValidationResult,
        });
      }),
    ),
  );

  private validate$ = (
    gesuchTrancheId: string,
    allowNullValidation?: boolean,
  ) => {
    const service$ = (
      allowNullValidation
        ? this.gesuchTrancheService.validateGesuchTranchePages$
        : this.gesuchTrancheService.gesuchTrancheEinreichenValidieren$
    ).bind(this.gesuchTrancheService);

    return service$({ gesuchTrancheId }, undefined, undefined, {
      context: shouldIgnoreErrorsIf(true),
    });
  };

  gesuchEinreichen$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          einreichungsResult: pending(),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.gesuchService.gesuchEinreichen$({ gesuchId }).pipe(
          handleApiResponse(
            (einreichen) =>
              patchState(this, { einreichungsResult: einreichen }),
            {
              onSuccess: () => {
                this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
              },
            },
          ),
        ),
      ),
    ),
  );

  trancheEinreichen$ = rxMethod<{ trancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          trancheEinreichenResult: pending(),
        }));
      }),
      switchMap(({ trancheId }) =>
        this.gesuchTrancheService
          .aenderungEinreichen$({ aenderungId: trancheId })
          .pipe(
            handleApiResponse(
              (einreichen) =>
                patchState(this, { trancheEinreichenResult: einreichen }),
              {
                onSuccess: () => {
                  this.store.dispatch(
                    SharedDataAccessGesuchEvents.loadGesuch(),
                  );
                },
              },
            ),
          ),
      ),
    ),
  );
}

const hasNoValidationErrors = (error: SharedModelError | undefined): boolean =>
  !error ||
  (error.type === 'validationError' && error.validationErrors.length === 0);

const getValidationErrors = (error?: SharedModelError) => {
  if (error?.type === 'validationError') {
    return error.validationErrors;
  }
  return undefined;
};

const transformValidationReportToFormSteps = (
  gesuchId: string | null,
  routeSuffix?: readonly string[],
  report?: ValidationReport,
  currentForm?: SharedModelGesuchFormular | null,
): (SharedModelGesuchFormStep & { routes: (string | null)[] })[] => {
  if (!report || !gesuchId) {
    return [];
  }

  const messages: (ValidationMessage | ValidationError)[] =
    report.validationErrors.concat(report.validationWarnings);

  const steps = messages.map((m) => {
    if (m.propertyPath?.startsWith('steuerdaten')) {
      return currentForm?.steuerdatenTabs?.map((tab) => {
        switch (tab) {
          case 'FAMILIE':
            return gesuchPropFormStepsMap['steuerdaten'];
          case 'VATER':
            return gesuchPropFormStepsMap['steuerdatenVater'];
          case 'MUTTER':
            return gesuchPropFormStepsMap['steuerdatenMutter'];
          default:
            return undefined;
        }
      });
    }

    if (m.messageTemplate?.includes('documents.required')) {
      return gesuchPropFormStepsMap.dokuments;
    }

    if (m.propertyPath) {
      return gesuchPropFormStepsMap[
        m.propertyPath as SharedModelGesuchFormularPropsSteuerdatenSteps
      ];
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
    }, [] as SharedModelGesuchFormStep[])
    .sort((a, b) => {
      const stepsArr = Object.keys(gesuchFormStepsFieldMap);

      return stepsArr.indexOf(a.route) - stepsArr.indexOf(b.route);
    })
    .map((step) => {
      return {
        ...step,
        routes: [
          '/',
          'gesuch',
          ...step.route.split('/'),
          gesuchId,
          ...(routeSuffix ?? []),
        ],
      };
    });
};

const transformValidationMessagesToFormKeys = (
  messages?: ValidationMessage[],
  currentForm?: SharedModelGesuchFormular | null,
) => {
  const formKeys: SharedModelGesuchFormularPropsSteuerdatenSteps[] = [
    ...(Object.keys(
      currentForm ?? {},
    ) as SharedModelGesuchFormularPropsSteuerdatenSteps[]),
    'steuerdaten',
    'steuerdatenMutter',
    'steuerdatenVater',
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
