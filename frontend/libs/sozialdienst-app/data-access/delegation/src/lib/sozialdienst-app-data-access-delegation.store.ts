import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DelegierenService,
  DelegierenServiceDelegierterMitarbeiterAendernRequestParams,
  DelegierenServiceGetDelegierungSozRequestParams,
  PaginatedSozDashboard,
  SozialdienstBenutzer,
  SozialdienstService,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type DelegationState = {
  paginatedSozDashboard: CachedRemoteData<PaginatedSozDashboard>;
  sozialdienstBenutzerList: CachedRemoteData<SozialdienstBenutzer[]>;
  delegierenState: CachedRemoteData<void>;
};

const initialState: DelegationState = {
  paginatedSozDashboard: initial(),
  sozialdienstBenutzerList: initial(),
  delegierenState: initial(),
};

@Injectable({
  providedIn: 'root',
})
export class DelegationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('DelegationStore'),
) {
  private delegierenService = inject(DelegierenService);
  private sozialdienstService = inject(SozialdienstService);

  cockpitViewSig = computed(() => {
    return {
      paginatedSozDashboard: fromCachedDataSig(this.paginatedSozDashboard),
      loading: isPending(this.paginatedSozDashboard()),
    };
  });

  sozialdienstMitrabeiterListSig = computed(() => {
    return {
      list: fromCachedDataSig(this.sozialdienstBenutzerList),
      loading: isPending(this.sozialdienstBenutzerList()),
    };
  });

  loadSozialdienstBenutzerList$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          sozialdienstBenutzerList: cachedPending(
            state.sozialdienstBenutzerList,
          ),
        }));
      }),
      switchMap(() =>
        this.sozialdienstService.getSozialdienstBenutzerList$().pipe(
          handleApiResponse((benutzer) => {
            patchState(this, { sozialdienstBenutzerList: benutzer });
          }),
        ),
      ),
    ),
  );

  delegierterMitarbeiterAendern$ =
    rxMethod<DelegierenServiceDelegierterMitarbeiterAendernRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            delegierenState: cachedPending(state.delegierenState),
          }));
        }),
        switchMap((params) =>
          this.delegierenService.delegierterMitarbeiterAendern$(params).pipe(
            handleApiResponse((response) =>
              patchState(this, {
                delegierenState: response,
              }),
            ),
          ),
        ),
      ),
    );

  loadPaginatedSozDashboard$ =
    rxMethod<DelegierenServiceGetDelegierungSozRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            paginatedSozDashboard: cachedPending(state.paginatedSozDashboard),
          }));
        }),
        switchMap((params) =>
          this.delegierenService.getDelegierungSoz$(params).pipe(
            handleApiResponse((paginatedSozDashboard) =>
              patchState(this, {
                paginatedSozDashboard: paginatedSozDashboard,
              }),
            ),
          ),
        ),
      ),
    );
}
