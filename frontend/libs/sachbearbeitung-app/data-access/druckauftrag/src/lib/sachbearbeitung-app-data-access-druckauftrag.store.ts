import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  DruckenService,
  DruckenServiceGetAllDruckauftraegeRequestParams,
  PaginatedDruckauftraege,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

export interface DruckEntry {
  versendet: boolean;
  gesuch: string;
  nachname: string;
  vorname: string;
  adressat: string;
}

type DruckauftragState = {
  cachedPaginatedDruckauftraege: CachedRemoteData<PaginatedDruckauftraege>;
  druckEntry: RemoteData<unknown>;
};

const initialState: DruckauftragState = {
  cachedPaginatedDruckauftraege: initial(),
  druckEntry: initial(),
};

@Injectable()
export class DruckauftragStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private druckService = inject(DruckenService);

  cachedDruckauftragListViewSig = computed(() => {
    return {
      druckauftraege: fromCachedDataSig(this.cachedPaginatedDruckauftraege),
      loading: isPending(this.cachedPaginatedDruckauftraege()),
    };
  });

  druckEntryViewSig = computed(() => {
    return {
      druckEntry: this.druckEntry(),
      loading: isPending(this.druckEntry()),
    };
  });

  getAllDruckauftraege$ =
    rxMethod<DruckenServiceGetAllDruckauftraegeRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            cachedPaginatedDruckauftraege: cachedPending(
              state.cachedPaginatedDruckauftraege,
            ),
          }));
        }),
        switchMap((req) =>
          this.druckService.getAllDruckauftraege$(req).pipe(
            handleApiResponse((data) =>
              patchState(this, {
                cachedPaginatedDruckauftraege: data,
              }),
            ),
          ),
        ),
      ),
    );
}
