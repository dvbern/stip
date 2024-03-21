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
  data?: never;
  error?: never;
};

const initial = () => ({
  type: 'initial' as const,
  data: undefined,
  error: undefined,
});

const isInitial = (
  response: Response<unknown>,
): response is ResponseInitial => {
  return response.type === 'initial';
};

type ResponseFailure = {
  type: 'failure';
  data?: never;
  error: Error;
};

const failure = (error: Error) => ({
  type: 'failure' as const,
  data: undefined,
  error,
});

const isFailure = (
  response: Response<unknown>,
): response is ResponseFailure => {
  return response.type === 'failure';
};

type ResponsePending = {
  type: 'pending';
  data?: never;
  error?: never;
};

const pending = () => ({
  type: 'pending' as const,
  data: undefined,
  error: undefined,
});

const isPending = (
  response: Response<unknown>,
): response is ResponsePending => {
  return response.type === 'pending';
};

type ResponseSuccess<T = unknown> = {
  type: 'success';
  data: T;
  error?: never;
};

const success = <T>(data: T) => ({
  type: 'success' as const,
  data,
  error: undefined,
});

const isSuccess = <T>(
  response: Response<T>,
): response is ResponseSuccess<T> => {
  return response.type === 'success';
};

type Response<T> =
  | ResponseInitial
  | ResponseFailure
  | ResponsePending
  | ResponseSuccess<T>;

export interface AdminAusbildungsstaetteState {
  tableData: MatTableDataSource<AusbildungsstaetteTableData>;
  response: Response<Ausbildungsstaette[]>;
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
      tableData,
      response: { type: 'initial' },
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
              response: { type: 'pending' },
            });
          }),
          switchMap(() =>
            ausbildungsStaetteService.getAusbildungsstaetten$().pipe(
              tapResponse({
                next: (ausbildungsstaetten) =>
                  patchState(store, (state) => {
                    state.tableData.data = ausbildungsstaetten.map(
                      (ausbildungsstaette) => ({
                        ...ausbildungsstaette,
                        ausbildungsgaengeCount:
                          ausbildungsstaette.ausbildungsgaenge?.length ?? 0,
                      }),
                    );

                    return {
                      ...state,
                      response: {
                        type: 'success' as const,
                        data: ausbildungsstaetten,
                      },
                    };
                  }),
                error: (error: HttpErrorResponse) => {
                  patchState(store, {
                    response: { type: 'failure', error: error },
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
                response: { type: 'pending' },
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
                      next: () => {
                        patchState(store, (state) => {
                          const data = state.tableData.data.map((s) => {
                            if (s.id === 'new') {
                              return {
                                ...s,
                                ...staette,
                                id: 'new-id',
                              };
                            }

                            return s;
                          });

                          state.tableData.data = data;

                          return {
                            ...state,
                            response: {
                              type: 'success' as const,
                              data: state.tableData.data, //not ideal
                            },
                          };
                        });
                      },
                      error: (error: HttpErrorResponse) => {
                        patchState(store, {
                          response: { type: 'failure', error: error },
                        });
                      },
                      // @philip: do we need finalize? => ich denke es in disem store nicht
                      // finalize: () => {
                      //   patchState(store, {
                      //     response: { type: 'initial' },
                      //   });
                      // },
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
                    next: () => {
                      patchState(store, (state) => {
                        const data = state.tableData.data.map((s) => {
                          if (s.id === staette.id) {
                            return {
                              ...s,
                              ...staette,
                            };
                          }

                          return s;
                        });

                        state.tableData.data = data;

                        return {
                          ...state,
                          response: {
                            type: 'success' as const,
                            data: state.tableData.data, // not ideal
                          },
                        };
                      });
                    },
                    error: (error: HttpErrorResponse) => {
                      patchState(store, {
                        response: { type: 'failure', error: error },
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
              response: { type: 'pending' },
            });
          }),
          switchMap((staette: AusbildungsstaetteTableData) => {
            if (!staette.id) {
              throw new Error('Ausbildungsstaette ID is undefined');
            }
            return ausbildungsStaetteService
              .deleteAusbildungsstaette$({ ausbildungsstaetteId: staette.id })
              .pipe(
                tapResponse({
                  next: () => {
                    patchState(store, (state) => {
                      const data = state.tableData.data.filter(
                        (s) => s.id !== staette.id,
                      );

                      state.tableData.data = data;

                      return {
                        ...state,
                        response: {
                          type: 'success' as const,
                          data: state.tableData.data, // not ideal
                        },
                      };
                    });
                  },
                  error: (error: HttpErrorResponse) => {
                    patchState(store, {
                      response: { type: 'failure', error: error },
                    });
                  },
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
  withComputed(({ tableData, response }) => ({
    ausbildungsstaetteCount: computed(() => tableData().data.length ?? 0),
    loading: computed(() => (response.type() === 'pending' ? true : false)),
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
