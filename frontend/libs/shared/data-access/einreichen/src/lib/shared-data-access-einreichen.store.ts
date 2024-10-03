import { Injectable, computed, inject } from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store, createSelector } from '@ngrx/store';
import {
  combineLatest,
  defer,
  distinctUntilChanged,
  filter,
  map,
  pipe,
  switchMap,
  tap,
} from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import {
  SharedDataAccessGesuchEvents,
  sharedDataAccessGesuchsFeature,
} from '@dv/shared/data-access/gesuch';
import { toAbschlussPhase } from '@dv/shared/model/einreichen';
import { SharedModelError } from '@dv/shared/model/error';
import {
  GesuchService,
  GesuchTrancheService,
  ValidationReport,
} from '@dv/shared/model/gesuch';
import {
  SPECIAL_VALIDATION_ERRORS,
  isSpecialValidationError,
} from '@dv/shared/model/gesuch-form';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { shouldIgnoreErrorsIf } from '@dv/shared/util/http';
import {
  RemoteData,
  handleApiResponse,
  initial,
  isPending,
  pending,
} from '@dv/shared/util/remote-data';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';
import { isDefined } from '@dv/shared/util-fn/type-guards';

type EinreichenState = {
  validationResult: RemoteData<ValidationReport>;
  einreichungsResult: RemoteData<unknown>;
  trancheEinreichenResult: RemoteData<unknown>;
};

const initialState: EinreichenState = {
  validationResult: initial(),
  einreichungsResult: initial(),
  trancheEinreichenResult: initial(),
};

const selectSharedDataAccessValidationView = createSelector(
  sharedDataAccessGesuchsFeature.selectGesuch,
  sharedDataAccessGesuchsFeature.selectIsEditingTranche,
  sharedDataAccessGesuchsFeature.selectLastUpdate,
  sharedDataAccessGesuchsFeature.selectValidations,
  (gesuch, isEditingTranche, lastUpdate, validations) => ({
    gesuch,
    isEditingTranche,
    lastUpdate,
    validations,
  }),
);

@Injectable()
export class EinreichenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('EinreichenStore'),
) {
  private store = inject(Store);
  private gesuchService = inject(GesuchService);
  private gesuchTrancheService = inject(GesuchTrancheService);
  private sharedDataAccessValidationSig = this.store.selectSignal(
    selectSharedDataAccessValidationView,
  );
  private sharedDataAccessConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

  einreichenViewSig = computed(() => {
    const validationReport = this.validationResult.data();
    const { gesuch, isEditingTranche, lastUpdate, validations } =
      this.sharedDataAccessValidationSig();
    const { compileTimeConfig } = this.sharedDataAccessConfigSig();

    const error = sharedUtilFnErrorTransformer({ error: validationReport });
    const checkValidationErrors = getValidationErrors(error);
    const allErrors = validations?.errors ?? [];
    const allValidations = allErrors.concat(checkValidationErrors ?? []);

    console.log('ALL ERRORS', {
      error,
      allErrors,
      validationReport,
      checkValidationErrors,
      canCheck: allErrors.length === 0,
      isComplete: hasNoValidationErrors(error),
    });

    return {
      loading: isPending(this.validationResult()),
      gesuch,
      isEditingTranche,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      lastUpdate,
      specialValidationErrors: allValidations
        .filter(isSpecialValidationError)
        .map((error) => SPECIAL_VALIDATION_ERRORS[error.messageTemplate]),
      canCheck: allErrors.length === 0,
      abschlussPhase: toAbschlussPhase(gesuch, {
        appType: compileTimeConfig?.appType,
        isComplete: hasNoValidationErrors(error),
        checkTranche: !!isEditingTranche,
      }),
    };
  });

  validateEinreichen$ = rxMethod<{ gesuchTrancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          validationResult: pending(),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
        this.gesuchTrancheService
          .gesuchTrancheEinreichenValidieren$(
            { gesuchTrancheId },
            undefined,
            undefined,
            {
              context: shouldIgnoreErrorsIf(true),
            },
          )
          .pipe(
            handleApiResponse((einreichen) =>
              patchState(this, { validationResult: einreichen }),
            ),
          ),
      ),
    ),
  );

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

  onCheckIsPossible$ = defer(() =>
    combineLatest([
      getLatestTrancheIdFromGesuchOnUpdate$(this.einreichenViewSig).pipe(
        filter(isDefined),
      ),
      toObservable(this.einreichenViewSig).pipe(
        map((view) => view.canCheck),
        distinctUntilChanged(),
        filter((canCheck) => !!canCheck),
        takeUntilDestroyed(),
      ),
    ]),
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
