import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DelegierenService,
  DelegierenServiceGetDelegierungSozRequestParams,
  PaginatedSozDashboard,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type DelegationState = {
  paginatedSozDashboard: CachedRemoteData<PaginatedSozDashboard>;
  delegation: RemoteData<unknown>;
};

const initialState: DelegationState = {
  paginatedSozDashboard: initial(),
  delegation: initial(),
};

@Injectable()
export class DelegationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('DelegationStore'),
) {
  private delegierenService = inject(DelegierenService);

  cockpitViewSig = computed(() => {
    return {
      paginatedSozDashboard: fromCachedDataSig(this.paginatedSozDashboard),
      loading: isPending(this.paginatedSozDashboard()),
    };
  });

  loadPaginatedSozDashboard$ =
    rxMethod<DelegierenServiceGetDelegierungSozRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            paginatedSozDashboard: cachedPending(state.paginatedSozDashboard),
          }));
        }),
        switchMap((params) =>
          this.delegierenService.getDelegierungSoz$(params).pipe(
            handleApiResponse((paginatedSozDashboard) =>
              patchState(this, {
                paginatedSozDashboard: paginatedSozDashboard,
              }),
            ),
          ),
        ),
      ),
    );
}
