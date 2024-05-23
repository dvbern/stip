import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import Fuse from 'fuse.js';
import { EMPTY, pipe, switchMap, tap } from 'rxjs';

import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

// TODO: Replace placholder types and service with OpenAPI generated types and service
type PlzLookup = {
  plz: number;
  ort: string;
};
export class PlzService {
  getPlz$() {
    return EMPTY;
  }
}

type PlzState = {
  plz: CachedRemoteData<PlzLookup[]>;
};

const initialState: PlzState = {
  plz: initial(),
};

@Injectable()
export class PlzStore extends signalStore(
  withState(initialState),
  withDevtools('PlzStore'),
) {
  private plzService = inject(PlzService);

  plzViewSig = computed(() => {
    const plz = this.plz();
    return {
      ...plz,
      data: toPlzLookupView(plz.data),
    };
  });

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

const toPlzLookupView = (plzLookups?: PlzLookup[]) => {
  if (!plzLookups) return undefined;
  return {
    rawList: plzLookups,
    fuzzyPlz: new Fuse(plzLookups, {
      keys: ['plz'],
      location: 0,
      includeScore: true,
      includeMatches: true,
      threshold: 0.3,
    }),
  };
};
