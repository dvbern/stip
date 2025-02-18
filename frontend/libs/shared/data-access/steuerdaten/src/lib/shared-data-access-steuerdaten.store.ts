import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { SteuerdatenService, SteuerdatenUpdate } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type SteuerdatenState = {
  cachedSteuerdaten: CachedRemoteData<SteuerdatenUpdate[]>;
  steuerdaten: RemoteData<SteuerdatenUpdate[]>;
};

const initialState: SteuerdatenState = {
  cachedSteuerdaten: initial(),
  steuerdaten: initial(),
};

@Injectable()
export class SteuerdatenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('SteuerdatenStore'),
) {
  private steuerdatenService = inject(SteuerdatenService);

  cachedSteuerdatenListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedSteuerdaten);
  });

  steuerdatenViewSig = computed(() => {
    return this.steuerdaten.data();
  });

  getSteuerdaten$ = rxMethod<{ gesuchTrancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedSteuerdaten: cachedPending(state.cachedSteuerdaten),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
        this.steuerdatenService
          .getSteuerdaten$({
            gesuchTrancheId,
          })
          .pipe(
            handleApiResponse((steuerdaten) =>
              patchState(this, { steuerdaten }),
            ),
          ),
      ),
    ),
  );

  updateSteuerdaten$ = rxMethod<{
    gesuchTrancheId: string;
    steuerdatenUpdate: SteuerdatenUpdate[];
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedSteuerdaten: cachedPending(state.cachedSteuerdaten),
        }));
      }),
      switchMap(({ gesuchTrancheId, steuerdatenUpdate }) =>
        this.steuerdatenService
          .updateSteuerdaten$({ gesuchTrancheId, steuerdatenUpdate })
          .pipe(
            handleApiResponse(
              (steuerdaten) => {
                patchState(this, { steuerdaten });
              },
              {
                // onSuccess: (steuerdaten) => {
                //   // Do something after save, like showing a notification
                // },
              },
            ),
          ),
      ),
    ),
  );
}
