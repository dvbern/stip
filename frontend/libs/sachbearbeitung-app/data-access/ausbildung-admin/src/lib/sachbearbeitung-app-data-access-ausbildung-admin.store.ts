import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  AusbildungService,
  AusbildungServiceUpdateAusbildungUnterbruchAntragSBRequestParams,
  AusbildungUnterbruchDashboardSB,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type AusbildungAdminState = {
  ausbildungUnterbruchDaschboardSb: CachedRemoteData<AusbildungUnterbruchDashboardSB>;
  lastUpdate: RemoteData<unknown>;
};

const initialState: AusbildungAdminState = {
  ausbildungUnterbruchDaschboardSb: initial(),
  lastUpdate: initial(),
};

@Injectable()
export class AusbildungAdminStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungService = inject(AusbildungService);

  ausbildungUnterbruchListViewSig = computed(() => {
    const unterbrueche = this.ausbildungUnterbruchDaschboardSb().data;
    return unterbrueche?.ausbildungUnterbruchs ?? [];
  });

  ausbildungUnterbruchDaschboardSb$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungUnterbruchDaschboardSb: cachedPending(
            state.ausbildungUnterbruchDaschboardSb,
          ),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.ausbildungService
          .getAusbildungUnterbruchAntragsByGesuchId$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((ausbildungUnterbruchDaschboardSb) =>
              patchState(this, { ausbildungUnterbruchDaschboardSb }),
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
        patchState(this, {
          lastUpdate: pending(),
        });
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
