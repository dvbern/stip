import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { tapResponse } from '@ngrx/operators';
import {
  patchState,
  signalStore,
  withComputed,
  withMethods,
  withState,
} from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import {
  Ausbildungsstaette,
  AusbildungsstaetteService,
} from '@dv/shared/model/gesuch';

type AdminAusbildungsstaetteState = {
  ausbildungsstaetten: Ausbildungsstaette[];
  hasLoadedOnce: boolean;
  loading: boolean;
  error?: string;
};

const initialState: AdminAusbildungsstaetteState = {
  ausbildungsstaetten: [],
  hasLoadedOnce: false,
  loading: false,
  error: undefined,
};

export const AdminAusbildungsstaetteStore = signalStore(
  withState(initialState),
  // withMethods(
  //   (store, ausbildungsStaetteService = inject(AusbildungsstaetteService)) => {
  //     async function loadAusbildungsstaetten() {
  //       patchState(store, {
  //         loading: true,
  //         error: undefined,
  //       });
  //       const ausbildungsstaetten = await lastValueFrom(
  //         ausbildungsStaetteService.getAusbildungsstaetten$(),
  //       );
  //       patchState(store, {
  //         ausbildungsstaetten: ausbildungsstaetten,
  //         hasLoadedOnce: true,
  //         loading: false,
  //         error: undefined,
  //       });
  //     }

  //     return {
  //       loadAusbildungsstaetten,
  //     };
  //   },
  // ),
  withMethods(
    (store, ausbildungsStaetteService = inject(AusbildungsstaetteService)) => ({
      loadAusbildungsstaetten: rxMethod(
        pipe(
          tap(() => {
            patchState(store, {
              loading: true,
              error: undefined,
            });
          }),
          switchMap(() =>
            ausbildungsStaetteService.getAusbildungsstaetten$().pipe(
              tapResponse({
                next: (ausbildungsstaetten) =>
                  patchState(store, {
                    ausbildungsstaetten: ausbildungsstaetten,
                    hasLoadedOnce: true,
                  }),
                error: (error: HttpErrorResponse) => {
                  patchState(store, {
                    error: error.message,
                  });
                },
                finalize: () => {
                  patchState(store, {
                    loading: false,
                  });
                },
              }),
            ),
          ),
        ),
      ),
    }),
  ),
  withComputed(({ ausbildungsstaetten }) => ({
    tableData: computed(
      () =>
        new MatTableDataSource<AusbildungsstaetteTableData>(
          ausbildungsstaetten().map((ausbildungsstaette) => ({
            ...ausbildungsstaette,
            ausbildungsgaengeCount:
              ausbildungsstaette.ausbildungsgaenge?.length ?? 0,
          })),
        ),
    ),
    ausbildungsstaetteCount: computed(() => ausbildungsstaetten.length ?? 0),
  })),
);
