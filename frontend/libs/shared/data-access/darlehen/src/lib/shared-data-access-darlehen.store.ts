import { Injectable, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  Darlehen,
  DarlehenGsResponse,
  DarlehenService,
  DarlehenServiceCreateDarlehenRequestParams,
  DarlehenServiceDarlehenUpdateGsRequestParams,
  DarlehenServiceDarlehenUpdateSbRequestParams,
  DarlehenServiceDarlehenZurueckweisenRequestParams,
  DarlehenServiceDeleteDarlehenGsRequestParams,
  DarlehenServiceGetAllDarlehenGsRequestParams,
  DarlehenServiceGetAllDarlehenSbRequestParams,
  DarlehenServiceGetDarlehenDashboardSbRequestParams,
  PaginatedSbDarlehenDashboard,
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
  cachedDarlehen: CachedRemoteData<Darlehen>;
  allDarlehen: CachedRemoteData<DarlehenGsResponse>;
  paginatedSbDarlehenDashboard: CachedRemoteData<PaginatedSbDarlehenDashboard>;
};

const initialState: DarlehenState = {
  cachedDarlehen: initial(),
  allDarlehen: initial(),
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
    return fromCachedDataSig(this.allDarlehen);
  });

  darlehenListViewSig = computed(() => {
    return {
      allDarlehen: fromCachedDataSig(this.allDarlehen),
      loading: isPending(this.allDarlehen()),
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
          .getDarlehenGs$({ darlehenId })
          .pipe(
            handleApiResponse(
              (darlehen) => patchState(this, { cachedDarlehen: darlehen }),
              { onFailure },
            ),
          ),
      ),
    ),
  );

  getAllDarlehenGs$ = rxMethod<DarlehenServiceGetAllDarlehenGsRequestParams>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          allDarlehen: pending(),
        }));
      }),
      switchMap((req) =>
        this.darlehenService
          .getAllDarlehenGs$(req)
          .pipe(
            handleApiResponse((allDarlehen) =>
              patchState(this, { allDarlehen }),
            ),
          ),
      ),
    ),
  );

  createDarlehen$ = rxMethod<DarlehenServiceCreateDarlehenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((req) =>
        this.darlehenService.createDarlehen$(req).pipe(
          handleApiResponse(
            (darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
            },
            {
              onSuccess: (data) => {
                this.router.navigate([
                  '/darlehen',
                  data.id,
                  'fall',
                  req.fallId,
                ]);
              },
            },
          ),
        ),
      ),
    ),
  );

  darlehenUpdateAndEingebenGs$ = rxMethod<{
    data: DarlehenServiceDarlehenUpdateGsRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.darlehenUpdateGs$(data).pipe(
          switchMap((updatedDarlehen) =>
            this.darlehenService
              .darlehenEingeben$({ darlehenId: updatedDarlehen.id })
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
    data: DarlehenServiceDeleteDarlehenGsRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.deleteDarlehenGs$(data).pipe(
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
    rxMethod<DarlehenServiceGetDarlehenDashboardSbRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            paginatedSbDarlehenDashboard: pending(),
          }));
        }),
        switchMap((req) =>
          this.darlehenService.getDarlehenDashboardSb$(req).pipe(
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
          .getDarlehenSb$({ darlehenId })
          .pipe(
            handleApiResponse(
              (darlehen) => patchState(this, { cachedDarlehen: darlehen }),
              { onFailure },
            ),
          ),
      ),
    ),
  );

  getAllDarlehenSb$ = rxMethod<DarlehenServiceGetAllDarlehenSbRequestParams>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          allDarlehen: pending(),
        }));
      }),
      switchMap((req) =>
        this.darlehenService.getAllDarlehenSb$(req).pipe(
          handleApiResponse((rd) =>
            patchState(this, () => {
              const data: DarlehenGsResponse = {
                darlehenList: rd.data ?? [],
                canCreateDarlehen: false,
              };
              return { allDarlehen: { ...rd, data } };
            }),
          ),
        ),
      ),
    ),
  );

  // SB Methoden
  darlehenUpdateAndFreigebenSb$ = rxMethod<{
    data: DarlehenServiceDarlehenUpdateSbRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.darlehenUpdateSb$(data).pipe(
          switchMap((updatedDarlehen) =>
            this.darlehenService
              .darlehenFreigeben$({ darlehenId: updatedDarlehen.id })
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
    rxMethod<DarlehenServiceDarlehenZurueckweisenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            cachedDarlehen: cachedPending(state.cachedDarlehen),
          }));
        }),
        switchMap((data) =>
          this.darlehenService.darlehenZurueckweisen$(data).pipe(
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
    data: DarlehenServiceDarlehenUpdateSbRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(({ data, onSuccess }) =>
        this.darlehenService.darlehenUpdateSb$(data).pipe(
          switchMap((updatedDarlehen) => {
            const action$ = updatedDarlehen.gewaehren
              ? this.darlehenService.darlehenAkzeptieren$({
                  darlehenId: updatedDarlehen.id,
                })
              : this.darlehenService.darlehenAblehen$({
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
