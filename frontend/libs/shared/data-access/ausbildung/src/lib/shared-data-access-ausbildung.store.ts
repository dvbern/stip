import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { AusbildungService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type AusbildungState = {
  cachedAusbildung: CachedRemoteData<unknown>;
  ausbildung: RemoteData<unknown>;
};

const initialState: AusbildungState = {
  cachedAusbildung: initial(),
  ausbildung: initial(),
};

@Injectable()
export class AusbildungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('AusbildungStore'),
) {
  private ausbildungService = inject(AusbildungService);

  cachedAusbildungListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedAusbildung);
  });

  ausbildungViewSig = computed(() => {
    return this.ausbildung.data();
  });

  loadCachedAusbildung$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedAusbildung: cachedPending(state.cachedAusbildung),
        }));
      }),
      switchMap(() =>
        this.ausbildungService
          .getAusbildung$()
          .pipe(
            handleApiResponse((cachedAusbildung) =>
              patchState(this, { cachedAusbildung }),
            ),
          ),
      ),
    ),
  );

  loadAusbildung$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          ausbildung: pending(),
        }));
      }),
      switchMap(() =>
        this.ausbildungService
          .getAusbildung$()
          .pipe(
            handleApiResponse((ausbildung) => patchState(this, { ausbildung })),
          ),
      ),
    ),
  );

  saveAusbildung$ = rxMethod<{
    ausbildungId: string;
    values: unknown;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedAusbildung: cachedPending(state.cachedAusbildung),
        }));
      }),
      switchMap(({ ausbildungId, values }) =>
        this.ausbildungService
          .updateAusbildung$({
            ausbildungId,
            payload: values,
          })
          .pipe(
            handleApiResponse(
              (ausbildung) => {
                patchState(this, { ausbildung });
              },
              {
                onSuccess: (ausbildung) => {
                  // Do something after save, like showing a notification
                },
              },
            ),
          ),
      ),
    ),
  );
}
