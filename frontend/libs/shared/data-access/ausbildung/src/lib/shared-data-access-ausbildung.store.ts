import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Ausbildung,
  AusbildungCreateResponse,
  AusbildungService,
  AusbildungUnterbruchAntragSB,
  AusbildungUnterbruchLimits,
  AusbildungUpdate,
  CreateAusbildungUnterbruchAntragGS,
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
  ausbildungUnterbrechenResponse: RemoteData<unknown>;
  ausbildungResponse: RemoteData<AusbildungCreateResponse>;
  ausbildungUnterbruchLimits: CachedRemoteData<AusbildungUnterbruchLimits>;
  ausbildungUnterbruchSb: CachedRemoteData<AusbildungUnterbruchAntragSB>;
};

const initialState: AusbildungState = {
  ausbildung: initial(),
  ausbildungUnterbrechenResponse: initial(),
  ausbildungResponse: initial(),
  ausbildungUnterbruchLimits: initial(),
  ausbildungUnterbruchSb: initial(),
};

@Injectable({ providedIn: 'root' })
export class AusbildungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungService = inject(AusbildungService);

  ausbildungViewSig = computed(() => {
    const ausbildung = fromCachedDataSig(this.ausbildung);

    return {
      ausbildung,
    };
  });

  ausbildungsUnterbruchViewSig = computed(() => {
    const ausbildungUnterbruch = fromCachedDataSig(this.ausbildungUnterbruchSb);

    return { ...ausbildungUnterbruch };
  });

  ausbildungCreateErrorResponseViewSig = computed(() => {
    return fromCachedDataSig(this.ausbildungResponse)?.error;
  });

  resetAusbildungErrors = () => {
    patchState(this, () => ({
      ausbildungResponse: initial(),
    }));
  };

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

  getAusbildungUnterbruchLimits$ = rxMethod<{
    ausbildungId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungUnterbruchLimits: cachedPending(
            state.ausbildungUnterbruchLimits,
          ),
        }));
      }),
      switchMap(({ ausbildungId }) =>
        this.ausbildungService
          .getAusbildungUnterbruchLimits$({
            ausbildungId,
          })
          .pipe(
            handleApiResponse((response) =>
              patchState(this, { ausbildungUnterbruchLimits: response }),
            ),
          ),
      ),
    ),
  );

  createAusbildungUnterbruchAntragGs$ = rxMethod<
    CreateAusbildungUnterbruchAntragGS & {
      ausbildungId: string;
      onSuccess: () => void;
    }
  >(
    pipe(
      tap(() => {
        patchState(this, () => ({
          ausbildungUnterbrechenResponse: pending(),
        }));
      }),
      switchMap(({ onSuccess, ausbildungId, ...payload }) =>
        this.ausbildungService
          .createAusbildungUnterbruchAntragGs$({
            ausbildungId,
            ...payload,
          })
          .pipe(
            handleApiResponse(
              (response) =>
                patchState(this, {
                  ausbildungUnterbrechenResponse: response,
                }),
              { onSuccess },
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
