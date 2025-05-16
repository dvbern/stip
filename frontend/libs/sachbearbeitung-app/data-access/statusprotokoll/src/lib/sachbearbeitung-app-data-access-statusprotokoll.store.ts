import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GesuchService, StatusprotokollEntry } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type StatusprotokollState = {
  cachedStatusprotokoll: CachedRemoteData<StatusprotokollEntry[]>;
};

const initialState: StatusprotokollState = {
  cachedStatusprotokoll: initial(),
};

@Injectable()
export class StatusprotokollStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);

  cachedStatusprotokollListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedStatusprotokoll);
  });

  loadCachedStatusprotokoll$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedStatusprotokoll: cachedPending(state.cachedStatusprotokoll),
        }));
      }),
      switchMap((loadParm) =>
        this.gesuchService
          .getStatusProtokoll$(loadParm)
          .pipe(
            handleApiResponse((cachedStatusprotokoll) =>
              patchState(this, { cachedStatusprotokoll }),
            ),
          ),
      ),
    ),
  );
}
