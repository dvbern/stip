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
  MassendruckServiceMassendruckDatenschutzbriefVersendenRequestParams,
  MassendruckServiceMassendruckVerfuegungVersendenRequestParams,
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
  isPending,
  pending,
} from '@dv/shared/util/remote-data';

type MassendruckState = {
  createMassendruckJobForQueryTypeReq: RemoteData<MassendruckJob>;
  paginatedMassendruckJobs: CachedRemoteData<PaginatedMassendruckJob>;
  massendruckJobDetail: RemoteData<MassendruckJobDetail>;
  versendenReq: RemoteData<MassendruckDatenschutzbrief | MassendruckVerfuegung>;
};

const initialState: MassendruckState = {
  createMassendruckJobForQueryTypeReq: initial(),
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
      loading: isPending(this.massendruckJobDetail()),
    };
  });

  createMassendruckJobForQueryType$ =
    rxMethod<MassendruckServiceCreateMassendruckJobForQueryTypeRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            createMassendruckJobForQueryTypeReq: pending(),
          }));
        }),
        switchMap((requestParams) =>
          this.massendruckService
            .createMassendruckJobForQueryType$(requestParams)
            .pipe(
              handleApiResponse((massendruckJob) =>
                patchState(this, {
                  createMassendruckJobForQueryTypeReq: massendruckJob,
                }),
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

  massendruckDatenschutzbriefVersenden$ =
    rxMethod<MassendruckServiceMassendruckDatenschutzbriefVersendenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            versendenReq: pending(),
          }));
        }),
        switchMap((requestParams) =>
          this.massendruckService
            .massendruckDatenschutzbriefVersenden$(requestParams)
            .pipe(
              handleApiResponse((datenschutzbrief) =>
                patchState(this, { versendenReq: datenschutzbrief }),
              ),
            ),
        ),
      ),
    );

  massendruckVerfuegungVersenden$ =
    rxMethod<MassendruckServiceMassendruckVerfuegungVersendenRequestParams>(
      pipe(
        tap(() => {
          patchState(this, () => ({
            versendenReq: pending(),
          }));
        }),
        switchMap((requestParams) =>
          this.massendruckService
            .massendruckVerfuegungVersenden$(requestParams)
            .pipe(
              handleApiResponse((verfuegung) =>
                patchState(this, { versendenReq: verfuegung }),
              ),
            ),
        ),
      ),
    );
}
