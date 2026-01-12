import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import {
  Berechnungsresultat,
  GesuchService,
  TranchenBerechnungsresultat,
} from '@dv/shared/model/gesuch';
import {
  TeilberechnungsArt,
  VerminderteBerechnung,
} from '@dv/shared/model/verfuegung';
import {
  CachedRemoteData,
  cachedPending,
  fromCachedDataSig,
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
) {
  private gesuchService = inject(GesuchService);

  /**
   * Transforms the raw berechnung data into a view model grouped by tranche ID.
   *
   * This computed signal:
   * - Groups `tranchenBerechnungsresultate` by `gesuchTrancheId`
   * - Handles split calculations (type 'a' and 'b') when a tranche has multiple results
   * - Includes reduced calculation details (`verminderteBerechnung`) if applicable
   *
   * @returns An object containing:
   * - `loading`: Boolean indicating if the data is pending
   * - `year`: The calculation year
   * - `berechnung`: The total calculation amount
   * - `berechnungsresultate`: Array of arrays of `TranchenBerechnungsresultat`, grouped by tranche ID and split type
   * - `verminderteBerechnung`: Optional reduced calculation details (months, reduction amount, reduced calculation)
   */
  berechnungZusammenfassungViewSig = computed(() => {
    const berechnungRd = this.berechnung();

    const value: {
      year: number;
      berechnung: number;
      berechnungsresultate: Record<string, TranchenBerechnungsresultat[]>;
      verminderteBerechnung?: VerminderteBerechnung;
    } = {
      year: berechnungRd.data?.year ?? 0,
      berechnung: 0,
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
      byTrancheId.berechnung = berechnungRd.data?.berechnung;
      const verminderteBerechnungMonate =
        berechnungRd.data?.verminderteBerechnungMonate;
      if (verminderteBerechnungMonate) {
        const berechnungReduziert = berechnungRd.data?.berechnungReduziert ?? 0;
        byTrancheId.verminderteBerechnung = {
          monate: 12 - verminderteBerechnungMonate,
          reduktionsBetrag: -(byTrancheId.berechnung - berechnungReduziert),
          berechnungReduziert,
        };
      }
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

  berechnungsViewSig = computed(() => {
    return {
      loading: isPending(this.berechnung()),
      berechnungen: fromCachedDataSig(this.berechnung),
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
