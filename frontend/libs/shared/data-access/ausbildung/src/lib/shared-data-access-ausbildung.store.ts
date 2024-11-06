import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { endOfDay, format } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  Ausbildung,
  AusbildungService,
  AusbildungUpdate,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  cachedResult,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  success,
  transformErrorSig,
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
  withDevtools('AusbildungStore'),
) {
  private config = inject(SharedModelCompileTimeConfig);
  private ausbildungService = inject(AusbildungService);

  ausbildungViewSig = computed(() => {
    const ausbildung = fromCachedDataSig(this.ausbildung);
    const minEndDatum = endOfDay(new Date());

    return {
      ausbildung,
      minEndDatum,
      minEndDatumFormatted: format(minEndDatum, 'MM.yyyy'),
      isEditable: true && this.config.isSachbearbeitungApp,
    };
  });

  ausbildungFailureViewSig = computed(() => {
    const ausbildung = transformErrorSig(this.ausbildung());

    return ausbildung;
  });

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
  }>(
    pipe(
      tap(({ ausbildung }) => {
        patchState(this, () => ({
          ausbildung: cachedPending(success(ausbildung as Ausbildung)),
        }));
      }),
      switchMap(({ ausbildung: ausbildungUpdate, onSuccess }) =>
        this.ausbildungService.createAusbildung$({ ausbildungUpdate }).pipe(
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
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildung: cachedPending(state.ausbildung),
        }));
      }),
      switchMap(({ ausbildungId, ausbildungUpdate, onSuccess }) =>
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
              },
            ),
          ),
      ),
    ),
  );
}
