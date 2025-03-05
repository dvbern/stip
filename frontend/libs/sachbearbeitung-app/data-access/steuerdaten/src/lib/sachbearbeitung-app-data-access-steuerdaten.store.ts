import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Steuerdaten,
  SteuerdatenService,
  SteuerdatenTyp,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

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
  withDevtools('SteuerdatenStore'),
) {
  private steuerdatenService = inject(SteuerdatenService);

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
    token: string;
  }>(
    pipe(
      switchMap(({ gesuchTrancheId, steuerdatenTyp, token }) =>
        this.steuerdatenService
          .updateSteuerdatenFromNesko$({
            gesuchTrancheId,
            steuerdatenTyp,
            neskoToken: { token },
          })
          .pipe(
            handleApiResponse((cachedSteuerdaten) =>
              patchState(this, { cachedSteuerdaten }),
            ),
          ),
      ),
    ),
  );
}
