import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Ausbildungsstaette,
  AusbildungsstaetteService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type AusbildungsstaetteState = {
  ausbildungsstaetten: CachedRemoteData<Ausbildungsstaette[]>;
};

const initialState: AusbildungsstaetteState = {
  ausbildungsstaetten: initial(),
};

@Injectable()
export class AusbildungsstaetteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungsstaetteService = inject(AusbildungsstaetteService);

  ausbildungsstaetteViewSig = computed(() => {
    return fromCachedDataSig(this.ausbildungsstaetten) ?? [];
  });

  loadAusbildungsstaetten$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungsstaetten: cachedPending(state.ausbildungsstaetten),
        }));
      }),
      switchMap(() =>
        this.ausbildungsstaetteService
          .getAusbildungsstaetten$()
          .pipe(
            handleApiResponse((ausbildungsstaette) =>
              patchState(this, { ausbildungsstaetten: ausbildungsstaette }),
            ),
          ),
      ),
    ),
  );
}
