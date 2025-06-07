import { HttpErrorResponse } from '@angular/common/http';
import { Injectable, computed, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
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
  isPending,
  pending,
  success,
} from '@dv/shared/util/remote-data';

export interface AdminAusbildungsstaetteState {
  tableData: AusbildungsstaetteTableData[];
  bildungskategorien: RemoteData<Bildungskategorie[]>;
  response: RemoteData<Ausbildungsstaette[]>;
}
const initialState: AdminAusbildungsstaetteState = {
  tableData: [],
  bildungskategorien: initial(),
  response: initial(),
};
@Injectable()
export class AdminAusbildungsstaetteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungsStaetteService = inject(AusbildungsstaetteService);
  private ausbildungsgangService = inject(AusbildungsgangService);
  private bildungskategorieService = inject(BildungskategorieService);

  ausbildungsstaetteCount = computed(() => this.tableData().length);
  loading = computed(() => isPending(this.response()));
  sortedBildungskategorien = computed(() =>
    [...(this.bildungskategorien().data ?? [])].sort((a, b) => a.bfs - b.bfs),
  );

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
              patchState(this, (state) => ({
                ...state,
                tableData: ausbildungsstaetten.map((ausbildungsstaette) => ({
                  ...ausbildungsstaette,
                  ausbildungsgaengeCount:
                    ausbildungsstaette.ausbildungsgaenge?.length ?? 0,
                })),
                response: success(ausbildungsstaetten),
              })),
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
    patchState(this, (state) => ({
      ...state,
      tableData: [newRow, ...state.tableData],
    }));
  }

  removeNewAusbildungsstaetteRow(staette: AusbildungsstaetteTableData) {
    patchState(this, (state) => ({
      ...state,
      tableData: state.tableData.filter((s) => s.id !== staette.id),
    }));
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
                    const tableData = state.tableData.map((s) =>
                      s.id === 'new'
                        ? {
                            ...s,
                            ...ausbildungsstaette,
                          }
                        : s,
                    );

                    return {
                      ...state,
                      tableData,
                      response: success(tableData),
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
                  const tableData = state.tableData.map((s) => {
                    if (s.id === staette.id) {
                      return {
                        ...s,
                        ...ausbildungsstaette,
                      };
                    }

                    return s;
                  });

                  return {
                    ...state,
                    tableData,
                    response: success(tableData),
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
                  const tableData = state.tableData.filter(
                    (s) => s.id !== staette.id,
                  );

                  return {
                    ...state,
                    tableData,
                    response: success(tableData),
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
    patchState(this, (state) => ({
      ...state,
      tableData: state.tableData.map((staette) =>
        staette.id === ausbildungsstaetteId
          ? {
              ...staette,
              ausbildungsgaengeCount: staette.ausbildungsgaengeCount + 1,
              ausbildungsgaenge: [newRow, ...(staette.ausbildungsgaenge ?? [])],
            }
          : staette,
      ),
    }));
  }

  removeNewAusbildungsgangRow(
    staette: AusbildungsstaetteTableData,
    gang: Ausbildungsgang,
  ) {
    patchState(this, (state) => ({
      ...state,
      tableData: state.tableData.map((s) =>
        s.id === staette.id
          ? {
              ...s,
              ausbildungsgaengeCount: s.ausbildungsgaengeCount - 1,
              ausbildungsgaenge: s.ausbildungsgaenge?.filter(
                (g) => g.id !== gang.id,
              ),
            }
          : s,
      ),
    }));
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
                  const tableData = state.tableData.map((s) =>
                    s.id === staette.id
                      ? {
                          ...s,
                          ausbildungsgaenge: s.ausbildungsgaenge?.map((g) =>
                            g.id === gang.id ? gang : g,
                          ),
                        }
                      : s,
                  );

                  return {
                    ...state,
                    tableData,
                    response: success(tableData),
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
      switchMap(({ staette, gang }) =>
        this.ausbildungsgangService
          .deleteAusbildungsgang$({ ausbildungsgangId: gang.id })
          .pipe(
            tapResponse({
              next: () => {
                patchState(this, (state) => {
                  const tableData = state.tableData.map((s) =>
                    s.id === staette.id
                      ? {
                          ...s,
                          ausbildungsgaengeCount: s.ausbildungsgaengeCount - 1,
                          ausbildungsgaenge: s.ausbildungsgaenge?.filter(
                            (g) => g.id !== gang.id,
                          ),
                        }
                      : s,
                  );

                  return {
                    ...state,
                    tableData,
                    response: success(tableData),
                  };
                });
              },
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
              const tableData = state.tableData.map((s) =>
                s.id === staette.id
                  ? {
                      ...s,
                      ausbildungsgaengeCount: s.ausbildungsgaengeCount + 1,
                      ausbildungsgaenge: s.ausbildungsgaenge?.map((g) =>
                        g.id === 'new' ? gang : g,
                      ),
                    }
                  : s,
              );

              return {
                ...state,
                tableData,
                response: success(tableData),
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
