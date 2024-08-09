import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, forkJoin, map, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  CreateAenderungsantragRequest,
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
  cachedGesuchAenderung: CachedRemoteData<Gesuch>;
  cachedAenderungsGesuche: CachedRemoteData<Gesuch[]>;
};

const initialState: GesuchAenderungState = {
  cachedGesuchAenderung: initial(),
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
    patchState(this, { cachedGesuchAenderung: initial() });
  }

  getAllGesuchAenderungen$ = rxMethod<string[]>(
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
                patchState(this, (state) => ({
                  cachedGesuchAenderung: gesuchAenderung,
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
