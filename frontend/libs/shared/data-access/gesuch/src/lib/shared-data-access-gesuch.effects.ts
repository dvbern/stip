import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { concatLatestFrom } from '@ngrx/operators';
import { ActionCreator, Creator, Store } from '@ngrx/store';
import {
  Observable,
  catchError,
  combineLatestWith,
  concatMap,
  exhaustMap,
  filter,
  map,
  switchMap,
  tap,
  withLatestFrom,
} from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
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
import { AppType } from '@dv/shared/model/config';
import { SharedModelError } from '@dv/shared/model/error';
import {
  AusbildungUpdate,
  Gesuch,
  GesuchFormularUpdate,
  GesuchService,
  GesuchTrancheTyp,
  GesuchUpdate,
  SharedModelGesuchFormular,
} from '@dv/shared/model/gesuch';
import { TRANCHE } from '@dv/shared/model/gesuch-form';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import {
  handleNotFoundAndUnauthorized,
  noGlobalErrorsIf,
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
  selectTrancheTyp,
} from './shared-data-access-gesuch.selectors';

export const LOAD_ALL_DEBOUNCE_TIME = 300;
export const ROUTE_ID_MISSING =
  'Make sure that the route is correct and contains the gesuch :id';

export const loadOwnGesuchs = createEffect(
  (
    actions$ = inject(Actions),
    gesuchService = inject(GesuchService),
    storeUtil = inject(StoreUtilService),
  ) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.init),
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

export const loadGsDashboard = createEffect(
  (
    actions$ = inject(Actions),
    gesuchService = inject(GesuchService),
    storeUtil = inject(StoreUtilService),
  ) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.loadGsDashboard),
      storeUtil.waitForBenutzerData$(),
      concatMap(() =>
        gesuchService.getGsDashboard$().pipe(
          map((gsDashboard) =>
            SharedDataAccessGesuchEvents.gsDashboardLoadedSuccess({
              gsDashboard,
            }),
          ),
          catchError((error) => [
            SharedDataAccessGesuchEvents.gsDashboardLoadedFailure({
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
          .pipe(
            combineLatestWith(
              store.select(selectTrancheTyp),
              store.select(selectRouteTrancheId),
            ),
          ),
      ),
      withLatestFrom(store.select(selectSharedDataAccessConfigsView)),
      switchMap(([[, [id, trancheTyp, trancheId]], { compileTimeConfig }]) => {
        if (!id) {
          throw new Error(ROUTE_ID_MISSING);
        }

        const handle404And401 = {
          context: noGlobalErrorsIf(
            true,
            handleNotFoundAndUnauthorized(
              (error: SharedModelError) => {
                globalNotifications.createNotification({
                  type: 'ERROR_PERMANENT',
                  messageKey:
                    'shared.genericError.gesuch-not-found-redirection',
                  content: error,
                });
                router.navigate(['/'], { replaceUrl: true });
              },
              (error: SharedModelError) => {
                globalNotifications.createNotification({
                  type: 'ERROR_PERMANENT',
                  messageKey: 'shared.genericError.gesuch-unauthorized',
                  content: error,
                });
                router.navigate(['/'], { replaceUrl: true });
              },
            ),
          ),
        };

        // Call the correct service based on the app type
        const aenderungServices$ = {
          'gesuch-app': (aenderungId: string) =>
            gesuchService.getGsTrancheChanges$(
              { aenderungId },
              undefined,
              undefined,
              handle404And401,
            ),
          'sachbearbeitung-app': (aenderungId: string) =>
            gesuchService.getSbTrancheChanges$(
              { aenderungId },
              undefined,
              undefined,
              handle404And401,
            ),
        } satisfies Record<AppType, unknown>;

        // Different services for different types of tranches
        const services$ = {
          AENDERUNG: (appType: AppType) => aenderungServices$[appType],
          TRANCHE: () => (gesuchTrancheId: string) =>
            gesuchService.getGesuch$(
              { gesuchId: id, gesuchTrancheId },
              undefined,
              undefined,
              handle404And401,
            ),
        } satisfies Record<GesuchTrancheTyp, unknown>;

        return (
          trancheTyp && trancheId && compileTimeConfig
            ? // If there is a trancheTyp, trancheId and compileTimeConfig, use the matching service call
              services$[trancheTyp](compileTimeConfig.appType)(trancheId)
            : // Otherwise use the normal current gesuch service call
              gesuchService.getCurrentGesuch$(
                { gesuchId: id },
                undefined,
                undefined,
                handle404And401,
              )
        ).pipe(
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
      }),
    );
  },
  { functional: true },
);

export const createGesuch = createEffect(
  (actions$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return actions$.pipe(
      ofType(SharedDataAccessGesuchEvents.createGesuch),
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
      ofType(SharedDataAccessGesuchEvents.deleteGesuch),
      concatMap(({ gesuchId }) =>
        gesuchService.deleteGesuch$({ gesuchId }).pipe(
          switchMap(() => [
            SharedDataAccessGesuchEvents.deleteGesuchSuccess(),
            SharedDataAccessGesuchEvents.init(),
          ]),
          catchError((error) => [
            SharedDataAccessGesuchEvents.deleteGesuchFailure({
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
        router.navigate(['gesuch', TRANCHE.route, id]);
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
          { gesuchFormular, readonly, trancheSetting },
        ]) => {
          router.navigate([
            'gesuch',
            ...stepManager
              .getNextStepOf(stepFlowSig, origin, gesuchFormular, readonly)
              .route.split('/'),
            id,
            ...(trancheSetting?.routesSuffix ?? []),
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
      tap(([{ id, origin }, { trancheSetting }]) => {
        router.navigate([
          'gesuch',
          origin.route,
          id,
          ...(trancheSetting?.routesSuffix ?? []),
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
          throw new Error(ROUTE_ID_MISSING);
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

export const setGesuchBearbeitungAbschliessen = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    gesuchService = inject(GesuchService),
  ) => {
    return handleStatusChange$(
      SharedDataAccessGesuchEvents.setGesuchBearbeitungAbschliessen,
      (gesuchId) =>
        gesuchService.bearbeitungAbschliessen$({
          gesuchId,
        }),
      actions$,
      store,
    );
  },
  { functional: true },
);

export const setGesuchZurueckweisen = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    gesuchService = inject(GesuchService),
  ) => {
    return handleStatusChange$(
      SharedDataAccessGesuchEvents.setGesuchZurueckweisen,
      (gesuchId, { kommentar }) =>
        gesuchService.gesuchZurueckweisen$({
          gesuchId,
          kommentar: { text: kommentar },
        }),
      actions$,
      store,
    );
  },
  { functional: true },
);

export const setGesuchVerfuegt = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    gesuchService = inject(GesuchService),
  ) => {
    return handleStatusChange$(
      SharedDataAccessGesuchEvents.setGesuchVerfuegt,
      (gesuchId) =>
        gesuchService.changeGesuchStatusToVerfuegt$({
          gesuchId,
        }),
      actions$,
      store,
    );
  },
  { functional: true },
);

export const setGesuchBereitFuerBearbeitung = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    gesuchService = inject(GesuchService),
  ) => {
    return handleStatusChange$(
      SharedDataAccessGesuchEvents.setGesuchBereitFuerBearbeitung,
      (gesuchId) =>
        gesuchService.changeGesuchStatusToBereitFuerBearbeitung$({
          gesuchId,
        }),
      actions$,
      store,
    );
  },
  { functional: true },
);

export const setGesuchVersendet = createEffect(
  (
    actions$ = inject(Actions),
    store = inject(Store),
    gesuchService = inject(GesuchService),
  ) => {
    return handleStatusChange$(
      SharedDataAccessGesuchEvents.setGesuchVersendet,
      (gesuchId) => gesuchService.changeGesuchStatusToVersendet$({ gesuchId }),
      actions$,
      store,
    );
  },
  { functional: true },
);

const handleStatusChange$ = <AC extends ActionCreator<string, Creator>>(
  action: AC,
  serviceCall: (
    gesuchId: string,
    payload: ReturnType<typeof action>,
  ) => Observable<Gesuch>,
  actions$: Actions,
  store: Store,
) => {
  return actions$.pipe(
    ofType(action),
    concatLatestFrom(() => store.select(selectRouteId)),
    concatMap(([payload, id]) => {
      if (!id) {
        throw new Error(ROUTE_ID_MISSING);
      }
      return serviceCall(id, payload).pipe(
        map(() => SharedDataAccessGesuchEvents.loadGesuch()),
        catchError((error) => [
          SharedDataAccessGesuchEvents.gesuchLoadedFailure({
            error: sharedUtilFnErrorTransformer(error),
          }),
        ]),
      );
    }),
  );
};

// add effects here
export const sharedDataAccessGesuchEffects = {
  loadOwnGesuchs,
  loadGesuch,
  loadGsDashboard,
  createGesuch,
  updateGesuch,
  updateGesuchSubform,
  removeGesuch,
  redirectToGesuchForm,
  redirectToGesuchFormNextStep,
  refreshGesuchFormStep,
  setGesuchToBearbeitung,
  setGesuchBearbeitungAbschliessen,
  setGesuchZurueckweisen,
  setGesuchVerfuegt,
  setGesuchBereitFuerBearbeitung,
  setGesuchVersendet,
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

// const combineLoadAllActions$ = (actions$: Actions) => {
//   /** Used to initially load all gesuche */
//   const loadAll$ = actions$.pipe(ofType(SharedDataAccessGesuchEvents.loadAll));
//   /** Used to reload the gesuche after a filter change */
//   const loadAllDebounced$ = merge(
//     // Merge the initial load to compare the new values with the initial in distinctUntilChanged
//     loadAll$,
//     actions$.pipe(
//       ofType(SharedDataAccessGesuchEvents.loadAllDebounced),
//       debounceTime(LOAD_ALL_DEBOUNCE_TIME),
//     ),
//   );
//   return merge(
//     loadAll$,
//     loadAllDebounced$.pipe(
//       distinctUntilChanged(),
//       // skip the first value, because it's the same as the initial load
//       skip(1),
//     ),
//   );
// };

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
