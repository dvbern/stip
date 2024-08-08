import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, forkJoin, map, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  AenderungsantragCreate,
  Gesuch,
  GesuchService,
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
  private gesuchService = inject(GesuchService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  resetCachedGesuchAenderung() {
    patchState(this, { cachedGesuchAenderung: initial() });
  }

  loadCachedGesuchAenderung$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.gesuchService.getAenderungsantrag$({ gesuchId }).pipe(
          handleApiResponse((gesuche) => {
            patchState(this, { cachedAenderungsGesuche: gesuche });
          }),
        ),
      ),
    ),
  );

  getAllGesuchAenderungen$ = rxMethod<string[]>(
    pipe(
      exhaustMap((gesuchIds) => {
        return forkJoin(
          gesuchIds.map((gesuchId) =>
            this.gesuchService
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
    aenderungsantrag: AenderungsantragCreate;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap((aenderung) =>
        this.gesuchService
          .createAenderungsantrag$({
            gesuchId: aenderung.gesuchId,
            aenderungsantragCreate: aenderung.aenderungsantrag,
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
