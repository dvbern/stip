import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import { Berechnungsresultat, GesuchService } from '@dv/shared/model/gesuch';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
} from '@dv/shared/util/remote-data';

type BerechnungState = {
  berechnungen: CachedRemoteData<Berechnungsresultat[]>;
};

const initialState: BerechnungState = {
  berechnungen: initial(),
};

@Injectable()
export class BerechnungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('BerechnungStore'),
) {
  private gesuchService = inject(GesuchService);

  berechnungZusammenfassungViewSig = computed(() => {
    const value = { totalBetragStipendium: 0, berechnungsresultate: [] };
    const data = this.berechnungen.data();

    return data
      ? data.reduce<{
          totalBetragStipendium: number;
          berechnungsresultate: Berechnungsresultat[];
        }>((acc, curr) => {
          return {
            totalBetragStipendium: acc.totalBetragStipendium + curr.berechnung,
            berechnungsresultate: [...acc.berechnungsresultate, curr],
          };
        }, value)
      : value;
  });

  getBerechnungForGesuch$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          berechnungen: cachedPending(state.berechnungen),
        }));
      }),
      exhaustMap(({ gesuchId }) =>
        this.gesuchService
          .getBerechnungForGesuch$({ gesuchId })
          .pipe(
            handleApiResponse((berechnung) =>
              patchState(this, { berechnungen: berechnung }),
            ),
          ),
      ),
    ),
  );
}