import { Injectable, Signal, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import {
  GesuchHeaderGs,
  GesuchHeaderSb,
  GesuchService,
  GesuchTrancheSlim,
} from '@dv/shared/model/gesuch';
import { getRelativeTrancheRoute } from '@dv/shared/model/router';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isPendingWithoutCache,
} from '@dv/shared/util/remote-data';

type GesuchHeaderStoreState = {
  headerSb: CachedRemoteData<GesuchHeaderSb>;
  headerGs: CachedRemoteData<GesuchHeaderGs>;
};

const initialState: GesuchHeaderStoreState = {
  headerSb: initial(),
  headerGs: initial(),
};

@Injectable({ providedIn: 'root' })
export class GesuchHeaderStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);
  private router = inject(Router);

  viewGsSig = computed(() => {
    return {
      ...this.headerGs().data,
      isLoading: isPendingWithoutCache(this.headerGs()),
    };
  });

  viewSbSig = computed(() => {
    return {
      ...this.headerSb().data,
      isLoading: isPendingWithoutCache(this.headerSb()),
    };
  });

  getRelativeTranchenViewGsSig = prepareTranchenListSig(
    this.viewGsSig,
    this.router,
  );

  getRelativeTranchenViewSbSig = prepareTranchenListSig(
    this.viewSbSig,
    this.router,
  );

  loadHeaderGs$ = rxMethod<{ gesuchTrancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          headerGs: cachedPending(state.headerGs),
        }));
      }),
      exhaustMap(({ gesuchTrancheId }) =>
        this.gesuchService
          .getGesuchHeaderGs$({ gesuchTrancheId })
          .pipe(
            handleApiResponse((headerGs) => patchState(this, { headerGs })),
          ),
      ),
    ),
  );

  loadHeaderSb$ = rxMethod<{ gesuchTrancheId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          headerSb: cachedPending(state.headerSb),
        }));
      }),
      exhaustMap(({ gesuchTrancheId }) =>
        this.gesuchService
          .getGesuchHeaderSb$({ gesuchTrancheId })
          .pipe(
            handleApiResponse((headerSb) => patchState(this, { headerSb })),
          ),
      ),
    ),
  );
}

const prepareTranchenListSig =
  (
    viewSig: Signal<{ currentTranchen?: GesuchTrancheSlim[] }>,
    router: Router,
  ) =>
  (gesuchIdSig: Signal<string | undefined>) => {
    const relativeRouteSig = getRelativeTrancheRoute(router, 'TRANCHE');

    return computed(() => {
      const gesuchId = gesuchIdSig();
      const relativeRoute = relativeRouteSig();
      const tranchen = viewSig().currentTranchen ?? [];

      return tranchen.map((tranche) => ({
        ...tranche,
        url: relativeRoute
          ? router.createUrlTree([...relativeRoute, tranche.id])
          : ['/', 'gesuch', gesuchId, 'tranche', tranche.id],
      }));
    });
  };
