import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GesuchService, Verfuegung } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type InfosAdminState = {
  verfuegungen: CachedRemoteData<Verfuegung[]>;
};

const initialState: InfosAdminState = {
  verfuegungen: initial(),
};

@Injectable()
export class InfosAdminStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);

  verfuegungenViewSig = computed(() => {
    return fromCachedDataSig(this.verfuegungen);
  });

  loadVerfuegungen$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          verfuegungen: cachedPending(state.verfuegungen),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.gesuchService
          .getAllVerfuegungen$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((verfuegungen) =>
              patchState(this, { verfuegungen }),
            ),
          ),
      ),
    ),
  );
}
