import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import { GesuchInfo, GesuchService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type GesuchState = {
  gesuchInfo: CachedRemoteData<GesuchInfo>;
};

const initialState: GesuchState = {
  gesuchInfo: initial(),
};

@Injectable()
export class GesuchInfoStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);

  infoViewSig = computed(() => ({
    gesuchInfo: fromCachedDataSig(this.gesuchInfo),
    loading: isPending(this.gesuchInfo()),
  }));

  loadGesuchInfo$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuchInfo: cachedPending(state.gesuchInfo),
        }));
      }),
      exhaustMap(({ gesuchId }) =>
        this.gesuchService
          .getGesuchInfo$({ gesuchId })
          .pipe(
            handleApiResponse((gesuchInfo) => patchState(this, { gesuchInfo })),
          ),
      ),
    ),
  );
}
