import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  GesuchService,
  GesuchServiceGetGesucheSbRequestParams,
  PaginatedSbDashboard,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type GesuchState = {
  gesuche: CachedRemoteData<PaginatedSbDashboard>;
};

const initialState: GesuchState = {
  gesuche: initial(),
};

@Injectable()
export class GesuchStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('GesuchStore'),
) {
  private gesuchService = inject(GesuchService);

  cockpitViewSig = computed(() => {
    return {
      gesuche: fromCachedDataSig(this.gesuche),
      loading: isPending(this.gesuche()),
    };
  });

  loadGesuche$ = rxMethod<GesuchServiceGetGesucheSbRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuche: cachedPending(state.gesuche),
        }));
      }),
      switchMap((params) =>
        this.gesuchService
          .getGesucheSb$(params)
          .pipe(handleApiResponse((gesuche) => patchState(this, { gesuche }))),
      ),
    ),
  );
}
