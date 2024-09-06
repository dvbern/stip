import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { concatLatestFrom } from '@ngrx/operators';
import { Store } from '@ngrx/store';
import {
  catchError,
  combineLatestWith,
  concatMap,
  debounceTime,
  distinctUntilChanged,
  exhaustMap,
  filter,
  map,
  merge,
  skip,
  switchMap,
  tap,
  withLatestFrom,
} from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
import { SharedEventGesuchFormEducation } from '@dv/shared/event/gesuch-form-education';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { SharedEventGesuchFormEltern } from '@dv/shared/event/gesuch-form-eltern';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuerdaten';
import { SharedEventGesuchFormFamiliensituation } from '@dv/shared/event/gesuch-form-familiensituation';
import { SharedEventGesuchFormGeschwister } from '@dv/shared/event/gesuch-form-geschwister';
import { SharedEventGesuchFormKinder } from '@dv/shared/event/gesuch-form-kinder';
import { SharedEventGesuchFormLebenslauf } from '@dv/shared/event/gesuch-form-lebenslauf';
import { SharedEventGesuchFormPartner } from '@dv/shared/event/gesuch-form-partner';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import { SharedEventGesuchFormProtokoll } from '@dv/shared/event/gesuch-form-protokoll';
import {
  AusbildungUpdate,
  GesuchFormularUpdate,
  GesuchService,
  GesuchUpdate,
  SharedModelGesuchFormular,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import {
  handleNotFound,
  noGlobalErrorsIf,
  shouldIgnoreNotFoundErrorsIf,
} from '@dv/shared/util/http';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';
import {
  selectRouteId,
  selectRouteTrancheId,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from './shared-data-access-gesuch.selectors';

export const LOAD_ALL_DEBOUNCE_TIME = 300;

export const loadOwnGesuchs = createEffect(
  (
    actions$ = inject(Actions),
    gesuchService = inject(GesuchService),
    storeUtil = inject(StoreUtilService),
  ) => {
    return actions$.pipe(
      ofType(
        SharedDataAccessGesuchEvents.init,
        SharedDataAccessGesuchEvents.gesuchRemovedSuccess,
      ),
      storeUtil.waitForBenutzerData$(),
      concatMap(() =>
        gesuchService.getGesucheGs$().pipe(
          map((gesuchs) =>
            SharedDataAccessGesuchEvents.gesuchsLoadedSuccess({
              gesuchs,
            }),
          ),
          catchError((error) => [
            SharedDataAccessGesuchEvents.gesuchsLoadedFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    );
  },
  { functional: true },
);

export const loadAllGesuchs = createEffect(
  (
    actions$ = inject(Actions),
    gesuchService = inject(GesuchService),
    storeUtil = inject(StoreUtilService),
  ) => {
    return combineLoadAllActions$(actions$).pipe(
      filter(isDefined),
      storeUtil.waitForBenutzerData$(),
      switchMap(({ query }) =>
        gesuchService.getGesucheSb$({ getGesucheSBQueryType: query }).pipe(
          map((gesuchs) =>
            SharedDataAccessGesuchEvents.gesuchsLoadedSuccess({
              gesuchs,
            }),
          ),
          catchError((error) => [
            SharedDataAccessGesuchEvents.gesuchsLoadedFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    );
  },
  { functional: true },
);

export const loadGesuch = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    gesuchService = inject(GesuchService),
    router = inject(Router),
    globalNotifications = inject(GlobalNotificationStore),
  ) => {
    return actions$.pipe(
      ofType(
        SharedDataAccessGesuchEvents.loadGesuch,
        SharedEventGesuchFormPartner.init,
        SharedEventGesuchFormPerson.init,
        SharedEventGesuchFormEducation.init,
        SharedEventGesuchFormEltern.init,
        SharedEventGesuchFormElternSteuerdaten.init,
        SharedEventGesuchFormFamiliensituation.init,
        SharedEventGesuchFormAuszahlung.init,
        SharedEventGesuchFormGeschwister.init,
        SharedEventGesuchFormKinder.init,
        SharedEventGesuchFormLebenslauf.init,
        SharedEventGesuchFormEinnahmenkosten.init,
        SharedEventGesuchDokumente.init,
        SharedEventGesuchFormAbschluss.init,
        SharedEventGesuchFormProtokoll.init,
      ),
      concatLatestFrom(() =>
        store
          .select(selectRouteId)
          .pipe(combineLatestWith(store.select(selectRouteTrancheId))),
      ),
      switchMap(([, [id, trancheId]]) => {
        if (!id) {
          throw new Error(
            'Load Gesuch without id, make sure that the route is correct and contains the gesuch :id',
          );
        }

        const navigateIfNotFound = {
          context: noGlobalErrorsIf(
            true,
            handleNotFound((error) => {
              globalNotifications.createNotification({
                type: 'ERROR',
                messageKey: 'shared.genericError.gesuch-not-found-redirection',
                content: error,
              });
              router.navigate(['/'], { replaceUrl: true });
            }),
          ),
        };

        if (trancheId) {
          return gesuchService
            .getGsTrancheChanges$(
              // TODO: Split with appType
              { aenderungId: trancheId },
              undefined,
              undefined,
              navigateIfNotFound,
            )
            .pipe(
              map((gesuch) =>
                SharedDataAccessGesuchEvents.gesuchLoadedSuccess({
                  gesuch,
                  trancheId,
                }),
              ),
              catchError((error) => [
                SharedDataAccessGesuchEvents.gesuchLoadedFailure({
                  error: sharedUtilFnErrorTransformer(error),
                }),
              ]),
            );
        }

        return gesuchService
          .getCurrentGesuch$(
            { gesuchId: id },
            undefined,
            undefined,
            navigateIfNotFound,
          )
          .pipe(
            map((gesuch) =>
              SharedDataAccessGesuchEvents.gesuchLoadedSuccess({ gesuch }),
            ),
            catchError((error) => [
              SharedDataAccessGesuchEvents.gesuchLoadedFailure({
                error: sharedUtilFnErrorTransformer(error),
              }),
            ]),
          );
      }),
    );
  },
  { functional: true },
);

export const createGesuch = createEffect(
  (actions$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.newTriggered),
      exhaustMap(({ create }) =>
        gesuchService.createGesuch$({ gesuchCreate: create }).pipe(
          switchMap(() =>
            gesuchService.getGesucheForFall$({
              fallId: create.fallId,
            }),
          ),
          map(
            (gesuche) =>
              gesuche.find(
                ({ gesuchsperiode: { id } }) => id === create.gesuchsperiodeId,
              )?.id,
          ),
          filter(isDefined),
          map((id) =>
            SharedDataAccessGesuchEvents.gesuchCreatedSuccess({
              id,
            }),
          ),
          catchError((error) => [
            SharedDataAccessGesuchEvents.gesuchCreatedFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    );
  },
  { functional: true },
);

export const updateGesuch = createEffect(
  (actions$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return actions$.pipe(
      ofType(
        SharedEventGesuchFormPartner.nextStepTriggered,
        SharedEventGesuchFormPerson.saveTriggered,
        SharedEventGesuchFormEducation.saveTriggered,
        SharedEventGesuchFormElternSteuerdaten.saveTriggered,
        SharedEventGesuchFormFamiliensituation.saveTriggered,
        SharedEventGesuchFormAuszahlung.saveTriggered,
        SharedEventGesuchFormEinnahmenkosten.saveTriggered,
      ),
      concatMap(({ gesuchId, trancheId, gesuchFormular, origin }) => {
        return gesuchService
          .updateGesuch$({
            gesuchId,
            gesuchUpdate: prepareFormularData(trancheId, gesuchFormular),
          })
          .pipe(
            map(() =>
              SharedDataAccessGesuchEvents.gesuchUpdatedSuccess({
                id: gesuchId,
                origin,
              }),
            ),
            catchError((error) => [
              SharedDataAccessGesuchEvents.gesuchUpdatedFailure({
                error: sharedUtilFnErrorTransformer(error),
              }),
            ]),
          );
      }),
    );
  },
  { functional: true },
);

export const updateGesuchSubform = createEffect(
  (actions$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return actions$.pipe(
      ofType(
        SharedEventGesuchFormEltern.saveSubformTriggered,
        SharedEventGesuchFormGeschwister.saveSubformTriggered,
        SharedEventGesuchFormKinder.saveSubformTriggered,
        SharedEventGesuchFormLebenslauf.saveSubformTriggered,
      ),
      concatMap(({ gesuchId, trancheId, gesuchFormular, origin }) => {
        return gesuchService
          .updateGesuch$({
            gesuchId,
            gesuchUpdate: prepareFormularData(trancheId, gesuchFormular),
          })
          .pipe(
            map(() =>
              SharedDataAccessGesuchEvents.gesuchUpdatedSubformSuccess({
                id: gesuchId,
                origin,
              }),
            ),
            catchError((error) => [
              SharedDataAccessGesuchEvents.gesuchUpdatedSubformFailure({
                error: sharedUtilFnErrorTransformer(error),
              }),
            ]),
          );
      }),
    );
  },
  { functional: true },
);

export const removeGesuch = createEffect(
  (actions$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.removeTriggered),
      concatMap(({ id }) =>
        gesuchService.deleteGesuch$({ gesuchId: id }).pipe(
          map(() => SharedDataAccessGesuchEvents.gesuchRemovedSuccess()),
          catchError((error) => [
            SharedDataAccessGesuchEvents.gesuchRemovedFailure({
              error: sharedUtilFnErrorTransformer(error),
            }),
          ]),
        ),
      ),
    );
  },
  { functional: true },
);

export const gesuchValidateSteps = createEffect(
  (events$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return events$.pipe(
      ofType(SharedDataAccessGesuchEvents.gesuchValidateSteps),
      switchMap(({ id: gesuchId }) =>
        gesuchService
          .validateGesuchPages$({ gesuchId }, undefined, undefined, {
            context: shouldIgnoreNotFoundErrorsIf(true),
          })
          .pipe(
            switchMap((validation) => [
              SharedDataAccessGesuchEvents.gesuchValidationSuccess({
                error: sharedUtilFnErrorTransformer({ error: validation }),
              }),
            ]),
            catchError((error) => [
              SharedDataAccessGesuchEvents.gesuchValidationFailure({
                error: sharedUtilFnErrorTransformer(error),
              }),
            ]),
          ),
      ),
    );
  },
  { functional: true },
);

export const redirectToGesuchForm = createEffect(
  (actions$ = inject(Actions), router = inject(Router)) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.gesuchCreatedSuccess),
      tap(({ id }) => {
        router.navigate(['gesuch', PERSON.route, id]);
      }),
    );
  },
  { functional: true, dispatch: false },
);

export const redirectToGesuchFormNextStep = createEffect(
  (
    store = inject(Store),
    actions$ = inject(Actions),
    router = inject(Router),
    stepManager = inject(SharedUtilGesuchFormStepManagerService),
  ) => {
    return actions$.pipe(
      ofType(
        SharedDataAccessGesuchEvents.gesuchUpdatedSuccess,
        SharedEventGesuchFormEltern.nextTriggered,
        SharedEventGesuchFormGeschwister.nextTriggered,
        SharedEventGesuchFormKinder.nextTriggered,
        SharedEventGesuchFormLebenslauf.nextTriggered,
        SharedEventGesuchFormPartner.nextTriggered,
        SharedEventGesuchFormPerson.nextTriggered,
        SharedEventGesuchFormEducation.nextTriggered,
        SharedEventGesuchFormElternSteuerdaten.nextTriggered,
        SharedEventGesuchFormFamiliensituation.nextTriggered,
        SharedEventGesuchFormAuszahlung.nextTriggered,
        SharedEventGesuchFormEinnahmenkosten.nextTriggered,
        SharedEventGesuchDokumente.nextTriggered,
      ),
      withLatestFrom(
        store.select(selectSharedDataAccessGesuchStepsView),
        store.select(selectSharedDataAccessGesuchsView),
      ),
      tap(
        ([
          { id, origin },
          { stepsFlow: stepFlowSig },
          { gesuchFormular, readonly, specificTrancheId },
        ]) => {
          router.navigate([
            'gesuch',
            ...stepManager
              .getNextStepOf(stepFlowSig, origin, gesuchFormular, readonly)
              .route.split('/'),
            id,
            ...(specificTrancheId ? ['tranche', specificTrancheId] : []),
          ]);
        },
      ),
    );
  },
  { functional: true, dispatch: false },
);

export const refreshGesuchFormStep = createEffect(
  (
    store = inject(Store),
    actions$ = inject(Actions),
    router = inject(Router),
  ) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.gesuchUpdatedSubformSuccess),
      withLatestFrom(store.select(selectSharedDataAccessGesuchsView)),
      tap(([{ id, origin }, { specificTrancheId }]) => {
        router.navigate([
          'gesuch',
          origin.route,
          id,
          ...(specificTrancheId ? ['tranche', specificTrancheId] : []),
        ]);
      }),
    );
  },
  { functional: true, dispatch: false },
);

export const setGesuchToBearbeitung = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    gesuchService = inject(GesuchService),
  ) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.setGesuchToBearbeitung),
      concatLatestFrom(() => store.select(selectRouteId)),
      concatMap(([, id]) => {
        if (!id) {
          throw new Error(
            'Make sure that the route is correct and contains the gesuch :id',
          );
        }
        return gesuchService
          .changeGesuchStatusToInBearbeitung$({ gesuchId: id })
          .pipe(
            map((gesuch) =>
              SharedDataAccessGesuchEvents.gesuchLoadedSuccess({ gesuch }),
            ),
            catchError((error) => [
              SharedDataAccessGesuchEvents.gesuchLoadedFailure({
                error: sharedUtilFnErrorTransformer(error),
              }),
            ]),
          );
      }),
    );
  },
  { functional: true },
);

// add effects here
export const sharedDataAccessGesuchEffects = {
  loadOwnGesuchs,
  loadAllGesuchs,
  loadGesuch,
  createGesuch,
  updateGesuch,
  updateGesuchSubform,
  removeGesuch,
  gesuchValidateSteps,
  redirectToGesuchForm,
  redirectToGesuchFormNextStep,
  refreshGesuchFormStep,
  setGesuchToBearbeitung,
};

const viewOnlyFields = ['steuerdatenTabs'] as const satisfies [
  keyof SharedModelGesuchFormular,
];
/**
 * Formular fields that are only used while viewing data but should be removed on update
 */
type ViewOnlyFields = (typeof viewOnlyFields)[number];

const prepareFormularData = (
  id: string,
  gesuchFormular: GesuchFormularUpdate | Partial<SharedModelGesuchFormular>,
): GesuchUpdate => {
  const { ausbildung, ...formular } = gesuchFormular;
  viewOnlyFields.forEach((field) => {
    if (field in formular) {
      delete formular[field];
    }
  });
  return {
    gesuchTrancheToWorkWith: {
      id,
      gesuchFormular: {
        ...formular,
        ausbildung: toAusbildung(ausbildung),
      },
    },
  };
};

const combineLoadAllActions$ = (actions$: Actions) => {
  /** Used to initially load all gesuche */
  const loadAll$ = actions$.pipe(ofType(SharedDataAccessGesuchEvents.loadAll));
  /** Used to reload the gesuche after a filter change */
  const loadAllDebounced$ = merge(
    // Merge the initial load to compare the new values with the initial in distinctUntilChanged
    loadAll$,
    actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.loadAllDebounced),
      debounceTime(LOAD_ALL_DEBOUNCE_TIME),
    ),
  );
  return merge(
    loadAll$,
    loadAllDebounced$.pipe(
      distinctUntilChanged(),
      // skip the first value, because it's the same as the initial load
      skip(1),
    ),
  );
};

/**
 * Get the given Formular property Data as View or Update Data
 *
 * * **View Type**: SharedModelGesuchFormular[K]
 *   > represents the type that is being returned from the API (GET)
 * * **Update Type**: GesuchFormularUpdate[K]
 *   > the values that can be sent to the API for an update (PUT)
 *
 * The distinciton is necessary because the API returns more data than is necessary for an update
 * for example the API returns the full Ausbildungsgang object, but for an update only the ID is necessary
 */
type ViewOrUpdateData<
  K extends Exclude<keyof SharedModelGesuchFormular, ViewOnlyFields>,
> = GesuchFormularUpdate[K] | Partial<SharedModelGesuchFormular>[K];

/**
 * Check if Type T represent the Edit type of R
 */
const isEditData = <T, R extends T>(
  value: T,
  keyExists: keyof R,
): value is R => {
  return typeof value === 'object' && value && keyExists in value;
};

/**
 * Convert the given Ausbildung to an AusbildungUpdate if necessary
 */
const toAusbildung = (ausbildung: ViewOrUpdateData<'ausbildung'>) => {
  if (!ausbildung) {
    return undefined;
  }
  if (
    isEditData<ViewOrUpdateData<'ausbildung'>, AusbildungUpdate>(
      ausbildung,
      'ausbildungsgangId',
    )
  ) {
    return ausbildung;
  }
  return {
    ...ausbildung,
    ausbildungsgang: undefined,
    ausbildungsgangId: ausbildung.ausbildungsgang?.id,
  };
};
