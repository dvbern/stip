import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { map, pipe, switchMap, tap, withLatestFrom } from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedDataAccessGesuchEvents,
  isGesuchFormularProp,
  selectSharedDataAccessGesuchCache,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { toAbschlussPhase } from '@dv/shared/model/einreichen';
import { SharedModelError, ValidationError } from '@dv/shared/model/error';
import {
  AllFormSteps,
  EinreichedatumAendernRequest,
  EinreichedatumStatus,
  GSFormStepProps,
  GSSteuererklaerungSteps,
  GesuchFormularType,
  GesuchService,
  GesuchTrancheService,
  SBFormStepProps,
  SBSteuerdatenSteps,
  ValidationMessage,
  ValidationReport,
} from '@dv/shared/model/gesuch';
import {
  FormPropsToStepsMap,
  FormRoutesToPropsMap,
  GesuchFormStep,
  SPECIAL_VALIDATION_ERRORS,
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
  einreichedatumAendernStatus: CachedRemoteData<EinreichedatumStatus>;
  einreichedatumAenderungsResult: RemoteData<unknown>;
};

const initialState: EinreichenState = {
  validationResult: initial(),
  einreichenValidationResult: initial(),
  einreichungsResult: initial(),
  trancheEinreichenResult: initial(),
  einreichedatumAendernStatus: initial(),
  einreichedatumAenderungsResult: initial(),
};

@Injectable({ providedIn: 'root' })
export class EinreichenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('EinreichenStore'),
) {
  private store = inject(Store);
  private gesuchService = inject(GesuchService);
  private dokumentsStore = inject(DokumentsStore);
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

  invalidFormularControlsSig = computed(() => {
    const validationReport = this.validationResult.data();
    return validationReport?.validationErrors
      .filter((error) => error.propertyPath && !isSpecialValidationError(error))
      .map(
        (error) =>
          error.messageTemplate.match(/([^.]+)\.(invalid|required)\./)?.[1],
      )
      .filter(isDefined);
  });

  einreichenViewSig = computed(() => {
    const validationReport = this.einreichenValidationResult.data();
    const { trancheSetting } = this.gesuchViewSig();
    const { gesuch, trancheTyp, gesuchId } = this.cachedGesuchViewSig();
    const { compileTimeConfig } = this.sharedDataAccessConfigSig();
    const hasNoDokumenteToUebermitteln =
      gesuch?.gesuchStatus !== 'FEHLENDE_DOKUMENTE' ||
      this.dokumentsStore.dokumenteCanFlagsSig().gsCanDokumenteUebermitteln;

    const routesSuffix = trancheSetting?.routesSuffix;
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
        .map((error) => {
          const specialError =
            SPECIAL_VALIDATION_ERRORS[error.messageTemplate](error);
          return {
            ...specialError,
            stepRoute: routesSuffix
              ? [
                  '/gesuch',
                  specialError.step.route,
                  gesuch?.id,
                  ...routesSuffix,
                ]
              : null,
          };
        }),
      invalidFormularSteps: transformValidationReportToFormSteps(
        gesuchId,
        routesSuffix,
        validationReport,
        gesuch?.gesuchTrancheToWorkWith.gesuchFormular,
      ),
      gesuchStatus: gesuch?.gesuchStatus,
      abschlussPhase: toAbschlussPhase(gesuch, {
        appType: compileTimeConfig?.appType,
        isComplete:
          hasNoValidationErrors(error) && hasNoDokumenteToUebermitteln,
        checkAenderung: trancheTyp === 'AENDERUNG',
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
          typ === 'TRANCHE' || status === 'IN_BEARBEITUNG_GS',
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
    const service = (
      allowNullValidation
        ? 'validateGesuchTranchePages$'
        : 'gesuchTrancheEinreichenValidieren$'
    ) satisfies keyof GesuchTrancheService;

    return this.gesuchTrancheService[service](
      { gesuchTrancheId },
      undefined,
      undefined,
      {
        context: shouldIgnoreErrorsIf(true),
      },
    );
  };

  gesuchEinreichen$ = rxMethod<{ gesuchTrancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          einreichungsResult: pending(),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
        this.gesuchService.gesuchEinreichen$({ gesuchTrancheId }).pipe(
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

  aenderungEinreichen$ = rxMethod<{ trancheId: string }>(
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

  checkEinreichedatumAendern$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() =>
        patchState(this, (state) => ({
          einreichedatumAendernStatus: cachedPending(
            state.einreichedatumAendernStatus,
          ),
        })),
      ),
      switchMap(({ gesuchId }) =>
        this.gesuchService
          .canEinreichedatumAendern$({ gesuchId })
          .pipe(
            handleApiResponse((einreichedatumAendernStatus) =>
              patchState(this, { einreichedatumAendernStatus }),
            ),
          ),
      ),
    ),
  );

  einreichedatumManuellAendern$ = rxMethod<{
    gesuchId: string;
    change: EinreichedatumAendernRequest;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          einreichedatumAenderungsResult: pending(),
        }));
      }),
      switchMap(({ gesuchId, change }) =>
        this.gesuchService
          .einreichedatumManuellAendern$({
            gesuchId,
            einreichedatumAendernRequest: change,
          })
          .pipe(
            handleApiResponse(
              (einreichedatumAenderungsResult) =>
                patchState(this, { einreichedatumAenderungsResult }),
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

const transformValidationMessagesToFormKeys = (
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
