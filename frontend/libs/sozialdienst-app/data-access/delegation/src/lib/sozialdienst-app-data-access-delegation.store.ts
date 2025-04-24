import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { DelegationService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type DelegationState = {
  cachedDelegation: CachedRemoteData<unknown>;
  delegation: RemoteData<unknown>;
};

const initialState: DelegationState = {
  cachedDelegation: initial(),
  delegation: initial(),
};

@Injectable()
export class DelegationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('DelegationStore'),
) {
  private delegationService = inject(DelegationService);

  cachedDelegationListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedDelegation);
  });

  delegationViewSig = computed(() => {
    return this.delegation.data();
  });

  loadCachedDelegation$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDelegation: cachedPending(state.cachedDelegation),
        }));
      }),
      switchMap(() =>
        this.delegationService
          .getDelegation$()
          .pipe(
            handleApiResponse((cachedDelegation) =>
              patchState(this, { cachedDelegation }),
            ),
          ),
      ),
    ),
  );

  loadDelegation$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          delegation: pending(),
        }));
      }),
      switchMap(() =>
        this.delegationService
          .getDelegation$()
          .pipe(
            handleApiResponse((delegation) => patchState(this, { delegation })),
          ),
      ),
    ),
  );

  saveDelegation$ = rxMethod<{
    delegationId: string;
    values: unknown;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDelegation: cachedPending(state.cachedDelegation),
        }));
      }),
      switchMap(({ delegationId, values }) =>
        this.delegationService
          .updateDelegation$({
            delegationId,
            payload: values,
          })
          .pipe(
            handleApiResponse(
              (delegation) => {
                patchState(this, { delegation });
              },
              {
                onSuccess: (delegation) => {
                  // Do something after save, like showing a notification
                },
              },
            ),
          ),
      ),
    ),
  );
}
