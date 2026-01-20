import { Injectable, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  DarlehenService,
  DarlehenServiceCreateFreiwilligDarlehenRequestParams,
  DarlehenServiceDeleteFreiwilligDarlehenGsRequestParams,
  DarlehenServiceFreiwilligDarlehenUpdateGsRequestParams,
  DarlehenServiceFreiwilligDarlehenUpdateSbRequestParams,
  DarlehenServiceFreiwilligDarlehenZurueckweisenRequestParams,
  DarlehenServiceGetAllFreiwilligDarlehenGsRequestParams,
  DarlehenServiceGetAllFreiwilligDarlehenSbRequestParams,
  DarlehenServiceGetFreiwilligDarlehenDashboardSbRequestParams,
  FreiwilligDarlehen,
  PaginatedSbFreiwilligDarlehenDashboard,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
  pending,
} from '@dv/shared/util/remote-data';

type DarlehenState = {
  cachedDarlehen: CachedRemoteData<FreiwilligDarlehen>;
  darlehenList: CachedRemoteData<FreiwilligDarlehen[]>;
  paginatedSbDarlehenDashboard: CachedRemoteData<PaginatedSbFreiwilligDarlehenDashboard>;
};

const initialState: DarlehenState = {
  cachedDarlehen: initial(),
  darlehenList: initial(),
  paginatedSbDarlehenDashboard: initial(),
};

@Injectable({ providedIn: 'root' })
export class DarlehenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private darlehenService = inject(DarlehenService);
  private router = inject(Router);
  private globalNotificationStore = inject(GlobalNotificationStore);

  dashboardViewSig = computed(() => {
    return {
      darlehen: fromCachedDataSig(this.paginatedSbDarlehenDashboard),
      loading: isPending(this.paginatedSbDarlehenDashboard()),
    };
  });

  darlehenViewSig = computed(() => {
    return {
      darlehen: fromCachedDataSig(this.cachedDarlehen),
      loading: isPending(this.cachedDarlehen()),
    };
  });

  darlehenListSig = computed(() => {
    return fromCachedDataSig(this.darlehenList);
  });

  darlehenListViewSig = computed(() => {
    return {
      darlehenList: fromCachedDataSig(this.darlehenList),
      loading: isPending(this.darlehenList()),
    };
  });

  getDarlehenGs$ = rxMethod<{
    darlehenId: string;
    onFailure?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          cachedDarlehen: pending(),
        }));
      }),
      switchMap(({ darlehenId, onFailure }) =>
        this.darlehenService
          .getFreiwilligDarlehenGs$({ darlehenId })
          .pipe(
            handleApiResponse(
              (darlehen) => patchState(this, { cachedDarlehen: darlehen }),
              { onFailure },
            ),
          ),
      ),
    ),
  );

  getAllDarlehenGs$ =
    rxMethod<DarlehenServiceGetAllFreiwilligDarlehenGsRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            darlehenList: pending(),
          }));
        }),
        switchMap((req) =>
          this.darlehenService
            .getAllFreiwilligDarlehenGs$(req)
            .pipe(
              handleApiResponse((darlehen) =>
                patchState(this, { darlehenList: darlehen }),
              ),
            ),
        ),
      ),
    );

  createDarlehen$ =
    rxMethod<DarlehenServiceCreateFreiwilligDarlehenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            cachedDarlehen: cachedPending(state.cachedDarlehen),
          }));
        }),
        switchMap((req) =>
          this.darlehenService.createFreiwilligDarlehen$(req).pipe(
            handleApiResponse(
              (darlehen) => {
                patchState(this, { cachedDarlehen: darlehen });
              },
              {
                onSuccess: (data) => {
                  this.router.navigate(['/darlehen', data.id]);
                },
              },
            ),
          ),
        ),
      ),
    );

  darlehenUpdateAndEingebenGs$ = rxMethod<{
    data: DarlehenServiceFreiwilligDarlehenUpdateGsRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.freiwilligDarlehenUpdateGs$(data).pipe(
          switchMap((updatedDarlehen) =>
            this.darlehenService
              .freiwilligDarlehenEingeben$({ darlehenId: updatedDarlehen.id })
              .pipe(
                handleApiResponse(
                  (darlehen) => {
                    patchState(this, { cachedDarlehen: darlehen });
                  },
                  {
                    onSuccess: () => {
                      onSuccess();
                      this.globalNotificationStore.createSuccessNotification({
                        messageKey: 'shared.form.darlehen.eingeben.success',
                      });
                    },
                  },
                ),
              ),
          ),
          catchError(() => {
            this.globalNotificationStore.createNotification({
              type: 'ERROR',
              messageKey: 'shared.form.darlehen.eingeben.failure',
            });

            // the form shall not be empty nor pending in case of an error, so the user can retry
            patchState(this, (state) => ({
              cachedDarlehen: state.cachedDarlehen,
            }));

            return EMPTY;
          }),
        ),
      ),
    ),
  );

  darlehenDeleteGs$ = rxMethod<{
    data: DarlehenServiceDeleteFreiwilligDarlehenGsRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.deleteFreiwilligDarlehenGs$(data).pipe(
          handleApiResponse(
            () => {
              patchState(this, { cachedDarlehen: initial() });
            },
            {
              onSuccess: () => {
                onSuccess();
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.form.darlehen.delete.success',
                });
              },
            },
          ),
        ),
      ),
    ),
  );

  getDarlehenDashboardSb$ =
    rxMethod<DarlehenServiceGetFreiwilligDarlehenDashboardSbRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            paginatedSbDarlehenDashboard: pending(),
          }));
        }),
        switchMap((req) =>
          this.darlehenService.getFreiwilligDarlehenDashboardSb$(req).pipe(
            handleApiResponse((darlehen) => {
              patchState(this, { paginatedSbDarlehenDashboard: darlehen });
            }),
          ),
        ),
      ),
    );

  getDarlehenSb$ = rxMethod<{
    darlehenId: string;
    onFailure?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          cachedDarlehen: pending(),
        }));
      }),
      switchMap(({ darlehenId, onFailure }) =>
        this.darlehenService
          .getFreiwilligDarlehenSb$({ darlehenId })
          .pipe(
            handleApiResponse(
              (darlehen) => patchState(this, { cachedDarlehen: darlehen }),
              { onFailure },
            ),
          ),
      ),
    ),
  );

  getAllDarlehenSb$ =
    rxMethod<DarlehenServiceGetAllFreiwilligDarlehenSbRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            darlehenList: pending(),
          }));
        }),
        switchMap((req) =>
          this.darlehenService
            .getAllFreiwilligDarlehenSb$(req)
            .pipe(
              handleApiResponse((darlehen) =>
                patchState(this, { darlehenList: darlehen }),
              ),
            ),
        ),
      ),
    );

  // SB Methoden

  darlehenUpdateAndFreigebenSb$ = rxMethod<{
    data: DarlehenServiceFreiwilligDarlehenUpdateSbRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.freiwilligDarlehenUpdateSb$(data).pipe(
          switchMap((updatedDarlehen) =>
            this.darlehenService
              .freiwilligDarlehenFreigeben$({ darlehenId: updatedDarlehen.id })
              .pipe(
                handleApiResponse(
                  (darlehen) => {
                    patchState(this, { cachedDarlehen: darlehen });
                  },
                  {
                    onSuccess: () => {
                      onSuccess();
                      this.globalNotificationStore.createSuccessNotification({
                        messageKey: 'shared.form.darlehen.freigeben.success',
                      });
                    },
                  },
                ),
              ),
          ),
          catchError(() => {
            this.globalNotificationStore.createNotification({
              type: 'ERROR',
              messageKey: 'shared.form.darlehen.freigeben.failure',
            });

            patchState(this, (state) => ({
              cachedDarlehen: state.cachedDarlehen,
            }));

            return EMPTY;
          }),
        ),
      ),
    ),
  );

  darlehenZurueckweisen$ =
    rxMethod<DarlehenServiceFreiwilligDarlehenZurueckweisenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            cachedDarlehen: cachedPending(state.cachedDarlehen),
          }));
        }),
        switchMap((data) =>
          this.darlehenService.freiwilligDarlehenZurueckweisen$(data).pipe(
            handleApiResponse(
              (darlehen) => {
                patchState(this, { cachedDarlehen: darlehen });
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.form.darlehen.zurueckweisen.success',
                  });
                },
              },
            ),
            catchError(() => {
              this.globalNotificationStore.createNotification({
                type: 'ERROR',
                messageKey: 'shared.form.darlehen.zurueckweisen.failure',
              });

              patchState(this, (state) => ({
                cachedDarlehen: state.cachedDarlehen,
              }));

              return EMPTY;
            }),
          ),
        ),
      ),
    );

  // SB Freigabestelle Methoden

  darlehenUpdateAndAbschliessenSb$ = rxMethod<{
    data: DarlehenServiceFreiwilligDarlehenUpdateSbRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.freiwilligDarlehenUpdateSb$(data).pipe(
          switchMap((updatedDarlehen) => {
            const action$ = updatedDarlehen.gewaehren
              ? this.darlehenService.freiwilligDarlehenAkzeptieren$({
                  darlehenId: updatedDarlehen.id,
                })
              : this.darlehenService.freiwilligDarlehenAblehen$({
                  darlehenId: updatedDarlehen.id,
                });

            return action$.pipe(
              handleApiResponse(
                (darlehen) => {
                  patchState(this, { cachedDarlehen: darlehen });
                },
                {
                  onSuccess: () => {
                    onSuccess();
                    this.globalNotificationStore.createSuccessNotification({
                      messageKey: 'shared.form.darlehen.abschliessen.success',
                    });
                  },
                },
              ),
            );
          }),
          catchError(() => {
            this.globalNotificationStore.createNotification({
              type: 'ERROR',
              messageKey: 'shared.form.darlehen.abschliessen.failure',
            });

            patchState(this, (state) => ({
              cachedDarlehen: state.cachedDarlehen,
            }));

            return EMPTY;
          }),
        ),
      ),
    ),
  );
}
