import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  MassendruckJob,
  MassendruckService,
  MassendruckServiceGetAllMassendruckJobsRequestParams,
  MassendruckServiceGetMassendruckJobDetailRequestParams,
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
  paginatedMassendruckJobs: CachedRemoteData<PaginatedMassendruckJob>;
  massendruckJob: RemoteData<MassendruckJob>;
};

const initialState: MassendruckState = {
  paginatedMassendruckJobs: initial(),
  massendruckJob: initial(),
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
      massendruckJob: this.massendruckJob(),
      loading: isPending(this.massendruckJob()),
    };
  });

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
            massendruckJob: pending(),
          }));
        }),
        switchMap((requestParams) =>
          this.massendruckService
            .getMassendruckJobDetail$(requestParams)
            .pipe(
              handleApiResponse((massendruckJob) =>
                patchState(this, { massendruckJob }),
              ),
            ),
        ),
      ),
    );
}
