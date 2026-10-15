import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { pipe, switchMap, tap } from 'rxjs';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  AbschlussSlim,
  AusbildungsstaetteService,
  AusbildungsstaetteSlim,
} from '@dv/shared/model/gesuch';
import { byAppConfig } from '@dv/shared/model/permission-state';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  mapCachedData,
} from '@dv/shared/util/remote-data';

type AusbildungsstaetteState = {
  ausbildungsstaetten: CachedRemoteData<AusbildungsstaetteSlim[]>;
  abschluesse: CachedRemoteData<AbschlussSlim[]>;
};

const initialState: AusbildungsstaetteState = {
  ausbildungsstaetten: initial(),
  abschluesse: initial(),
};

/**
 * Store for AusbildungsstaetteSlim data
 */
@Injectable()
export class AusbildungsstaetteStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  private ausbildungsstaetteService = inject(AusbildungsstaetteService);
  private config = inject(SharedModelCompileTimeConfig);

  ausbildungsstaetteViewSig = computed(() =>
    mapCachedData(
      this.ausbildungsstaetten(),
      (rd) =>
        rd.map((ausbildungsstaette) => ({
          ...ausbildungsstaette,
          testId: ausbildungsstaette.nameDe,
          // Invalid is set by the component based on depending values
          disabled: !ausbildungsstaette.aktiv,
          displayValueDe: ausbildungsstaette.nameDe,
          displayValueFr: ausbildungsstaette.nameFr,
        })) ?? [],
    ),
  );
  ausbildungsstaettenWithAusbildungsgaengeViewSig = computed(() =>
    mapCachedData(this.ausbildungsstaetteViewSig(), (ausbildungsstaette) =>
      ausbildungsstaette.filter((a) => a.ausbildungsgaenge.length > 0),
    ),
  );

  abschluesseViewSig = computed(() =>
    mapCachedData(
      this.abschluesse(),
      (abschluesse) =>
        abschluesse.map((abschluss) => ({
          ...abschluss,
          testId: abschluss.bezeichnungDe,
          invalid: !abschluss.aktiv,
          disabled: !abschluss.aktiv,
          displayValueDe: abschluss.bezeichnungDe,
          displayValueFr: abschluss.bezeichnungFr,
        })) ?? [],
    ),
  );

  loadAusbildungsstaetten$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          ausbildungsstaetten: cachedPending(state.ausbildungsstaetten),
        }));
      }),
      switchMap(() =>
        this.ausbildungsstaetteService
          .getAllAusbildungsstaetteForAuswahl$()
          .pipe(
            handleApiResponse((ausbildungsstaetten) =>
              patchState(this, { ausbildungsstaetten }),
            ),
          ),
      ),
    ),
  );

  loadAbschluesse$ = rxMethod<void>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          abschluesse: cachedPending(state.abschluesse),
        }));
      }),
      switchMap(() =>
        byAppConfig(this.config.app, {
          gesuchsteller: () =>
            this.ausbildungsstaetteService.getAllAbschluessForAuswahlGs$(),
          sachbearbeiter: () =>
            this.ausbildungsstaetteService.getAllAbschluessForAuswahlSb$(),
        }).pipe(
          handleApiResponse((abschluesse) => patchState(this, { abschluesse })),
        ),
      ),
    ),
  );
}
