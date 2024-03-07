import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
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
import { map, pipe, switchMap, tap } from 'rxjs';

import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import {
  Ausbildungsgang,
  Ausbildungsstaette,
  AusbildungsstaetteService,
} from '@dv/shared/model/gesuch';

type AdminAusbildungsstaetteState = {
  ausbildungsstaetten: Ausbildungsstaette[];
  tableData: MatTableDataSource<AusbildungsstaetteTableData>;
  hasLoadedOnce: boolean;
  loading: boolean;
  error?: string;
};

const tableData = new MatTableDataSource<AusbildungsstaetteTableData>();

tableData.filterPredicate = (data, filter) => {
  const f = filter.trim().toLocaleLowerCase();

  return (
    data.nameDe.toLocaleLowerCase().includes(f) ||
    data.nameFr.toLocaleLowerCase().includes(f)
  );
};

const initialState: AdminAusbildungsstaetteState = {
  ausbildungsstaetten: [],
  tableData: tableData,
  hasLoadedOnce: false,
  loading: false,
  error: undefined,
};

export const AdminAusbildungsstaetteStore = signalStore(
  withState(initialState),
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
              map((ausbildungsstaetten) => {
                const generated = generateRandomAusbildungsstaetten(60);
                return [...ausbildungsstaetten, ...generated];
              }),
              tapResponse({
                next: (ausbildungsstaetten) =>
                  patchState(store, (state) => {
                    state.ausbildungsstaetten = ausbildungsstaetten;
                    (state.tableData.data = ausbildungsstaetten.map(
                      (ausbildungsstaette) => ({
                        ...ausbildungsstaette,
                        ausbildungsgaengeCount:
                          ausbildungsstaette.ausbildungsgaenge?.length ?? 0,
                      }),
                    )),
                      (state.hasLoadedOnce = true);

                    return state;
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
      setPaginator: (paginator: MatPaginator) => {
        patchState(store, (state) => {
          state.tableData.paginator = paginator;
          return state;
        });
      },
      setSort: (sort: MatSort) => {
        patchState(store, (state) => {
          state.tableData.sort = sort;
          return state;
        });
      },
      setFilter: (filter: string) => {
        patchState(store, (state) => {
          state.tableData.filter = filter;
          return state;
        });
      },
      addAusbildungsstaetteRow: (newRow: AusbildungsstaetteTableData) => {
        patchState(store, (state) => {
          const data = [newRow, ...state.tableData.data];

          state.tableData.data = data;

          return state;
        });
      },
      removeNewAusbildungsstaetteRow: () => {
        patchState(store, (state) => {
          const data = state.tableData.data.slice(1);

          state.tableData.data = data;

          return state;
        });
      },
      addAusbildungsgangRow: (
        ausbildungsstaetteId: string,
        newRow: Ausbildungsgang,
      ) => {
        patchState(store, (state) => {
          const data = state.tableData.data.map((staette) => {
            if (staette.id === ausbildungsstaetteId) {
              return {
                ...staette,
                ausbildungsgaengeCount: staette.ausbildungsgaengeCount + 1,
                ausbildungsgaenge: [
                  newRow,
                  ...(staette.ausbildungsgaenge ?? []),
                ],
              };
            }

            return staette;
          });

          state.tableData.data = data;

          return state;
        });
      },
      removeNewAusbildungsgangRow: (staette: AusbildungsstaetteTableData) => {
        patchState(store, (state) => {
          // staette.ausbildungsgaengeCount -= 1;
          // staette.ausbildungsgaenge?.filter((g) => g.id !== 'new');

          const data = state.tableData.data.map((s) => {
            if (s.id === staette.id) {
              return {
                ...s,
                ausbildungsgaengeCount: s.ausbildungsgaengeCount - 1,
                ausbildungsgaenge: s.ausbildungsgaenge?.filter(
                  (g) => g.id !== 'new',
                ),
              };
            }

            return s;
          });

          state.tableData.data = data;

          return state;
        });
      },
    }),
  ),
  withComputed(({ ausbildungsstaetten }) => ({
    ausbildungsstaetteCount: computed(() => ausbildungsstaetten().length ?? 0),
  })),
);

function generateRandomAusbildungsstaetten(n: number): Ausbildungsstaette[] {
  return Array.from({ length: n }, (_, i) => ({
    id: `${i}`,
    nameDe: `Ausbildungsstaette ${i}`,
    nameFr: `Ausbildungsstaette ${i}`,
    ausbildungsgaenge: [],
  }));
}
