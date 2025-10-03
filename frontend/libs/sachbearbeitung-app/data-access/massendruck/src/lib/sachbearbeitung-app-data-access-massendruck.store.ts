import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  MassendruckDatenschutzbrief,
  MassendruckJob,
  MassendruckJobDetail,
  MassendruckService,
  MassendruckServiceCreateMassendruckJobForQueryTypeRequestParams,
  MassendruckServiceGetAllMassendruckJobsRequestParams,
  MassendruckServiceGetMassendruckJobDetailRequestParams,
  MassendruckVerfuegung,
  PaginatedMassendruckJob,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isFailure,
  isPending,
  mapData,
  pending,
} from '@dv/shared/util/remote-data';

type MassendruckState = {
  createMassendruckJobForQueryTypeReq: RemoteData<MassendruckJob>;
  cancelMassendruckJobReq: RemoteData<unknown>;
  paginatedMassendruckJobs: CachedRemoteData<PaginatedMassendruckJob>;
  massendruckJobDetail: RemoteData<MassendruckJobDetail>;
  versendenReq: RemoteData<MassendruckDatenschutzbrief | MassendruckVerfuegung>;
};

const initialState: MassendruckState = {
  createMassendruckJobForQueryTypeReq: initial(),
  cancelMassendruckJobReq: initial(),
  paginatedMassendruckJobs: initial(),
  massendruckJobDetail: initial(),
  versendenReq: initial(),
};

@Injectable()
export class MassendruckStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private massendruckService = inject(MassendruckService);

  paginatedMassendruckListViewSig = computed(() => {
    return {
      paginatedMassendruckJobs: fromCachedDataSig(
        this.paginatedMassendruckJobs,
      ),
      loading: isPending(this.paginatedMassendruckJobs()),
    };
  });

  massendruckViewSig = computed(() => {
    return {
      massendruckJob: fromCachedDataSig(this.massendruckJobDetail),
      areAllSent:
        mapData(this.massendruckJobDetail(), (detail) =>
          [
            ...(detail?.datenschutzbriefMassendrucks ?? []),
            ...(detail?.verfuegungMassendrucks ?? []),
          ].every((item) => item.isVersendet),
        )?.data ?? null,
      loading: isPending(this.massendruckJobDetail()),
    };
  });

  versendenReqViewSig = computed(() => {
    return {
      loading: isPending(this.versendenReq()),
    };
  });

  createMassendruckLoadingSig = computed(() => {
    return isPending(this.createMassendruckJobForQueryTypeReq());
  });

  createMassendruckJobForQueryType$ = rxMethod<{
    req: MassendruckServiceCreateMassendruckJobForQueryTypeRequestParams;
    onSuccess: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          createMassendruckJobForQueryTypeReq: pending(),
        }));
      }),
      switchMap(({ req, onSuccess }) =>
        this.massendruckService.createMassendruckJobForQueryType$(req).pipe(
          handleApiResponse(
            (massendruckJob) =>
              patchState(this, {
                createMassendruckJobForQueryTypeReq: massendruckJob,
              }),
            {
              onSuccess,
            },
          ),
        ),
      ),
    ),
  );

  loadPaginatedMassendruckJobs$ =
    rxMethod<MassendruckServiceGetAllMassendruckJobsRequestParams>(
      pipe(
        tap(() => {
          patchState(this, (state) => ({
            paginatedMassendruckJobs: cachedPending(
              state.paginatedMassendruckJobs,
            ),
          }));
        }),
        switchMap((requestParams) =>
          this.massendruckService
            .getAllMassendruckJobs$(requestParams)
            .pipe(
              handleApiResponse((paginatedMassendruckJobs) =>
                patchState(this, { paginatedMassendruckJobs }),
              ),
            ),
        ),
      ),
    );

  loadMassendruckJob$ =
    rxMethod<MassendruckServiceGetMassendruckJobDetailRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            massendruckJobDetail: pending(),
          }));
        }),
        switchMap((requestParams) =>
          this.massendruckService
            .getMassendruckJobDetail$(requestParams)
            .pipe(
              handleApiResponse((massendruckJobDetail) =>
                patchState(this, { massendruckJobDetail }),
              ),
            ),
        ),
      ),
    );

  massendruckDatenschutzbriefVersenden$ = rxMethod<{
    massendruckDatenschutzbriefId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          versendenReq: pending(),
        }));
      }),
      switchMap(({ massendruckDatenschutzbriefId }) =>
        this.massendruckService
          .massendruckDatenschutzbriefVersenden$({
            massendruckDatenschutzbriefId,
          })
          .pipe(
            handleApiResponse((datenschutzbriefResult) =>
              patchState(this, (state) => ({
                versendenReq: datenschutzbriefResult,
                massendruckJobDetail: mapData(
                  state.massendruckJobDetail,
                  (detail) => {
                    if (
                      !detail.datenschutzbriefMassendrucks ||
                      isFailure(datenschutzbriefResult)
                    ) {
                      return detail;
                    }

                    return {
                      ...detail,
                      datenschutzbriefMassendrucks:
                        detail.datenschutzbriefMassendrucks.map((b) => {
                          if (b.id === datenschutzbriefResult.data.id) {
                            return {
                              ...b,
                              ...datenschutzbriefResult.data,
                            };
                          }

                          return b;
                        }),
                    };
                  },
                ),
              })),
            ),
          ),
      ),
    ),
  );

  massendruckVerfuegungVersenden$ = rxMethod<{
    massendruckVerfuegungId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          versendenReq: pending(),
        }));
      }),
      switchMap(({ massendruckVerfuegungId }) =>
        this.massendruckService
          .massendruckVerfuegungVersenden$({ massendruckVerfuegungId })
          .pipe(
            handleApiResponse((verfuegung) =>
              patchState(this, (state) => ({
                versendenReq: verfuegung,
                massendruckJobDetail: mapData(
                  state.massendruckJobDetail,
                  (detail) => {
                    if (
                      !detail.verfuegungMassendrucks ||
                      isFailure(verfuegung)
                    ) {
                      return detail;
                    }

                    return {
                      ...detail,
                      verfuegungMassendrucks: detail.verfuegungMassendrucks.map(
                        (v) => {
                          if (v.id === verfuegung.data.id) {
                            return {
                              ...v,
                              ...verfuegung.data,
                            };
                          }

                          return v;
                        },
                      ),
                    };
                  },
                ),
              })),
            ),
          ),
      ),
    ),
  );

  retryMassendruckJob$ = rxMethod<{
    massendruckJobId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          massendruckJobDetail: pending(),
        }));
      }),
      switchMap(({ massendruckJobId }) =>
        this.massendruckService
          .retryMassendruckJob$({
            massendruckId: massendruckJobId,
          })
          .pipe(
            handleApiResponse((massendruckJobDetail) =>
              patchState(this, { massendruckJobDetail }),
            ),
          ),
      ),
    ),
  );

  cancelMassendruckJob$ = rxMethod<{
    massendruckJobId: string;
    onSuccess?: () => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, () => ({
          cancelMassendruckJobReq: pending(),
        }));
      }),
      switchMap(({ massendruckJobId, onSuccess }) =>
        this.massendruckService
          .deleteMassendruckJob$({ massendruckId: massendruckJobId })
          .pipe(
            handleApiResponse(
              (cancelMassendruckJobReq) =>
                patchState(this, { cancelMassendruckJobReq }),
              {
                onSuccess,
              },
            ),
          ),
      ),
    ),
  );
}
