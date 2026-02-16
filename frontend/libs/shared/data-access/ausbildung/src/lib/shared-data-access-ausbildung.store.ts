import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { endOfDay, format } from 'date-fns';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Ausbildung,
  AusbildungCreateResponse,
  AusbildungService,
  AusbildungServiceEinreichenAusbildungUnterbruchAntragRequestParams,
  AusbildungUpdate,
  Dokument,
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
  ausbildungUnterbruchDokumente: RemoteData<Dokument[]>;
};

const initialState: AusbildungState = {
  ausbildung: initial(),
  ausbildungUnterbrechenResponse: initial(),
  ausbildungResponse: initial(),
  ausbildungUnterbruchDokumente: initial(),
};

@Injectable({ providedIn: 'root' })
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

  getAusbildungUnterbruchDokumente$ = rxMethod<{
    ausbildungUnterbruchAntragId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          ausbildungUnterbruchDokumente: pending(),
        }));
      }),
      switchMap(({ ausbildungUnterbruchAntragId }) =>
        this.ausbildungService
          .getAusbildungUnterbruchAntragDokuments$({
            ausbildungUnterbruchAntragId,
          })
          .pipe(
            handleApiResponse((response) =>
              patchState(this, { ausbildungUnterbruchDokumente: response }),
            ),
          ),
      ),
    ),
  );

  createAusbildungUnterbruchAntrag$ = rxMethod<{
    ausbildungId: string;
    onSuccess: (id: string) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          ausbildungUnterbrechenResponse: pending(),
        }));
      }),
      switchMap(({ ausbildungId, onSuccess }) =>
        this.ausbildungService
          .createAusbildungUnterbruchAntrag$({ ausbildungId })
          .pipe(
            handleApiResponse(
              (response) =>
                patchState(this, { ausbildungUnterbrechenResponse: response }),
              { onSuccess: (unterbruch) => onSuccess(unterbruch.id) },
            ),
          ),
      ),
    ),
  );

  deleteAusbildungUnterbruchAntrag$ = rxMethod<{
    ausbildungUnterbruchAntragId: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          ausbildungUnterbrechenResponse: pending(),
        }));
      }),
      switchMap(({ ausbildungUnterbruchAntragId, onSuccess }) =>
        this.ausbildungService
          .deleteAusbildungUnterbruchAntrag$({
            ausbildungUnterbruchAntragId,
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

  einreichenAusbildungUnterbruchAntrag$ = rxMethod<
    AusbildungServiceEinreichenAusbildungUnterbruchAntragRequestParams & {
      onSuccess: () => void;
    }
  >(
    pipe(
      tap(() => {
        patchState(this, () => ({
          ausbildungUnterbrechenResponse: pending(),
        }));
      }),
      switchMap(
        ({
          ausbildungUnterbruchAntragId,
          updateAusbildungUnterbruchAntragGS,
          onSuccess,
        }) =>
          this.ausbildungService
            .einreichenAusbildungUnterbruchAntrag$({
              ausbildungUnterbruchAntragId,
              updateAusbildungUnterbruchAntragGS,
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
