import { Injectable, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { ElternService, ElternTyp } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type VersteckteElternState = {
  versteckteEltern: CachedRemoteData<ElternTyp[]>;
};

const initialState: VersteckteElternState = {
  versteckteEltern: initial(),
};

@Injectable()
export class VersteckteElternStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private elternService = inject(ElternService);

  saveVersteckteEltern$ = rxMethod<{
    gesuchTrancheId: string;
    versteckteEltern: ElternTyp[];
    onSuccess?: (versteckteEltern: ElternTyp[]) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          versteckteEltern: cachedPending(state.versteckteEltern),
        }));
      }),
      switchMap(({ gesuchTrancheId, versteckteEltern, onSuccess }) =>
        this.elternService
          .setVersteckteEltern$({
            gesuchTrancheId,
            elternTyp: versteckteEltern,
          })
          .pipe(
            handleApiResponse(
              (versteckteEltern) => {
                patchState(this, { versteckteEltern });
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
