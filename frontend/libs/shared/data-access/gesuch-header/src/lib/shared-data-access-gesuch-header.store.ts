import { Injectable, Signal, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  GesuchHeader,
  GesuchService,
  GesuchTrancheSlim,
} from '@dv/shared/model/gesuch';
import { byBusinessAppType } from '@dv/shared/model/permission-state';
import { getRelativeTrancheRoute } from '@dv/shared/model/router';
import { assertUnreachable } from '@dv/shared/model/type-util';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isPendingWithoutCache,
} from '@dv/shared/util/remote-data';

type GesuchHeaderStoreState = {
  header: CachedRemoteData<GesuchHeader>;
};

const initialState: GesuchHeaderStoreState = {
  header: initial(),
};

@Injectable({ providedIn: 'root' })
export class GesuchHeaderStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private gesuchService = inject(GesuchService);
  private router = inject(Router);
  private config = inject(SharedModelCompileTimeConfig);

  viewSig = computed(() => {
    const headerData = this.header().data;
    return {
      ...headerData,
      canGetBerechnung: byBusinessAppType(this.config.appType, {
        'gesuch-app': () => headerData?.gesuchInfo.state.canGSGetBerechnung,
        'sachbearbeitung-app': () =>
          headerData?.gesuchInfo.state.canSBGetBerechnung,
      }),
      // The initial tranchen are also returned as version, but they are already handled with header.initial
      // so we can skip the last element as it is always the initial tranchen
      versions: headerData?.versions?.slice(0, -1),
      isLoading: isPendingWithoutCache(this.header()),
    };
  });

  getRelativeTranchenViewSig = prepareTranchenListSig(
    this.viewSig,
    this.router,
  );

  loadHeader$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          header: cachedPending(state.header),
        }));
      }),
      exhaustMap(({ gesuchId }) => {
        switch (this.config.appType) {
          case 'gesuch-app': {
            return this.gesuchService
              .getGesuchHeaderGs$({ gesuchId })
              .pipe(
                handleApiResponse((header) => patchState(this, { header })),
              );
          }
          case 'sachbearbeitung-app': {
            return this.gesuchService
              .getGesuchHeaderSb$({ gesuchId })
              .pipe(
                handleApiResponse((header) => patchState(this, { header })),
              );
          }
          case 'demo-data-app': {
            throw new Error('App-Type not handled');
          }
          default:
            assertUnreachable(this.config.appType);
        }
      }),
    ),
  );
}

const prepareTranchenListSig =
  (
    viewSig: Signal<{ currentTranches?: GesuchTrancheSlim[] }>,
    router: Router,
  ) =>
  (gesuchIdSig: Signal<string | undefined>) => {
    const relativeRouteSig = getRelativeTrancheRoute(router, 'TRANCHE');

    // todo-review: @scph relative relativeRouteSig is always null! remove?
    // console.log('relativeRouteSig', relativeRouteSig());

    return computed(() => {
      const gesuchId = gesuchIdSig();
      const relativeRoute = relativeRouteSig();
      const tranchen = viewSig().currentTranches ?? [];

      return tranchen.map((tranche) => ({
        ...tranche,
        url: relativeRoute
          ? router.createUrlTree([...relativeRoute, tranche.id])
          : ['/', 'gesuch', gesuchId, 'tranche', tranche.id],
      }));
    });
  };
