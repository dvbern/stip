import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, forkJoin, map, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  CreateAenderungsantragRequest,
  CreateGesuchTrancheRequest,
  Gesuch,
  GesuchTrancheService,
} from '@dv/shared/model/gesuch';
import { shouldIgnoreNotFoundErrorsIf } from '@dv/shared/util/http';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isSuccess,
  success,
} from '@dv/shared/util/remote-data';

type GesuchAenderungState = {
  cachedAenderungsGesuch: CachedRemoteData<Gesuch>;
  cachedAenderungsGesuche: CachedRemoteData<Gesuch[]>;
};

const initialState: GesuchAenderungState = {
  cachedAenderungsGesuch: initial(),
  cachedAenderungsGesuche: initial(),
};

@Injectable()
export class GesuchAenderungStore extends signalStore(
  withState(initialState),
  withDevtools('GesuchAenderungStore'),
) {
  private gesuchTrancheService = inject(GesuchTrancheService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  resetCachedGesuchAenderung() {
    patchState(this, { cachedAenderungsGesuch: initial() });
  }

  getAllAenderungsGesuche$ = rxMethod<string[]>(
    pipe(
      exhaustMap((gesuchIds) => {
        return forkJoin(
          gesuchIds.map((gesuchId) =>
            this.gesuchTrancheService
              .getAenderungsantrag$({ gesuchId }, undefined, undefined, {
                context: shouldIgnoreNotFoundErrorsIf(true),
              })
              .pipe(map((gesuch) => gesuch ?? [])),
          ),
        );
      }),
      map((gesuche) => gesuche.flat()),
      tap((x) => {
        console.log('getAllAenderungsGesuche$', x);
      }),
      handleApiResponse((gesuche) => {
        patchState(this, { cachedAenderungsGesuche: gesuche });
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
          cachedAenderungsGesuch: cachedPending(state.cachedAenderungsGesuch),
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
                patchState(this, (state) => ({
                  cachedAenderungsGesuch: gesuchAenderung,
                  cachedAenderungsGesuche: isSuccess(gesuchAenderung)
                    ? success([
                        ...(state.cachedAenderungsGesuche.data ?? []),
                        gesuchAenderung.data,
                      ])
                    : cachedPending(state.cachedAenderungsGesuche),
                }));
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.dialog.gesuch-aenderung.success',
                  });
                },
              },
            ),
          ),
      ),
    ),
  );

  createGesuchTrancheCopy$ = rxMethod<{
    gesuchId: string;
    trancheId: string;
    createGesuchTrancheRequest?: CreateGesuchTrancheRequest;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          // change to not cached?
          cachedAenderungsGesuch: cachedPending(state.cachedAenderungsGesuch),
        }));
      }),
      switchMap(({ gesuchId, trancheId, createGesuchTrancheRequest }) =>
        this.gesuchTrancheService
          .createGesuchTrancheCopy$({
            gesuchId,
            trancheId,
            createGesuchTrancheRequest,
          })
          .pipe(
            handleApiResponse(
              (gesuchAenderung) => {
                patchState(this, (state) => ({
                  cachedAenderungsGesuch: gesuchAenderung,
                  cachedAenderungsGesuche: isSuccess(gesuchAenderung)
                    ? success([
                        ...(state.cachedAenderungsGesuche.data ?? []),
                        gesuchAenderung.data,
                      ])
                    : cachedPending(state.cachedAenderungsGesuche),
                }));
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.dialog.gesuch-aenderung.success',
                  });
                },
              },
            ),
          ),
      ),
    ),
  );
}
