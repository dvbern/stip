import { Injectable, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { Store } from '@ngrx/store';
import { map, pipe, switchMap, tap } from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import {
  CreateAenderungsantragRequest,
  CreateGesuchTrancheRequest,
  GesuchTranche,
  GesuchTrancheService,
  GesuchTrancheSlim,
} from '@dv/shared/model/gesuch';
import { PERSON } from '@dv/shared/model/gesuch-form';
import { shouldIgnoreNotFoundErrorsIf } from '@dv/shared/util/http';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type GesuchAenderungState = {
  cachedGesuchAenderung: CachedRemoteData<GesuchTranche>;
  cachedTranchenSlim: CachedRemoteData<GesuchTrancheSlim[]>;
};

const initialState: GesuchAenderungState = {
  cachedGesuchAenderung: initial(),
  cachedTranchenSlim: initial(),
};

@Injectable({ providedIn: 'root' })
export class GesuchAenderungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('GesuchAenderungStore'),
) {
  private store = inject(Store);
  private configSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );
  private gesuchTrancheService = inject(GesuchTrancheService);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private router = inject(Router);

  aenderungenViewSig = computed(() => {
    const tranchen = this.cachedTranchenSlim();
    return {
      loading: isPending(tranchen),
      list: tranchen.data?.filter((t) => t.typ === 'AENDERUNG') ?? [],
    };
  });

  openAenderungViewSig = computed(() => {
    const tranchen = this.cachedTranchenSlim();
    const { isSachbearbeitungApp } = this.configSig();
    return {
      loading: isPending(tranchen),
      openAenderung: tranchen.data?.find(
        (t) =>
          t.typ === 'AENDERUNG' &&
          (!isSachbearbeitungApp || t.status !== 'IN_BEARBEITUNG_GS'),
      ),
    };
  });

  tranchenViewSig = computed(() => {
    const tranchen = this.cachedTranchenSlim();
    return {
      loading: isPending(tranchen),
      list: tranchen.data?.filter((t) => t.typ === 'TRANCHE') ?? [],
    };
  });

  resetCachedGesuchAenderung() {
    patchState(this, { cachedGesuchAenderung: initial() });
  }

  getAllTranchenForGesuch$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedTranchenSlim: cachedPending(state.cachedTranchenSlim),
        }));
      }),
      switchMap((req) =>
        this.gesuchTrancheService
          .getAllTranchenForGesuch$(req, undefined, undefined, {
            context: shouldIgnoreNotFoundErrorsIf(true),
          })
          .pipe(map((tranchen) => tranchen ?? [])),
      ),
      handleApiResponse((tranchen) => {
        patchState(this, () => ({
          cachedTranchenSlim: tranchen,
        }));
      }),
    ),
  );

  createGesuchAenderung$ = rxMethod<{
    gesuchId: string;
    createAenderungsantragRequest: CreateAenderungsantragRequest;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          // change to not cached?
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(({ gesuchId, createAenderungsantragRequest }) =>
        this.gesuchTrancheService
          .createAenderungsantrag$({
            gesuchId,
            createAenderungsantragRequest,
          })
          .pipe(
            handleApiResponse(
              (gesuchAenderung) => {
                patchState(this, () => ({
                  cachedGesuchAenderung: gesuchAenderung,
                }));
              },
              {
                onSuccess: (data) => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.dialog.gesuch-aenderung.success',
                  });
                  this.router.navigate([
                    'gesuch',
                    PERSON.route,
                    gesuchId,
                    'aenderung',
                    data.id,
                  ]);
                },
              },
            ),
          ),
      ),
    ),
  );

  createGesuchTrancheCopy$ = rxMethod<{
    gesuchId: string;
    createGesuchTrancheRequest?: CreateGesuchTrancheRequest;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedGesuchAenderung: cachedPending(state.cachedGesuchAenderung),
        }));
      }),
      switchMap(({ gesuchId, createGesuchTrancheRequest }) =>
        this.gesuchTrancheService
          .createGesuchTrancheCopy$({
            gesuchId,
            createGesuchTrancheRequest,
          })
          .pipe(
            handleApiResponse(
              (gesuchAenderung) => {
                patchState(this, () => ({
                  cachedGesuchAenderung: gesuchAenderung,
                }));
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.dialog.gesuch.tranche.success',
                  });
                  this.getAllTranchenForGesuch$({ gesuchId });
                },
              },
            ),
          ),
      ),
    ),
  );
}
