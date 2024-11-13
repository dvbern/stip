import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { LandEuEfta, StammdatenService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type EuEftaLaenderState = {
  euEftaLaender: CachedRemoteData<LandEuEfta[]>;
};

const initialState: EuEftaLaenderState = {
  euEftaLaender: initial(),
};

@Injectable()
export class EuEftaLaenderStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('EuEftaLaenderStore'),
) {
  private stammdatenService = inject(StammdatenService);

  euEftaLaenderListViewSig = computed(() => {
    return fromCachedDataSig(this.euEftaLaender);
  });

  loadLaender$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          euEftaLaender: cachedPending(state.euEftaLaender),
        }));
      }),
      switchMap(() =>
        this.stammdatenService
          .getLaenderEuEfta$()
          .pipe(
            handleApiResponse((euEftaLaender) =>
              patchState(this, { euEftaLaender }),
            ),
          ),
      ),
    ),
  );

  saveLaender$ = rxMethod<LandEuEfta[]>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          euEftaLaender: cachedPending(state.euEftaLaender),
        }));
      }),
      switchMap((payload) =>
        this.stammdatenService.setLaenderEuEfta$({ landEuEfta: payload }).pipe(
          handleApiResponse((euEftaLaender) => {
            patchState(this, { euEftaLaender });
          }),
        ),
      ),
    ),
  );
}
