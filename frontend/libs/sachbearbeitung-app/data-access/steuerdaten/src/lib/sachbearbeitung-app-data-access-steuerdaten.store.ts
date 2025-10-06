import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  Steuerdaten,
  SteuerdatenService,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';
import { handleUnauthorized, noGlobalErrorsIf } from '@dv/shared/util/http';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

type SteuerdatenState = {
  cachedSteuerdaten: CachedRemoteData<Steuerdaten[]>;
};

const initialState: SteuerdatenState = {
  cachedSteuerdaten: initial(),
};

@Injectable()
export class SteuerdatenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private steuerdatenService = inject(SteuerdatenService);
  private globalNotificationStore = inject(GlobalNotificationStore);

  cachedSteuerdatenListViewSig = computed(() => {
    return fromCachedDataSig(this.cachedSteuerdaten);
  });

  getSteuerdaten$ = rxMethod<{ gesuchTrancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedSteuerdaten: cachedPending(state.cachedSteuerdaten),
        }));
      }),
      switchMap(({ gesuchTrancheId }) =>
        this.steuerdatenService
          .getSteuerdaten$({
            gesuchTrancheId,
          })
          .pipe(
            handleApiResponse((cachedSteuerdaten) =>
              patchState(this, { cachedSteuerdaten }),
            ),
          ),
      ),
    ),
  );

  updateSteuerdaten$ = rxMethod<{
    gesuchTrancheId: string;
    steuerdaten: Steuerdaten[];
    onSuccess?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedSteuerdaten: cachedPending(state.cachedSteuerdaten),
        }));
      }),
      switchMap(({ gesuchTrancheId, steuerdaten, onSuccess }) =>
        this.steuerdatenService
          .updateSteuerdaten$({ gesuchTrancheId, steuerdaten })
          .pipe(
            handleApiResponse(
              (cachedSteuerdaten) => {
                patchState(this, { cachedSteuerdaten });
              },
              { onSuccess },
            ),
          ),
      ),
    ),
  );

  updateSteuerdatenFromNesko$ = rxMethod<{
    gesuchTrancheId: string;
    steuerdatenTyp: SteuerdatenTyp;
    steuerjahr: number;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedSteuerdaten: cachedPending(state.cachedSteuerdaten),
        }));
      }),
      switchMap(({ gesuchTrancheId, steuerjahr, steuerdatenTyp }) =>
        this.steuerdatenService
          .updateSteuerdatenFromNesko$(
            {
              gesuchTrancheId,
              neskoGetSteuerdatenRequest: {
                steuerjahr,
                steuerdatenTyp,
              },
            },
            undefined,
            undefined,
            {
              context: noGlobalErrorsIf(
                true,
                handleUnauthorized((error) => {
                  this.globalNotificationStore.handleHttpRequestFailed([error]);
                }),
              ),
            },
          )
          .pipe(
            handleApiResponse(
              (cachedSteuerdaten) => patchState(this, { cachedSteuerdaten }),
              {
                onFailure: (error) => {
                  const parsedError = sharedUtilFnErrorTransformer(error);

                  if (parsedError.type === 'neskoError') {
                    this.globalNotificationStore.createNotification({
                      type: 'ERROR_PERMANENT',
                      message: parsedError.message,
                      content: parsedError,
                    });
                  } else {
                    this.globalNotificationStore.createNotification({
                      type: 'ERROR',
                    });
                  }
                },
              },
            ),
          ),
      ),
    ),
  );
}
