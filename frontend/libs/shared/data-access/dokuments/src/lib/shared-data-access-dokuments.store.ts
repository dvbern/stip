import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { DokumentsService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type DokumentsState = {
  cachedDokuments: CachedRemoteData<unknown>;
  dokuments: RemoteData<unknown>;
};

const initialState: DokumentsState = {
  cachedDokuments: initial(),
  dokuments: initial(),
};

@Injectable()
export class DokumentsStore extends signalStore(
  withState(initialState),
  withDevtools('DokumentsStore'),
) {
  private dokumentsService = inject(DokumentsService);

  cachedDokumentsListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedDokuments);
  });

  dokumentsViewSig = computed(() => {
    return this.dokuments.data();
  });

  loadCachedDokuments$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDokuments: cachedPending(state.cachedDokuments),
        }));
      }),
      switchMap(() =>
        this.dokumentsService
          .getDokuments$()
          .pipe(
            handleApiResponse((cachedDokuments) =>
              patchState(this, { cachedDokuments }),
            ),
          ),
      ),
    ),
  );

  loadDokuments$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          dokuments: pending(),
        }));
      }),
      switchMap(() =>
        this.dokumentsService
          .getDokuments$()
          .pipe(
            handleApiResponse((dokuments) => patchState(this, { dokuments })),
          ),
      ),
    ),
  );

  saveDokuments$ = rxMethod<{
    dokumentsId: string;
    values: unknown;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDokuments: cachedPending(state.cachedDokuments),
        }));
      }),
      switchMap(({ dokumentsId, values }) =>
        this.dokumentsService
          .updateDokuments$({
            dokumentsId,
            payload: values,
          })
          .pipe(
            handleApiResponse(
              (dokuments) => {
                patchState(this, { dokuments });
              },
              {
                onSuccess: (dokuments) => {
                  // Do something after save, like showing a notification
                },
              },
            ),
          ),
      ),
    ),
  );
}
