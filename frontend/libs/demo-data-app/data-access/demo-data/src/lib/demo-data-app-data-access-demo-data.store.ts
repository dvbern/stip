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
};

const initialState: DemoDataState = {
  demoData: initial(),
  lastDemoDataRun: initial(),
};

type DemoDataError = SharedModelError & {
  demoErrorType?: 'file-upload' | 'apply-demo-data';
  allErrors?: {
    origin: string | undefined;
    message: string;
    technical?: string;
  }[];
};

const AENDERUNG_TEST_FALL_REGEX = /.*(\.\d[02-9])$/;

@Injectable()
export class DemoDataStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private demoDataService = inject(DemoDataService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  cachedDemoDataListViewSig = computed(() => {
    return mapCachedData(this.demoData(), (demoData) => {
      return !demoData?.demoDatas
        ? undefined
        : {
            ...demoData,
            demoDatas:
              demoData.demoDatas.filter(
                (item) => !AENDERUNG_TEST_FALL_REGEX.test(item.testFall),
              ) ?? [],
          };
    });
  });

  demoDataViewSig = computed(() => {
    return this.demoData.data();
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
}
