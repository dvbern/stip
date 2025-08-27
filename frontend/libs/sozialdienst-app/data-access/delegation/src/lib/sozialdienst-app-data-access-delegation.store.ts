import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { PermissionStore } from '@dv/shared/global/permission';
import {
  DelegierenService,
  DelegierenServiceDelegierterMitarbeiterAendernRequestParams,
  DelegierenServiceGetDelegierungsOfSozialdienstAdminRequestParams,
  DelegierenServiceGetDelegierungsOfSozialdienstMaRequestParams,
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
import { LoadPaginatedDashboardByRoles } from '@dv/sozialdienst-app/model/delegation';

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
) {
  private delegierenService = inject(DelegierenService);
  private sozialdienstService = inject(SozialdienstService);
  private permissionStore = inject(PermissionStore);

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

  loadPaginatedSozDashboard$ = rxMethod<LoadPaginatedDashboardByRoles>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          paginatedSozDashboard: cachedPending(state.paginatedSozDashboard),
        }));
      }),
      switchMap((params) => {
        return this.loadPaginatedDashboardByRoles$(params).pipe(
          handleApiResponse((paginatedSozDashboard) =>
            patchState(this, {
              paginatedSozDashboard: paginatedSozDashboard,
            }),
          ),
        );
      }),
    ),
  );

  private loadPaginatedDashboardByRoles$(req: LoadPaginatedDashboardByRoles) {
    const roles = this.permissionStore.rolesMapSig();

    if (roles['V0_Sozialdienst-Admin']) {
      return this.delegierenService.getDelegierungsOfSozialdienstAdmin$(
        req as DelegierenServiceGetDelegierungsOfSozialdienstAdminRequestParams,
      );
    } else {
      return this.delegierenService.getDelegierungsOfSozialdienstMa$(
        req as DelegierenServiceGetDelegierungsOfSozialdienstMaRequestParams,
      );
    }
  }

  resetDelegierenState() {
    patchState(this, {
      delegierenState: initial(),
    });
  }
}
