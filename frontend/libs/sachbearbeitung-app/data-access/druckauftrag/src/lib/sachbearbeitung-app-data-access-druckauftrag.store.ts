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

type DruckauftragState = {
  cachedDruckauftrag: CachedRemoteData<unknown>;
  druckauftrag: RemoteData<unknown>;
};

const initialState: DruckauftragState = {
  cachedDruckauftrag: initial(),
  druckauftrag: initial(),
};

@Injectable()
export class DruckauftragStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  // private druckauftragService = inject(DruckauftragService);

  cachedDruckauftragListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedDruckauftrag);
  });

  druckauftragViewSig = computed(() => {
    return this.druckauftrag.data();
  });

  loadCachedDruckauftrag$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDruckauftrag: cachedPending(state.cachedDruckauftrag),
        }));
      }),
    ),
  );

  loadDruckauftrag$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          druckauftrag: pending(),
        }));
      }),
    ),
  );
}
