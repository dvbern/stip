import { Injectable, computed, inject } from '@angular/core';
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

type LandState = {
  laender: CachedRemoteData<Land[]>;
};

const initialState: LandState = {
  laender: initial(),
};

@Injectable({ providedIn: 'root' })
export class LandStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private landService = inject(LandService);

  landListViewSig = computed(() => {
    return fromCachedDataSig(this.laender);
  });

  autocompleteLandListViewSig = computed(() => {
    return (
      fromCachedDataSig(this.laender)?.map((land) => ({
        ...land,
        alwaysOnTop: land.iso3code === 'CHE',
        testId: land.deKurzform,
        invalid: !land.eintragGueltig,
        displayValueDe: land.deKurzform,
        displayValueFr: land.frKurzform,
      })) ?? []
    );
  });

  landAdminViewSig = computed(() => {
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

  updateLand$ = rxMethod<{ land: Land; landId: string; onSuccess: () => void }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          laender: cachedPending(state.laender),
        }));
      }),
      switchMap(({ onSuccess, ...req }) =>
        this.landService.updateLand$(req).pipe(
          handleApiResponse(
            (res) => {
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
            },
            { onSuccess },
          ),
        ),
      ),
    ),
  );

  createLand$ = rxMethod<{ land: Land; onSuccess: () => void }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          laender: cachedPending(state.laender),
        }));
      }),
      switchMap(({ land, onSuccess }) =>
        this.landService.createLand$({ land }).pipe(
          handleApiResponse(
            (land) => {
              patchState(this, (state) => ({
                laender: mapCachedData(state.laender, (laender) => {
                  if (isSuccess(land)) {
                    return [...laender, land.data].sort(
                      (a, b) =>
                        Number(a.laendercodeBfs) - Number(b.laendercodeBfs),
                    );
                  }
                  return laender;
                }),
              }));
            },
            { onSuccess },
          ),
        ),
      ),
    ),
  );
}
