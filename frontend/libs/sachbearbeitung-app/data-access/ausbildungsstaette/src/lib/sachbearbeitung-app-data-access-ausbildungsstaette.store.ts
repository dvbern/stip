import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import {
  Abschluss,
  AusbildungsstaetteService,
  AusbildungsstaetteServiceCreateAbschlussBrueckenangebotRequestParams,
  AusbildungsstaetteServiceGetAllAbschlussForUebersichtRequestParams,
  AusbildungsstaetteServiceGetAllAusbildungsgangForUebersichtRequestParams,
  PaginatedAbschluss,
  PaginatedAusbildungsgang,
} from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  RemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type AusbildungsstaetteState = {
  lastCreate: RemoteData<unknown>;
  abschluesse: CachedRemoteData<PaginatedAbschluss>;
  ausbildungsgaenge: CachedRemoteData<PaginatedAusbildungsgang>;
};

const initialState: AusbildungsstaetteState = {
  lastCreate: initial(),
  abschluesse: initial(),
  ausbildungsgaenge: initial(),
};

@Injectable()
export class AusbildungsstaetteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungsstaetteService = inject(AusbildungsstaetteService);

  abschluesseViewSig = computed(() => {
    return this.abschluesse.data();
  });
  ausbildungsgaengeViewSig = computed(() => {
    return this.ausbildungsgaenge.data();
  });

  loadAbschluesse$ = rxMethod<{
    filter: AusbildungsstaetteServiceGetAllAbschlussForUebersichtRequestParams;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          abschluesse: cachedPending(state.abschluesse),
        }));
      }),
      switchMap(({ filter }) =>
        this.ausbildungsstaetteService
          .getAllAbschlussForUebersicht$(filter)
          .pipe(
            handleApiResponse((abschluesse) =>
              patchState(this, { abschluesse }),
            ),
          ),
      ),
    ),
  );

  loadAusbildungsgaenge$ = rxMethod<{
    filter: AusbildungsstaetteServiceGetAllAusbildungsgangForUebersichtRequestParams;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungsgaenge: cachedPending(state.ausbildungsgaenge),
        }));
      }),
      switchMap(({ filter }) =>
        this.ausbildungsstaetteService
          .getAllAusbildungsgangForUebersicht$(filter)
          .pipe(
            handleApiResponse((ausbildungsgaenge) =>
              patchState(this, { ausbildungsgaenge }),
            ),
          ),
      ),
    ),
  );

  createAbschluss$ = rxMethod<{
    values: AusbildungsstaetteServiceCreateAbschlussBrueckenangebotRequestParams;
    onSuccess?: (abschluss: Abschluss) => void;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          abschluesse: cachedPending(state.abschluesse),
        }));
      }),
      switchMap(({ values, onSuccess }) =>
        this.ausbildungsstaetteService
          .createAbschlussBrueckenangebot$(values)
          .pipe(
            handleApiResponse(
              (abschluss) => {
                patchState(this, { lastCreate: abschluss });
              },
              {
                onSuccess,
              },
            ),
          ),
      ),
    ),
  );
}
