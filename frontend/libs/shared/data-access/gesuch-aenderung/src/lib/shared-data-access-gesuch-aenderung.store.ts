import { Injectable, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { map, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  CreateAenderungsantragRequest,
  CreateGesuchTrancheRequest,
  GesuchTranche,
  GesuchTrancheService,
  GesuchTrancheSlim,
  GesuchTrancheStatus,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { shouldIgnoreNotFoundErrorsIf } from '@dv/shared/util/http';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type GesuchAenderungState = {
  cachedGesuchAenderung: CachedRemoteData<GesuchTranche>;
  cachedTranchenSlim: CachedRemoteData<GesuchTrancheSlim[]>;
};

const initialState: GesuchAenderungState = {
  cachedGesuchAenderung: initial(),
  cachedTranchenSlim: initial(),
};

export type AenderungChangeState = Extract<
  GesuchTrancheStatus,
  'MANUELLE_AENDERUNG' | 'AKZEPTIERT' | 'ABGELEHNT'
>;

type AenderungCompletionState = 'open' | 'completed';
const aenderungStatusMap = {
  IN_BEARBEITUNG_GS: null,
  ABGELEHNT: null,
  AKZEPTIERT: 'completed',
  MANUELLE_AENDERUNG: 'completed',
  UEBERPRUEFEN: 'open',
} satisfies Record<GesuchTrancheStatus, AenderungCompletionState | null>;

@Injectable({ providedIn: 'root' })
export class GesuchAenderungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('GesuchAenderungStore'),
) {
  private gesuchTrancheService = inject(GesuchTrancheService);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private store = inject(Store);
  private router = inject(Router);

  aenderungenViewSig = computed(() => {
    const tranchen = this.cachedTranchenSlim();
    const aenderungen =
      tranchen.data
        ?.filter((t) => t.typ === 'AENDERUNG')
        .map((t, index) => ({ ...t, index })) ?? [];
    return {
      loading: isPending(tranchen),
      hasAenderungen: aenderungen.length > 0,
      list: aenderungen,
      byStatus: aenderungen.reduce(
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
        {} as Record<AenderungCompletionState, typeof aenderungen>,
      ),
    };
  });

  tranchenViewSig = computed(() => {
    const tranchen = this.cachedTranchenSlim();
    return {
      loading: isPending(tranchen),
      list: tranchen.data?.filter((t) => t.typ === 'TRANCHE') ?? [],
    };
  });

  getAllTranchenForGesuch$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedTranchenSlim: cachedPending(state.cachedTranchenSlim),
        }));
      }),
      switchMap((req) =>
        this.gesuchTrancheService
          .getAllTranchenForGesuch$(req, undefined, undefined, {
            context: shouldIgnoreNotFoundErrorsIf(true),
          })
          .pipe(map((tranchen) => tranchen ?? [])),
      ),
      handleApiResponse((tranchen) => {
        patchState(this, () => ({
          cachedTranchenSlim: tranchen,
        }));
      }),
    ),
  );

  createGesuchAenderung$ = rxMethod<{
    gesuchId: string;
    createAenderungsantragRequest: CreateAenderungsantragRequest;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          // change to not cached?
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(({ gesuchId, createAenderungsantragRequest }) =>
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
                    messageKey: 'shared.dialog.gesuch-aenderung.create.success',
                  });
                  this.router.navigate([
                    'gesuch',
                    PERSON.route,
                    gesuchId,
                    'aenderung',
                    data.id,
                  ]);
                },
              },
            ),
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
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(({ gesuchId, createGesuchTrancheRequest }) =>
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
                },
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
              this.gesuchTrancheService.aenderungAkzeptieren$({ aenderungId }),
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
              },
            ),
          );
        },
      ),
    ),
  );
}
