import { Injectable, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  AuszahlungService,
  FallAuszahlung,
  FallAuszahlungUpdate,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  optimisticCachedPending,
} from '@dv/shared/util/remote-data';

type AuszahlungState = {
  auszahlung: CachedRemoteData<FallAuszahlung>;
};

const initialState: AuszahlungState = {
  auszahlung: initial(),
};

@Injectable()
export class AuszahlungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private auszahlungService = inject(AuszahlungService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  loadAuszahlung$ = rxMethod<{ fallId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          auszahlung: cachedPending(state.auszahlung),
        }));
      }),
      switchMap(({ fallId }) =>
        this.auszahlungService
          .getAuszahlungForGesuch$({ fallId })
          .pipe(
            handleApiResponse((cachedAuszahlung) =>
              patchState(this, { auszahlung: cachedAuszahlung }),
            ),
          ),
      ),
    ),
  );

  createAuszahlung$ = rxMethod<{
    fallId: string;
    auszahlung: FallAuszahlungUpdate;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          auszahlung: cachedPending(state.auszahlung),
        }));
      }),
      switchMap(({ fallId, auszahlung }) =>
        this.auszahlungService
          .createAuszahlungForGesuch$({
            fallAuszahlungUpdate: auszahlung,
            fallId,
          })
          .pipe(
            handleApiResponse(
              (auszahlung) => {
                patchState(this, { auszahlung });
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.auszahlung.create.success',
                  });
                },
              },
            ),
          ),
      ),
    ),
  );

  updateAuszahlung$ = rxMethod<{
    fallId: string;
    auszahlung: FallAuszahlungUpdate;
  }>(
    pipe(
      tap(({ auszahlung }) => {
        patchState(this, (state) => ({
          auszahlung: optimisticCachedPending(state.auszahlung, {
            auszahlung,
          }),
        }));
      }),
      switchMap(({ fallId, auszahlung }) =>
        this.auszahlungService
          .updateAuszahlungForGesuch$({
            fallAuszahlungUpdate: auszahlung,
            fallId,
          })
          .pipe(
            handleApiResponse(
              (auszahlung) => {
                patchState(this, { auszahlung });
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.auszahlung.update.success',
                  });
                },
              },
            ),
          ),
      ),
    ),
  );
}
