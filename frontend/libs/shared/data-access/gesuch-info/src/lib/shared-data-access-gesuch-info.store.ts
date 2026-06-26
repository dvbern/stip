import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap, throwError } from 'rxjs';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { GesuchInfo, GesuchService } from '@dv/shared/model/gesuch';
import { byAppType } from '@dv/shared/model/permission-state';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type GesuchState = {
  gesuchInfo: CachedRemoteData<GesuchInfo>;
};

const initialState: GesuchState = {
  gesuchInfo: initial(),
};

@Injectable()
export class GesuchInfoStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private config = inject(SharedModelCompileTimeConfig);
  private gesuchService = inject(GesuchService);

  infoViewSig = computed(() => ({
    gesuchInfo: fromCachedDataSig(this.gesuchInfo),
    loading: isPending(this.gesuchInfo()),
  }));

  loadGesuchInfo$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          gesuchInfo: cachedPending(state.gesuchInfo),
        }));
      }),
      exhaustMap(({ gesuchId }) =>
        byAppType(this.config.appType, {
          'gesuch-app': () => this.gesuchService.getGesuchInfoGs$({ gesuchId }),
          'sachbearbeitung-app': () =>
            this.gesuchService.getGesuchInfoSb$({ gesuchId }),
          'demo-data-app': () =>
            throwError(() => new Error('Not implemented for this AppType')),
        }).pipe(
          handleApiResponse((gesuchInfo) => patchState(this, { gesuchInfo })),
        ),
      ),
    ),
  );
}
