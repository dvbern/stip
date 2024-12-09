import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { concatLatestFrom } from '@ngrx/operators';
import { Store } from '@ngrx/store';
import {
  catchError,
  combineLatestWith,
  concatMap,
  map,
  switchMap,
  tap,
  withLatestFrom,
} from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
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
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { AppType } from '@dv/shared/model/config';
import { SharedModelError } from '@dv/shared/model/error';
import {
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

export const updateGesuch = createEffect(
  (actions$ = inject(Actions), gesuchService = inject(GesuchService)) => {
    return actions$.pipe(
      ofType(
        SharedEventGesuchFormPartner.nextStepTriggered,
        SharedEventGesuchFormPerson.saveTriggered,
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
          { gesuch, trancheSetting },
        ]) => {
          router.navigate([
            'gesuch',
            ...stepManager
              .getNextStepOf(stepFlowSig, origin, gesuch)
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

// add effects here
export const sharedDataAccessGesuchEffects = {
  loadOwnGesuchs,
  loadGesuch,
  updateGesuch,
  updateGesuchSubform,
  removeGesuch,
  redirectToGesuchForm,
  redirectToGesuchFormNextStep,
  refreshGesuchFormStep,
  setGesuchToBearbeitung,
};

const viewOnlyFields = [
  'steuerdatenTabs',
] as const satisfies (keyof SharedModelGesuchFormular)[];

const prepareFormularData = (
  id: string,
  gesuchFormular: GesuchFormularUpdate | Partial<SharedModelGesuchFormular>,
): GesuchUpdate => {
  const { ...formular } = gesuchFormular;
  if ('ausbildung' in formular) {
    delete formular.ausbildung;
  }
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
      },
    },
  };
};
