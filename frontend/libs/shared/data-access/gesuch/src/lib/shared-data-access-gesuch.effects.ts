import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { concatLatestFrom } from '@ngrx/operators';
import { Store } from '@ngrx/store';
import {
  EMPTY,
  catchError,
  combineLatestWith,
  concatMap,
  filter,
  map,
  switchMap,
  tap,
  throwError,
  withLatestFrom,
} from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
import { SharedEventGesuchFormAbschluss } from '@dv/shared/event/gesuch-form-abschluss';
import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
import { SharedEventGesuchFormEinnahmenkosten } from '@dv/shared/event/gesuch-form-einnahmenkosten';
import { SharedEventGesuchFormEltern } from '@dv/shared/event/gesuch-form-eltern';
import { SharedEventGesuchFormElternSteuerdaten } from '@dv/shared/event/gesuch-form-eltern-steuererklaerung';
import { SharedEventGesuchFormFamiliensituation } from '@dv/shared/event/gesuch-form-familiensituation';
import { SharedEventGesuchFormGeschwister } from '@dv/shared/event/gesuch-form-geschwister';
import { SharedEventGesuchFormKinder } from '@dv/shared/event/gesuch-form-kinder';
import { SharedEventGesuchFormLebenslauf } from '@dv/shared/event/gesuch-form-lebenslauf';
import { SharedEventGesuchFormPartner } from '@dv/shared/event/gesuch-form-partner';
import { SharedEventGesuchFormPerson } from '@dv/shared/event/gesuch-form-person';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import { AppType } from '@dv/shared/model/config';
import {
  GesuchFormular,
  GesuchFormularType,
  GesuchFormularUpdate,
  GesuchService,
  GesuchUpdate,
  GesuchUrlType,
} from '@dv/shared/model/gesuch';
import { TRANCHE } from '@dv/shared/model/gesuch-form';
import { ifPropsAreDefined, isDefined } from '@dv/shared/model/type-util';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import {
  handleNotFoundAndUnauthorized,
  noGlobalErrorsIf,
} from '@dv/shared/util/http';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import { SharedDataAccessGesuchEvents } from './shared-data-access-gesuch.events';
import {
  selectRevision,
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
      ),
      concatLatestFrom(() =>
        store
          .select(selectRouteId)
          .pipe(
            combineLatestWith(
              store.select(selectTrancheTyp),
              store.select(selectRouteTrancheId),
              store.select(selectRevision),
            ),
          ),
      ),
      withLatestFrom(store.select(selectSharedDataAccessConfigsView)),
      switchMap(
        ([
          [, [id, trancheTyp, trancheId, revision]],
          { compileTimeConfig },
        ]) => {
          if (!id) {
            throw new Error(ROUTE_ID_MISSING);
          }
          if (!trancheTyp || !trancheId || !compileTimeConfig) {
            throw new Error(
              'Missing trancheTyp, trancheId or compileTimeConfig',
            );
          }

          const handle404And401 = {
            context: noGlobalErrorsIf(
              true,
              handleNotFoundAndUnauthorized(
                (error) => {
                  globalNotifications.createNotification({
                    type: 'ERROR_PERMANENT',
                    messageKey:
                      'shared.genericError.gesuch-not-found-redirection',
                    content: error,
                  });
                  router.navigate(['/'], { replaceUrl: true });
                },
                (error) => {
                  globalNotifications.createNotification({
                    type: 'ERROR_PERMANENT',
                    messageKey: 'shared.genericError.unauthorized',
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
              gesuchService.getGsAenderungChangesInBearbeitung$(
                { aenderungId },
                undefined,
                undefined,
                handle404And401,
              ),
            'sachbearbeitung-app': (aenderungId: string) =>
              gesuchService.getSbAenderungChanges$(
                { aenderungId, revision },
                undefined,
                undefined,
                handle404And401,
              ),
            'demo-data-app': () =>
              throwError(() => new Error('Not implemented for this AppType')),
          } satisfies Record<AppType, unknown>;

          const trancheServices$ = {
            'gesuch-app': (gesuchTrancheId: string) =>
              gesuchService.getGesuchGS$(
                {
                  gesuchTrancheId,
                },
                undefined,
                undefined,
                handle404And401,
              ),
            'sachbearbeitung-app': (gesuchTrancheId: string) =>
              gesuchService.getGesuchSB$(
                {
                  gesuchTrancheId,
                },
                undefined,
                undefined,
                handle404And401,
              ),
            'demo-data-app': () =>
              throwError(() => new Error('Not implemented for this AppType')),
          } satisfies Record<AppType, unknown>;

          // Different services for different types of tranches
          const services$ = {
            AENDERUNG: (appType: AppType) => aenderungServices$[appType],
            TRANCHE: (appType: AppType) => trancheServices$[appType],
            INITIAL: () => (gesuchTrancheId: string) =>
              gesuchService.getInitialTrancheChanges$(
                {
                  gesuchTrancheId,
                },
                undefined,
                undefined,
                handle404And401,
              ),
          } satisfies Record<GesuchUrlType, unknown>;

          return services$[trancheTyp](compileTimeConfig.appType)(
            trancheId,
          ).pipe(
            map((gesuch) =>
              SharedDataAccessGesuchEvents.gesuchLoadedSuccess({
                gesuch,
                typ: trancheTyp,
              }),
            ),
            catchError((error) => [
              SharedDataAccessGesuchEvents.gesuchLoadedFailure({
                error: sharedUtilFnErrorTransformer(error),
              }),
            ]),
          );
        },
      ),
    );
  },
  { functional: true },
);

const getUpdateGesuchServiceCalls = (
  gesuchService: GesuchService,
  gesuchId: string,
  trancheId: string,
  gesuchFormular: Partial<GesuchFormular> | Partial<GesuchFormularUpdate>,
) => {
  return {
    'gesuch-app': () =>
      gesuchService.updateGesuchGS$({
        gesuchId,
        gesuchUpdate: prepareFormularData(trancheId, gesuchFormular),
      }),
    'sachbearbeitung-app': () =>
      gesuchService.updateGesuchSB$({
        gesuchId,
        gesuchUpdate: prepareFormularData(trancheId, gesuchFormular),
      }),
    'demo-data-app': () => EMPTY,
  } satisfies Record<AppType, unknown>;
};

export const updateGesuch = createEffect(
  (
    actions$ = inject(Actions),
    gesuchService = inject(GesuchService),
    store = inject(Store),
  ) => {
    return actions$.pipe(
      ofType(
        SharedEventGesuchFormPartner.nextStepTriggered,
        SharedEventGesuchFormPerson.saveTriggered,
        SharedEventGesuchFormElternSteuerdaten.saveTriggered,
        SharedEventGesuchFormFamiliensituation.saveTriggered,
        SharedEventGesuchFormEinnahmenkosten.saveTriggered,
      ),
      withLatestFrom(store.select(selectSharedDataAccessConfigsView)),
      concatMap(
        ([
          { gesuchId, trancheId, gesuchFormular, origin },
          { compileTimeConfig },
        ]) => {
          if (!trancheId || !compileTimeConfig) {
            throw new Error('Missing trancheId or compileTimeConfig');
          }
          return getUpdateGesuchServiceCalls(
            gesuchService,
            gesuchId,
            trancheId,
            gesuchFormular,
          )
            [compileTimeConfig.appType]()
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
        },
      ),
    );
  },
  { functional: true },
);

export const updateGesuchSubform = createEffect(
  (
    actions$ = inject(Actions),
    gesuchService = inject(GesuchService),
    store = inject(Store),
  ) => {
    return actions$.pipe(
      ofType(
        SharedEventGesuchFormEltern.saveSubformTriggered,
        SharedEventGesuchFormGeschwister.saveSubformTriggered,
        SharedEventGesuchFormKinder.saveSubformTriggered,
        SharedEventGesuchFormLebenslauf.saveSubformTriggered,
      ),
      withLatestFrom(store.select(selectSharedDataAccessConfigsView)),
      concatMap(
        ([
          { gesuchId, trancheId, gesuchFormular, origin },
          { compileTimeConfig },
        ]) => {
          if (!trancheId || !compileTimeConfig) {
            throw new Error('Missing trancheId or compileTimeConfig');
          }
          return getUpdateGesuchServiceCalls(
            gesuchService,
            gesuchId,
            trancheId,
            gesuchFormular,
          )
            [compileTimeConfig.appType]()
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
        },
      ),
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
    permissionsStore = inject(PermissionStore),
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
        store.select(selectSharedDataAccessGesuchsView).pipe(
          map(({ gesuch, trancheSetting }) => ({
            gesuch,
            trancheSetting,
          })),
          filter(ifPropsAreDefined),
        ),
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
              .getNextStepOf(
                stepFlowSig,
                trancheSetting.type,
                origin,
                gesuch,
                permissionsStore.rolesMapSig(),
              )
              .route.split('/'),
            id,
            ...trancheSetting.routesSuffix,
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
      withLatestFrom(
        store.select(selectSharedDataAccessGesuchsView).pipe(
          map(({ trancheSetting }) => trancheSetting),
          filter(isDefined),
        ),
      ),
      tap(([{ id, origin }, trancheSetting]) => {
        router.navigate([
          'gesuch',
          origin.route,
          id,
          ...trancheSetting.routesSuffix,
        ]);
      }),
    );
  },
  { functional: true, dispatch: false },
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
};

const viewOnlyFields = [
  'steuerdatenTabs',
] as const satisfies (keyof GesuchFormularType)[];

const prepareFormularData = (
  id: string,
  gesuchFormular: GesuchFormularUpdate | Partial<GesuchFormularType>,
): GesuchUpdate => {
  const { ...formular } = gesuchFormular;
  if ('ausbildung' in formular) {
    delete formular.ausbildung;
  }
  if ('versteckteEltern' in formular) {
    delete formular.versteckteEltern;
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
