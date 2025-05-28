import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Land,
  LandEuEfta,
  LandService,
  StammdatenService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type EuEftaLaenderState = {
  laender: CachedRemoteData<LandEuEfta[]>;
};

const initialState: EuEftaLaenderState = {
  laender: initial(),
};

@Injectable()
export class EuEftaLaenderStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('EuEftaLaenderStore'),
) {
  private landService = inject(LandService);

  euEftaLaenderListViewSig = computed(() => {
    return fromCachedDataSig(this.laender);
  });

  loadLaender$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          laender: cachedPending(state.laender),
        }));
      }),
      switchMap(() =>
        this.landService.getLaender$().pipe(
          tap((x) => console.log('Request', x)), //todo: remove
          handleApiResponse((laender) => patchState(this, { laender })),
        ),
      ),
    ),
  );

  updateLand$ = rxMethod<{ landEuEfta: LandEuEfta[] }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          laender: cachedPending(state.laender),
        }));
      }),
      switchMap((req) =>
        this.landService.updateLand$(req).pipe(
          handleApiResponse((land) => {
            // find the updated land via id in the current state an upodate it
            patchState(this, (state) => {
              const updatedLaender = state.laender.data.map((l) =>
                l.id === land.id ? land : l,
              );
              return { laender: { ...state.laender, data: updatedLaender } };
            });
          }),
        ),
      ),
    ),
  );

  createLand$ = rxMethod<{ land: Land }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          laender: cachedPending(state.laender),
        }));
      }),
      switchMap((land) =>
        this.landService.createLand$(land).pipe(
          handleApiResponse((land) => {
            patchState(this, { laender: euEftaLaender });
          }),
        ),
      ),
    ),
  );
}
