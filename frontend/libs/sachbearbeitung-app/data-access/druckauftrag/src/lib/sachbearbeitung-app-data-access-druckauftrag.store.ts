import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, tap } from 'rxjs';

import {
  DruckenService,
  PaginatedDruckauftraege,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  initial,
  isPending,
  pending,
} from '@dv/shared/util/remote-data';

type DruckauftragState = {
  cachedPaginatedDruckauftraege: CachedRemoteData<PaginatedDruckauftraege>;
  druckauftrag: RemoteData<unknown>;
};

const initialState: DruckauftragState = {
  cachedPaginatedDruckauftraege: initial(),
  druckauftrag: initial(),
};

@Injectable()
export class DruckauftragStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private druckService = inject(DruckenService);

  cachedDruckauftragListViewSig = computed(() => {
    return {
      druckauftraege: fromCachedDataSig(this.cachedPaginatedDruckauftraege),
      loading: isPending(this.cachedPaginatedDruckauftraege()),
    };
  });

  druckauftragViewSig = computed(() => {
    return this.druckauftrag.data();
  });

  loadCachedDruckauftrag$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedPaginatedDruckauftraege: cachedPending(
            state.cachedPaginatedDruckauftraege,
          ),
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
