import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  FallHeader,
  FallHeaderService,
  FallHeaderServiceGetFallHeaderRequestParams,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type FallHeaderState = {
  cachedFallHeader: CachedRemoteData<FallHeader>;
};

const initialState: FallHeaderState = {
  cachedFallHeader: initial(),
};

@Injectable({ providedIn: 'root' })
export class FallHeaderStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private fallHeaderService = inject(FallHeaderService);

  fallHeaderViewSig = computed(() => {
    return fromCachedDataSig(this.cachedFallHeader);
  });

  loadFallHeader$ = rxMethod<FallHeaderServiceGetFallHeaderRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedFallHeader: cachedPending(state.cachedFallHeader),
        }));
      }),
      switchMap(({ fallId }) =>
        this.fallHeaderService
          .getFallHeader$({ fallId })
          .pipe(
            handleApiResponse((cachedFallHeader) =>
              patchState(this, { cachedFallHeader }),
            ),
          ),
      ),
    ),
  );
}
