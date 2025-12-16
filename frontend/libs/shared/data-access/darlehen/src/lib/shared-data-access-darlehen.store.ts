import { Injectable, Signal, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { SharedModelGsDashboardView } from '@dv/shared/model/ausbildung';
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

// todo: improve this check, maybe move to helper or backend
export const canCreateDarlehenFn = (
  dashBoardSig: Signal<SharedModelGsDashboardView | undefined>,
  darlehenListSig: Signal<Darlehen[] | undefined>,
): Signal<boolean> => {
  return computed(() => {
    const dashboardView = dashBoardSig();
    const darlehenList = darlehenListSig();
    if (!dashboardView || !darlehenList) {
      return false;
    }

    const hasActiveAusbildungWithGesuchNotInBearbeitung =
      dashboardView.activeAusbildungen.some((ausbildung) =>
        ausbildung.gesuchs.some(
          (gesuch) => gesuch.gesuchStatus !== 'IN_BEARBEITUNG_GS',
        ),
      );

    const hasNoDarlehen = !darlehenList.some((darlehen) => {
      return (
        darlehen.status === 'IN_BEARBEITUNG_GS' ||
        darlehen.status === 'EINGEGEBEN' ||
        darlehen.status === 'IN_FREIGABE'
      );
    });

    return hasActiveAusbildungWithGesuchNotInBearbeitung && hasNoDarlehen;
  });
};

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

  darlehenUpdateGs$ = rxMethod<{
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

  darlehenEingeben$ = rxMethod<DarlehenServiceDarlehenEingebenRequestParams>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          cachedDarlehen: cachedPending(state.cachedDarlehen),
        }));
      }),
      switchMap((req) =>
        this.darlehenService.darlehenEingeben$(req).pipe(
          handleApiResponse((darlehen) => {
            patchState(this, { cachedDarlehen: darlehen });
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
