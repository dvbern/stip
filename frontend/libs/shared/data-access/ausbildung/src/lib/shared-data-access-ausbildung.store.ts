import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { endOfDay, format } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Ausbildung,
  AusbildungCreateResponse,
  AusbildungService,
  AusbildungUpdate,
} from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isSuccess,
  pending,
  success,
} from '@dv/shared/util/remote-data';

type AusbildungState = {
  ausbildung: CachedRemoteData<Ausbildung>;
  ausbildungResponse: RemoteData<AusbildungCreateResponse>;
};

const initialState: AusbildungState = {
  ausbildung: initial(),
  ausbildungResponse: initial(),
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

  ausbildungCreateErrorResponseViewSig = computed(() => {
    return fromCachedDataSig(this.ausbildungResponse)?.error;
  });

  resetAusbildungErrors = () => {
    patchState(this, () => ({
      ausbildungResponse: initial(),
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
    onSuccess: (response: AusbildungCreateResponse) => void;
  }>(
    pipe(
      tap(({ ausbildung }) => {
        patchState(this, () => ({
          ausbildung: cachedPending(success(ausbildung as Ausbildung)),
          ausbildungResponse: pending(),
        }));
      }),
      switchMap(({ ausbildung: ausbildungUpdate, onSuccess }) =>
        this.ausbildungService.createAusbildung$({ ausbildungUpdate }).pipe(
          handleApiResponse(
            (res) => {
              patchState(this, () => {
                if (!isSuccess(res)) {
                  return {
                    ausbildung: res,
                    ausbildungResponse: res,
                  };
                }
                if (isDefined(res.data.error)) {
                  return {
                    ausbildung: initial(),
                    ausbildungResponse: res,
                  };
                }
                if (isDefined(res.data.ausbildung)) {
                  return {
                    ausbildung: success(res.data.ausbildung),
                    ausbildungResponse: res,
                  };
                }
                return {
                  ausbildung: initial(),
                  ausbildungResponse: res,
                };
              });
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
