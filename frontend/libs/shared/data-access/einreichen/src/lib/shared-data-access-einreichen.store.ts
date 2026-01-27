import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { map, pipe, switchMap, tap, throwError, withLatestFrom } from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchCache,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { toAbschlussPhase } from '@dv/shared/model/einreichen';
import {
  EinreichedatumAendernRequest,
  EinreichedatumStatus,
  GesuchService,
  GesuchTrancheService,
  ValidationReport,
} from '@dv/shared/model/gesuch';
import {
  SPECIAL_VALIDATION_ERRORS,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';
import { byAppType } from '@dv/shared/model/permission-state';
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
import {
  getValidationErrors,
  hasNoValidationErrors,
  sharedUtilFnErrorTransformer,
  transformValidationMessagesToFormKeys,
  transformValidationReportToFormSteps,
} from '@dv/shared/util-fn/error-transformer';

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
) {
  private store = inject(Store);
  private gesuchService = inject(GesuchService);
  private dokumentsStore = inject(DokumentsStore);
  private gesuchTrancheService = inject(GesuchTrancheService);
  private config = inject(SharedModelCompileTimeConfig);
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
    const { isFehlendeDokumente, permissions, trancheSetting } =
      this.gesuchViewSig();
    const { gesuch, trancheTyp, gesuchId } = this.cachedGesuchViewSig();
    const { compileTimeConfig } = this.sharedDataAccessConfigSig();
    const hasDokumenteToUebermitteln =
      isFehlendeDokumente &&
      !this.dokumentsStore.dokumenteCanFlagsSig().gsCanDokumenteUebermitteln;

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
      permissions,
      isFehlendeDokumente,
      gesuchStatus: gesuch?.gesuchStatus,
      abschlussPhase: toAbschlussPhase(gesuch, {
        appType: compileTimeConfig?.appType,
        isComplete: hasNoValidationErrors(error) && !hasDokumenteToUebermitteln,
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
    const requestArgs = [
      { gesuchTrancheId },
      undefined,
      undefined,
      {
        context: shouldIgnoreErrorsIf(true),
      },
    ] as const;

    if (allowNullValidation) {
      return byAppType(this.config.appType, {
        'gesuch-app': () =>
          this.gesuchTrancheService.validateGesuchTranchePagesGS$(
            ...requestArgs,
          ),
        'sachbearbeitung-app': () =>
          this.gesuchTrancheService.gesuchTrancheEinreichenValidierenSB$(
            ...requestArgs,
          ),
        'demo-data-app': () =>
          throwError(() => new Error('Not implemented for this AppType')),
      });
    }

    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.gesuchTrancheService.gesuchTrancheEinreichenValidierenGS$(
          ...requestArgs,
        ),
      'sachbearbeitung-app': () =>
        this.gesuchTrancheService.gesuchTrancheEinreichenValidierenSB$(
          ...requestArgs,
        ),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  };

  gesuchEinreichen$ = rxMethod<{ gesuchTrancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          einreichungsResult: pending(),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
        this.gesuchService.gesuchEinreichenGs$({ gesuchTrancheId }).pipe(
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
