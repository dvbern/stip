import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { Land, LandService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isSuccess,
  mapCachedData,
} from '@dv/shared/util/remote-data';

type EuEftaLaenderState = {
  laender: CachedRemoteData<Land[]>;
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
        this.landService
          .getLaender$()
          .pipe(handleApiResponse((laender) => patchState(this, { laender }))),
      ),
    ),
  );

  updateLand$ = rxMethod<{ land: Land; landId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          laender: cachedPending(state.laender),
        }));
      }),
      switchMap((req) =>
        this.landService.updateLand$(req).pipe(
          handleApiResponse((res) => {
            patchState(this, (state) => ({
              laender: mapCachedData(state.laender, (laender) => {
                if (isSuccess(res)) {
                  return laender.map((land) =>
                    land.id === req.landId ? { ...land, ...res.data } : land,
                  );
                } else {
                  return laender;
                }
              }),
            }));
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
            patchState(this, (state) => ({
              laender: mapCachedData(state.laender, (laender) => {
                // todo: check sorting from backend, if there is any?
                if (isSuccess(land)) {
                  return [...laender, land.data];
                }
                return laender;
              }),
            }));
          }),
        ),
      ),
    ),
  );
}
