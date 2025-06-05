import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { endOfDay, format } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Ausbildung,
  AusbildungService,
  AusbildungUpdate,
} from '@dv/shared/model/gesuch';
import {
  noGlobalErrorsIf,
  shouldIgnoreBadRequestErrorsIf,
} from '@dv/shared/util/http';
import {
  CachedRemoteData,
  cachedPending,
  cachedResult,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  success,
} from '@dv/shared/util/remote-data';

type AusbildungState = {
  ausbildung: CachedRemoteData<Ausbildung>;
};

const initialState: AusbildungState = {
  ausbildung: initial(),
};

@Injectable()
export class AusbildungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungService = inject(AusbildungService);

  ausbildungViewSig = computed(() => {
    const ausbildung = fromCachedDataSig(this.ausbildung);
    const minEndDatum = endOfDay(new Date());

    return {
      ausbildung,
      minEndDatum,
      minEndDatumFormatted: format(minEndDatum, 'MM.yyyy'),
    };
  });

  resetAusbildungErrors = () => {
    patchState(this, (state) => ({
      ausbildung: { ...state.ausbildung, error: undefined },
    }));
  };

  loadAusbildung$ = rxMethod<{ ausbildungId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildung: cachedPending(state.ausbildung),
        }));
      }),
      switchMap(({ ausbildungId }) =>
        this.ausbildungService
          .getAusbildung$({ ausbildungId })
          .pipe(
            handleApiResponse((ausbildung) => patchState(this, { ausbildung })),
          ),
      ),
    ),
  );

  createAusbildung$ = rxMethod<{
    ausbildung: AusbildungUpdate;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      tap(({ ausbildung }) => {
        patchState(this, () => ({
          ausbildung: cachedPending(success(ausbildung as Ausbildung)),
        }));
      }),
      switchMap(({ ausbildung: ausbildungUpdate, onSuccess, onFailure }) =>
        this.ausbildungService
          .createAusbildung$({ ausbildungUpdate }, undefined, undefined, {
            context: shouldIgnoreBadRequestErrorsIf(
              true,
              noGlobalErrorsIf(true),
            ),
          })
          .pipe(
            handleApiResponse(
              (ausbildung) => {
                patchState(this, () => ({
                  ausbildung: cachedResult(
                    success(ausbildungUpdate as Ausbildung),
                    ausbildung,
                  ),
                }));
              },
              {
                onSuccess,
                onFailure,
              },
            ),
          ),
      ),
    ),
  );

  saveAusbildung$ = rxMethod<{
    ausbildungId: string;
    ausbildungUpdate: AusbildungUpdate;
    onSuccess: () => void;
    onFailure: (error: unknown) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildung: cachedPending(state.ausbildung),
        }));
      }),
      switchMap(({ ausbildungId, ausbildungUpdate, onSuccess, onFailure }) =>
        this.ausbildungService
          .updateAusbildung$({
            ausbildungId,
            ausbildungUpdate,
          })
          .pipe(
            handleApiResponse(
              (ausbildung) => {
                patchState(this, { ausbildung });
              },
              {
                onSuccess,
                onFailure,
              },
            ),
          ),
      ),
    ),
  );
}
