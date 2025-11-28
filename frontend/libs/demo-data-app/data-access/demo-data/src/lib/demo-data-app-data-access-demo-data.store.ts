import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { DemoDataService, DemoDataSlim } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type DemoDataState = {
  demoData: CachedRemoteData<DemoDataSlim[]>;
};

const initialState: DemoDataState = {
  demoData: initial(),
};

@Injectable()
export class DemoDataStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private demoDataService = inject(DemoDataService);

  cachedDemoDataListViewSig = computed(() => {
    return fromCachedDataSig(this.demoData);
  });

  demoDataViewSig = computed(() => {
    return this.demoData.data();
  });

  loadDemoData$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          demoData: cachedPending(state.demoData),
        }));
      }),
      switchMap(() =>
        this.demoDataService
          .getAllDemoData$()
          .pipe(
            handleApiResponse((demoData) => patchState(this, { demoData })),
          ),
      ),
    ),
  );
}
