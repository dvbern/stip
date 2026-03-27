import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DokumentService,
  SachbearbeiterGesuchDokument,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  pending,
} from '@dv/shared/util/remote-data';

type SachbearbeiterDokumentsState = {
  sachbearbeiterDokuments: CachedRemoteData<SachbearbeiterGesuchDokument[]>;
  lastAction: RemoteData<unknown>;
};

const initialState: SachbearbeiterDokumentsState = {
  sachbearbeiterDokuments: initial(),
  lastAction: initial(),
};

@Injectable()
export class SachbearbeiterDokumentsStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private sachbearbeiterDokumentsService = inject(DokumentService);

  sachbearbeiterDokumentsViewSig = computed(() => {
    return fromCachedDataSig(this.sachbearbeiterDokuments);
  });

  loadSachbearbeiterDokuments$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sachbearbeiterDokuments: cachedPending(state.sachbearbeiterDokuments),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.sachbearbeiterDokumentsService
          .getAllSachbearbeiterGesuchDokumentsOfGesuch$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((sachbearbeiterDokuments) =>
              patchState(this, { sachbearbeiterDokuments }),
            ),
          ),
      ),
    ),
  );

  createSachbearbeiterDokument$ = rxMethod<{
    gesuchId: string;
    type: string;
    description: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sachbearbeiterDokuments: cachedPending(state.sachbearbeiterDokuments),
          lastAction: pending(),
        }));
      }),
      switchMap(({ gesuchId, type, description, onSuccess }) =>
        this.sachbearbeiterDokumentsService
          .createSachbearbeiterGesuchDokument$({
            gesuchId,
            sachbearbeiterGesuchDokumentCreate: {
              type,
              description,
            },
          })
          .pipe(
            handleApiResponse(
              (sachbearbeiterDokument) =>
                patchState(this, { lastAction: sachbearbeiterDokument }),
              {
                onSuccess,
              },
            ),
          ),
      ),
    ),
  );

  deleteSachbearbeiterDokument$ = rxMethod<{
    id: string;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sachbearbeiterDokuments: cachedPending(state.sachbearbeiterDokuments),
          lastAction: pending(),
        }));
      }),
      switchMap(({ id, onSuccess }) =>
        this.sachbearbeiterDokumentsService
          .deleteSachbearbeiterGesuchDokument$({
            sachbearbeiterGesuchDokumentId: id,
          })
          .pipe(
            handleApiResponse(
              (sachbearbeiterDokument) =>
                patchState(this, { lastAction: sachbearbeiterDokument }),
              {
                onSuccess,
              },
            ),
          ),
      ),
    ),
  );
}
