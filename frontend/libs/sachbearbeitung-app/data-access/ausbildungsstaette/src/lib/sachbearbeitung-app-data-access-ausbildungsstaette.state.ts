import { HttpErrorResponse } from '@angular/common/http';
import { Injectable, computed, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import { UpdateOrCreate, isNewEntry } from '@dv/shared/model/api';
import {
  Ausbildungsgang,
  AusbildungsgangCreate,
  AusbildungsgangService,
  Ausbildungsstaette,
  AusbildungsstaetteService,
  Bildungskategorie,
  BildungskategorieService,
} from '@dv/shared/model/gesuch';
import {
  RemoteData,
  failure,
  initial,
  pending,
  success,
} from '@dv/shared/util/remote-data';

export interface AdminAusbildungsstaetteState {
  tableData: MatTableDataSource<AusbildungsstaetteTableData>;
  bildungskategorien: RemoteData<Bildungskategorie[]>;
  response: RemoteData<Ausbildungsstaette[]>;
}
@Injectable()
export class AdminAusbildungsstaetteStore extends signalStore(
  { protectedState: false },
  withState(() => {
    const tableData = new MatTableDataSource<AusbildungsstaetteTableData>();

    tableData.sortingDataAccessor = (data, sortHeaderId) => {
      const value = data[sortHeaderId as keyof AusbildungsstaetteTableData];

      if (typeof value === 'string') {
        return value.toLocaleLowerCase();
      }

      return sortHeaderId;
    };

    tableData.filterPredicate = (data, filter) => {
      const f = filter.trim().toLocaleLowerCase();

      return (
        data.nameDe.toLocaleLowerCase().includes(f) ||
        data.nameFr.toLocaleLowerCase().includes(f)
      );
    };

    const initialState: AdminAusbildungsstaetteState = {
      tableData,
      bildungskategorien: initial(),
      response: initial(),
    };

    return initialState;
  }),
) {
  private ausbildungsStaetteService = inject(AusbildungsstaetteService);
  private ausbildungsgangService = inject(AusbildungsgangService);
  private bildungskategorieService = inject(BildungskategorieService);

  ausbildungsstaetteCount = computed(() => this.tableData().data.length ?? 0);
  loading = computed(() => this.response.type() === 'pending');
  sortedBildungskategorien = computed(
    () => this.bildungskategorien().data?.sort((a, b) => a.bfs - b.bfs) ?? [],
  );

  setPaginator(paginator: MatPaginator) {
    patchState(this, (state) => {
      state.tableData.paginator = paginator;
      return state;
    });
  }

  setSort(sort: MatSort) {
    patchState(this, (state) => {
      state.tableData.sort = sort;
      return state;
    });
  }

  setFilter(filter: string) {
    patchState(this, (state) => {
      state.tableData.filter = filter;
      return state;
    });
  }

  // Ausbildungsstaette ==========================================================
  loadAusbildungsstaetten = rxMethod(
    pipe(
      tap(() => {
        patchState(this, {
          response: pending(),
        });
      }),
      switchMap(() =>
        this.ausbildungsStaetteService.getAusbildungsstaetten$().pipe(
          tapResponse({
            next: (ausbildungsstaetten) =>
              patchState(this, (state) => {
                state.tableData.data = ausbildungsstaetten.map(
                  (ausbildungsstaette) => ({
                    ...ausbildungsstaette,
                    ausbildungsgaengeCount:
                      ausbildungsstaette.ausbildungsgaenge?.length ?? 0,
                  }),
                );

                return {
                  ...state,
                  response: success(ausbildungsstaetten),
                };
              }),
            error: (error: HttpErrorResponse) => {
              patchState(this, {
                response: failure(error),
              });
            },
          }),
        ),
      ),
      takeUntilDestroyed(),
    ),
  );

  addAusbildungsstaetteRow(newRow: AusbildungsstaetteTableData) {
    patchState(this, (state) => {
      state.tableData.paginator?.firstPage();

      const data = [newRow, ...state.tableData.data];

      state.tableData.data = data;

      return state;
    });
  }

  removeNewAusbildungsstaetteRow(staette: AusbildungsstaetteTableData) {
    patchState(this, (state) => {
      const data = state.tableData.data.filter((s) => s.id !== staette.id);

      state.tableData.data = data;

      return state;
    });
  }

  handleCreateUpdateAusbildungsstaette = rxMethod<AusbildungsstaetteTableData>(
    pipe(
      tap(() => {
        patchState(this, {
          response: pending(),
        });
      }),
      switchMap((staette: AusbildungsstaetteTableData) => {
        if (!staette.id) {
          throw new Error('Ausbildungsstaette ID is undefined');
        }

        if (staette.id === 'new') {
          return this.ausbildungsStaetteService
            .createAusbildungsstaette$({
              ausbildungsstaetteCreate: {
                nameDe: staette.nameDe,
                nameFr: staette.nameFr,
              },
            })
            .pipe(
              tapResponse({
                next: (ausbildungsstaette: Ausbildungsstaette) => {
                  patchState(this, (state) => {
                    const data = state.tableData.data.map((s) => {
                      if (s.id === 'new') {
                        return {
                          ...s,
                          ...ausbildungsstaette,
                        };
                      }

                      return s;
                    });

                    state.tableData.data = data;

                    return {
                      ...state,
                      response: success(data),
                    };
                  });
                },
                error: (error: HttpErrorResponse) => {
                  patchState(this, {
                    response: failure(error),
                  });
                },
              }),
            );
        }

        return this.ausbildungsStaetteService
          .updateAusbildungsstaette$({
            ausbildungsstaetteId: staette.id,
            ausbildungsstaetteUpdate: {
              nameDe: staette.nameDe,
              nameFr: staette.nameFr,
            },
          })
          .pipe(
            tapResponse({
              next: (ausbildungsstaette: Ausbildungsstaette) => {
                patchState(this, (state) => {
                  const data = state.tableData.data.map((s) => {
                    if (s.id === staette.id) {
                      return {
                        ...s,
                        ...ausbildungsstaette,
                      };
                    }

                    return s;
                  });

                  state.tableData.data = data;

                  return {
                    ...state,
                    response: success(data),
                  };
                });
              },
              error: (error: HttpErrorResponse) => {
                patchState(this, {
                  response: failure(error),
                });
              },
            }),
          );
      }),
      takeUntilDestroyed(),
    ),
  );

  deleteAusbildungsstaette = rxMethod<AusbildungsstaetteTableData>(
    pipe(
      tap(() => {
        patchState(this, {
          response: pending(),
        });
      }),
      switchMap((staette: AusbildungsstaetteTableData) => {
        if (!staette.id) {
          throw new Error('Ausbildungsstaette ID is undefined');
        }
        return this.ausbildungsStaetteService
          .deleteAusbildungsstaette$({ ausbildungsstaetteId: staette.id })
          .pipe(
            tapResponse({
              next: () => {
                patchState(this, (state) => {
                  const data = state.tableData.data.filter(
                    (s) => s.id !== staette.id,
                  );

                  state.tableData.data = data;

                  return {
                    ...state,
                    response: success(data),
                  };
                });
              },
              error: (error: HttpErrorResponse) => {
                patchState(this, {
                  response: failure(error),
                });
              },
            }),
          );
      }),
      takeUntilDestroyed(),
    ),
  );

  // Ausbildungsgang  ==========================================================
  addAusbildungsgangRow(ausbildungsstaetteId: string, newRow: Ausbildungsgang) {
    patchState(this, (state) => {
      const data = state.tableData.data.map((staette) => {
        if (staette.id === ausbildungsstaetteId) {
          return {
            ...staette,
            ausbildungsgaengeCount: staette.ausbildungsgaengeCount + 1,
            ausbildungsgaenge: [newRow, ...(staette.ausbildungsgaenge ?? [])],
          };
        }

        return staette;
      });

      state.tableData.data = data;

      return state;
    });
  }

  removeNewAusbildungsgangRow(
    staette: AusbildungsstaetteTableData,
    gang: Ausbildungsgang,
  ) {
    patchState(this, (state) => {
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
  }

  handleCreateUpdateAusbildungsgang = rxMethod<{
    staette: AusbildungsstaetteTableData;
    gang: UpdateOrCreate<AusbildungsgangCreate, Ausbildungsgang>;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          response: pending(),
        });
      }),
      switchMap(({ staette, gang }) => {
        if (!staette.id) {
          throw new Error('Ausbildungsstaette ID is undefined');
        }

        if (isNewEntry(gang)) {
          return this.createAusbildungsgang$(staette, gang);
        }

        return this.ausbildungsgangService
          .updateAusbildungsgang$({
            ausbildungsgangId: gang.id,
            ausbildungsgangUpdate: {
              ausbildungsstaetteId: staette.id,
              bildungskategorieId: gang.bildungskategorie.id,
              bezeichnungDe: gang.bezeichnungDe ?? '',
              bezeichnungFr: gang.bezeichnungFr ?? '',
            },
          })
          .pipe(
            tapResponse({
              next: (gang: Ausbildungsgang) => {
                patchState(this, (state) => {
                  const data = state.tableData.data.map((s) => {
                    if (s.id === staette.id) {
                      const ausbildungsgaenge = s.ausbildungsgaenge?.map(
                        (g) => {
                          if (g.id === gang.id) {
                            return gang;
                          }

                          return g;
                        },
                      );

                      return {
                        ...s,
                        ausbildungsgaenge,
                      };
                    }

                    return s;
                  });

                  state.tableData.data = data;

                  return {
                    ...state,
                    response: success(data),
                  };
                });
              },
              error: (error: HttpErrorResponse) => {
                patchState(this, {
                  response: failure(error),
                });
              },
            }),
          );
      }),
      takeUntilDestroyed(),
    ),
  );

  deleteAusbildungsgang = rxMethod<{
    staette: AusbildungsstaetteTableData;
    gang: Ausbildungsgang;
  }>(
    pipe(
      tap(() => {
        patchState(this, {
          response: pending(),
        });
      }),
      switchMap(({ staette, gang }) => {
        return this.ausbildungsgangService
          .deleteAusbildungsgang$({ ausbildungsgangId: gang.id })
          .pipe(
            tapResponse({
              next: () => {
                patchState(this, (state) => {
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

                  return {
                    ...state,
                    response: success(data),
                  };
                });
              },
              error: (error: HttpErrorResponse) => {
                patchState(this, {
                  response: failure(error),
                });
              },
            }),
          );
      }),
      takeUntilDestroyed(),
    ),
  );

  loadBildungskategorien = rxMethod(
    pipe(
      tap(() => {
        patchState(this, {
          bildungskategorien: pending(),
        });
      }),
      switchMap(() =>
        this.bildungskategorieService.getBildungskategorien$().pipe(
          tapResponse({
            next: (bildungskategorien) =>
              patchState(this, {
                bildungskategorien: success(bildungskategorien),
              }),
            error: (error: HttpErrorResponse) => {
              patchState(this, {
                bildungskategorien: failure(error),
              });
            },
          }),
        ),
      ),
      takeUntilDestroyed(),
    ),
  );

  private createAusbildungsgang$(
    staette: AusbildungsstaetteTableData,
    gang: AusbildungsgangCreate & { id: 'new' },
  ) {
    return this.ausbildungsgangService
      .createAusbildungsgang$({
        ausbildungsgangCreate: {
          ausbildungsstaetteId: staette.id,
          bildungskategorieId: gang.bildungskategorieId,
          bezeichnungDe: gang.bezeichnungDe ?? '',
          bezeichnungFr: gang.bezeichnungFr ?? '',
        },
      })
      .pipe(
        tapResponse({
          next: (gang: Ausbildungsgang) => {
            patchState(this, (state) => {
              const data = state.tableData.data.map((s) => {
                if (s.id === staette.id) {
                  return {
                    ...s,
                    ausbildungsgaengeCount: s.ausbildungsgaengeCount + 1,
                    ausbildungsgaenge: s.ausbildungsgaenge?.map((g) => {
                      if (g.id === 'new') {
                        return gang;
                      }

                      return g;
                    }),
                  };
                }

                return s;
              });

              state.tableData.data = data;

              return {
                ...state,
                response: success(data),
              };
            });
          },
          error: (error: HttpErrorResponse) => {
            patchState(this, {
              response: failure(error),
            });
          },
        }),
      );
  }
}
