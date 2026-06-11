import { Injectable, computed, inject } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { exhaustMap, pipe, tap } from 'rxjs';

import {
  BerechnungsStammdaten,
  Berechnungsresultat,
  GesuchService,
} from '@dv/shared/model/gesuch';
import { TranchenBerechnungsresultatView } from '@dv/shared/model/verfuegung';
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
   * - `berechnungStipendium`: Total betrag Stipendium
   * - `berechnungDarlehen`: Total betrag Darlehen
   * - `berechnungsresultate`: Array of arrays of `TranchenBerechnungsresultat`, grouped by tranche ID and split type
   */
  berechnungZusammenfassungViewSig = computed(() => {
    const berechnungRd = this.berechnung();

    const value: {
      year: number;
      berechnungVorTeilungDarlehen?: number;
      berechnungVorKuerzungUndTeilung: number;
      totalNachKuerzungNachEinreichefrist?: number;
      anzahlMonateEinreichefrist?: number;
      totalNachKuerzungUnterbruch?: number;
      anzahlMonateUnterbruch?: number;
      berechnungStipendium: number;
      berechnungDarlehen?: number;
      berechnungsresultate: Record<string, TranchenBerechnungsresultatView>;
      stammdaten?: BerechnungsStammdaten;
    } = {
      year: berechnungRd.data?.year ?? 0,
      berechnungVorTeilungDarlehen:
        berechnungRd.data?.berechnungVorTeilungDarlehen,
      berechnungVorKuerzungUndTeilung:
        berechnungRd.data?.berechnungVorKuerzungUndTeilung ?? 0,
      totalNachKuerzungNachEinreichefrist:
        berechnungRd.data?.totalNachKuerzungNachEinreichefrist,
      anzahlMonateEinreichefrist: berechnungRd.data?.anzahlMonateEinreichefrist,
      totalNachKuerzungUnterbruch:
        berechnungRd.data?.totalNachKuerzungUnterbruch,
      anzahlMonateUnterbruch: berechnungRd.data?.anzahlMonateUnterbruch,
      berechnungStipendium: berechnungRd.data?.berechnungStipendium ?? 0,
      berechnungDarlehen: berechnungRd.data?.berechnungDarlehen,
      berechnungsresultate: {},
      stammdaten:
        berechnungRd.data?.tranchenBerechnungsresultate[0]
          ?.berechnungsStammdaten,
    };

    const byTrancheId = berechnungRd.data
      ? berechnungRd.data.tranchenBerechnungsresultate.reduce((acc, curr) => {
          if (!acc.berechnungsresultate[curr.gesuchTrancheId]) {
            acc.berechnungsresultate[curr.gesuchTrancheId] = {
              gesuchTrancheId: curr.gesuchTrancheId,
              startDate: curr.gueltigAb,
              endDate: curr.gueltigBis,
              anzahlMonate: curr.persoenlichesBudgetresultat.anzahlMonate,
              total: 0,
              berechnungen: [],
            };
          }
          acc.berechnungsresultate[curr.gesuchTrancheId].total += curr.total;
          acc.berechnungsresultate[curr.gesuchTrancheId].berechnungen.push(
            curr,
          );
          return acc;
        }, value)
      : value;

    return {
      loading: isPending(berechnungRd),
      ...byTrancheId,
      berechnungsresultate: byTrancheId.berechnungsresultate,
    };
  });

  berechnungsViewSig = computed(() => {
    return {
      loading: isPending(this.berechnung()),
      berechnungen: fromCachedDataSig(this.berechnung),
    };
  });

  getBerechnungForGesuch$ = rxMethod<{
    gesuchId: string;
  }>(
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

  getBerechnungForVerfuegung$ = rxMethod<{
    verfuegungId: string;
  }>(
    pipe(
      tap(() => {
        patchState(this, (state) => ({
          berechnung: cachedPending(state.berechnung),
        }));
      }),
      exhaustMap(({ verfuegungId }) =>
        this.gesuchService
          .getBerechnungForVerfuegung$({ verfuegungId })
          .pipe(
            handleApiResponse((berechnung) => patchState(this, { berechnung })),
          ),
      ),
    ),
  );
}
