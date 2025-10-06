import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { AdminDokumente, GesuchService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type InfosAdminState = {
  adminDokumente: CachedRemoteData<AdminDokumente>;
};

const initialState: InfosAdminState = {
  adminDokumente: initial(),
};

@Injectable()
export class InfosGesuchsdokumenteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);

  adminDokumenteViewSig = computed(() => {
    return fromCachedDataSig(this.adminDokumente);
  });

  loadAdminDokumente$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          adminDokumente: cachedPending(state.adminDokumente),
        }));
      }),
      switchMap(({ gesuchId }) =>
        this.gesuchService
          .getAdminDokumente$({
            gesuchId,
          })
          .pipe(
            handleApiResponse((adminDokumente) =>
              patchState(this, { adminDokumente }),
            ),
          ),
      ),
    ),
  );
}
