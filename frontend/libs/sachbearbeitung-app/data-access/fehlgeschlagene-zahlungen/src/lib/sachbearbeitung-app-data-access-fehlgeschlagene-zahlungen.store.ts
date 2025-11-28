import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import {
  BuchhaltungService,
  PaginatedFailedAuszahlungBuchhaltung,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type FehlgeschlageneZahlungenState = {
  paginatedFailedAuszahlungBuchhaltung: CachedRemoteData<PaginatedFailedAuszahlungBuchhaltung>;
};

const initialState: FehlgeschlageneZahlungenState = {
  paginatedFailedAuszahlungBuchhaltung: initial(),
};

@Injectable({ providedIn: 'root' })
export class FehlgeschlageneZahlungenStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private buchhaltungService = inject(BuchhaltungService);
  fehlgeschlageneZahlungenView = computed(() => {
    return {
      data: fromCachedDataSig(this.paginatedFailedAuszahlungBuchhaltung),
      loading: isPending(this.paginatedFailedAuszahlungBuchhaltung()),
    };
  });

  hasFehlgeschalgeneZahlungenSig = computed(() => {
    const data = fromCachedDataSig(this.paginatedFailedAuszahlungBuchhaltung);
    return (data?.totalEntries ?? 0) > 0;
  });

  getFehlgeschlageneZahlungen$ = rxMethod<{
    page: number;
    pageSize: number;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          paginatedFailedAuszahlungBuchhaltung: cachedPending(
            state.paginatedFailedAuszahlungBuchhaltung,
          ),
        }));
      }),
      exhaustMap(({ page, pageSize }) =>
        this.buchhaltungService
          .getFailedAuszahlungBuchhaltungEntrys$({
            page,
            pageSize,
          })
          .pipe(
            handleApiResponse((paginatedFailedAuszahlungBuchhaltung) =>
              patchState(this, { paginatedFailedAuszahlungBuchhaltung }),
            ),
          ),
      ),
    ),
  );
}
