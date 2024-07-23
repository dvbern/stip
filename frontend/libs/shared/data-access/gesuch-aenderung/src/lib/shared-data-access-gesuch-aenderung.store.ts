import { Injectable, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { catchError, of, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  GesuchAenderungService,
  GesuchAenderungsAntrag,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type GesuchAenderungState = {
  cachedGesuchAenderung: CachedRemoteData<{ id: string }>;
};

const initialState: GesuchAenderungState = {
  cachedGesuchAenderung: initial(),
};

@Injectable()
export class GesuchAenderungStore extends signalStore(
  withState(initialState),
  withDevtools('GesuchAenderungStore'),
) {
  private gesuchAenderungService = inject(GesuchAenderungService);
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
        this.gesuchAenderungService
          .getGesuchsAntraege$({ gesuchId })
          .pipe(
            handleApiResponse((cachedGesuchAenderung) =>
              patchState(this, { cachedGesuchAenderung }),
            ),
          ),
      ),
    ),
  );

  createGesuchAenderung$ = rxMethod<GesuchAenderungsAntrag>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap((aenderung) =>
        this.gesuchAenderungService
          .createGesuchsAntrag$({ gesuchAenderungsAntrag: aenderung })
          .pipe(
            catchError((err) => {
              console.error(err);
              return of({ id: '123' }); // TODO: Remove once backend is implemented
            }),
            handleApiResponse(
              (gesuchAenderung) => {
                patchState(this, { cachedGesuchAenderung: gesuchAenderung });
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
