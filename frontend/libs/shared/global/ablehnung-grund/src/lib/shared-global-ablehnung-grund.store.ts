import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { StipDecisionService, StipDecisionText } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type AblehnungGrundState = {
  gruende: CachedRemoteData<StipDecisionText[]>;
};

const initialState: AblehnungGrundState = {
  gruende: initial(),
};

@Injectable({ providedIn: 'root' })
export class AblehnungGrundStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private stipDecisionService = inject(StipDecisionService);

  ablehnungsGruende = computed(() => {
    return fromCachedDataSig(this.gruende);
  });

  loadAblehnungsGruende$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gruende: cachedPending(state.gruende),
        }));
      }),
      switchMap(() =>
        this.stipDecisionService
          .getAll$()
          .pipe(handleApiResponse((gruende) => patchState(this, { gruende }))),
      ),
    ),
  );
}
