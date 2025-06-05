import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import Fuse, { FuseSortFunctionArg } from 'fuse.js';
import { pipe, switchMap, tap } from 'rxjs';

import { Plz, PlzService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type PlzOrtState = {
  plz: CachedRemoteData<Plz[]>;
};

const initialState: PlzOrtState = {
  plz: initial(),
};

@Injectable({ providedIn: 'root' })
export class PlzOrtStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private plzService = inject(PlzService);

  plzViewSig = computed(() => {
    const plz = this.plz();
    return {
      plz,
      list: toPlzLookupView(plz.data),
    };
  });

  getKantonByPlz(plz: string | undefined) {
    if (!plz) return '';
    if (plz.length < 4) return '';

    return this.plz().data?.find((p) => p.plz === plz)?.kantonskuerzel ?? '';
  }

  loadAllPlz$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          plz: cachedPending(state.plz),
        }));
      }),
      switchMap(() =>
        this.plzService
          .getPlz$()
          .pipe(handleApiResponse((plz) => patchState(this, { plz }))),
      ),
    ),
  );
}

const toPlzLookupView = (plzLookups?: Plz[]) => {
  if (!plzLookups) return undefined;
  return {
    rawList: plzLookups,
    fuzzyPlz: new Fuse(plzLookups, {
      keys: ['plz'],
      location: 0,
      threshold: 0,
      includeScore: true,
      includeMatches: true,
      shouldSort: true,
      sortFn: (a, b) => {
        return getComparisonValue(a) - getComparisonValue(b);
      },
    }),
  };
};

const getComparisonValue = (value: FuseSortFunctionArg) => {
  return +(value.matches?.[0].value ?? value.score);
};
