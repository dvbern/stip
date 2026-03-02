import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  AusbildungService,
  AusbildungServiceUpdateAusbildungUnterbruchAntragSBRequestParams,
  AusbildungUnterbruchAntragSB,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type AusbildungAdminState = {
  ausbildungUnterbrueche: CachedRemoteData<AusbildungUnterbruchAntragSB[]>;
  lastUpdate: RemoteData<unknown>;
};

const initialState: AusbildungAdminState = {
  ausbildungUnterbrueche: initial(),
  lastUpdate: initial(),
};

@Injectable()
export class AusbildungAdminStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungService = inject(AusbildungService);

  ausbildungUnterbruchListViewSig = computed(() => {
    const unterbrueche = this.ausbildungUnterbrueche().data;
    return unterbrueche ?? [];
  });

  loadAusbildungUnterbrueche$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungUnterbrueche: cachedPending(state.ausbildungUnterbrueche),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.ausbildungService
          .getAusbildungUnterbruchAntragsByGesuchId$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((ausbildungUnterbrueche) =>
              patchState(this, { ausbildungUnterbrueche }),
            ),
          ),
      ),
    ),
  );

  updateAusbildungUnterbruch$ = rxMethod<{
    data: AusbildungServiceUpdateAusbildungUnterbruchAntragSBRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungUnterbrueche: cachedPending(state.ausbildungUnterbrueche),
        }));
      }),
      switchMap(({ data, onSuccess }) =>
        this.ausbildungService
          .updateAusbildungUnterbruchAntragSB$(data)
          .pipe(
            handleApiResponse(
              (lastUpdate) => patchState(this, { lastUpdate }),
              { onSuccess },
            ),
          ),
      ),
    ),
  );
}
