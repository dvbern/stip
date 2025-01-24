import { Injectable, computed, inject } from '@angular/core';
import { withDevtools } from '@angular-architects/ngrx-toolkit';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import {
  Berechnungsresultat,
  GesuchService,
  TranchenBerechnungsresultat,
} from '@dv/shared/model/gesuch';
import { TeilberechnungsArt } from '@dv/shared/model/verfuegung';
import {
  CachedRemoteData,
  cachedPending,
  handleApiResponse,
  initial,
  isPending,
} from '@dv/shared/util/remote-data';

type BerechnungState = {
  berechnung: CachedRemoteData<Berechnungsresultat>;
};

const initialState: BerechnungState = {
  berechnung: initial(),
};

@Injectable()
export class BerechnungStore extends signalStore(
  { protectedState: false },
  withState(initialState),
  withDevtools('BerechnungStore'),
) {
  private gesuchService = inject(GesuchService);

  berechnungZusammenfassungViewSig = computed(() => {
    const berechnungRd = this.berechnung();
    const value: {
      year: number;
      totalBetragStipendium: number;
      berechnungsresultate: Record<string, TranchenBerechnungsresultat[]>;
      verminderteBerechnungMonate?: number;
    } = {
      year: berechnungRd.data?.year ?? 0,
      totalBetragStipendium: 0,
      berechnungsresultate: {},
    };

    const byTrancheId = berechnungRd.data
      ? berechnungRd.data.tranchenBerechnungsresultate.reduce((acc, curr) => {
          if (!acc.berechnungsresultate[curr.gesuchTrancheId]) {
            acc.berechnungsresultate[curr.gesuchTrancheId] = [];
          }
          acc.berechnungsresultate[curr.gesuchTrancheId].push(curr);
          return acc;
        }, value)
      : value;

    if (berechnungRd.data) {
      byTrancheId.totalBetragStipendium = berechnungRd.data?.berechnung;
      byTrancheId.verminderteBerechnungMonate =
        berechnungRd.data?.verminderteBerechnungMonate;
    }

    return {
      loading: isPending(berechnungRd),
      ...byTrancheId,
      berechnungsresultate: Object.values(byTrancheId.berechnungsresultate).map(
        (r) =>
          r.map((b, index) => ({
            ...b,
            type:
              r.length > 1
                ? // It should only be possible to split a berechnung into two parts, a and b
                  ('ab'.charAt(index % 2) as TeilberechnungsArt)
                : '',
          })),
      ),
    };
  });

  getBerechnungForGesuch$ = rxMethod<{ gesuchId: string }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          berechnung: cachedPending(state.berechnung),
        }));
      }),
      exhaustMap(({ gesuchId }) =>
        this.gesuchService
          .getBerechnungForGesuch$({ gesuchId })
          .pipe(
            handleApiResponse((berechnung) => patchState(this, { berechnung })),
          ),
      ),
    ),
  );
}
