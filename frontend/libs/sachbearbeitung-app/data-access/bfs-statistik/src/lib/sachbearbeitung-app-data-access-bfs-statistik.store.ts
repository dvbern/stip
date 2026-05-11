import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { Statistik, StatistikService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type BfsStatistikState = {
  bfsStatistik: CachedRemoteData<Statistik[]>;
};

const initialState: BfsStatistikState = {
  bfsStatistik: initial(),
};

@Injectable()
export class BfsStatistikStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private bfsStatistikService = inject(StatistikService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  bfsStatistikListViewSig = computed(() => {
    return fromCachedDataSig(this.bfsStatistik);
  });

  loadAllBfsStatistik$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          bfsStatistik: cachedPending(state.bfsStatistik),
        }));
      }),
      switchMap(() =>
        this.bfsStatistikService
          .getAllStatistiks$()
          .pipe(
            handleApiResponse((bfsStatistik) =>
              patchState(this, { bfsStatistik }),
            ),
          ),
      ),
    ),
  );

  createBfsStatistik$ = rxMethod<{
    year: number;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          bfsStatistik: cachedPending(state.bfsStatistik),
        }));
      }),
      switchMap(({ year, onSuccess }) =>
        this.bfsStatistikService.createStatistikJob$({ year }).pipe(
          handleApiResponse(
            (bfsStatistik) => {
              patchState(this, { bfsStatistik });
            },
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification<SachbearbeitungAppTranslationKey>(
                  {
                    messageKey:
                      'sachbearbeitung-app.admin.bfs-statistik.createSuccess',
                  },
                );
                onSuccess();
              },
            },
          ),
        ),
      ),
    ),
  );
}
