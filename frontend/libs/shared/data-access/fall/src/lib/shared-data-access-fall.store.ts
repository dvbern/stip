import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { merge, partition, pipe, shareReplay, switchMap, tap } from 'rxjs';

import { Fall, FallService } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';
import { StoreUtilService } from '@dv/shared/util-data-access/store-util';

type FallState = {
  paymentSuspensionResponse: RemoteData<unknown>;
  cachedCurrentFall: CachedRemoteData<Fall>;
};

const initialState: FallState = {
  paymentSuspensionResponse: initial(),
  cachedCurrentFall: initial(),
};

@Injectable({ providedIn: 'root' })
export class FallStore extends signalStore(
  { protectedState: false },
  withState(initialState),
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

  suspendPayment$ = rxMethod<{
    gesuchId: string;
    suspended: boolean;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          paymentSuspensionResponse: pending(),
        });
      }),
      switchMap(({ gesuchId, suspended, onSuccess }) =>
        this.fallService
          .setPaymentProcessingSuspended$({
            gesuchId,
            suspended,
          })
          .pipe(
            handleApiResponse(
              (paymentSuspensionResponse) => {
                patchState(this, {
                  paymentSuspensionResponse,
                });
              },
              { onSuccess },
            ),
          ),
      ),
    ),
  );

  private loadOrCreateFall$() {
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
