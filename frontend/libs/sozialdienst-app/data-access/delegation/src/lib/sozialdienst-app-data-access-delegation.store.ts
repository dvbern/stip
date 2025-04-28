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
  faelleWithDelegierung: CachedRemoteData<PaginatedSozDashboard>;
  delegation: RemoteData<unknown>;
};

const initialState: DelegationState = {
  faelleWithDelegierung: initial(),
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
      faelleWithDelegierung: fromCachedDataSig(this.faelleWithDelegierung),
      loading: isPending(this.faelleWithDelegierung()),
    };
  });

  loadFaelleWithDelegierung$ =
    rxMethod<DelegierenServiceGetDelegierungSozRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            faelleWithDelegierung: cachedPending(state.faelleWithDelegierung),
          }));
        }),
        switchMap((params) =>
          this.delegierenService
            .getDelegierungSoz$(params)
            .pipe(
              handleApiResponse((faelleWithDelegierung) =>
                patchState(this, { faelleWithDelegierung }),
              ),
            ),
        ),
      ),
    );
}
