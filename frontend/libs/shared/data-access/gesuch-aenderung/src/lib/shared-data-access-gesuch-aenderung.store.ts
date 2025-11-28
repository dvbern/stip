import { Injectable, Signal, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { map, pipe, switchMap, tap, throwError } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { isSharedModelError } from '@dv/shared/model/error';
import {
  CreateAenderungsantragRequest,
  CreateGesuchTrancheRequest,
  GesuchTranche,
  GesuchTrancheList,
  GesuchTrancheService,
  GesuchTrancheStatus,
  PatchAenderungsInfoRequest,
  getTrancheRoute,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { byAppType } from '@dv/shared/model/permission-state';
import { getRelativeTrancheRoute } from '@dv/shared/model/router';
import {
  shouldIgnoreBadRequestErrorsIf,
  shouldIgnoreNotFoundErrorsIf,
} from '@dv/shared/util/http';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type GesuchAenderungState = {
  cachedGesuchAenderung: CachedRemoteData<GesuchTranche>;
  cachedTranchenList: CachedRemoteData<GesuchTrancheList>;
};

const initialState: GesuchAenderungState = {
  cachedGesuchAenderung: initial(),
  cachedTranchenList: initial(),
};

const EXPECTED_ERRORS: Record<string, string> = {
  '{jakarta.validation.constraints.gesuchTranche.daterangeTooShort.message}':
    'shared.form.error.tranche.daterangeTooShort',
};

export type AenderungChangeState = Extract<
  GesuchTrancheStatus,
  'MANUELLE_AENDERUNG' | 'AKZEPTIERT' | 'ABGELEHNT'
>;

export type AenderungCompletionState =
  | 'open'
  | 'completed'
  | 'rejected'
  | 'initial';
const aenderungStatusMap = {
  IN_BEARBEITUNG_GS: null,
  UEBERPRUEFEN: 'open',
  FEHLENDE_DOKUMENTE: 'open',
  ABGELEHNT: null,
  AKZEPTIERT: 'completed',
  MANUELLE_AENDERUNG: 'completed',
} satisfies Record<GesuchTrancheStatus, AenderungCompletionState | null>;

@Injectable({ providedIn: 'root' })
export class GesuchAenderungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchTrancheService = inject(GesuchTrancheService);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private config = inject(SharedModelCompileTimeConfig);
  private router = inject(Router);

  aenderungenViewSig = computed(() => {
    const tranchenList = this.cachedTranchenList();
    const initialGesucheList = this.cachedTranchenList();
    const [aenderungen, abgelehnteAenderungen, initialGesuche] = (
      [
        ['aenderung', tranchenList.data?.aenderungen],
        ['aenderung', tranchenList.data?.abgelehnteAenderungen],
        ['initial', initialGesucheList.data?.initialTranchen],
      ] as const
    ).map(
      ([route, lists]) =>
        lists?.map((tranche, index) => ({
          ...tranche,
          index,
          route: getTrancheRoute(route),
          revision: tranche.revision ?? undefined, // API sends null
        })) ?? [],
    );
    return {
      loading: isPending(tranchenList),
      hasAenderungen:
        aenderungen.length > 0 ||
        abgelehnteAenderungen.length > 0 ||
        initialGesuche.length > 0,
      aenderungen,
      abgelehnteAenderungen,
      initialGesuche,
      byStatus: {
        open: [],
        completed: [],
        ...aenderungen.reduce(
          (acc, tranche) => {
            const key = aenderungStatusMap[tranche.status];
            if (key) {
              if (!acc[key]) {
                acc[key] = [];
              }
              acc[key].push(tranche);
            }
            return acc;
          },
          {} as Partial<Record<AenderungCompletionState, typeof aenderungen>>,
        ),
        rejected: abgelehnteAenderungen,
        initial: initialGesuche,
      },
    };
  });

  tranchenViewSig = computed(() => {
    const list = this.cachedTranchenList();
    return {
      loading: isPending(list),
      list: list.data?.tranchen?.filter((t) => t.typ === 'TRANCHE') ?? [],
    };
  });

  getRelativeTranchenViewSig = (gesuchIdSig: Signal<string | undefined>) => {
    const relativeRouteSig = getRelativeTrancheRoute(this.router, 'TRANCHE');

    return computed(() => {
      const gesuchId = gesuchIdSig();
      const relativeRoute = relativeRouteSig();
      const listRd = this.tranchenViewSig();

      return {
        ...listRd,
        list: listRd.list.map((tranche) => ({
          ...tranche,
          url: relativeRoute
            ? this.router.createUrlTree([...relativeRoute, tranche.id])
            : ['/', 'gesuch', gesuchId, 'tranche', tranche.id],
        })),
      };
    });
  };

  getAllTranchenForGesuch$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedTranchenList: cachedPending(state.cachedTranchenList),
        }));
      }),
      switchMap((req) => {
        const params = [
          req,
          undefined,
          undefined,
          {
            context: shouldIgnoreNotFoundErrorsIf(true),
          },
        ] as const;
        const serviceCall$ = byAppType(this.config.appType, {
          'gesuch-app': () =>
            this.gesuchTrancheService.getAllTranchenForGesuchGS$(...params),
          'sachbearbeitung-app': () =>
            this.gesuchTrancheService.getAllTranchenForGesuchSB$(...params),
          'demo-data-app': () =>
            throwError(() => new Error('Not implemented for this AppType')),
        });
        return serviceCall$.pipe().pipe(map((tranchen) => tranchen ?? []));
      }),
      handleApiResponse((tranchen) => {
        patchState(this, () => ({
          cachedTranchenList: tranchen,
        }));
      }),
    ),
  );

  createGesuchAenderung$ = rxMethod<{
    gesuchId: string;
    createAenderungsantragRequest: CreateAenderungsantragRequest;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          // change to not cached?
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(
        ({ gesuchId, createAenderungsantragRequest, onSuccess, onFailure }) =>
          this.gesuchTrancheService
            .createAenderungsantrag$({
              gesuchId,
              createAenderungsantragRequest,
            })
            .pipe(
              handleApiResponse(
                (gesuchAenderung) => {
                  patchState(this, () => ({
                    cachedGesuchAenderung: gesuchAenderung,
                  }));
                },
                {
                  onSuccess: (data) => {
                    this.globalNotificationStore.createSuccessNotification({
                      messageKey:
                        'shared.dialog.gesuch-aenderung.create.success',
                    });
                    this.router.navigate([
                      'gesuch',
                      PERSON.route,
                      gesuchId,
                      getTrancheRoute('aenderung'),
                      data.id,
                    ]);
                    onSuccess();
                  },
                  onFailure,
                },
              ),
            ),
      ),
    ),
  );

  updateAenderungVonBis$ = rxMethod<{
    aenderungId: string;
    patchAenderungsInfoRequest: PatchAenderungsInfoRequest;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      switchMap(
        ({ aenderungId, patchAenderungsInfoRequest, onSuccess, onFailure }) =>
          this.gesuchTrancheService
            .patchAenderungInfo$({
              aenderungId,
              patchAenderungsInfoRequest,
            })
            .pipe(
              handleApiResponse(() => undefined, {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey:
                      'shared.dialog.gesuch-aenderung.update-von-bis.success',
                  });
                  onSuccess();
                },
                onFailure,
              }),
            ),
      ),
    ),
  );

  deleteGesuchAenderung$ = rxMethod<{
    aenderungId: string;
    onSuccess?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(({ aenderungId, onSuccess }) =>
        this.gesuchTrancheService.deleteAenderung$({ aenderungId }).pipe(
          handleApiResponse(
            () => {
              patchState(this, () => ({
                cachedGesuchAenderung: initial(),
              }));
            },
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.dialog.gesuch-aenderung.delete.success',
                });
                onSuccess?.();
              },
            },
          ),
        ),
      ),
    ),
  );

  createGesuchTrancheCopy$ = rxMethod<{
    gesuchId: string;
    createGesuchTrancheRequest?: CreateGesuchTrancheRequest;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(
        ({ gesuchId, createGesuchTrancheRequest, onSuccess, onFailure }) =>
          this.gesuchTrancheService
            .createGesuchTrancheCopy$({
              gesuchId,
              createGesuchTrancheRequest,
            })
            .pipe(
              handleApiResponse(
                (gesuchAenderung) => {
                  patchState(this, () => ({
                    cachedGesuchAenderung: gesuchAenderung,
                  }));
                },
                {
                  onSuccess: () => {
                    this.globalNotificationStore.createSuccessNotification({
                      messageKey: 'shared.dialog.gesuch.tranche.create.success',
                    });
                    this.getAllTranchenForGesuch$({ gesuchId });
                    onSuccess();
                  },
                  onFailure,
                },
              ),
            ),
      ),
    ),
  );

  changeAenderungState$ = rxMethod<{
    aenderungId: string;
    gesuchId: string;
    comment: string;
    target: AenderungChangeState;
    onSuccess: (trancheId: string) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(
        ({
          aenderungId,
          target,
          gesuchId,
          comment,
          onSuccess: additionalOnSuccess,
        }) => {
          const services$ = {
            AKZEPTIERT: () =>
              this.gesuchTrancheService.aenderungAkzeptieren$(
                { aenderungId },
                undefined,
                undefined,
                {
                  context: shouldIgnoreBadRequestErrorsIf(true),
                },
              ),
            ABGELEHNT: () =>
              this.gesuchTrancheService.aenderungAblehnen$({
                aenderungId,
                kommentar: { text: comment },
              }),
            MANUELLE_AENDERUNG: () =>
              this.gesuchTrancheService.aenderungManuellAnpassen$({
                aenderungId,
              }),
          } satisfies Record<AenderungChangeState, unknown>;

          return services$[target]().pipe(
            handleApiResponse(
              (gesuchAenderung) => {
                patchState(this, () => ({
                  cachedGesuchAenderung: gesuchAenderung,
                }));
              },
              {
                onSuccess: (value) => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: `shared.dialog.gesuch-aenderung.${target}.success`,
                  });
                  this.getAllTranchenForGesuch$({ gesuchId });
                  additionalOnSuccess(value.id);
                },
                onFailure: handleKnownErrors(this.globalNotificationStore),
              },
            ),
          );
        },
      ),
    ),
  );
}

const handleKnownErrors =
  (notificationStore: GlobalNotificationStore) =>
  (error: unknown): void => {
    if (
      isSharedModelError(error) &&
      error.type === 'validationError' &&
      EXPECTED_ERRORS[error.messageKey]
    ) {
      notificationStore.createNotification({
        type: 'ERROR',
        messageKey: EXPECTED_ERRORS[error.messageKey],
      });
    }
  };
