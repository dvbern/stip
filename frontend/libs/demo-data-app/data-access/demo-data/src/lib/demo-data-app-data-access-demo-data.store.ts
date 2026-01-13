import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { DemoDataList, DemoDataService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

type DemoDataState = {
  demoData: CachedRemoteData<DemoDataList>;
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

  demoDataImportErrorViewSig = computed(() => {
    const error = sharedUtilFnErrorTransformer(this.demoData().error);

    if (error.type !== 'demoDataError') {
      return;
    }

    return error;
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

  createNewDemoDataImport$ = rxMethod<{
    fileUpload: File;
    kommentar: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          demoData: cachedPending(state.demoData),
        }));
      }),
      switchMap(({ fileUpload, kommentar, onSuccess }) =>
        this.demoDataService
          .createNewDemoDataImport$({ fileUpload, kommentar })
          .pipe(
            handleApiResponse((demoData) => patchState(this, { demoData }), {
              onSuccess,
            }),
          ),
      ),
    ),
  );
}
