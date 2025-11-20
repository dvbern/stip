import { Injectable, computed } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, tap } from 'rxjs';

import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type DarlehenState = {
  cachedDarlehen: CachedRemoteData<unknown>;
  darlehen: RemoteData<unknown>;
};

const initialState: DarlehenState = {
  cachedDarlehen: initial(),
  darlehen: initial(),
};

@Injectable()
export class DarlehenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  // private darlehenService = inject(DarlehenService);

  cachedDarlehenListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedDarlehen);
  });

  darlehenViewSig = computed(() => {
    return this.darlehen.data();
  });

  loadCachedDarlehen$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      // switchMap(() =>
      //   this.darlehenService
      //     .getDarlehen$()
      //     .pipe(
      //       handleApiResponse((cachedDarlehen) =>
      //         patchState(this, { cachedDarlehen }),
      //       ),
      //     ),
      // ),
    ),
  );

  loadDarlehen$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          darlehen: pending(),
        }));
      }),
      // switchMap(() =>
      //   this.darlehenService
      //     .getDarlehen$()
      //     .pipe(
      //       handleApiResponse((darlehen) => patchState(this, { darlehen })),
      //     ),
      // ),
    ),
  );

  saveDarlehen$ = rxMethod<{
    darlehenId: string;
    values: unknown;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      // switchMap(({ darlehenId, values }) =>
      //   this.darlehenService
      //     .updateDarlehen$({
      //       darlehenId,
      //       payload: values,
      //     })
      //     .pipe(
      //       handleApiResponse(
      //         (darlehen) => {
      //           patchState(this, { darlehen });
      //         },
      //         {
      //           onSuccess: (darlehen) => {
      //             // Do something after save, like showing a notification
      //           },
      //         },
      //       ),
      //     ),
      // ),
    ),
  );
}
