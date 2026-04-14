import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { SharedModelError } from '@dv/shared/model/error';
import {
  ApplyDemoDataResponse,
  DemoDataList,
  DemoDataService,
  DemoDataSlim,
  DemoDataTestBerechnungResultat,
  ValidationMessage,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedFailure,
  cachedPending,
  handleApiResponse,
  initial,
  mapCachedData,
  pending,
} from '@dv/shared/util/remote-data';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

type DemoDataState = {
  demoData: CachedRemoteData<DemoDataList>;
  lastDemoDataRun: RemoteData<ApplyDemoDataResponse>;
  demoDataTestBerechnungResultats: CachedRemoteData<
    DemoDataTestBerechnungResultat[]
  >;
};

const initialState: DemoDataState = {
  demoData: initial(),
  lastDemoDataRun: initial(),
  demoDataTestBerechnungResultats: initial(),
};

type DemoDataError = SharedModelError & {
  demoErrorType?: 'file-upload' | 'apply-demo-data';
  allErrors?: {
    origin: string | undefined;
    message: string;
    technical?: string;
  }[];
};

@Injectable()
export class DemoDataStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private demoDataService = inject(DemoDataService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  cachedDemoDataListViewSig = computed(() =>
    mapCachedData(this.demoData(), (demoData) =>
      !demoData?.demoDatas
        ? undefined
        : {
            ...demoData,
            demoDatas:
              demoData.demoDatas
                .filter((item) => item.typ === 'TRANCHE')
                .sort(sortByTestfall) ?? [],
          },
    ),
  );

  demoDataTestBerechnungResultatsSig = computed(() => {
    const testResults = this.demoDataTestBerechnungResultats().data ?? [];

    return testResults.reduce(
      (acc, result) => ({ ...acc, [result.demoDataId]: result }),
      {} as Record<string, DemoDataTestBerechnungResultat>,
    );
  });

  lastDemoDataRunViewSig = computed(() => {
    const lastDemoDataRun = this.lastDemoDataRun().data;
    if (!lastDemoDataRun) {
      return null;
    }

    const { valid, ist, soll } = lastDemoDataRun.berechnungResultat;

    return {
      gesuchStatus: lastDemoDataRun.gesuchStatus,
      allValid: Object.values(valid ?? {}).every(Boolean),
      valid,
      soll,
      ist,
    };
  });

  demoDataErrorViewSig = computed<DemoDataError | undefined>(() => {
    const error = sharedUtilFnErrorTransformer(
      this.demoData().error ?? this.lastDemoDataRun().error,
    );
    const prepareError = (error: ValidationMessage) => ({
      origin: error.propertyPath?.split('.').slice(-1).join(''),
      message: error.message,
      technical: error.messageTemplate,
    });

    switch (error.type) {
      case 'demoDataError': {
        return {
          ...error,
          demoErrorType: this.demoData().error
            ? 'file-upload'
            : 'apply-demo-data',
          allErrors: error.validationErrors?.map(prepareError),
        };
      }
      case 'validationError': {
        return {
          ...error,
          allErrors: [
            ...error.validationErrors,
            ...error.validationWarnings,
          ].map(prepareError),
        };
      }
      case 'genericValidationError': {
        return {
          ...error,
          allErrors: error.validationErrors.map((genericError) => ({
            origin: genericError.path,
            message: genericError.message,
          })),
        };
      }
      default:
        return;
    }
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

  applyDemoData$ = rxMethod<string>(
    pipe(
      tap(() => {
        patchState(this, {
          lastDemoDataRun: pending(),
        });
      }),
      switchMap((demoDataId) =>
        this.demoDataService.applyDemoData$({ demoDataId }).pipe(
          handleApiResponse(
            (demoData) => patchState(this, { lastDemoDataRun: demoData }),
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'demo-data-app.overview.apply-demo-data.success',
                });
              },
            },
          ),
        ),
      ),
    ),
  );

  createNewDemoDataImport$ = rxMethod<{
    fileUpload: File;
    ignoreBerechnungErrors: boolean;
    kommentar: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          demoData: cachedPending(state.demoData),
          lastDemoDataRun: initial(),
          demoDataTestBerechnungResultats: initial(),
        }));
      }),
      switchMap(
        ({ fileUpload, ignoreBerechnungErrors, kommentar, onSuccess }) =>
          this.demoDataService
            .createNewDemoDataImport$({
              fileUpload,
              ignoreBerechnungErrors,
              kommentar,
            })
            .pipe(
              handleApiResponse(
                (demoData) =>
                  patchState(this, (state) => ({
                    demoData: cachedFailure(state.demoData, demoData.error),
                  })),
                {
                  onSuccess: () => {
                    onSuccess();
                    this.globalNotificationStore.createSuccessNotification({
                      messageKey: 'demo-data-app.overview.file-upload.success',
                    });
                  },
                },
              ),
            ),
      ),
    ),
  );

  testAllDemoDataBerechnung$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          demoDataTestBerechnungResultats: cachedPending(
            state.demoDataTestBerechnungResultats,
          ),
        }));
      }),
      switchMap(() =>
        this.demoDataService.testAllDemoDataBerechnung$().pipe(
          handleApiResponse(
            (testResult) =>
              patchState(this, () => ({
                demoDataTestBerechnungResultats: testResult,
              })),
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'demo-data-app.overview.file-upload.success',
                });
              },
            },
          ),
        ),
      ),
    ),
  );
}

const TESTFALL_REGEX = /^(ST|TF)-([0-9]+)(\.([0-9]+))?$/;
const sortByTestfall = (a: DemoDataSlim, b: DemoDataSlim) => {
  const matchesA = TESTFALL_REGEX.exec(a.testFall);
  const matchesB = TESTFALL_REGEX.exec(b.testFall);
  if (!matchesA || !matchesB) {
    return a.testFall.localeCompare(b.testFall);
  }

  const comparingPrefix = matchesA[1].localeCompare(matchesB[1]);
  if (comparingPrefix != 0) {
    return comparingPrefix;
  }

  const totalA = parseInt(matchesA[2]) + parseInt(matchesA[4] ?? 0) / 100;
  const totalB = parseInt(matchesB[2]) + parseInt(matchesB[4] ?? 0) / 100;
  return totalA - totalB;
};
