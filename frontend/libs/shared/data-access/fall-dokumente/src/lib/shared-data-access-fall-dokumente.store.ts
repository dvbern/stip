import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DarlehenBuchhaltungOverview,
  DarlehenService,
  DarlehenServiceGetDarlehenBuchhaltungEntrysByFallIdRequestParams,
  VerfuegungFall,
  VerfuegungService,
  VerfuegungServiceGetVerfuegungenByFallIdRequestParams,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type FallDokumenteState = {
  verfuegungen: CachedRemoteData<VerfuegungFall[]>;
  darlehenBuchhaltung: CachedRemoteData<DarlehenBuchhaltungOverview>;
};

const initialState: FallDokumenteState = {
  verfuegungen: initial(),
  darlehenBuchhaltung: initial(),
};

@Injectable()
export class FallDokumenteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private verfuegungService = inject(VerfuegungService);
  private darlehenService = inject(DarlehenService);

  verfuegungenViewSig = computed(() => {
    return {
      verfuegungen: fromCachedDataSig(this.verfuegungen),
      loading: isPending(this.verfuegungen()),
    };
  });

  darlehenBuchhaltungViewSig = computed(() => {
    return {
      darlehenBuchhaltung: fromCachedDataSig(this.darlehenBuchhaltung),
      loading: isPending(this.darlehenBuchhaltung()),
    };
  });

  loadVerfuegungDokumente$ =
    rxMethod<VerfuegungServiceGetVerfuegungenByFallIdRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            verfuegungen: cachedPending(state.verfuegungen),
          }));
        }),
        switchMap((req) =>
          this.verfuegungService
            .getVerfuegungenByFallId$(req)
            .pipe(
              handleApiResponse((verfuegungen) =>
                patchState(this, { verfuegungen }),
              ),
            ),
        ),
      ),
    );

  loadDarlehenBuchhaltungEntrys$ =
    rxMethod<DarlehenServiceGetDarlehenBuchhaltungEntrysByFallIdRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            darlehenBuchhaltung: cachedPending(state.darlehenBuchhaltung),
          }));
        }),
        switchMap((req) =>
          this.darlehenService
            .getDarlehenBuchhaltungEntrysByFallId$(req)
            .pipe(
              handleApiResponse((darlehenBuchhaltung) =>
                patchState(this, { darlehenBuchhaltung }),
              ),
            ),
        ),
      ),
    );
}
