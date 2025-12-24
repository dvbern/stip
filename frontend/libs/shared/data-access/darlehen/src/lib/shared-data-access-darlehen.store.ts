import { Injectable, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, pipe, switchMap, tap } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import {
  Darlehen,
  DarlehenService,
  DarlehenServiceCreateDarlehenRequestParams,
  DarlehenServiceDarlehenAblehenRequestParams,
  DarlehenServiceDarlehenAkzeptierenRequestParams,
  DarlehenServiceDarlehenEingebenRequestParams,
  DarlehenServiceDarlehenFreigebenRequestParams,
  DarlehenServiceDarlehenUpdateGsRequestParams,
  DarlehenServiceDarlehenUpdateSbRequestParams,
  DarlehenServiceDarlehenZurueckweisenRequestParams,
  DarlehenServiceDeleteDarlehenGsRequestParams,
  DarlehenServiceGetAllDarlehenGsRequestParams,
  DarlehenServiceGetAllDarlehenSbRequestParams,
  DarlehenServiceGetDarlehenDashboardSbRequestParams,
  DarlehenServiceGetDarlehenGsRequestParams,
  DarlehenServiceGetDarlehenSbRequestParams,
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
  darlehenList: CachedRemoteData<Darlehen[]>;
  paginatedSbDarlehenDashboard: CachedRemoteData<PaginatedSbDarlehenDashboard>;
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

  setDarlehen(rd: CachedRemoteData<Darlehen>) {
    patchState(this, { cachedDarlehen: rd });
  }

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

  getDarlehenGs$ = rxMethod<DarlehenServiceGetDarlehenGsRequestParams>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          cachedDarlehen: pending(),
        }));
      }),
      switchMap((req) =>
        this.darlehenService
          .getDarlehenGs$(req)
          .pipe(
            handleApiResponse((darlehen) =>
              patchState(this, { cachedDarlehen: darlehen }),
            ),
          ),
      ),
    ),
  );

  getAllDarlehenGs$ = rxMethod<DarlehenServiceGetAllDarlehenGsRequestParams>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          darlehenList: pending(),
        }));
      }),
      switchMap((req) =>
        this.darlehenService
          .getAllDarlehenGs$(req)
          .pipe(
            handleApiResponse((darlehen) =>
              patchState(this, { darlehenList: darlehen }),
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
                this.router.navigate(['/darlehen', data.id]);
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

  darlehenEingeben$ = rxMethod<DarlehenServiceDarlehenEingebenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((req) =>
        this.darlehenService.darlehenEingeben$(req).pipe(
          handleApiResponse(
            (darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
            },
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.form.darlehen.eingeben.success',
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

  getDarlehenSb$ = rxMethod<DarlehenServiceGetDarlehenSbRequestParams>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          cachedDarlehen: pending(),
        }));
      }),
      switchMap((req) =>
        this.darlehenService
          .getDarlehenSb$(req)
          .pipe(
            handleApiResponse((darlehen) =>
              patchState(this, { cachedDarlehen: darlehen }),
            ),
          ),
      ),
    ),
  );

  getAllDarlehenSb$ = rxMethod<DarlehenServiceGetAllDarlehenSbRequestParams>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          darlehenList: pending(),
        }));
      }),
      switchMap((req) =>
        this.darlehenService
          .getAllDarlehenSb$(req)
          .pipe(
            handleApiResponse((darlehen) =>
              patchState(this, { darlehenList: darlehen }),
            ),
          ),
      ),
    ),
  );

  darlehenUpdateSb$ = rxMethod<{
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
          handleApiResponse(
            (darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
            },
            {
              onSuccess: () => {
                onSuccess();
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.form.darlehen.update.success',
                });
              },
            },
          ),
        ),
      ),
    ),
  );

  darlehenFreigeben$ = rxMethod<DarlehenServiceDarlehenFreigebenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((data) =>
        this.darlehenService.darlehenFreigeben$(data).pipe(
          handleApiResponse(
            (darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
            },
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.form.darlehen.freigeben.success',
                });
              },
            },
          ),
        ),
      ),
    ),
  );

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
          ),
        ),
      ),
    );

  darlehenUpdateAndZurueckweisenSb$ = rxMethod<{
    data: DarlehenServiceDarlehenUpdateSbRequestParams;
    kommentar: { text: string };
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap(({ data, kommentar, onSuccess }) =>
        this.darlehenService.darlehenUpdateSb$(data).pipe(
          switchMap((updatedDarlehen) =>
            this.darlehenService
              .darlehenZurueckweisen$({
                darlehenId: updatedDarlehen.id,
                kommentar,
              })
              .pipe(
                handleApiResponse(
                  (darlehen) => {
                    patchState(this, { cachedDarlehen: darlehen });
                  },
                  {
                    onSuccess: () => {
                      onSuccess();
                      this.globalNotificationStore.createSuccessNotification({
                        messageKey:
                          'shared.form.darlehen.zurueckweisen.success',
                      });
                    },
                  },
                ),
              ),
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

  darlehenAkzeptieren$ =
    rxMethod<DarlehenServiceDarlehenAkzeptierenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            cachedDarlehen: cachedPending(state.cachedDarlehen),
          }));
        }),
        switchMap((data) =>
          this.darlehenService.darlehenAkzeptieren$(data).pipe(
            handleApiResponse(
              (darlehen) => {
                patchState(this, { cachedDarlehen: darlehen });
              },
              {
                onSuccess: () => {
                  this.globalNotificationStore.createSuccessNotification({
                    messageKey: 'shared.form.darlehen.abschliessen.success',
                  });
                },
              },
            ),
          ),
        ),
      ),
    );

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

  darlehenAblehnen$ = rxMethod<DarlehenServiceDarlehenAblehenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((data) =>
        this.darlehenService.darlehenAblehen$(data).pipe(
          handleApiResponse(
            (darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
            },
            {
              onSuccess: () => {
                this.globalNotificationStore.createSuccessNotification({
                  messageKey: 'shared.form.darlehen.abschliessen.success',
                });
              },
            },
          ),
        ),
      ),
    ),
  );

  darlehenUpdateAndAblehnenSb$ = rxMethod<{
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
              .darlehenAblehen$({ darlehenId: updatedDarlehen.id })
              .pipe(
                handleApiResponse(
                  (darlehen) => {
                    patchState(this, { cachedDarlehen: darlehen });
                  },
                  {
                    onSuccess: () => {
                      onSuccess();
                      this.globalNotificationStore.createSuccessNotification({
                        messageKey: 'shared.form.darlehen.ablehnen.success',
                      });
                    },
                  },
                ),
              ),
          ),
          catchError(() => {
            this.globalNotificationStore.createNotification({
              type: 'ERROR',
              messageKey: 'shared.form.darlehen.ablehnen.failure',
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
