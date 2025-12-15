import { Injectable, computed, inject, untracked } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { EMPTY, catchError, pipe, switchMap, tap } from 'rxjs';

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
  DarlehenServiceGetAllDarlehenSbRequestParams,
  DarlehenServiceGetDarlehenDashboardSbRequestParams,
  DarlehenServiceGetDarlehenGsRequestParams,
  DarlehenServiceGetDarlehenSbRequestParams,
  PaginatedSbDarlehenDashboard,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  cachedResult,
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

  createDarlehen$ = rxMethod<DarlehenServiceCreateDarlehenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((req) =>
        this.darlehenService.createDarlehen$(req).pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );

  // darlehenUpdateGs$ = rxMethod<DarlehenServiceDarlehenUpdateGsRequestParams>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         cachedDarlehen: cachedPending(state.cachedDarlehen),
  //       }));
  //     }),
  //     switchMap((data) =>
  //       this.darlehenService.darlehenUpdateGs$(data).pipe(
  //         handleApiResponse((darlehen) => {
  //           patchState(this, { cachedDarlehen: darlehen });
  //         }),
  //       ),
  //     ),
  //   ),
  // );

  // darlehenEingeben$ = rxMethod<DarlehenServiceDarlehenEingebenRequestParams>(
  //   pipe(
  //     tap(() => {
  //       patchState(this, (state) => ({
  //         cachedDarlehen: cachedPending(state.cachedDarlehen),
  //       }));
  //     }),
  //     switchMap((data) =>
  //       this.darlehenService.darlehenEingeben$(data).pipe(
  //         handleApiResponse((darlehen) => {
  //           patchState(this, { cachedDarlehen: darlehen });
  //         }),
  //       ),
  //     ),
  //   ),
  // );

  darlehenUpdateAndEingeben$ = rxMethod<{
    data: DarlehenServiceDarlehenUpdateGsRequestParams &
      DarlehenServiceDarlehenEingebenRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      // todo: is the outer error handling sufficient here?
      switchMap(({ data, onSuccess }) =>
        this.darlehenService
          .darlehenUpdateGs$({
            darlehenId: data.darlehenId,
            darlehenUpdateGs: data.darlehenUpdateGs,
          })
          .pipe(
            switchMap(() =>
              this.darlehenService.darlehenEingeben$({
                darlehenId: data.darlehenId,
              }),
            ),
            handleApiResponse(
              (darlehen) => {
                patchState(this, {
                  cachedDarlehen: cachedResult(
                    untracked(this.cachedDarlehen),
                    darlehen,
                  ),
                });
              },
              { onSuccess },
            ),
            catchError((error) => {
              console.error('Error in darlehenUpdateAndEingeben$', error);
              return EMPTY;
            }),
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
            { onSuccess },
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
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
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
            handleApiResponse((darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
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
            handleApiResponse((darlehen) => {
              patchState(this, { cachedDarlehen: darlehen });
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
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
          }),
        ),
      ),
    ),
  );
}
