import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import Fuse from 'fuse.js';
import { of, pipe, switchMap, tap } from 'rxjs';

import { PlzOrtLookup } from '@dv/shared/model/plz-ort-lookup';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

@Injectable({ providedIn: 'root' })
export class PlzService {
  getPlz$() {
    return of([
      { plz: 3000, ort: 'Bern' },
      { plz: 3011, ort: 'Bern' },
      { plz: 3084, ort: 'Wabern' },
      { plz: 3094, ort: 'Schliern b. Köniz' },
      { plz: 8000, ort: 'Zürich' },
      { plz: 8452, ort: 'Adlikon' },
    ]);
  }
}

type PlzOrtState = {
  plz: CachedRemoteData<PlzOrtLookup[]>;
};

const initialState: PlzOrtState = {
  plz: initial(),
};

@Injectable({ providedIn: 'root' })
export class PlzOrtStore extends signalStore(
  withState(initialState),
  withDevtools('PlzStore'),
) {
  private plzService = inject(PlzService);

  plzViewSig = computed(() => {
    const plz = this.plz();
    return {
      plz,
      list: toPlzLookupView(plz.data),
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

const toPlzLookupView = (plzLookups?: PlzOrtLookup[]) => {
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
