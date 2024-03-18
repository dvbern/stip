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
import { pipe, switchMap, tap } from 'rxjs';

import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import {
  Ausbildungsgang,
  Ausbildungsstaette,
  AusbildungsstaetteService,
} from '@dv/shared/model/gesuch';

type ResponseInitial = {
  type: 'initial';
};

type ResponseFailure = {
  type: 'failure';
  error: Error;
};

type ResponseSuccess = {
  type: 'success';
};

type Response = ResponseInitial | ResponseFailure | ResponseSuccess;

export interface AdminAusbildungsstaetteState {
  ausbildungsstaetten: Ausbildungsstaette[];
  tableData: MatTableDataSource<AusbildungsstaetteTableData>;
  hasLoadedOnce: boolean;
  loading: boolean;
  response: Response;
  error?: string;
}

export const AdminAusbildungsstaetteStore = signalStore(
  withState(() => {
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
      tableData,
      hasLoadedOnce: false,
      loading: false,
      response: { type: 'initial' },
      error: undefined,
    };

    return initialState;
  }),
  withMethods(
    (store, ausbildungsStaetteService = inject(AusbildungsstaetteService)) => ({
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

      // Ausbildungsstaette ==========================================================
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
                  patchState(store, (state) => {
                    state.ausbildungsstaetten = ausbildungsstaetten;
                    state.tableData.data = ausbildungsstaetten.map(
                      (ausbildungsstaette) => ({
                        ...ausbildungsstaette,
                        ausbildungsgaengeCount:
                          ausbildungsstaette.ausbildungsgaenge?.length ?? 0,
                      }),
                    );

                    state.hasLoadedOnce = true;

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
      addAusbildungsstaetteRow: (newRow: AusbildungsstaetteTableData) => {
        patchState(store, (state) => {
          const data = [newRow, ...state.tableData.data];

          state.tableData.data = data;

          return state;
        });
      },
      removeNewAusbildungsstaetteRow: (
        staette: AusbildungsstaetteTableData,
      ) => {
        patchState(store, (state) => {
          const data = state.tableData.data.filter((s) => s.id !== staette.id);

          state.tableData.data = data;

          return state;
        });
      },
      handleCreateUpdateAusbildungsstaette:
        rxMethod<AusbildungsstaetteTableData>(
          pipe(
            tap(() => {
              patchState(store, {
                loading: true,
                error: undefined,
              });
            }),
            switchMap((staette: AusbildungsstaetteTableData) => {
              if (!staette.id) {
                throw new Error('Ausbildungsstaette ID is undefined');
              }

              if (staette.id === 'new') {
                return ausbildungsStaetteService
                  .createAusbildungsstaette$({
                    ausbildungsstaetteCreate: {
                      nameDe: staette.nameDe,
                      nameFr: staette.nameFr,
                    },
                  })
                  .pipe(
                    tapResponse({
                      next: (ausbildungsstaette) => {
                        patchState(store, (state) => {
                          const data = [
                            ausbildungsstaette,
                            ...state.tableData.data,
                          ];

                          state.tableData.data = data;

                          return {
                            ...state,
                            response: { type: 'success' } as ResponseSuccess,
                          };
                        });
                      },
                      error: (error: HttpErrorResponse) => {
                        patchState(store, {
                          error: error.message,
                          response: { type: 'failure', error: error },
                        });
                      },
                      finalize: () => {
                        patchState(store, {
                          loading: false,
                        });
                      },
                    }),
                  );
              }

              return ausbildungsStaetteService
                .updateAusbildungsstaette$({
                  ausbildungsstaetteId: staette.id,
                  ausbildungsstaetteUpdate: {
                    nameDe: staette.nameDe,
                    nameFr: staette.nameFr,
                  },
                })
                .pipe(
                  tapResponse({
                    next: (ausbildungsstaette) => {
                      patchState(store, (state) => {
                        const data = state.tableData.data.map((s) => {
                          if (s.id === ausbildungsstaette.id) {
                            return ausbildungsstaette;
                          }

                          return s;
                        });

                        state.tableData.data = data;

                        return state;
                      });
                    },
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
                );
            }),
          ),
        ),
      deleteAusbildungsstaette: rxMethod<AusbildungsstaetteTableData>(
        pipe(
          tap(() => {
            patchState(store, {
              loading: true,
              error: undefined,
            });
          }),
          switchMap((staette: AusbildungsstaetteTableData) => {
            if (!staette.id) {
              throw new Error('Ausbildungsstaette ID is undefined');
            }
            return ausbildungsStaetteService
              .deleteAusbildungsstaette$({ ausbildungsstaetteId: staette.id })
              .pipe(
                tap(() => {
                  patchState(store, (state) => {
                    const data = state.tableData.data.filter(
                      (s) => s.id !== staette.id,
                    );

                    state.tableData.data = data;

                    return state;
                  });
                }),
              );
          }),
        ),
      ),
      // Ausbildungsgang  ==========================================================
      addAusbildungsgangRow: (
        ausbildungsstaetteId: string,
        newRow: Ausbildungsgang,
      ) => {
        patchState(store, (state) => {
          // could find be more efficent?
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
      removeNewAusbildungsgangRow: (
        staette: AusbildungsstaetteTableData,
        gang: Ausbildungsgang,
      ) => {
        patchState(store, (state) => {
          const data = state.tableData.data.map((s) => {
            if (s.id === staette.id) {
              const ausbildungsgaenge = s.ausbildungsgaenge?.filter(
                (g) => g.id !== gang.id,
              );

              return {
                ...s,
                ausbildungsgaengeCount: s.ausbildungsgaengeCount - 1,
                ausbildungsgaenge,
              };
            }

            return s;
          });

          state.tableData.data = data;

          return state;
        });
      },
      handleCreateUpdateAusbildungsgang: (
        staette: AusbildungsstaetteTableData,
        gang: Ausbildungsgang,
      ) => {
        patchState(store, (state) => {
          const data = state.tableData.data.map((s) => {
            if (s.id === staette.id) {
              const ausbildungsgaenge = s.ausbildungsgaenge?.map((g) => {
                if (g.id === gang.id) {
                  return gang;
                }

                return g;
              });

              return {
                ...s,
                ausbildungsgaenge,
              };
            }

            return s;
          });

          state.tableData.data = data;

          return state;
        });
      },
      deleteAusbildungsgang: (
        staette: AusbildungsstaetteTableData,
        gang: Ausbildungsgang,
      ) => {
        patchState(store, (state) => {
          const data = state.tableData.data.map((s) => {
            if (s.id === staette.id) {
              const ausbildungsgaenge = s.ausbildungsgaenge?.filter(
                (g) => g.id !== gang.id,
              );

              return {
                ...s,
                ausbildungsgaengeCount: s.ausbildungsgaengeCount - 1,
                ausbildungsgaenge,
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

// function generateRandomAusbildungsstaetten(n: number): Ausbildungsstaette[] {
//   return Array.from({ length: n }, (_, i) => ({
//     id: `${i}`,
//     nameDe: `Ausbildungsstaette ${i}`,
//     nameFr: `Ausbildungsstaette ${i}`,
//     ausbildungsgaenge: [],
//   }));
// }
