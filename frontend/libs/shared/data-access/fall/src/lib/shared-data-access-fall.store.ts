import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { merge, partition, pipe, shareReplay, switchMap, tap } from 'rxjs';

import { Fall, FallService } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

type FallState = {
  cachedCurrentFall: CachedRemoteData<Fall>;
};

const initialState: FallState = {
  cachedCurrentFall: initial(),
};

@Injectable()
export class FallStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('FallStore'),
) {
  private storeUtilService = inject(StoreUtilService);
  private fallService = inject(FallService);

  currentFallViewSig = computed(() => {
    return fromCachedDataSig(this.cachedCurrentFall);
  });

  loadCurrentFall$ = rxMethod<void>(
    pipe(
      this.storeUtilService.waitForBenutzerData$(),
      tap(() => {
        patchState(this, (state) => ({
          cachedCurrentFall: cachedPending(state.cachedCurrentFall),
        }));
      }),
      switchMap(() => this.loadOrCreateFall$()),
    ),
  );

  loadOrCreateFall$() {
    const [hasFall$, hasNoFall$] = partition(
      this.fallService
        .getFallForGs$()
        .pipe(shareReplay({ bufferSize: 1, refCount: true })),
      isDefined,
    );

    return merge(
      hasFall$.pipe(
        handleApiResponse((fall) =>
          patchState(this, { cachedCurrentFall: fall }),
        ),
      ),
      hasNoFall$.pipe(
        switchMap(() =>
          this.fallService
            .createFallForGs$()
            .pipe(
              handleApiResponse((fall) =>
                patchState(this, { cachedCurrentFall: fall }),
              ),
            ),
        ),
      ),
    );
  }
}
